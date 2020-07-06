package com.payment.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@ApiModel(description = "Payment cancelation object")
public class PaymentCancelationResponse {

    @ApiModelProperty(notes = "Payment Id")
    private Long id;

    @ApiModelProperty(notes = "Payment cancelation fee")
    private BigDecimal cancelationFee;
}

