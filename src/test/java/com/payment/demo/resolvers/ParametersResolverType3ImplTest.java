package com.payment.demo.resolvers;

import com.payment.demo.dto.PaymentDto;
import com.payment.demo.model.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class ParametersResolverType3ImplTest {

    private static final String DETAILS = "details";
    private static final String BIC = "bic";

    @InjectMocks
    private ParametersResolverType3Impl resolver;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldPassdoApllyToPayment() {
        //given
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setDetails(null);
        paymentDto.setBicCode(BIC);

        //when
        boolean applies = resolver.doApllyToPayment(paymentDto);

        //then
        assertTrue(applies);
    }

    @Test
    void shouldFaildoApllyToPaymentIfBicNull(){
        //given
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setBicCode(null);

        //when
        boolean applies = resolver.doApllyToPayment(paymentDto);

        //then
        assertFalse(applies);
    }

    @Test
    void shouldFaildoApllyToPaymentIfDetailsNotNull(){
        //given
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setDetails(DETAILS);
        paymentDto.setBicCode(BIC);

        //when
        boolean applies = resolver.doApllyToPayment(paymentDto);

        //then
        assertFalse(applies);
    }
}