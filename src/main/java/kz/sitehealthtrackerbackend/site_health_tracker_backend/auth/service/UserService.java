package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.Role;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.User;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.enums.ERole;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request.RegisterUserRequest;

import java.util.Set;

public interface UserService {

    User findById(Long id);

    User findByEmail(String email);

    Role findUserRoleByEName(ERole eRole, boolean throwExc);

    User getCurrentUser(boolean throwExc);

    void register(RegisterUserRequest request);

    void updatePassword(User user);

    void save(User user);

    boolean existsByEmail(String email);

    Set<Role> getRoles(Set<ERole> requestRoles);
}