package kz.sitehealthtrackerbackend.site_health_tracker_backend.web.controller;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteGroupService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.utils.ConverterUtil;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos.SiteDto;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos.SiteGroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/site-groups")
public class SiteGroupController {
    @Autowired
    private SiteGroupService siteGroupService;

    @GetMapping
    public ResponseEntity<List<SiteGroupDto>> getAllSiteGroups() {
        List<SiteGroup> sitesGroup = siteGroupService.getAllSiteGroups();
        List<SiteGroupDto> siteGroupsDto = ConverterUtil.convertList(sitesGroup, SiteGroupDto.class);
        return ResponseEntity.ok(siteGroupsDto);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<SiteGroupDto> getSiteGroupById(@PathVariable("groupId") Long id) {
        SiteGroup siteGroup = siteGroupService.getSiteGroupById(id);
        SiteGroupDto siteGroupDtoResponse = ConverterUtil.convertObject(siteGroup, SiteGroupDto.class);
        return ResponseEntity.ok(siteGroupDtoResponse);
    }

    @GetMapping("/{groupId}/sites")
    public ResponseEntity<List<SiteDto>> getAllGroupSitesById(@PathVariable("groupId") Long id) {
        List<Site> sitesGroup = siteGroupService.getAllGroupSitesById(id);
        List<SiteDto> sitesGroupDto = ConverterUtil.convertList(sitesGroup, SiteDto.class);
        return ResponseEntity.ok(sitesGroupDto);
    }

    @PostMapping
    public ResponseEntity<Long> addSiteGroup(@RequestBody SiteGroupDto siteGroupDto) {
        SiteGroup siteGroup = ConverterUtil.convertObject(siteGroupDto, SiteGroup.class);
        siteGroupService.addSiteGroup(siteGroup);
        return new ResponseEntity<>(siteGroup.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/{groupId}/sites")
    public ResponseEntity<Void> addSitesToGroup(@RequestBody List<SiteDto> sitesDto, @PathVariable("groupId") Long id) {
        List<Site> sites = ConverterUtil.convertList(sitesDto, Site.class);

        siteGroupService.addSitesToGroupById(sites, id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<SiteGroupDto> updateSiteGroup(@RequestBody SiteGroupDto siteGroupDto) {
        SiteGroup siteGroup = ConverterUtil.convertObject(siteGroupDto, SiteGroup.class);
        SiteGroupDto siteGroupDtoResponse = ConverterUtil
                .convertObject(siteGroupService.updateSiteGroup(siteGroup), SiteGroupDto.class);
        return ResponseEntity.ok(siteGroupDtoResponse);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteSiteGroupById(@PathVariable("groupId") Long id) {
        siteGroupService.deleteSiteGroupById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{groupId}/sites")
    public ResponseEntity<Void> deleteSitesFromGroup(@RequestBody List<SiteDto> sitesDto, @PathVariable("groupId") Long id) {
        List<Site> sites = ConverterUtil.convertList(sitesDto, Site.class);

        siteGroupService.deleteSitesFromGroupById(sites, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
