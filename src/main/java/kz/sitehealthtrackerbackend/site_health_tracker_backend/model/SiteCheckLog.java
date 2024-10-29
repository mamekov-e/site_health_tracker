package kz.sitehealthtrackerbackend.site_health_tracker_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "site_check_logs")
@Getter
@Setter
@RequiredArgsConstructor
public class SiteCheckLog extends BaseEntity<Long> {
    @Serial
    private static final long serialVersionUID = 1128829114369223468L;

    @Column(name = "check_time", nullable = false)
    private LocalDateTime checkTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "site_status", nullable = false)
    private SiteStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "site_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @ToString.Exclude
    private Site site;

    public SiteCheckLog(LocalDateTime checkTime, SiteStatus status, Site site) {
        this.checkTime = checkTime;
        this.status = status;
        this.site = site;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SiteCheckLog that = (SiteCheckLog) o;
        return Objects.equals(getCheckTime(), that.getCheckTime()) && getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCheckTime(), getStatus());
    }

    @Override
    public String toString() {
        return "SiteCheckLog{" +
                "checkTime=" + checkTime +
                ", status=" + status +
                '}';
    }
}
