package kz.sitehealthtrackerbackend.site_health_tracker_backend.web.controller;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteGroupService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.utils.ConverterUtil;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos.SiteDto;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos.SiteGroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/site-groups")
@CrossOrigin(origins = "http://localhost:3000")
public class SiteGroupController {
    @Autowired
    private SiteGroupService siteGroupService;

    @GetMapping
    public ResponseEntity<Page<SiteGroupDto>> getAllSiteGroupsInPage(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize,
                sortDir.equalsIgnoreCase(Sort.Direction.ASC.toString()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        Page<SiteGroup> sites = siteGroupService.getAllSiteGroupsInPage(pageRequest);
        Page<SiteGroupDto> sitesDto = ConverterUtil.convertPage(sites, SiteGroupDto.class);
        return ResponseEntity.ok(sitesDto);
    }

    @GetMapping("/search/{searchText}")
    public ResponseEntity<Page<SiteGroupDto>> getAllSiteGroupsInPageWithSearch(Pageable pageable, @PathVariable("searchText") String searchText) {
        Page<SiteGroup> sites = siteGroupService.getAllSiteGroupsInPageWithSearchText(pageable, searchText);
        Page<SiteGroupDto> sitesDto = ConverterUtil.convertPage(sites, SiteGroupDto.class);
        return ResponseEntity.ok(sitesDto);
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
