package com.payment.demo.services;

import com.payment.demo.dto.PaymentCancelationResponse;
import com.payment.demo.dto.PaymentDto;
import com.payment.demo.dto.PaymentResponse;
import com.payment.demo.dto.SearchPayment;
import com.payment.demo.exceptions.ValidationException;
import com.payment.demo.model.Payment;
import com.payment.demo.resolvers.ParametersResolver;
import com.payment.demo.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {

    private final List<ParametersResolver> availableResolvers;

    private final PaymentRepository paymentRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentResponse savePayment(PaymentDto paymentDto) {

        primaryValidation(paymentDto);
        Payment payment = mapToEntity(findResolver(p -> p.doApllyToPayment(paymentDto)), paymentDto);
        payment = paymentRepository.save(payment);

        return new PaymentResponse(payment.getCancelationLink().toString(), payment.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelPayment(UUID paymentUuid) {

        SearchPayment search = new SearchPayment();
        search.setIsCanceled(false);
        search.setCancelationLink(paymentUuid);

        List<Payment> found = paymentRepository.customSearchPayments(search);
        if (found.size() != 1) {
            throw new ValidationException("provided cancelation link is not valid");
        }
         paymentRepository.save(addFeeToEntity(found.get(0)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> getActivePayments(BigDecimal minAmmount) {
        if (minAmmount != null) {
            validateAmountPositivity(minAmmount);
        }
        return paymentRepository.searchActivePaymentsWithLimitedAmmount(minAmmount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentCancelationResponse findCanceledpayments(long id) {

        return paymentRepository.findCanceledPaymentById(id).orElseThrow(() -> new IllegalArgumentException("such canceled payment with provided id does not exist in database"));
    }


    private ParametersResolver findResolver(Predicate<? super ParametersResolver> check) {

        List<ParametersResolver> resolver = availableResolvers.stream().filter(check).collect(Collectors.toList());

        if (resolver.size() != 1) {
            throw new IllegalArgumentException("cann't determine payment type based on provided data");
        }
        return resolver.get(0);
    }

    private Payment mapToEntity(ParametersResolver resolver, PaymentDto paymentDto) {

        Payment payment = new Payment();
        payment.setAmmount(paymentDto.getAmmount());
        payment.setBicCode(paymentDto.getBicCode());
        payment.setCreditorIban(paymentDto.getCreditorIban());
        payment.setDebtorIban(paymentDto.getDebtorIban());
        payment.setCurrency(paymentDto.getCurrency());
        payment.setDetails(paymentDto.getDetails());
        payment.setPaymentType(resolver.getPaymentType());
        payment.setCreationTime(LocalDateTime.now());
        payment.setCancelationLink(UUID.randomUUID());

        return payment;
    }

    private Payment addFeeToEntity(Payment payment) {

        if (LocalDate.now().isAfter(payment.getCreationTime().toLocalDate())) {
            throw new IllegalArgumentException("It is too late to cancel payment");
        }

        LocalDateTime cancelationTime = LocalDateTime.now();
        BigDecimal rate = findResolver(p -> p.getPaymentType() == payment.getPaymentType()).getCancelationRate();
        long hours = Duration.between(payment.getCreationTime(), cancelationTime).toHours();

        payment.setCanceled(true);
        payment.setCancelationFee(rate.multiply(new BigDecimal(hours)));
        payment.setCancelationTime(cancelationTime);

        return payment;
    }

    private void primaryValidation(PaymentDto paymentDto) {
        if (paymentDto.getAmmount() == null
                || paymentDto.getCurrency() == null
                || paymentDto.getCreditorIban() == null
                || paymentDto.getDebtorIban() == null) {
            throw new ValidationException("some obligatory request fields must not be null values");
        }
        validateAmountPositivity(paymentDto.getAmmount());
    }

    private void validateAmountPositivity(BigDecimal ammount) {
        if (!(ammount.signum() == 1)) {
            throw new ValidationException("ammount field must be positive value");
        }
    }
}