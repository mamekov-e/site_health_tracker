package kz.sitehealthtracker.site_health_tracker.model;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import kz.sitehealthtracker.site_health_tracker.model.enums.SiteStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;

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
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "sites")
    private List<SiteGroup> groups = new ArrayList<>();
}
