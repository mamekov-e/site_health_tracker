package kz.sitehealthtracker.site_health_tracker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name = "status")
    private String status;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "site_group",
            joinColumns = @JoinColumn(name = "site_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<Site> sites;
}
