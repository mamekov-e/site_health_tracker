package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.dto;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.Role;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.User;

import java.util.HashSet;
import java.util.Set;

public class UserAdapter {

    public static UserDto toDto(User user) {
        UserDto dto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .phone(user.getPhone())
                .createdDate(user.getCreatedDate())
                .build();

        Set<String> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            roles.add(role.getName().name());
        }
        dto.setRoles(roles);

        return dto;
    }
}
