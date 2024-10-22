package kz.sitehealthtrackerbackend.site_health_tracker_backend.web.controller;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteCheckLog;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteCheckLogService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.utils.ConverterUtil;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos.SiteCheckLogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/site-check-logs")
public class SiteCheckLogController {

    @Autowired
    private SiteCheckLogService siteCheckLogService;

    @GetMapping("/{siteId}")
    public ResponseEntity<List<SiteCheckLogDto>> getAllSiteCheckLogsByDay(@PathVariable("siteId") Long siteId, LocalDate date) {
        List<SiteCheckLog> siteCheckLogs = siteCheckLogService.getAllSiteCheckLogsBySiteId(siteId, date);
        List<SiteCheckLogDto> siteCheckLogsDto = ConverterUtil.convertList(siteCheckLogs, SiteCheckLogDto.class);
        return ResponseEntity.ok(siteCheckLogsDto);
    }

}
