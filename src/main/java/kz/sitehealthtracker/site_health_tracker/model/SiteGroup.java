package kz.sitehealthtracker.site_health_tracker.model;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import kz.sitehealthtracker.site_health_tracker.model.enums.SiteGroupStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

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
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "group_site",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "site_id"))
    private List<Site> sites;
}
