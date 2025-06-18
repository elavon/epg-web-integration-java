package com.elavon.epg.web.integration.epgmodel;

import lombok.Data;

@Data
public class ThreeDSecure
{
    private String directoryServerTransactionId;
    private String transactionStatus;
    private String electronicCommerceIndicator;
    private String authenticationValue;
}
