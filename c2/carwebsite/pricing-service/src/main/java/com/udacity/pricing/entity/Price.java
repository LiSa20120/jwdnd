package com.udacity.pricing.domain.price;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents the price of a given vehicle, including currency.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Price {
    private String currency;
    private BigDecimal price;
    private Long vehicleId;
}
