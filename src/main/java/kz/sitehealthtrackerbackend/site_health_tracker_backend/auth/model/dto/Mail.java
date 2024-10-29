package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.dto;

import lombok.Data;

import java.util.Map;

@Data
public class Mail {
    private String from;
    private String to;
    private String subject;
    private Map<String, Object> model;
}
