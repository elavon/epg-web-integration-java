package com.elavon.epg.web.integration.epgmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PaymentSession
{
    private String id;
    private String expiresAt;
    private String modifiedAt;
    private String createdAt;
    private String forexAdvice;
    private String order;
    private String transaction;
    private ThreeDSecure threeDSecure;
}
