package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.annotation.ValidEmail;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String EMAIL_PATTERN = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) {
            return true;
        }

        return email.matches(EMAIL_PATTERN);
    }
}
