package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.security;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.stream.Collectors;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String userName = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                MyUserDetails springSecurityUser = (MyUserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getEmail();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            }
        }
        return userName;
    }

    public static Long getCurrentUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Long userId = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                MyUserDetails springSecurityUser = (MyUserDetails) authentication.getPrincipal();
                userId = springSecurityUser.getId();
            }
        }
        return userId;
    }

    private static MyUserDetails getCurrentUserDetails() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return authentication != null && authentication.getPrincipal() instanceof UserDetails ? (MyUserDetails) authentication.getPrincipal() : null;
    }

    private static Set<String> getCurrentUserRoles() {
        return getCurrentUserDetails() != null ? getCurrentUserDetails().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet()) : null;
    }

    public static boolean hasAnyRoleCurrentUser(String... roles) {
        return hasAnyRole(roles, getCurrentUserRoles());
    }

    public static boolean hasAnyRole(Set<Role> userRoles, String... roles) {
        Set<String> userRolesStr = userRoles.stream()
                .map(userRole -> userRole.getName().name())
                .collect(Collectors.toSet());
        return hasAnyRole(roles, userRolesStr);
    }

    private static boolean hasAnyRole(String[] roles, Set<String> userRoles) {
        if (userRoles != null) {
            for (String role : roles) {
                if (userRoles.contains(role))
                    return true;
            }
        }
        return false;
    }
}
