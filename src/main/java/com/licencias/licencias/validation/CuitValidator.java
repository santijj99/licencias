package com.licencias.licencias.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CuitValidator implements ConstraintValidator<ValidCuit, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        String digits = value.replaceAll("[^0-9]", "");
        if (digits.length() != 11) {
            return false;
        }
        int[] multipliers = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * multipliers[i];
        }
        int mod = 11 - (sum % 11);
        int checkDigit = mod == 11 ? 0 : mod == 10 ? 9 : mod;
        return checkDigit == Character.getNumericValue(digits.charAt(10));
    }
}
