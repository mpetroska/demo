package com.payment.demo.controller;

import com.payment.demo.api.PaymentRestService;
import com.payment.demo.dto.PaymentCancelationResponse;
import com.payment.demo.dto.PaymentDto;
import com.payment.demo.dto.PaymentResponse;
import com.payment.demo.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


/**
 * Payment REST service implementation.
 */
@RequiredArgsConstructor
@RestController
public class PaymentRestServiceImpl implements PaymentRestService {

    private final PaymentService paymentService;

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentResponse doPayment(PaymentDto paymentDto) {
        return paymentService.savePayment(paymentDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelPayment(String uuid) {
        paymentService.cancelPayment(UUID.fromString(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> getActivePaymentsWithMinAmmount(BigDecimal minAmmount) {
        return paymentService.getActivePayments(minAmmount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentCancelationResponse getCanceledById(long id) {
        return paymentService.findCanceledpayments(id);
    }

}
