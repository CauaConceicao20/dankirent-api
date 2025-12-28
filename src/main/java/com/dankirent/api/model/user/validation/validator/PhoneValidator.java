package com.dankirent.api.model.user.validation.validator;

import com.dankirent.api.model.user.validation.annotations.Phone;
import jakarta.validation.ConstraintValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public boolean isValid(String phone, jakarta.validation.ConstraintValidatorContext context) {
        return phone != null && phone.matches("^\\(\\d{2}\\) \\d{5}-\\d{4}$");
    }
}
