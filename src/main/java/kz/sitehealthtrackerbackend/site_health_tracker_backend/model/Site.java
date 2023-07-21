package kz.sitehealthtrackerbackend.site_health_tracker_backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "sites")
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties(value = {"groups"})
public class Site extends BaseEntity<Long> {
    @Serial
    private static final long serialVersionUID = 1128829114369223468L;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "url", unique = true)
    private String url;
    @Column(name = "site_health_check_interval")
    private Integer siteHealthCheckInterval;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "site_status")
    @Type(PostgreSQLEnumType.class)
    private SiteStatus status;
    @ManyToMany(mappedBy = "sites")
    private List<SiteGroup> groups = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Site site)) return false;
        return super.getId().equals(site.getId()) &&
                getName().equals(site.getName()) &&
                Objects.equals(getDescription(), site.getDescription()) &&
                Objects.equals(getSiteHealthCheckInterval(), site.getSiteHealthCheckInterval()) &&
                getUrl().equals(site.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), getName(), getDescription(), getSiteHealthCheckInterval(), getUrl());
    }

    @Override
    public String toString() {
        return "Site{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", siteHealthCheckInterval=" + siteHealthCheckInterval +
                ", status=" + status +
                '}';
    }
}
