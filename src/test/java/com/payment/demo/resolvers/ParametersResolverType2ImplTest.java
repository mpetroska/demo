package com.payment.demo.resolvers;

import com.payment.demo.dto.PaymentDto;
import com.payment.demo.model.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class ParametersResolverType2ImplTest {

    private static final String DETAILS = "details";
    private static final String BIC = "bic";

    @InjectMocks
    private ParametersResolverType2Impl resolver;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void doApllyToPayment() {
        //given
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setCurrency(Currency.USD);
        paymentDto.setDetails(DETAILS);
        paymentDto.setBicCode(null);

        //when
        boolean applies = resolver.doApllyToPayment(paymentDto);

        //then
        assertTrue(applies);
    }

    @Test
    void shoulfailApplyToPaymentIfBicCodeExists(){
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setCurrency(Currency.USD);
        paymentDto.setBicCode(BIC);
        //when
        boolean applies = resolver.doApllyToPayment(paymentDto);

        //then
        assertFalse(applies);
    }

    @Test
    void shouldFailApplyToPaymentIfNotUsd(){
        //given
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setCurrency(Currency.EUR);

        //when
        boolean applies = resolver.doApllyToPayment(paymentDto);

        //then
        assertFalse(applies);
    }
}