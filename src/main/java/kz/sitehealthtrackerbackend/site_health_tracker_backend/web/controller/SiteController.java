package kz.sitehealthtrackerbackend.site_health_tracker_backend.web.controller;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.utils.ConverterUtil;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.SecurityController;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos.SiteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/sites")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
public class SiteController implements SecurityController {
    @Autowired
    private SiteService siteService;

    @GetMapping
    public ResponseEntity<Page<SiteDto>> getAllSitesInPage(@RequestParam(required = false, defaultValue = "0") int pageNumber,
                                                           @RequestParam(required = false, defaultValue = "5") int pageSize,
                                                           @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                           @RequestParam(required = false, defaultValue = "desc") String sortDir) {
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, Sort.Direction.fromString(sortDir), sortBy);

        Page<Site> sites = siteService.getAllSiteInPage(pageRequest);
        Page<SiteDto> sitesDto = ConverterUtil.convertPage(sites, SiteDto.class);
        return ResponseEntity.ok(sitesDto);
    }

    @GetMapping("/search/{searchText}")
    public ResponseEntity<Page<SiteDto>> getAllSitesInPageWithSearch(Pageable pageable, @PathVariable("searchText") String searchText) {
        Page<Site> sites = siteService.getAllSiteInPageWithSearchText(pageable, searchText);
        Page<SiteDto> sitesDto = ConverterUtil.convertPage(sites, SiteDto.class);
        return ResponseEntity.ok(sitesDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SiteDto> getSiteById(@PathVariable("id") Long id) {
        Site site = siteService.getSiteById(id, true);
        SiteDto siteDtoResponse = ConverterUtil.convertObject(site, SiteDto.class);
        return ResponseEntity.ok(siteDtoResponse);
    }

    @PostMapping
    public ResponseEntity<Long> addSite(@RequestBody SiteDto siteDto) {
        Site site = ConverterUtil.convertObject(siteDto, Site.class);
        siteService.addSite(site);
        return new ResponseEntity<>(site.getId(), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<SiteDto> updateSite(@RequestBody SiteDto siteDto) {
        Site site = ConverterUtil.convertObject(siteDto, Site.class);
        Site siteUpdated = siteService.updateSite(site);
        SiteDto siteDtoResponse = ConverterUtil.convertObject(siteUpdated, SiteDto.class);
        return ResponseEntity.ok(siteDtoResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSiteById(@PathVariable Long id) {
        siteService.deleteSiteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllSites() {
        siteService.deleteAllSites();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
