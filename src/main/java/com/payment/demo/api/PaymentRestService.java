package com.payment.demo.api;

import com.payment.demo.dto.PaymentCancelationResponse;
import com.payment.demo.dto.PaymentDto;
import com.payment.demo.dto.PaymentResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Payment REST service API.
 */
@Api(value = "Payment system")
@RequestMapping("/api/payments")
public interface PaymentRestService {

    /**
     * Creates payment from provided payment information
     *
     * @param paymentDto DTO object with data required for Payment to create
     * @return PaymentResponse with required data for retrieving and cancelation
     */
    @ApiOperation(value = "process payment", response = PaymentResponse.class)
    @ApiParam(value = "PaymentDto object must be provided into request body")
    @PostMapping("")
    PaymentResponse doPayment(@RequestBody PaymentDto paymentDto);

    /**
     * Cancels payment based on received payment cancelation UUID
     *
     * @param uuid string value of payment cancelation UUID
     */
    @ApiOperation(value = "Delete payment")
    @ApiParam(value = "provide transaction UUID as path variable to cancel payment")
    @DeleteMapping("/{uuid}")
    void cancelPayment(@PathVariable("uuid") String uuid);

    /**
     * Retrieves list of payment ids. List could be be filtered by minimum amount.
     *
     * @param minAmmount BigDecimal value for filtration. Must be null or positive value
     * @return List<Long> returns list of active payments.
     */
    @ApiOperation(value = "Get Active payments Ids")
    @ApiParam(value = "provide minimum displayed ammount as request parameter", required = false)
    @GetMapping("")
    List<Long> getActivePaymentsWithMinAmmount(@RequestParam(name = "minAmmount", required = false) BigDecimal minAmmount);

    /**
     * Retrieves canceled payments by id
     *
     * @param id Payment entity Id
     * @return PaymentCancelationResponse response object with cancelation data
     */
    @ApiOperation(value = "Get canceled payment by Id")
    @ApiParam(value = "provide canceled payment id as path variable")
    @GetMapping("/canceled/{id}")
    PaymentCancelationResponse getCanceledById(@PathVariable("id") long id);
}
