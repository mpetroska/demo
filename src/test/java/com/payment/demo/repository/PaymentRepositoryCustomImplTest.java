package com.payment.demo.repository;


import com.payment.demo.dto.PaymentCancelationResponse;
import com.payment.demo.dto.SearchPayment;
import com.payment.demo.model.Currency;
import com.payment.demo.model.Payment;
import com.payment.demo.model.PaymentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
class PaymentRepositoryCustomImplTest {

    private static final UUID UUID1 = UUID.fromString("f443fbeb-aa84-45d1-a1ab-db5612e4969a");
    private static final UUID UUID2 = UUID.fromString("12cdf2fa-ba1b-49fa-a40c-2b7ade391eda");
    private static final UUID UUID3 = UUID.fromString("12cdf2fa-ba1b-49fa-a40c-2b7ade391ed9");
    private static final UUID UUID4 = UUID.fromString("12cdf2fa-ba1b-49fa-a40c-2b7ade391ed7");

    private static final BigDecimal AMMOUNT_1 = new BigDecimal("11.33");
    private static final BigDecimal AMMOUNT_2 = new BigDecimal("0.90");

    private static final String DEBTOR_IBAN = "DK9520000123456789";
    private static final String CREDITOR_IBAN = "BG18RZBB91550123456789";
    private static final String BIC_CODE = "DEUTDEFF333";
    private static final String DETAILS = "details";

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        List<Payment> list = new ArrayList<>();
        list.add(preparePaymentObjects(PaymentType.PAYMENT1, UUID1, LocalDateTime.now(), Currency.EUR, AMMOUNT_1, DEBTOR_IBAN, CREDITOR_IBAN, DETAILS, null, false));
        list.add(preparePaymentObjects(PaymentType.PAYMENT2, UUID2, LocalDateTime.now().minus(1, ChronoUnit.DAYS), Currency.USD, AMMOUNT_2, DEBTOR_IBAN, CREDITOR_IBAN, null, null, true));
        list.add(preparePaymentObjects(PaymentType.PAYMENT3, UUID3, LocalDateTime.now(), Currency.EUR, AMMOUNT_2, DEBTOR_IBAN, CREDITOR_IBAN, null, BIC_CODE, false));
        paymentRepository.saveAll(list);
    }

    @Test
    void shouldRetrieveAllActive() {
        List<Long> result = paymentRepository.searchActivePaymentsWithLimitedAmmount(null);
        assertThat(result.size(), equalTo(2));
    }

    @Test
    void shouldRetrieveActiveWithLimitedAmmount() {
        List<Long> result = paymentRepository.searchActivePaymentsWithLimitedAmmount(AMMOUNT_1);
        assertThat(result.size(), equalTo(1));
    }

    @Test
    void shouldFindActiveByUuid() {
        SearchPayment search = new SearchPayment();
        search.setIsCanceled(false);
        search.setCancelationLink(UUID1);

        List<Payment> found = paymentRepository.customSearchPayments(search);

        assertThat(found.size(), equalTo(1));
        assertThat(found.get(0).getPaymentType(), equalTo(PaymentType.PAYMENT1));
        assertThat(found.get(0).getCurrency(), equalTo(Currency.EUR));
        assertThat(found.get(0).getAmmount(), equalTo(AMMOUNT_1));
    }

    @Test
    void shouldNotFindInactiveByUuid() {
        SearchPayment search = new SearchPayment();
        search.setIsCanceled(false);
        search.setCancelationLink(UUID2);

        List<Payment> found = paymentRepository.customSearchPayments(search);

        assertThat(found.size(), equalTo(0));
    }

    @Test
    void shouldFindInactiveById() {

        Payment payment = preparePaymentObjects(PaymentType.PAYMENT2, UUID4, LocalDateTime.now().minus(1, ChronoUnit.DAYS), Currency.USD, AMMOUNT_2, DEBTOR_IBAN, CREDITOR_IBAN, null, null, true);
        payment.setCancelationFee(AMMOUNT_2);
        Long id = paymentRepository.save(payment).getId();

        Optional<PaymentCancelationResponse> response = paymentRepository.findCanceledPaymentById(id);

        assertThat(response.isPresent(), equalTo(true));
        assertThat(response.get().getCancelationFee(), equalTo(AMMOUNT_2));
    }

    @Test
    void shouldnotFindActiveById() {

        Payment payment = preparePaymentObjects(PaymentType.PAYMENT2, UUID4, LocalDateTime.now().minus(1, ChronoUnit.DAYS), Currency.USD, AMMOUNT_2, DEBTOR_IBAN, CREDITOR_IBAN, null, null, false);
        payment.setCancelationFee(AMMOUNT_2);
        Long id = paymentRepository.save(payment).getId();

        Optional<PaymentCancelationResponse> response = paymentRepository.findCanceledPaymentById(id);

        assertThat(response.isPresent(), equalTo(false));
    }

    private Payment preparePaymentObjects(PaymentType paymenType, UUID cancelationUuid, LocalDateTime creationTime
            , Currency currency, BigDecimal amount, String debtorIban, String creditorIban, String details, String bicCode, boolean isCanceled) {
        Payment result = new Payment();
        result.setPaymentType(paymenType);
        result.setCancelationLink(cancelationUuid);
        result.setCreationTime(creationTime);
        result.setCurrency(currency);
        result.setAmmount(amount);
        result.setDebtorIban(debtorIban);
        result.setCreditorIban(creditorIban);
        result.setDetails(details);
        result.setBicCode(bicCode);
        result.setCanceled(isCanceled);
        return result;
    }
}