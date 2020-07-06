package com.payment.demo.services;

import com.payment.demo.dto.PaymentCancelationResponse;
import com.payment.demo.dto.PaymentDto;
import com.payment.demo.dto.PaymentResponse;
import com.payment.demo.dto.SearchPayment;
import com.payment.demo.exceptions.ValidationException;
import com.payment.demo.model.Currency;
import com.payment.demo.model.Payment;
import com.payment.demo.model.PaymentType;
import com.payment.demo.repository.PaymentRepository;
import com.payment.demo.resolvers.ParametersResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    public static final String DEBTOR_IBAN = "LT601010012345678901";
    public static final String CREDITOR_IBAN = "LV97HABA0012345678910";
    public static final UUID CANCELATION_LINK = UUID.randomUUID();
    public static final long ID = 10l;
    public static final BigDecimal CANCELATION_FEE = BigDecimal.ONE;
    public static final BigDecimal POSITIVE_AMOUNT = BigDecimal.ONE;


    @InjectMocks
    private PaymentServiceImpl service;

    @Spy
    private List<ParametersResolver> resolvers = initMockResolver();

    @Mock
    private PaymentRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void shouldSavePayment() {
        //given
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setAmmount(new BigDecimal("100"));
        paymentDto.setCreditorIban(CREDITOR_IBAN);
        paymentDto.setDebtorIban(DEBTOR_IBAN);
        paymentDto.setCurrency(Currency.USD);

        Payment payment = new Payment();
        payment.setId(ID);
        payment.setCancelationLink(CANCELATION_LINK);
        when(repository.save(any(Payment.class))).thenReturn(payment);

        //when
        PaymentResponse result = service.savePayment(paymentDto);

        //then
        verify(repository).save(any(Payment.class));
        assertNotNull(result);
        assertEquals(result.getPaymentId(), ID);
        assertEquals(result.getCancelationLink(), CANCELATION_LINK.toString());
    }

    @Test
    void shouldFailValidationifCurrencyMissing() {
        //given
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setAmmount(new BigDecimal("100"));
        paymentDto.setCreditorIban(CREDITOR_IBAN);
        paymentDto.setDebtorIban(DEBTOR_IBAN);

        //when
        assertThrows(ValidationException.class,
                () -> {
                    service.savePayment(paymentDto);
                }
        );
    }

    @Test
    void shouldFailValidationifAmmountisNegative() {
        //given
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setAmmount(new BigDecimal("-10"));
        paymentDto.setCreditorIban(CREDITOR_IBAN);
        paymentDto.setDebtorIban(DEBTOR_IBAN);
        paymentDto.setCurrency(Currency.USD);

        //when
        assertThrows(ValidationException.class,
                () -> {
                    service.savePayment(paymentDto);
                }
        );
    }

    @Test
    void shouldTworExceptionDuringCancel() {
        //given
        List<Payment> list = Collections.emptyList();
        when(repository.customSearchPayments(new SearchPayment())).thenReturn(list);

        //when
        assertThrows(ValidationException.class,
                () -> {
                    service.cancelPayment(CANCELATION_LINK);
                }
        );
    }

    @Test
    void shouldCancelPayment() {
        //given
        Payment payment = new Payment();
        payment.setCreationTime(LocalDateTime.now());
        payment.setPaymentType(PaymentType.PAYMENT1);
        List<Payment> list = Arrays.asList(payment);
        when(repository.customSearchPayments(any(SearchPayment.class))).thenReturn(list);
        when(repository.save(any(Payment.class))).thenReturn(any(Payment.class));

        //when
        service.cancelPayment(CANCELATION_LINK);

        //then
        verify(repository).customSearchPayments(any(SearchPayment.class));
        verify(repository).save(any(Payment.class));
    }

    @Test
    void shouldFailCancelIfNotTodayPayment() {
        //given
        Payment payment = new Payment();
        payment.setCreationTime(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
        List<Payment> list = Arrays.asList(payment);
        when(repository.customSearchPayments(any(SearchPayment.class))).thenReturn(list);

        //when
        assertThrows(IllegalArgumentException.class,
                () -> {
                    service.cancelPayment(CANCELATION_LINK);
                }
        );
    }

    @Test
    void getActivePayments() {
        //given
        List<Long> resultList = Arrays.asList(1L, 2L, 3L);
        when(repository.searchActivePaymentsWithLimitedAmmount(any(BigDecimal.class))).thenReturn(resultList);

        //when
        List<Long> result = service.getActivePayments(POSITIVE_AMOUNT);

        //then
        verify(repository).searchActivePaymentsWithLimitedAmmount(any(BigDecimal.class));
        assertNotNull(result);
        assertEquals(result.size(), 3);
    }

    @Test
    void shouldFailIfNotPositiveAmount() {

        //when
        assertThrows(ValidationException.class,
                () -> {
                    service.getActivePayments(BigDecimal.ZERO);
                }
        );
    }

    @Test
    void findCanceledpayments() {
        //given
        Optional<PaymentCancelationResponse> response = Optional.of(new PaymentCancelationResponse(ID, CANCELATION_FEE));
        when(repository.findCanceledPaymentById(any(Long.class))).thenReturn(response);

        //when
        PaymentCancelationResponse result = service.findCanceledpayments(ID);

        //then
        verify(repository).findCanceledPaymentById(any(Long.class));
        assertNotNull(result);
        assertEquals(result.getId(), ID);
        assertEquals(result.getCancelationFee(), CANCELATION_FEE);
    }

    @Test
    void thowExceptionIfNotFound() {
        //given
        Optional<PaymentCancelationResponse> response = Optional.ofNullable(null);
        when(repository.findCanceledPaymentById(any(Long.class))).thenReturn(response);

        //when
        assertThrows(IllegalArgumentException.class,
                () -> {
                    service.findCanceledpayments(ID);
                }
        );
    }

    private List<ParametersResolver> initMockResolver() {
        return new ArrayList(Collections.singleton(new ParametersResolver() {
            @Override
            public boolean doApllyToPayment(PaymentDto paymentDto) {
                return true;
            }

            @Override
            public PaymentType getPaymentType() {
                return PaymentType.PAYMENT1;
            }

            @Override
            public BigDecimal getCancelationRate() {
                return new BigDecimal("0.33");
            }
        }));
    }
}