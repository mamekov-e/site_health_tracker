package kz.sitehealthtrackerbackend.site_health_tracker_backend.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Delimiters {
    REPLACING_DELIMITER("<d>"),
    HTML_BR("<br>"),
    NEW_LINE("\n");

    public final String DELIMITER;
}

