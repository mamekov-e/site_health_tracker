package kz.sitehealthtrackerbackend.site_health_tracker_backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.User;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteGroupStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "site_groups")
@Getter
@Setter
@RequiredArgsConstructor
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
    private SiteGroupStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
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
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SiteGroup siteGroup = (SiteGroup) o;
        return Objects.equals(getName(), siteGroup.getName()) && Objects.equals(getDescription(), siteGroup.getDescription()) && getStatus() == siteGroup.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getDescription(), getStatus());
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
