package kz.sitehealthtracker.site_health_tracker.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import kz.sitehealthtracker.site_health_tracker.model.statuses.SiteGroupStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "site_groups")
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties(value = {"sites"})
public class SiteGroup extends BaseEntity<Long> {
    @Serial
    private static final long serialVersionUID = -4641758150802733484L;
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
    private List<Site> sites = new ArrayList<>();

    public void addSites(List<Site> sitesOfGroup) {
        for (Site site : sitesOfGroup) {
            this.sites.add(site);
            site.getGroups().add(this);
        }
    }

    public void removeSites(List<Site> sitesOfGroup) {
        for (Site site : sitesOfGroup) {
            this.sites.remove(site);
            site.getGroups().remove(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SiteGroup siteGroup)) return false;
        return super.getId().equals(siteGroup.getId()) && getName().equals(siteGroup.getName()) && Objects.equals(getDescription(), siteGroup.getDescription()) && getStatus() == siteGroup.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), getName(), getDescription(), getStatus());
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
