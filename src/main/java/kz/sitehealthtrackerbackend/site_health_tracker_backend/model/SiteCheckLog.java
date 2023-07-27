package kz.sitehealthtrackerbackend.site_health_tracker_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "site_check_logs")
@Data
@NoArgsConstructor
public class SiteCheckLog extends BaseEntity<Long> {
    @Serial
    private static final long serialVersionUID = 1128829114369223468L;

    @Column(name = "check_time", nullable = false)
    private LocalDateTime checkTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "site_status", nullable = false)
    @Type(PostgreSQLEnumType.class)
    private SiteStatus status;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "site_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Site site;

    public SiteCheckLog(LocalDateTime checkTime, SiteStatus status, Site site) {
        this.checkTime = checkTime;
        this.status = status;
        this.site = site;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SiteCheckLog that)) return false;
        return getCheckTime().equals(that.getCheckTime()) && getStatus() == that.getStatus() && getSite().equals(that.getSite());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCheckTime(), getStatus(), getSite());
    }

    @Override
    public String toString() {
        return "SiteCheckLog{" +
                "checkTime=" + checkTime +
                ", status=" + status +
                '}';
    }
}
