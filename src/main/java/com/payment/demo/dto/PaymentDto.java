package com.payment.demo.dto;


import com.payment.demo.model.Currency;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;

@Getter
@Setter
@ApiModel(description = "Payment creation object")
public class PaymentDto {

    @ApiModelProperty(notes = "Currency value")
    private Currency currency;

    @ApiModelProperty(notes = "money ammount")
    private BigDecimal ammount;

    @ApiModelProperty(notes = "debtor IBAN code")
    private String debtorIban;

    @ApiModelProperty(notes = "creditor IBAN code")
    private String creditorIban;

    @ApiModelProperty(notes = "transfer details")
    private String details;

    @ApiModelProperty(notes = "BIC code")
    private String bicCode;
}
