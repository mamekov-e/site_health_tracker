package kz.sitehealthtracker.site_health_tracker.model;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import kz.sitehealthtracker.site_health_tracker.model.enums.SiteGroupStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;

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
    private List<Site> sites = new ArrayList<>();

    public void addSites(List<Site> sitesList) {
        this.sites.addAll(sitesList);
        sitesList.forEach(site -> site.getGroups().add(this));
    }

    public void removeSites(List<Site> sitesOfGroup) {
        this.sites.removeAll(sitesOfGroup);
        sitesOfGroup.forEach(site -> site.getGroups().remove(this));
    }
}
