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
        List<Site> siteList = siteService.getAllSites();
        List<SiteDto> siteDtoList = siteList.stream()
                .map(site -> ConverterUtil.convert(site, SiteDto.class)).toList();
        return ResponseEntity.ok(siteDtoList);
    }

    @GetMapping("{id}")
    public ResponseEntity<SiteDto> getSiteById(@PathVariable("id") Long id) {
        Site site = siteService.getSiteById(id);
        SiteDto siteDtoResponse = ConverterUtil.convert(site, SiteDto.class);
        return ResponseEntity.ok(siteDtoResponse);
    }

    @PostMapping
    public ResponseEntity<Long> addSite(@RequestBody SiteDto siteDto) {
        Site site = ConverterUtil.convert(siteDto, Site.class);
        siteService.addSite(site);
        return ResponseEntity.ok(site.getId());
    }

    @PutMapping
    public ResponseEntity<SiteDto> updateSite(@RequestBody SiteDto siteDto) {
        Site site = ConverterUtil.convert(siteDto, Site.class);
        Site siteUpdated = siteService.updateSite(site);
        SiteDto siteDtoResponse = ConverterUtil.convert(siteUpdated, SiteDto.class);
        return ResponseEntity.ok(siteDtoResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSiteById(@PathVariable Long id) {
        siteService.deleteSiteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
