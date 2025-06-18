package com.elavon.epg.web.integration.epgmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PaymentSessionRequest
{
    private String order;
    private String returnUrl;
    private String cancelUrl;
    private String OriginUrl;
    private Boolean doThreeDSecure;
    private Boolean doCreateTransaction;
}
