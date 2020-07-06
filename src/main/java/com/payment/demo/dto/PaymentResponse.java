package com.payment.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@AllArgsConstructor
@Getter
@ApiModel(description = "Payment response object with cancelation link")
public class PaymentResponse {

    @ApiModelProperty(notes = "Payment cancelation link")
    private String cancelationLink;

    @ApiModelProperty(notes = "Payment Id")
    private long paymentId;
}
