package com.payment.demo.resolvers;

import com.payment.demo.dto.PaymentDto;
import com.payment.demo.model.PaymentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Type3 resolver
 */
@Component
public class ParametersResolverType3Impl implements ParametersResolver {

    @Value("${type3.fee:0.15}")
    private BigDecimal fee;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doApllyToPayment(PaymentDto paymentDto) {
        return paymentDto.getBicCode() != null && paymentDto.getDetails() == null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentType getPaymentType() {
        return PaymentType.PAYMENT3;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getCancelationRate() {
        return fee;
    }
}
