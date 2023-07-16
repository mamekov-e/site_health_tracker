package kz.sitehealthtracker.site_health_tracker.repository;

import kz.sitehealthtracker.site_health_tracker.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {
    List<Email> findAllByEnabledIs(boolean enabled);

    Optional<Email> findByAddress(String address);

    Email findByVerificationCode(String code);
}
