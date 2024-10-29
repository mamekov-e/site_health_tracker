package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.annotation.ValidPhone;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final String PHONE_PATTERN = "^\\+7 \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}$";

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isEmpty()) {
            return true;
        }

        return phone.matches(PHONE_PATTERN);
    }
}
