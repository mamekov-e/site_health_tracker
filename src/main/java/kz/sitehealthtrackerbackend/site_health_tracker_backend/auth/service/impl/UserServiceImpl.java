package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.impl;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.EmailConfirmationToken;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.Role;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.User;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.enums.ERole;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request.RegisterUserRequest;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.repository.RoleRepository;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.repository.UserRepository;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.security.SecurityUtils;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.EmailConfirmationTokenService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.EmailSenderService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.UserService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception.BadRequestException;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception.NotFoundException;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.EntityNames;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailSenderService emailSenderService;
    private final EmailConfirmationTokenService emailConfirmationTokenService;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> NotFoundException.entityNotFoundById(EntityNames.USER.getName(), id));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> NotFoundException.entityNotFoundBy(EntityNames.USER.getName(), email));
    }

    @Override
    public Role findUserRoleByEName(ERole eName, boolean throwExc) {
        return roleRepository.findByName(eName)
                .orElseThrow(() -> NotFoundException.entityNotFoundBy(EntityNames.ROLE.getName(), eName.name()));
    }

    @Override
    public User getCurrentUser(boolean throwExc) {
        return findByEmail(SecurityUtils.getCurrentUserLogin());
    }

    @Override
    public void register(RegisterUserRequest request) {
        if (existsByEmail(request.getEmail()))
            throw BadRequestException.entityWithFieldValueAlreadyExist(EntityNames.USER.getName(), request.getEmail());

        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getFirstName(),
                request.getLastName(),
                request.getMiddleName()
        );
        user.setRoles(getRoles(Set.of(ERole.ROLE_USER)));
        save(user);
        EmailConfirmationToken token = emailConfirmationTokenService.createEmailConfirmationToken(user, LocalDateTime.now().plusDays(3));

        emailSenderService.sendEmailConfirmation(user.getEmail(), token);
    }

    @Override
    @Modifying
    public void updatePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Set<Role> getRoles(Set<ERole> requestRoles) {
        Set<Role> roles = new HashSet<>();

        if (requestRoles == null || requestRoles.isEmpty()) {
            throw NotFoundException.entityNotFoundBy(EntityNames.ROLE.getName(), "role");
        }

        requestRoles.forEach(erole -> {
            Role userRole = findUserRoleByEName(erole, true);
            roles.add(userRole);
        });
        return roles;
    }
}