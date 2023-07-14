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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SiteGroupServiceImpl implements SiteGroupService {

    @Autowired
    private SiteGroupRepository siteGroupRepository;

    @Override
    public List<SiteGroup> getAllSitesOfGroup() {
        return siteGroupRepository.findAll();
    }

    @Override
    public SiteGroup getSiteGroupById(Long id) {
        return siteGroupRepository.findById(id)
                .orElseThrow(() -> NotFoundException.entityNotFoundById(SiteGroup.class.getSimpleName(), id));
    }

    @Override
    public List<Site> getAllGroupSitesById(Long id) {
        SiteGroup siteGroup = getSiteGroupById(id);

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
    public List<Site> addSitesToGroupById(List<Site> sitesOfGroup, Long id) {
        SiteGroup siteGroup = getSiteGroupById(id);

        List<Site> alreadyExistingSites = new ArrayList<>();
        List<Site> siteOfGroupInDb = siteGroup.getSites();
        for (Site siteOfGroup : sitesOfGroup) {
            if (siteOfGroupInDb.contains(siteOfGroup)) {
                alreadyExistingSites.add(siteOfGroup);
            }
        }
        if (!alreadyExistingSites.isEmpty()) {
            return alreadyExistingSites;
        }

        siteGroup.addSites(sitesOfGroup);
        updateGroupStatus(siteGroup);
        return null;
    }

    @Transactional
    @Override
    public SiteGroup updateSiteGroup(SiteGroup updatedSiteGroup) {
        SiteGroup siteGroupInDb = getSiteGroupById(updatedSiteGroup.getId());

        boolean siteUpdatedNameAlreadyExist = siteGroupRepository
                .existsSiteGroupsByNameAndIdIsNot(updatedSiteGroup.getName(), updatedSiteGroup.getId());
        if (siteUpdatedNameAlreadyExist) {
            throw BadRequestException
                    .entityWithFieldValueAlreadyExist(Site.class.getSimpleName(), updatedSiteGroup.getName());
        }

        updatedSiteGroup.setStatus(siteGroupInDb.getStatus());
        siteGroupRepository.updateSiteGroupById(
                updatedSiteGroup.getName(),
                updatedSiteGroup.getDescription(),
                updatedSiteGroup.getId());

        return updatedSiteGroup;
    }

    @Override
    public void updateGroupStatus(SiteGroup siteGroup) {
        List<Site> sitesOfGroup = siteGroup.getSites();
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

        siteGroup.setStatus(siteGroupStatus);
        siteGroupRepository.save(siteGroup);
    }

    @Override
    public void deleteSiteGroupById(Long id) {
        checkIfSiteGroupExistById(id);

        siteGroupRepository.deleteById(id);
    }


    @Override
    public List<Site> deleteSitesFromGroupById(List<Site> sitesOfGroup, Long id) {
        SiteGroup siteGroup = getSiteGroupById(id);

        List<Site> nonExistentSites = new ArrayList<>();
        List<Site> sitesOfGroupInDb = siteGroup.getSites();

        for (Site siteOfGroup : sitesOfGroup) {
            if (!sitesOfGroupInDb.contains(siteOfGroup)) {
                nonExistentSites.add(siteOfGroup);
            }
        }
        if (!nonExistentSites.isEmpty()) {
            return nonExistentSites;
        }


        if (!sitesOfGroupInDb.isEmpty()) {
            siteGroup.removeSites(sitesOfGroup);
            updateGroupStatus(siteGroup);
        }
        return null;
    }

    private void checkIfSiteGroupExistById(Long id) {
        boolean siteGroupExist = siteGroupRepository.existsById(id);
        if (!siteGroupExist) {
            throw NotFoundException.entityNotFoundById(SiteGroup.class.getSimpleName(), id);
        }
    }
}
