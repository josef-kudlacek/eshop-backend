package cz.jkdabing.backend.validation;

import cz.jkdabing.backend.dto.CouponDTO;
import cz.jkdabing.backend.enums.DiscountType;
import cz.jkdabing.backend.validation.annotation.ValidDiscount;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class DiscountValidator implements ConstraintValidator<ValidDiscount, CouponDTO> {

    @Override
    public boolean isValid(CouponDTO couponDTO, ConstraintValidatorContext constraintValidatorContext) {
        if (couponDTO == null) {
            addErrorMessage(constraintValidatorContext, "Discount cannot be null");
            return false;
        }

        if (DiscountType.VALUE.name().equals(couponDTO.getDiscountType())) {
            return couponDTO.getDiscountValue() != null
                    && couponDTO.getDiscountValue().compareTo(BigDecimal.ZERO) > 0;
        } else if (DiscountType.PERCENTAGE.name().equals(couponDTO.getDiscountType())) {
            return couponDTO.getDiscountValue() != null
                    && couponDTO.getDiscountValue().compareTo(BigDecimal.ZERO) > 0
                    && couponDTO.getDiscountValue().compareTo(BigDecimal.valueOf(100)) <= 0;
        } else {
            return true;
        }
    }

    private void addErrorMessage(ConstraintValidatorContext context, String errorMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage)
                .addConstraintViolation();
    }
}
