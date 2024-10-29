package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.repository;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
