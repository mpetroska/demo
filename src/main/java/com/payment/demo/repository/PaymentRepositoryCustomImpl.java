package com.payment.demo.repository;

import com.payment.demo.dto.PaymentCancelationResponse;
import com.payment.demo.dto.SearchPayment;
import com.payment.demo.model.Payment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class PaymentRepositoryCustomImpl implements PaymentRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Payment> customSearchPayments(SearchPayment search) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Payment> q = cb.createQuery(Payment.class);
        Root<Payment> root = q.from(Payment.class);

        q.where(buildPredicates(search, cb, root).toArray(new Predicate[0]));

        return em.createQuery(q).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> searchActivePaymentsWithLimitedAmmount(BigDecimal minAmmount) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<Payment> root = q.from(Payment.class);
        q.select(root.get("id"));

        SearchPayment search = new SearchPayment();
        search.setIsCanceled(false);
        search.setMinAmmount(minAmmount);
        q.where(buildPredicates(search, cb, root).toArray(new Predicate[0]));

        return em.createQuery(q).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PaymentCancelationResponse> findCanceledPaymentById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PaymentCancelationResponse> q = cb.createQuery(PaymentCancelationResponse.class);
        Root<Payment> root = q.from(Payment.class);
        q.multiselect(root.get("id"), root.get("cancelationFee"));

        SearchPayment search = new SearchPayment();
        search.setId(id);
        search.setIsCanceled(true);
        q.where(buildPredicates(search, cb, root).toArray(new Predicate[0]));

        List<PaymentCancelationResponse> resultList = em.createQuery(q).getResultList();
        return Optional.ofNullable(resultList.isEmpty() ? null : resultList.get(0));
}

    private List<Predicate> buildPredicates(SearchPayment search, CriteriaBuilder cb, Root root) {
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(cb.equal(root.get("id"), search.getId()));
        }

        if (search.getCancelationLink() != null) {
            predicates.add(cb.equal(root.get("cancelationLink"), search.getCancelationLink()));
        }

        if(search.getIsCanceled() != null) {
            predicates.add(search.getIsCanceled() ? cb.isTrue(root.get("isCanceled")) : cb.isFalse(root.get("isCanceled")));
        }

        if (search.getMinAmmount() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("ammount").as(BigInteger.class), search.getMinAmmount()));
        }

        return predicates;
    }
}