package com.payment.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Payment entity. Holds all payment related data.
 */
@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private PaymentType paymentType;

    @Column(nullable = false, updatable = false)
    private UUID cancelationLink;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationTime;

    private LocalDateTime cancelationTime;

    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private BigDecimal ammount;

    private BigDecimal cancelationFee;

    @Column(nullable = false)
    private String debtorIban;

    @Column(nullable = false)
    private String creditorIban;

    private String details;

    private String bicCode;

    @Column(nullable = false)
    private boolean isCanceled;
}
