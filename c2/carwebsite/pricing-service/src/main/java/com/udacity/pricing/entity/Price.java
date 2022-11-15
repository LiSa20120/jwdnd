package com.udacity.pricing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Represents the price of a given vehicle, including currency.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Price {
    private String currency;
    private BigDecimal price;
    @Id
    private Long vehicleId;
}
