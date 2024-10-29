package kz.sitehealthtrackerbackend.site_health_tracker_backend.web.controller;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteGroupService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.utils.ConverterUtil;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.SecurityController;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos.SiteDto;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos.SiteGroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/site-groups")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
public class SiteGroupController implements SecurityController {
    @Autowired
    private SiteGroupService siteGroupService;

    @Autowired
    private SiteService siteService;

    @GetMapping
    public ResponseEntity<Page<SiteGroupDto>> getAllSiteGroupsInPage(@RequestParam(required = false, defaultValue = "0") int pageNumber,
                                                                     @RequestParam(required = false, defaultValue = "5") int pageSize,
                                                                     @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                                     @RequestParam(required = false, defaultValue = "desc") String sortDir) {
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, Sort.Direction.fromString(sortDir), sortBy);

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
    public ResponseEntity<Page<SiteDto>> getAllGroupSitesById(@PathVariable("groupId") Long id,
                                                              int pageNumber, int pageSize, String sortBy, String sortDir) {
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize,
                sortDir.equalsIgnoreCase(Sort.Direction.ASC.toString()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        SiteGroup siteGroup = siteGroupService.getSiteGroupById(id);
        List<Site> sitesGroup = siteService.getAllSitesBySiteGroup(siteGroup);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), sitesGroup.size());

        List<Site> pageContent = sitesGroup.subList(start, end);
        Page<Site> page = new PageImpl<>(pageContent, pageRequest, sitesGroup.size());
        Page<SiteDto> sitesGroupDto = ConverterUtil.convertPage(page, SiteDto.class);
        return ResponseEntity.ok(sitesGroupDto);
    }

    @GetMapping("/{groupId}/sites/search/{searchText}")
    public ResponseEntity<Page<SiteDto>> getAllGroupSitesInPageWithSearch(Pageable pageable,
                                                                          @PathVariable("groupId") Long id,
                                                                          @PathVariable("searchText") String searchText) {
        Page<Site> sites = siteGroupService.getAllSitesOfGroupByIdInPageWithSearchText(id, pageable, searchText);
        Page<SiteDto> sitesDto = ConverterUtil.convertPage(sites, SiteDto.class);
        return ResponseEntity.ok(sitesDto);
    }

    @PostMapping
    public ResponseEntity<Long> addSiteGroup(@RequestBody SiteGroupDto siteGroupDto) {
        SiteGroup siteGroup = ConverterUtil.convertObject(siteGroupDto, SiteGroup.class);
        siteGroupService.addSiteGroup(siteGroup);
        return new ResponseEntity<>(siteGroup.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/{groupId}/sites/add")
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

    @DeleteMapping
    public ResponseEntity<Void> deleteAllSiteGroups() {
        siteGroupService.deleteAllSiteGroups();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{groupId}/sites/delete")
    public ResponseEntity<Void> deleteSitesFromGroup(@RequestBody List<SiteDto> sitesDto, @PathVariable("groupId") Long id) {
        List<Site> sites = ConverterUtil.convertList(sitesDto, Site.class);

        siteGroupService.deleteSitesFromGroupById(sites, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{groupId}/sites")
    public ResponseEntity<Void> deleteAllSitesFromGroup(@PathVariable("groupId") Long id) {
        siteGroupService.deleteAllSitesFromGroupById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
