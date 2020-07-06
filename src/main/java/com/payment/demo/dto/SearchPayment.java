package com.payment.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Search object used for predicated building in repository {@link com.payment.demo.repository.PaymentRepositoryCustomImpl}.
 */
@Data
public class SearchPayment {
    private Long id;
    private UUID cancelationLink;
    private Boolean isCanceled;
    private BigDecimal minAmmount;
}
