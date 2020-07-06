package com.payment.demo.resolvers;

import com.payment.demo.dto.PaymentDto;
import com.payment.demo.model.Currency;
import com.payment.demo.model.PaymentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Type2 resolver
 */
@Component
public class ParametersResolverType2Impl implements ParametersResolver {

    @Value("${type2.fee:0.10}")
    private BigDecimal fee;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doApllyToPayment(PaymentDto paymentDto) {
        return Currency.USD == paymentDto.getCurrency() && paymentDto.getBicCode() == null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentType getPaymentType() {
        return PaymentType.PAYMENT2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getCancelationRate() {
        return fee;
    }
}
