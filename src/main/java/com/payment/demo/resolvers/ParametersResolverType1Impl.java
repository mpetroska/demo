package com.payment.demo.resolvers;

import com.payment.demo.dto.PaymentDto;
import com.payment.demo.model.Currency;
import com.payment.demo.model.PaymentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Type1 resolver
 */
@Component
public class ParametersResolverType1Impl implements ParametersResolver {

    @Value("${type1.fee:0.05}")
    private BigDecimal fee;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doApllyToPayment(PaymentDto paymentDto) {
        return Currency.EUR == paymentDto.getCurrency() && paymentDto.getDetails() != null && paymentDto.getBicCode() == null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentType getPaymentType() {
        return PaymentType.PAYMENT1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getCancelationRate() {
        return fee;
    }
}
