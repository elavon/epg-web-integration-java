package com.elavon.epg.web.integration;

import com.elavon.epg.web.integration.epgmodel.*;
import com.elavon.epg.web.integration.config.EPGProperties;
import lombok.Data;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Data
@Service
public class EPGApiClient
{
    private final RestTemplate apiRestTemplate;
    private final EPGProperties epgProperties;

    public Order createOrder(String amount, String currencyCode)
    {
        OrderRequest orderRequest = new OrderRequest();
        Total total = new Total();
        total.setAmount(amount);
        total.setCurrencyCode(currencyCode);
        orderRequest.setTotal(total);

        HttpEntity request = new HttpEntity(orderRequest);

        Order order = apiRestTemplate.postForObject( "/orders", request, Order.class);
        return order;
    }

    // a payment-session must exist before calling 3ds.  It is composed of an order and other attributes needed by hpp and the lightbox
    public PaymentSession createPaymentSession(Order order)
    {
        PaymentSessionRequest pr = new PaymentSessionRequest();
        pr.setOrder( epgProperties.getApi() + "/orders/" + order.getId());

        // return and cancel used for hpp but not the lightbox
        pr.setReturnUrl(epgProperties.getMerchantSite() + "/epg/hpp/return");
        pr.setCancelUrl(epgProperties.getMerchantSite() + "/epg/hpp/cancel");

        // originurl is required for the lightbox but not hpp
        pr.setOriginUrl(epgProperties.getMerchantSite());
        pr.setDoThreeDSecure(true);
        pr.setDoCreateTransaction(true);

        // create request
        HttpEntity request = new HttpEntity(pr);
        PaymentSession result = apiRestTemplate.postForObject( "/payment-sessions", request, PaymentSession.class);
        return result;
    }

    public PaymentSession retrievePaymentSession(String sessionId)
    {
        HttpEntity request = new HttpEntity(null);

        if (!sessionId.matches("^[a-zA-Z0-9\\-]+$")) {
            throw new IllegalArgumentException("Invalid sessionId");
        }

        String paymentsessionuri = UriComponentsBuilder
                .fromHttpUrl(epgProperties.getApi())
                .pathSegment("payment-sessions", sessionId)
                .build()
                .toUriString();

        ResponseEntity<PaymentSession>
                response = apiRestTemplate.exchange(paymentsessionuri, HttpMethod.GET, request, PaymentSession.class);
        PaymentSession ps = response.getBody();
        return ps;
    }


    public String retrieveTransaction(String transactionurifromapi)
    {
        HttpEntity request = new HttpEntity(null);

        // rebuild the URI to avoid a fortify Server Request Forgery (SRF) vulnerability
        String transactionId = transactionurifromapi.substring(transactionurifromapi.lastIndexOf("/") + 1);

        String transactionuri = UriComponentsBuilder
                .fromHttpUrl(epgProperties.getApi())
                .pathSegment("transactions", transactionId)
                .build()
                .toUriString();

        ResponseEntity<String> response =
                apiRestTemplate.exchange(transactionuri, HttpMethod.GET, request, String.class);
        return response.getBody();
    }

}
