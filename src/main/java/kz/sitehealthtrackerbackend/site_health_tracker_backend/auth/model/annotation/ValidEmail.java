package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.annotation.validator.EmailValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message() default "Некорректный формат электронной почты. Маска: *@*.*";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
