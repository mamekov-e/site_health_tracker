package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.repository;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query(value = """
                select u from User u
                where u.email like concat('%', ?1, '%')
                or u.firstName like concat('%', ?1, '%')
                or u.lastName like concat('%', ?1, '%')
                or u.middleName like concat('%', ?1, '%')
                or u.phone like concat('%', ?1, '%')
            """)
    Page<User> findAllBySearchText(String searchText, Pageable pageable);
}
