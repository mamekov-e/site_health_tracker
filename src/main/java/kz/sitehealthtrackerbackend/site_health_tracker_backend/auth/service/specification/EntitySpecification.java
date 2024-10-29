package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.specification;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request.FilterParams;
import org.springframework.data.jpa.domain.Specification;

public class EntitySpecification {

    public static <T> Specification<T> buildSpecification(FilterParams filterParams) {
        Specification<T> spec = Specification.where(null);

        if (filterParams.getStatus() != null) {
            spec = spec.and(hasStatus(filterParams.getStatus()));
        }

        return spec;
    }

    private static <T> Specification<T> hasStatus(String status) {
        return (root, query, cb) -> cb.equal(root.get("status").as(String.class), status);
    }
}
