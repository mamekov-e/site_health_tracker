package kz.sitehealthtrackerbackend.site_health_tracker_backend.service.impls;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception.BadRequestException;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception.NotFoundException;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.EntityNames;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteGroupStatus;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteStatus;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.notifier.EventNotifier;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.repository.SiteGroupRepository;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteGroupService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.utils.ConverterUtil;
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

import java.util.ArrayList;
import java.util.List;

import static kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.CacheConstants.*;

@Service
public class SiteGroupServiceImpl implements SiteGroupService {

    @Autowired
    private SiteGroupRepository siteGroupRepository;

    @Autowired
    private EventNotifier eventNotifier;

    @Autowired
    private SiteService siteService;

    @Override
    public Page<SiteGroup> getAllSiteGroupsInPageWithSearchText(Pageable pageable, String searchText) {
        return siteGroupRepository.findAllInPageWithSearchText(pageable, searchText);
    }

    @Override
    public Page<Site> getAllSitesOfGroupByIdInPageWithSearchText(Long id, Pageable pageable, String searchText) {
        SiteGroup siteGroup = siteGroupRepository.findById(id)
                .orElseThrow(() -> NotFoundException.entityNotFoundById(EntityNames.SITE_GROUP.getName(), id));

        return siteService.getAllGroupSitesInPageWithSearchText(siteGroup.getId(), pageable, searchText);
    }

    @Override
    public Page<SiteGroup> getAllSiteGroupsInPage(Pageable pageable) {
        return siteGroupRepository.findAll(pageable);
    }

    @Cacheable(cacheNames = GROUPS_OF_SITE_CACHE_NAME, key = "#site.id")
    public List<SiteGroup> getAllSiteGroupsBySite(Site site) {
        List<Site> siteList = List.of(site);
        return siteGroupRepository.findAllBySitesIn(siteList);
    }

    @Cacheable(cacheNames = SITE_GROUP_CACHE_NAME, key = "#id", unless = "#result == null")
    @Override
    public SiteGroup getSiteGroupById(Long id) {
        return siteGroupRepository.findById(id)
                .orElseThrow(() -> NotFoundException.entityNotFoundById(EntityNames.SITE_GROUP.getName(), id));
    }

//    @Cacheable(cacheNames = SITES_OF_GROUP_CACHE_NAME, key = "#id")
    @Override
    public List<Site> getAllGroupSitesById(Long id) {
        SiteGroup siteGroup = siteGroupRepository.findById(id)
                .orElseThrow(() -> NotFoundException.entityNotFoundById(EntityNames.SITE_GROUP.getName(), id));

        return  siteGroup.getSites();
    }


    @CacheEvict(cacheNames = SITE_GROUPS_CACHE_NAME, allEntries = true, condition = "#result == true")
    @Override
    public boolean addSiteGroup(SiteGroup siteGroup) {
        boolean siteGroupNameAlreadyExist = siteGroupRepository.existsSiteGroupsByNameIsIgnoreCase(siteGroup.getName());
        if (siteGroupNameAlreadyExist) {
            throw BadRequestException.entityWithFieldValueAlreadyExist(EntityNames.SITE_GROUP.getName(), siteGroup.getName());
        }

        siteGroup.setStatus(SiteGroupStatus.NO_SITES);
        siteGroupRepository.save(siteGroup);
        return true;
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = SITE_GROUPS_CACHE_NAME, allEntries = true, condition = "#result == true"),
            @CacheEvict(cacheNames = SITE_GROUP_CACHE_NAME, key = "#id", condition = "#result == true"),
            @CacheEvict(cacheNames = GROUPS_OF_SITE_CACHE_NAME, allEntries = true, condition = "#result == true"),
            @CacheEvict(cacheNames = SITES_OF_GROUP_CACHE_NAME, key = "#id")
    })
    @Override
    public boolean addSitesToGroupById(List<Site> sitesOfGroup, Long id) {
        SiteGroup siteGroup = siteGroupRepository.findById(id)
                .orElseThrow(() -> NotFoundException.entityNotFoundById(EntityNames.SITE_GROUP.getName(), id));

        List<Site> alreadyExistingSites = new ArrayList<>();
        List<Site> siteOfGroupInDb = siteGroup.getSites();
        for (Site siteOfGroup : sitesOfGroup) {
            if (siteOfGroupInDb.contains(siteOfGroup)) {
                alreadyExistingSites.add(siteOfGroup);
            }
        }
        if (!alreadyExistingSites.isEmpty()) {
            List<SiteDto> alreadyExistingSitesDto = ConverterUtil.convertList(alreadyExistingSites, SiteDto.class);
            throw BadRequestException
                    .entityCollectionWithElementsFailedByExistence(EntityNames.SITE.getName(), alreadyExistingSitesDto, true);
        }

        siteGroup.addSites(sitesOfGroup);
        saveGroupChangesIfGroupStatusWasNotChanged(siteGroup);
        return true;
    }


    @Caching(evict = {
            @CacheEvict(cacheNames = SITE_GROUPS_CACHE_NAME, allEntries = true, condition = "#result != null"),
            @CacheEvict(cacheNames = GROUPS_OF_SITE_CACHE_NAME, allEntries = true, condition = "#result != null")},
            put = @CachePut(cacheNames = SITE_GROUP_CACHE_NAME, key = "#updatedSiteGroup.id", unless = "#result == null"))
    @Transactional
    @Override
    public SiteGroup updateSiteGroup(SiteGroup updatedSiteGroup) {
        SiteGroup siteGroupInDb = getSiteGroupById(updatedSiteGroup.getId());

        boolean siteUpdatedNameAlreadyExist = siteGroupRepository
                .existsSiteGroupsByNameAndIdIsNot(updatedSiteGroup.getName(), updatedSiteGroup.getId());
        if (siteUpdatedNameAlreadyExist) {
            throw BadRequestException
                    .entityWithFieldValueAlreadyExist(EntityNames.SITE.getName(), updatedSiteGroup.getName());
        }

        updatedSiteGroup.setStatus(siteGroupInDb.getStatus());
        siteGroupRepository.updateSiteGroupById(
                updatedSiteGroup.getName(),
                updatedSiteGroup.getDescription(),
                updatedSiteGroup.getId());

        return updatedSiteGroup;
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = SITE_GROUPS_CACHE_NAME, allEntries = true, condition = "#result == true"),
            @CacheEvict(cacheNames = GROUPS_OF_SITE_CACHE_NAME, allEntries = true, condition = "#result == true"),
            @CacheEvict(cacheNames = SITE_GROUP_CACHE_NAME, key = "#siteGroup.id", condition = "#result == true")
    })
    @Override
    public boolean updateGroupStatus(SiteGroup siteGroup) {
        List<Site> sitesOfGroup = getAllGroupSitesById(siteGroup.getId());
        System.out.printf("All sites of %s group: %s\n", siteGroup.getName(), sitesOfGroup);
        SiteGroupStatus siteGroupStatus;
        if (sitesOfGroup.isEmpty()) {
            siteGroupStatus = SiteGroupStatus.NO_SITES;
        } else {
            long upSitesCount = 0L;
            for (Site site : sitesOfGroup) {
                if (SiteStatus.UP.equals(site.getStatus())) {
                    upSitesCount++;
                }
            }
            if (upSitesCount == 0) {
                siteGroupStatus = SiteGroupStatus.ALL_DOWN;
            } else if (upSitesCount == sitesOfGroup.size()) {
                siteGroupStatus = SiteGroupStatus.ALL_UP;
            } else {
                siteGroupStatus = SiteGroupStatus.PARTIAL_UP;
            }
        }
        final SiteGroupStatus oldStatus = siteGroup.getStatus();

        if (!siteGroupStatus.equals(oldStatus)) {
            siteGroup.setStatus(siteGroupStatus);
            // maybe it is worth to setGroups also to not get removed the sites from group
            // due to siteGroup's sites list is empty
            siteGroupRepository.save(siteGroup);
            eventNotifier.notifyAll(siteGroup);
            System.out.printf("Status of site group %s updated: %s%n", siteGroup.getName(), siteGroup.getStatus());
            return true;
        } else {
            System.out.println("Checked all sites and group status was not changed");
            return false;
        }
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = SITE_GROUPS_CACHE_NAME, allEntries = true),
            @CacheEvict(cacheNames = GROUPS_OF_SITE_CACHE_NAME, allEntries = true),
            @CacheEvict(cacheNames = SITE_GROUP_CACHE_NAME, key = "#siteGroup.id")
    })
    @Override
    public void saveGroupChangesIfGroupStatusWasNotChanged(SiteGroup siteGroup) {
        boolean siteGroupStatusUpdated = updateGroupStatus(siteGroup);
        if (!siteGroupStatusUpdated) {
            siteGroupRepository.save(siteGroup);
        }
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = SITE_GROUPS_CACHE_NAME, allEntries = true, condition = "#result == true"),
            @CacheEvict(cacheNames = GROUPS_OF_SITE_CACHE_NAME, key = "#id", condition = "#result == true"),
            @CacheEvict(cacheNames = SITES_OF_GROUP_CACHE_NAME, key = "#id", condition = "#result == true"),
            @CacheEvict(cacheNames = SITE_GROUP_CACHE_NAME, key = "#id", condition = "#result == true")
    })
    @Override
    public boolean deleteSiteGroupById(Long id) {
        boolean siteExist = checkIfSiteGroupExistById(id);

        siteGroupRepository.deleteById(id);
        return siteExist;
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = SITE_GROUPS_CACHE_NAME, allEntries = true, condition = "#result == true"),
            @CacheEvict(cacheNames = GROUPS_OF_SITE_CACHE_NAME, allEntries = true, condition = "#result == true"),
            @CacheEvict(cacheNames = SITE_GROUP_CACHE_NAME, key = "#id", condition = "#result == true"),
            @CacheEvict(cacheNames = SITES_OF_GROUP_CACHE_NAME, key = "#id")
    })
    @Override
    public boolean deleteSitesFromGroupById(List<Site> sitesOfGroup, Long id) {
        SiteGroup siteGroup = siteGroupRepository.findById(id)
                .orElseThrow(() -> NotFoundException.entityNotFoundById(EntityNames.SITE_GROUP.getName(), id));

        List<Site> nonExistentSites = new ArrayList<>();
        List<Site> sitesOfGroupInDb = siteGroup.getSites();

        for (Site siteOfGroup : sitesOfGroup) {
            if (!sitesOfGroupInDb.contains(siteOfGroup)) {
                nonExistentSites.add(siteOfGroup);
            }
        }
        if (!nonExistentSites.isEmpty()) {
            List<SiteDto> nonExistentSitesDto = ConverterUtil.convertList(nonExistentSites, SiteDto.class);
            throw BadRequestException
                    .entityCollectionWithElementsFailedByExistence(EntityNames.SITE.getName(), nonExistentSitesDto, false);
        }


        if (!sitesOfGroupInDb.isEmpty()) {
            siteGroup.removeSites(sitesOfGroup);
            saveGroupChangesIfGroupStatusWasNotChanged(siteGroup);
            return true;
        }
        return false;
    }

    private boolean checkIfSiteGroupExistById(Long id) {
        boolean siteGroupExist = siteGroupRepository.existsById(id);
        if (!siteGroupExist) {
            throw NotFoundException.entityNotFoundById(EntityNames.SITE_GROUP.getName(), id);
        }
        return true;
    }
}
