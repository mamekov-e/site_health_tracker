package kz.sitehealthtracker.site_health_tracker.service.impls;

import kz.sitehealthtracker.site_health_tracker.config.exception.BadRequestException;
import kz.sitehealthtracker.site_health_tracker.config.exception.NotFoundException;
import kz.sitehealthtracker.site_health_tracker.model.Site;
import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;
import kz.sitehealthtracker.site_health_tracker.model.enums.SiteGroupStatus;
import kz.sitehealthtracker.site_health_tracker.model.enums.SiteStatus;
import kz.sitehealthtracker.site_health_tracker.repository.SiteGroupRepository;
import kz.sitehealthtracker.site_health_tracker.service.SiteGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteGroupServiceImpl implements SiteGroupService {

    @Autowired
    private SiteGroupRepository siteGroupRepository;

    @Override
    public List<SiteGroup> getAllSiteGroups() {
        return siteGroupRepository.findAll();
    }

    @Override
    public SiteGroup getSiteGroupById(Long id) {
        return siteGroupRepository.findById(id)
                .orElseThrow(() -> NotFoundException.entityNotFoundById(SiteGroup.class.getSimpleName(), id));
    }

    @Override
    public List<Site> getAllGroupSitesById(Long id) {
        SiteGroup siteGroup = siteGroupRepository.findById(id)
                .orElseThrow(() -> NotFoundException.entityNotFoundById(SiteGroup.class.getSimpleName(), id));

        return siteGroup.getSites();
    }

    @Override
    public void addSiteGroup(SiteGroup siteGroup) {
        boolean siteGroupNameAlreadyExist = siteGroupRepository.existsSiteGroupsByNameIsIgnoreCase(siteGroup.getName());
        if (siteGroupNameAlreadyExist) {
            throw BadRequestException.entityWithFieldValueAlreadyExist(SiteGroup.class.getSimpleName(), siteGroup.getName());
        }

        siteGroup.setStatus(SiteGroupStatus.NO_SITES);

        siteGroupRepository.save(siteGroup);
    }

    @Override
    public void addSitesToGroup(List<Site> sitesOfGroup, Long groupId) {
        SiteGroup siteGroup = siteGroupRepository.findById(groupId)
                .orElseThrow(() -> NotFoundException.entityNotFoundById(SiteGroup.class.getSimpleName(), groupId));

        siteGroup.addSites(sitesOfGroup);
        updateGroupStatus(siteGroup);

        siteGroupRepository.save(siteGroup);
    }

    @Override
    public SiteGroup updateSiteGroup(SiteGroup updatedSiteGroup) {
        siteGroupRepository.findById(updatedSiteGroup.getId())
                .orElseThrow(() -> NotFoundException.entityNotFoundById(SiteGroup.class.getSimpleName(), updatedSiteGroup.getId()));

        boolean siteUpdatedNameAlreadyExist = siteGroupRepository
                .existsSiteGroupsByNameAndIdIsNot(updatedSiteGroup.getName(), updatedSiteGroup.getId());

        if (siteUpdatedNameAlreadyExist) {
            throw BadRequestException
                    .entityWithFieldValueAlreadyExist(Site.class.getSimpleName(), updatedSiteGroup.getName());
        }
        updateGroupStatus(updatedSiteGroup);

        return siteGroupRepository.save(updatedSiteGroup);
    }

    protected void updateGroupStatus(SiteGroup siteGroup) {
        List<Site> sitesOfGroup = siteGroup.getSites();
        SiteGroupStatus siteGroupStatus = null;
        if (sitesOfGroup == null) {
            if (siteGroup.getStatus() == null) {
                siteGroupStatus = SiteGroupStatus.NO_SITES;
            }
        } else {
            Long upSitesCount = 0L;
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

        siteGroup.setStatus(siteGroupStatus);
    }

    @Override
    public void deleteSiteGroupById(Long id) {
        siteGroupRepository.findById(id)
                .orElseThrow(() -> NotFoundException.entityNotFoundById(SiteGroup.class.getSimpleName(), id));

        siteGroupRepository.deleteById(id);
    }


    @Override
    public void deleteSitesFromGroup(List<Site> sitesOfGroup, Long groupId) {
        SiteGroup siteGroup = siteGroupRepository.findById(groupId)
                .orElseThrow(() -> NotFoundException.entityNotFoundById(SiteGroup.class.getSimpleName(), groupId));

        siteGroup.removeSites(sitesOfGroup);
        updateGroupStatus(siteGroup);

        siteGroupRepository.save(siteGroup);
    }

}
