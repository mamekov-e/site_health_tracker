package kz.sitehealthtracker.site_health_tracker.service.impls;

import kz.sitehealthtracker.site_health_tracker.config.exception.BadRequestException;
import kz.sitehealthtracker.site_health_tracker.config.exception.NotFoundException;
import kz.sitehealthtracker.site_health_tracker.model.Site;
import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;
import kz.sitehealthtracker.site_health_tracker.model.enums.SiteStatus;
import kz.sitehealthtracker.site_health_tracker.repository.SiteRepository;
import kz.sitehealthtracker.site_health_tracker.service.SiteGroupService;
import kz.sitehealthtracker.site_health_tracker.service.SiteHealthSchedulerService;
import kz.sitehealthtracker.site_health_tracker.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private SiteGroupService siteGroupService;

    @Autowired
    private SiteHealthSchedulerService siteHealthSchedulerService;


    @Override
    public List<Site> getAllSites() {
        return siteRepository.findAll();
    }

    @Override
    public Site getSiteById(Long id) {
        return siteRepository.findById(id)
                .orElseThrow(() -> NotFoundException.entityNotFoundById(Site.class.getSimpleName(), id));
    }

    @Override
    public void addSite(Site site) {
        boolean siteUrlAlreadyExist = siteRepository.existsSitesByUrlIsIgnoreCase(site.getUrl());
        if (siteUrlAlreadyExist) {
            throw BadRequestException.entityWithFieldValueAlreadyExist(Site.class.getSimpleName(), site.getUrl());
        }
        site.setStatus(SiteStatus.DOWN);
        Site siteSaved = siteRepository.save(site);
        siteHealthSchedulerService.addScheduledTask(siteSaved);
    }

    @Override
    public Site updateSite(Site updatedSite) {
        Site siteInDb = getSiteById(updatedSite.getId());

        boolean siteUpdatedUrlAlreadyExist = siteRepository.existsSitesByUrlAndIdIsNot(updatedSite.getUrl(), updatedSite.getId());
        if (siteUpdatedUrlAlreadyExist) {
            throw BadRequestException.entityWithFieldValueAlreadyExist(Site.class.getSimpleName(), updatedSite.getUrl());
        }

        siteHealthSchedulerService.updateScheduledTask(siteInDb, updatedSite);

        updatedSite.setStatus(siteInDb.getStatus());
        updatedSite.setGroups(siteInDb.getGroups());

        return siteRepository.save(updatedSite);
    }

    @Transactional
    @Override
    public void updateSiteStatusById(SiteStatus status, Long id) {
        siteRepository.updateSiteStatusById(status, id);
    }

    @Override
    public void deleteSiteById(Long id) {
        Site site = getSiteById(id);

        List<SiteGroup> siteGroups = site.getGroups();
        for (SiteGroup group : siteGroups) {
            group.getSites().remove(site);
            siteGroupService.updateGroupStatus(group);
        }
        siteHealthSchedulerService.deleteScheduledTask(site);
        siteRepository.deleteById(id);
    }

}
