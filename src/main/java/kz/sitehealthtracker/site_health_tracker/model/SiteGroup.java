package kz.sitehealthtracker.site_health_tracker.model;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import kz.sitehealthtracker.site_health_tracker.model.enums.SiteGroupStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "site_groups")
@Data
@NoArgsConstructor
public class SiteGroup extends BaseEntity<Long> {
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Type(PostgreSQLEnumType.class)
    private SiteGroupStatus status;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "group_site",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "site_id"))
    private Set<Site> sites = new HashSet<>();

    public void addSites(Set<Site> sitesList) {
        this.sites.addAll(sitesList);
    }

    public void removeSites(List<Site> sitesOfGroup) {
        sitesOfGroup.forEach(this.sites::remove);
        sitesOfGroup.forEach(site -> site.getGroups().remove(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SiteGroup siteGroup)) return false;
        return getName().equals(siteGroup.getName()) && Objects.equals(getDescription(), siteGroup.getDescription()) && getStatus() == siteGroup.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getStatus());
    }

    @Override
    public String toString() {
        return "SiteGroup{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
