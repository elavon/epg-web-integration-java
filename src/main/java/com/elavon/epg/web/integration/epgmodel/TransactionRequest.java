package com.elavon.epg.web.integration.epgmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TransactionRequest
{
    private Total total;
    private String hostedCard;
    private String paymentSession;
    private String doCapture;
    private ThreeDSecure threeDSecure;
}
