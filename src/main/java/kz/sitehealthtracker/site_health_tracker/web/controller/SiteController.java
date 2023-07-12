package kz.sitehealthtracker.site_health_tracker.web.controller;

import kz.sitehealthtracker.site_health_tracker.config.exception.BadRequestException;
import kz.sitehealthtracker.site_health_tracker.model.Site;
import kz.sitehealthtracker.site_health_tracker.model.enums.SiteStatus;
import kz.sitehealthtracker.site_health_tracker.service.SiteService;
import kz.sitehealthtracker.site_health_tracker.web.dtos.SiteDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sites")
public class SiteController {
    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private SiteService siteService;

    @GetMapping
    public ResponseEntity<List<SiteDto>> getAllSites() {
        List<Site> siteList = siteService.getAllSites();
        List<SiteDto> siteDtoList = siteList.stream()
                .map(this::convertToDto).toList();
        return ResponseEntity.ok(siteDtoList);
    }

    @GetMapping("{id}")
    public ResponseEntity<SiteDto> getSiteById(@PathVariable("id") Long id) {
        Site site = siteService.getSiteById(id);
        return ResponseEntity.ok(convertToDto(site));
    }

    @PostMapping
    public ResponseEntity<Long> addSite(@RequestBody SiteDto site) {
        Site dtoToSite = convertToEntity(site);
        siteService.addSite(dtoToSite);
        return ResponseEntity.ok(dtoToSite.getId());
    }

    @PutMapping
    public ResponseEntity<SiteDto> updateSite(@RequestBody SiteDto site) {
        Site dtoToSite = convertToEntity(site);
        return ResponseEntity.ok(convertToDto(siteService.updateSite(dtoToSite)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSiteById(@PathVariable Long id) {
        siteService.deleteSiteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Site convertToEntity(SiteDto siteDto) {
        return modelMapper.map(siteDto, Site.class);
    }

    private SiteDto convertToDto(Site site) {
        return modelMapper.map(site, SiteDto.class);
    }
}
