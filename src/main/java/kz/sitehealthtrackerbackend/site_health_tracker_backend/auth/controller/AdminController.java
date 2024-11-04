package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.controller;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.dto.UserDto;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.UserService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.SecurityController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/admin")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminController implements SecurityController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Page<UserDto>> getAllUsersInPage(@RequestParam(required = false, defaultValue = "0") int pageNumber,
                                                           @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                           @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                           @RequestParam(required = false, defaultValue = "desc") String sortDir) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.fromString(sortDir), sortBy);

        return ResponseEntity.ok(userService.getAll(pageable));
    }

    @GetMapping("/users/search/{searchText}")
    public ResponseEntity<Page<UserDto>> getAllSitesInPageWithSearch(Pageable pageable, @PathVariable("searchText") String searchText) {
        return ResponseEntity.ok(userService.getAllWithSearchText(pageable, searchText));
    }

}
