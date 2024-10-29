package kz.sitehealthtrackerbackend.site_health_tracker_backend.service.impls;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.User;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.security.SecurityUtils;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception.BadRequestException;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception.NotFoundException;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.EntityNames;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteStatus;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.repository.SiteRepository;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteCheckLogService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteGroupService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteHealthSchedulerService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.utils.EntityUtils;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos.SiteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.CacheConstants.*;

@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private SiteGroupService siteGroupService;

    @Autowired
    private SiteCheckLogService siteCheckLogService;

    @Autowired
    private SiteHealthSchedulerService siteHealthSchedulerService;

    @Override
    public Page<Site> getAllSiteInPageWithSearchText(Pageable pageable, String searchText) {
        return siteRepository.findAllInPageWithSearchText(searchText, SecurityUtils.getCurrentUserId(), pageable);
    }

    @Override
    public Page<Site> getAllGroupSitesInPageWithSearchText(Long siteGroupId, Pageable pageable, String searchText) {
        return siteRepository.findAllGroupSitesInPageWithSearchText(siteGroupId, searchText, SecurityUtils.getCurrentUserId(), pageable);
    }

    @Override
    public Page<Site> getAllSiteInPage(Pageable pageable) {
        return siteRepository.findAllByUser_Id(SecurityUtils.getCurrentUserId(), pageable);
    }

    @Override
    public List<Site> getAllSites() {
        return siteRepository.findAll();
    }

    @Cacheable(cacheNames = SITE_CACHE_NAME, key = "#id", unless = "#result == null")
    @Override
    public Site getSiteById(Long id, boolean validate) {
        Site siteInDb = siteRepository.findById(id)
                .orElseThrow(() -> NotFoundException.entityNotFoundById(EntityNames.SITE.getName(), id));

        if (validate)
            EntityUtils.checkUserAllowed(siteInDb.getUser().getId());

        return siteInDb;
    }

    @Cacheable(cacheNames = SITES_OF_GROUP_CACHE_NAME, key = "#siteGroup.id")
    public List<Site> getAllSitesBySiteGroup(SiteGroup siteGroup) {
        List<SiteGroup> siteGroupsList = List.of(siteGroup);
        return siteRepository.findAllByGroupsIn(siteGroupsList);
    }

    @Override
    public void addSite(Site site) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        boolean siteNameAlreadyExist = siteRepository.existsSitesByNameIgnoreCaseAndUser_IdIsNot(site.getName(), currentUserId);
        if (siteNameAlreadyExist) {
            throw BadRequestException.entityWithFieldValueAlreadyExist(EntityNames.SITE.getName(), site.getName());
        }
        boolean siteUrlAlreadyExist = siteRepository.existsSitesByUrlIgnoreCaseAndUser_IdIsNot(site.getUrl(), currentUserId);
        if (siteUrlAlreadyExist) {
            throw BadRequestException.entityWithFieldValueAlreadyExist(EntityNames.SITE.getName(), site.getUrl());
        }

        site.setUser(new User(currentUserId));
        site.setStatus(SiteStatus.DOWN);
        Site siteSaved = siteRepository.save(site);
        siteHealthSchedulerService.addScheduledTask(siteSaved);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = GROUPS_OF_SITE_CACHE_NAME, key = "#updatedSite.id", condition = "#result != null"),
            @CacheEvict(cacheNames = SITES_OF_GROUP_CACHE_NAME, allEntries = true, condition = "#result != null")},
            put = @CachePut(cacheNames = SITE_CACHE_NAME, key = "#updatedSite.id", unless = "#result == null"))
    @Override
    public Site updateSite(Site updatedSite) {
        long updatedSiteId = updatedSite.getId();
        final Site siteInDb = siteRepository.findById(updatedSiteId)
                .orElseThrow(() -> NotFoundException.entityNotFoundById(EntityNames.SITE.getName(), updatedSiteId));

        EntityUtils.checkUserAllowed(siteInDb.getUser().getId());

        Long currentUserId = SecurityUtils.getCurrentUserId();
        boolean siteNameAlreadyExist = siteRepository.existsSitesByNameIgnoreCaseAndIdIsNotAndUser_IdIsNot(updatedSite.getName(),
                updatedSite.getId(), currentUserId);
        if (siteNameAlreadyExist) {
            throw BadRequestException.entityWithFieldValueAlreadyExist(EntityNames.SITE.getName(), updatedSite.getName());
        }
        boolean siteUpdatedUrlAlreadyExist = siteRepository.existsSitesByUrlIgnoreCaseAndIdIsNotAndUser_IdIsNot(updatedSite.getUrl(),
                updatedSite.getId(), currentUserId);
        if (siteUpdatedUrlAlreadyExist) {
            throw BadRequestException.entityWithFieldValueAlreadyExist(EntityNames.SITE.getName(), updatedSite.getUrl());
        }

        updatedSite.setUser(siteInDb.getUser());
        updatedSite.setStatus(siteInDb.getStatus());
        List<SiteGroup> siteGroups = siteInDb.getGroups();
        updatedSite.setGroups(siteGroups);
        siteHealthSchedulerService.updateScheduledTask(siteInDb, updatedSite);

        return siteRepository.save(updatedSite);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = GROUPS_OF_SITE_CACHE_NAME, key = "#id", condition = "#result != null"),
            @CacheEvict(cacheNames = SITES_OF_GROUP_CACHE_NAME, allEntries = true, condition = "#result != null")},
            put = @CachePut(cacheNames = SITE_CACHE_NAME, key = "#id", unless = "#result == null"))
    @Transactional
    @Override
    public Site updateSiteStatusById(SiteStatus status, Long id) {
        siteRepository.updateSiteStatusById(status, id);

        return siteRepository.findById(id)
                .orElse(null);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = SITE_CACHE_NAME, key = "#id", condition = "#result == true"),
            @CacheEvict(cacheNames = GROUPS_OF_SITE_CACHE_NAME, key = "#id", condition = "#result == true"),
            @CacheEvict(cacheNames = SITES_OF_GROUP_CACHE_NAME, allEntries = true, condition = "#result == true")
    })
    @Override
    @Transactional
    public boolean deleteSiteById(Long id) {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> NotFoundException.entityNotFoundById(EntityNames.SITE.getName(), id));

        EntityUtils.checkUserAllowed(site.getUser().getId());

        List<SiteGroup> siteGroups = site.getGroups();

        for (SiteGroup group : siteGroups) {
            group.getSites().remove(site);
            SiteDto siteDeletedDto = new SiteDto(site.getName(), SiteStatus.DELETED);
            siteGroupService.saveGroupChangesIfGroupStatusWasNotChanged(group, siteDeletedDto);
        }
        siteCheckLogService.deleteSiteCheckLogBySiteId(site.getId());
        siteHealthSchedulerService.deleteScheduledTask(site);
        siteRepository.deleteById(id);
        return true;
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = SITE_CACHE_NAME, allEntries = true, condition = "#result == true"),
            @CacheEvict(cacheNames = GROUPS_OF_SITE_CACHE_NAME, allEntries = true, condition = "#result == true"),
            @CacheEvict(cacheNames = SITES_OF_GROUP_CACHE_NAME, allEntries = true, condition = "#result == true")
    })
    @Override
    public boolean deleteAllSites() {
        List<Site> siteList = siteRepository.findAllByUser_Id(SecurityUtils.getCurrentUserId());
        for (Site site : siteList) {
            List<SiteGroup> siteGroups = site.getGroups();

            for (SiteGroup group : siteGroups) {
                group.getSites().remove(site);
            }
            siteHealthSchedulerService.deleteScheduledTask(site);
        }
        siteRepository.deleteAll();
        SiteDto siteDto = new SiteDto();
        List<SiteGroup> siteGroups = siteGroupService.getAllSiteGroups();
        for (SiteGroup siteGroup : siteGroups) {
            siteGroupService.updateGroupStatus(siteGroup, siteDto);
        }
        return true;
    }
}
