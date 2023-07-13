package kz.sitehealthtracker.site_health_tracker.model;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import kz.sitehealthtracker.site_health_tracker.model.enums.SiteStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "sites")
@Data
@NoArgsConstructor
public class Site extends BaseEntity<Long> {
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "url", unique = true)
    private String url;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "site_status")
    @Type(PostgreSQLEnumType.class)
    private SiteStatus status;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "group_site",
            joinColumns = @JoinColumn(name = "site_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<SiteGroup> groups = new HashSet<>();

    public void removeGroups(Set<SiteGroup> siteGroups) {
        siteGroups.forEach(this.groups::remove);
        siteGroups.forEach(siteGroup -> siteGroup.getSites().remove(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Site site)) return false;
        return getName().equals(site.getName()) && Objects.equals(getDescription(), site.getDescription()) && getUrl().equals(site.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getUrl());
    }

    @Override
    public String toString() {
        return "Site{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", status=" + status +
                '}';
    }
}
