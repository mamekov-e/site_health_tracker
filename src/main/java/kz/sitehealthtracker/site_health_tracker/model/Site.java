package kz.sitehealthtracker.site_health_tracker.model;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import kz.sitehealthtracker.site_health_tracker.model.enums.SiteStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @ManyToMany(mappedBy = "sites", fetch = FetchType.EAGER)
    private List<SiteGroup> groups = new ArrayList<>();

//    @PreRemove
//    public void removeGroupsAssociation() {
//        for (SiteGroup group : this.groups) {
//            group.getSites().remove(this);
//        }
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Site site)) return false;
        return super.getId().equals(site.getId()) && getName().equals(site.getName()) && Objects.equals(getDescription(), site.getDescription()) && getUrl().equals(site.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), getName(), getDescription(), getUrl());
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
