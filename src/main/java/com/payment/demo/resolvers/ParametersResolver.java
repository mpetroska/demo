package com.payment.demo.resolvers;

import com.payment.demo.dto.PaymentDto;
import com.payment.demo.model.PaymentType;
import java.math.BigDecimal;

/**
 *  Resolver should provide PaymentType and CancelationRate for Payment operations
 *  based on PaymentDto object fields configuration.
 */
public interface ParametersResolver {

    /**
     * resolves does this resolver data could be applied to payment which will be created from DTO.
     *
     * @param paymentDto payment creation DTO object
     * @return booelan does this resolver could be applied to payment
     */
    boolean doApllyToPayment(PaymentDto paymentDto);

    /**
     * provides payment type
     *
     * @return PaymentType
     */
    PaymentType getPaymentType();

    /**
     * provides cancelation fee size depending of PaymentType.
     *
     * @return BigDecimal
     */
    BigDecimal getCancelationRate();
}
