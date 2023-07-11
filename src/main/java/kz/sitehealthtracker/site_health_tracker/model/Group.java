package kz.sitehealthtracker.site_health_tracker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "groups")
@Data
@NoArgsConstructor
public class Group extends BaseEntity<Long> {
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private String status;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "site_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "site_id"))
    private List<Site> sites;
}
