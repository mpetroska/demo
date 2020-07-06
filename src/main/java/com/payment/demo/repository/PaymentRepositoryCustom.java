package com.payment.demo.repository;

import com.payment.demo.dto.PaymentCancelationResponse;
import com.payment.demo.dto.SearchPayment;
import com.payment.demo.model.Payment;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


/**
 * Custom Payment Repository.
 */
public interface PaymentRepositoryCustom {

        /**
         * Retvieves list of Payments based on custom search object.
         *
         * @param search SearchPayment object provides search criteria
         * @return List of Payment which meets search criteria
         */
        List<Payment> customSearchPayments(SearchPayment search);

        /**
         * Retrieves List of Ids of Active Payments. With possibility to limit by minAmount.
         *
         * @param minAmmount BigDecimal filtration value.
         * @return List of ids of Payments which meet search criteria
         */
        List<Long> searchActivePaymentsWithLimitedAmmount(BigDecimal minAmmount);

        /**
         * Retrieves canceled Payment by Id.
         *
         * @param id canceled Payment Entity id
         * @return Optional object of PaymentCancelationResponse.
         */
        Optional<PaymentCancelationResponse> findCanceledPaymentById(Long id);
}
