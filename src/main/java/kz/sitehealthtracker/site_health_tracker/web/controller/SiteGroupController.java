package kz.sitehealthtracker.site_health_tracker.web.controller;

import kz.sitehealthtracker.site_health_tracker.model.Site;
import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;
import kz.sitehealthtracker.site_health_tracker.service.SiteGroupService;
import kz.sitehealthtracker.site_health_tracker.web.dtos.SiteDto;
import kz.sitehealthtracker.site_health_tracker.web.dtos.SiteGroupDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/site-groups")
public class SiteGroupController {
    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private SiteGroupService siteGroupService;

    @GetMapping
    public ResponseEntity<List<SiteGroupDto>> getAllSiteGroups() {
        List<SiteGroup> siteGroupList = siteGroupService.getAllSiteGroups();
        List<SiteGroupDto> siteGroupDtoList = siteGroupList.stream()
                .map(this::convertToDto).toList();
        return ResponseEntity.ok(siteGroupDtoList);
    }

    @GetMapping("{id}")
    public ResponseEntity<SiteGroupDto> getSiteGroupById(@PathVariable("id") Long id) {
        SiteGroup siteGroup = siteGroupService.getSiteGroupById(id);
        return ResponseEntity.ok(convertToDto(siteGroup));
    }

    @GetMapping("{id}/sites")
    public ResponseEntity<List<SiteDto>> getAllGroupSitesById(@PathVariable("id") Long id) {
        List<Site> groupSites = siteGroupService.getAllGroupSitesById(id);
        List<SiteDto> groupSitesDtoList = groupSites.stream()
                .map(site -> modelMapper.map(site, SiteDto.class))
                .toList();
        return ResponseEntity.ok(groupSitesDtoList);
    }

    @PostMapping
    public ResponseEntity<Long> addSiteGroup(@RequestBody SiteGroupDto siteGroup) {
        SiteGroup dtoToSiteGroup = convertToEntity(siteGroup);
        siteGroupService.addSiteGroup(dtoToSiteGroup);
        return ResponseEntity.ok(dtoToSiteGroup.getId());
    }

    @PostMapping("/{id}/sites")
    public ResponseEntity<Void> addSitesToGroup(@RequestBody List<SiteDto> sites, @PathVariable("id") Long groupId) {
        List<Site> dtoToSiteList = sites.stream()
                .map(siteDto -> modelMapper.map(siteDto, Site.class))
                .toList();
        siteGroupService.addSitesToGroup(dtoToSiteList, groupId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<SiteGroupDto> updateSiteGroup(@RequestBody SiteGroupDto siteGroup) {
        SiteGroup dtoToSiteGroup = convertToEntity(siteGroup);
        return ResponseEntity.ok(convertToDto(siteGroupService.updateSiteGroup(dtoToSiteGroup)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSiteGroupById(@PathVariable Long id) {
        siteGroupService.deleteSiteGroupById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/sites")
    public ResponseEntity<Void> deleteSitesFromGroup(@RequestBody List<SiteDto> sites, @PathVariable("id") Long groupId) {
        List<Site> dtoToSiteList = sites.stream()
                .map(siteDto -> modelMapper.map(siteDto, Site.class))
                .toList();
        siteGroupService.deleteSitesFromGroup(dtoToSiteList, groupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private SiteGroup convertToEntity(SiteGroupDto siteGroupDto) {
        return modelMapper.map(siteGroupDto, SiteGroup.class);
    }

    private SiteGroupDto convertToDto(SiteGroup siteGroup) {
        return modelMapper.map(siteGroup, SiteGroupDto.class);
    }
}
