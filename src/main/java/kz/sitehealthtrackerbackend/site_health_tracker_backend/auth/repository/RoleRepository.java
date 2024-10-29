package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.repository;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.Role;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
