package com.payment.demo.repository;

import com.payment.demo.model.Payment;
import org.springframework.data.repository.CrudRepository;


public interface PaymentRepository extends CrudRepository<Payment, Long>, PaymentRepositoryCustom {

}
