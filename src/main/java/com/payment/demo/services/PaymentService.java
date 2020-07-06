package com.payment.demo.services;

import com.payment.demo.dto.PaymentCancelationResponse;
import com.payment.demo.dto.PaymentDto;
import com.payment.demo.dto.PaymentResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Payment Service to manage REST operations.
 */
public interface PaymentService {

    /**
     * validates and saves payments.
     *
     * @param paymentDto rest DTO object
     * @return PaymentResponse
     */
    PaymentResponse savePayment(PaymentDto paymentDto);

    /**
     * Cancels payment and calculates cancelation fee.
     *
     * @param paymentUuid cacekationlink
     */
    void cancelPayment(UUID paymentUuid);

    /**
     * Retrieves active payments ids. Could be filtered by minAmmount.
     *
     * @param minAmmount BigDecimal
     * @return List of Ids
     */
    List<Long> getActivePayments(BigDecimal minAmmount);

    /**
     * Retrieves canceled payment by id.
     *
     * @param id canceled payment Id
     * @return PaymentCancelationResponse
     */
    PaymentCancelationResponse findCanceledpayments(long id);
}
