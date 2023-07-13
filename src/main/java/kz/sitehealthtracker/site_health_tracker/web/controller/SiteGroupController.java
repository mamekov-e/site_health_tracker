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

    @GetMapping("{groupId}")
    public ResponseEntity<SiteGroupDto> getSiteGroupById(@PathVariable("groupId") Long id) {
        SiteGroup siteGroup = siteGroupService.getSiteGroupById(id);
        return ResponseEntity.ok(convertToDto(siteGroup));
    }

    @GetMapping("{groupId}/sites")
    public ResponseEntity<List<SiteDto>> getAllGroupSitesById(@PathVariable("groupId") Long id) {
        List<Site> groupSitesList = siteGroupService.getAllGroupSitesById(id);
        List<SiteDto> groupSitesDtoList = groupSitesList.stream()
                .map(site -> modelMapper.map(site, SiteDto.class))
                .toList();
        return ResponseEntity.ok(groupSitesDtoList);
    }

    @PostMapping
    public ResponseEntity<Long> addSiteGroup(@RequestBody SiteGroupDto siteGroupDto) {
        SiteGroup siteGroup = convertToEntity(siteGroupDto);
        siteGroupService.addSiteGroup(siteGroup);
        return ResponseEntity.ok(siteGroup.getId());
    }

    @PostMapping("/{groupId}/sites")
    public ResponseEntity<Void> addSitesToGroup(@RequestBody List<SiteDto> sites, @PathVariable("groupId") Long id) {
        List<Site> groupSitesList = sites.stream()
                .map(siteDto -> modelMapper.map(siteDto, Site.class))
                .toList();
        siteGroupService.addSitesToGroupById(groupSitesList, id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<SiteGroupDto> updateSiteGroup(@RequestBody SiteGroupDto siteGroupDto) {
        SiteGroup siteGroup = convertToEntity(siteGroupDto);
        return ResponseEntity.ok(convertToDto(siteGroupService.updateSiteGroup(siteGroup)));
    }

    @DeleteMapping("{groupId}")
    public ResponseEntity<Void> deleteSiteGroupById(@PathVariable("groupId") Long id) {
        siteGroupService.deleteSiteGroupById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{groupId}/sites")
    public ResponseEntity<Void> deleteSitesFromGroup(@RequestBody List<SiteDto> siteDtoList, @PathVariable("groupId") Long id) {
        List<Site> groupSitesList = siteDtoList.stream()
                .map(siteDto -> modelMapper.map(siteDto, Site.class))
                .toList();
        siteGroupService.deleteSitesFromGroupById(groupSitesList, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private SiteGroup convertToEntity(SiteGroupDto siteGroupDto) {
        return modelMapper.map(siteGroupDto, SiteGroup.class);
    }

    private SiteGroupDto convertToDto(SiteGroup siteGroup) {
        return modelMapper.map(siteGroup, SiteGroupDto.class);
    }
}
