package cz.jkdabing.backend.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceUtils {

    private PriceUtils() {
    }

    public static BigDecimal calculateTotalPrice(BigDecimal price, BigDecimal vat) {
        BigDecimal vatDecimal = vat.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP).add(BigDecimal.ONE);
        BigDecimal totalPrice = price.multiply(vatDecimal);
        return totalPrice.setScale(2, RoundingMode.HALF_UP);
    }
}
