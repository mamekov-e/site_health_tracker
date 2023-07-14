package kz.sitehealthtracker.site_health_tracker.web.controller;

import kz.sitehealthtracker.site_health_tracker.model.Site;
import kz.sitehealthtracker.site_health_tracker.service.SiteService;
import kz.sitehealthtracker.site_health_tracker.utils.ConverterUtil;
import kz.sitehealthtracker.site_health_tracker.web.dtos.SiteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sites")
public class SiteController {
    @Autowired
    private SiteService siteService;

    @GetMapping
    public ResponseEntity<List<SiteDto>> getAllSites() {
        List<Site> sites = siteService.getAllSites();
        List<SiteDto> sitesDto = sites.stream()
                .map(site -> ConverterUtil.convertObject(site, SiteDto.class)).toList();
        return ResponseEntity.ok(sitesDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<SiteDto> getSiteById(@PathVariable("id") Long id) {
        Site site = siteService.getSiteById(id);
        SiteDto siteDtoResponse = ConverterUtil.convertObject(site, SiteDto.class);
        return ResponseEntity.ok(siteDtoResponse);
    }

    @PostMapping
    public ResponseEntity<Long> addSite(@RequestBody SiteDto siteDto) {
        Site site = ConverterUtil.convertObject(siteDto, Site.class);
        siteService.addSite(site);
        return new ResponseEntity<>(site.getId(),HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<SiteDto> updateSite(@RequestBody SiteDto siteDto) {
        Site site = ConverterUtil.convertObject(siteDto, Site.class);
        Site siteUpdated = siteService.updateSite(site);
        SiteDto siteDtoResponse = ConverterUtil.convertObject(siteUpdated, SiteDto.class);
        return ResponseEntity.ok(siteDtoResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSiteById(@PathVariable Long id) {
        siteService.deleteSiteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
