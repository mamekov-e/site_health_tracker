package kz.sitehealthtracker.site_health_tracker.web.controller;

import kz.sitehealthtracker.site_health_tracker.model.Site;
import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;
import kz.sitehealthtracker.site_health_tracker.service.SiteGroupService;
import kz.sitehealthtracker.site_health_tracker.utils.ConverterUtil;
import kz.sitehealthtracker.site_health_tracker.web.dtos.SiteDto;
import kz.sitehealthtracker.site_health_tracker.web.dtos.SiteGroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/site-groups")
public class SiteGroupController {
    @Autowired
    private SiteGroupService siteGroupService;

    @GetMapping
    public ResponseEntity<List<SiteGroupDto>> getAllSiteGroups() {
        List<SiteGroup> siteGroupList = siteGroupService.getAllSiteGroups();
        List<SiteGroupDto> siteGroupDtoList = siteGroupList.stream()
                .map(siteGroup -> ConverterUtil.convert(siteGroup, SiteGroupDto.class)).toList();
        return ResponseEntity.ok(siteGroupDtoList);
    }

    @GetMapping("{groupId}")
    public ResponseEntity<SiteGroupDto> getSiteGroupById(@PathVariable("groupId") Long id) {
        SiteGroup siteGroup = siteGroupService.getSiteGroupById(id);
        SiteGroupDto siteGroupDtoResponse = ConverterUtil.convert(siteGroup, SiteGroupDto.class);
        return ResponseEntity.ok(siteGroupDtoResponse);
    }

    @GetMapping("{groupId}/sites")
    public ResponseEntity<Set<SiteDto>> getAllGroupSitesById(@PathVariable("groupId") Long id) {
        Set<Site> groupSitesList = siteGroupService.getAllGroupSitesById(id);
        Set<SiteDto> groupSitesDtoList = groupSitesList.stream()
                .map(site -> ConverterUtil.convert(site, SiteDto.class))
                .collect(Collectors.toSet());
        return ResponseEntity.ok(groupSitesDtoList);
    }

    @PostMapping
    public ResponseEntity<Long> addSiteGroup(@RequestBody SiteGroupDto siteGroupDto) {
        SiteGroup siteGroup = ConverterUtil.convert(siteGroupDto, SiteGroup.class);
        siteGroupService.addSiteGroup(siteGroup);
        return new ResponseEntity<>(siteGroup.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/{groupId}/sites")
    public ResponseEntity<Void> addSitesToGroup(@RequestBody List<SiteDto> sites, @PathVariable("groupId") Long id) {
        Set<Site> groupSitesList = sites.stream()
                .map(siteDto -> ConverterUtil.convert(siteDto, Site.class))
                .collect(Collectors.toSet());
        siteGroupService.addSitesToGroupById(groupSitesList, id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<SiteGroupDto> updateSiteGroup(@RequestBody SiteGroupDto siteGroupDto) {
        SiteGroup siteGroup = ConverterUtil.convert(siteGroupDto, SiteGroup.class);
        SiteGroupDto siteGroupDtoResponse = ConverterUtil
                .convert(siteGroupService.updateSiteGroup(siteGroup), SiteGroupDto.class);
        return ResponseEntity.ok(siteGroupDtoResponse);
    }

    @DeleteMapping("{groupId}")
    public ResponseEntity<Void> deleteSiteGroupById(@PathVariable("groupId") Long id) {
        siteGroupService.deleteSiteGroupById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{groupId}/sites")
    public ResponseEntity<Void> deleteSitesFromGroup(@RequestBody List<SiteDto> siteDtoList, @PathVariable("groupId") Long id) {
        List<Site> groupSitesList = siteDtoList.stream()
                .map(siteDto -> ConverterUtil.convert(siteDto, Site.class))
                .toList();
        siteGroupService.deleteSitesFromGroupById(groupSitesList, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
