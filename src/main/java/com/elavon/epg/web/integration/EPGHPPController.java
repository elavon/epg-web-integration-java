package com.elavon.epg.web.integration;

import com.elavon.epg.web.integration.config.EPGProperties;
import com.elavon.epg.web.integration.epgmodel.Order;
import com.elavon.epg.web.integration.epgmodel.PaymentSession;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.elavon.epg.web.integration.epgmodel.*;
import org.springframework.web.util.UriComponentsBuilder;

// used in the EPG Hosted Payment Page (HPP) example which shows uses a dedicated page to take the credit card details
@Controller
@Data
public class EPGHPPController
{
    private final EPGApiClient apiClient;
    private final EPGProperties epgProperties;

    // sample page for entering amount and currency and purchasing
    @GetMapping(value = "/epg/hpp/epg-hpp-merchant-demo")
    public String epgHPPDemoPage(Model model)
    {
        return "epg/hpp/epg-hpp-merchant-demo";
    }

    // post amounts here which will create an order and a payment-session and forward to the 3ds verification page
    @PostMapping(value = "/epg/hpp/epg-hpp-open-hpp")
    public String epgOpenHPP(@RequestParam("amount") String amount, @RequestParam("currency-code") String currencyCode,
        Model model) throws com.fasterxml.jackson.core.JsonProcessingException
    {
        Order order = apiClient.createOrder(amount, currencyCode);
        PaymentSession response = apiClient.createPaymentSession(order);

        UriComponentsBuilder hppUrlBuilder = UriComponentsBuilder.fromUriString(epgProperties.getHpp())
                .queryParam("sessionId", response.getId());

        return "redirect:" + hppUrlBuilder.build().toUri();
    }

    // used for hpp integration to receive data back from 3ds verification
    @GetMapping(value = "/epg/hpp/return")
    public String epgReturn(@RequestParam("sessionId") String sessionId,
        Model model) throws Exception
    {
        PaymentSession ps = apiClient.retrievePaymentSession(sessionId);
        String transaction = apiClient.retrieveTransaction(ps.getTransaction());

        JSONParser transactionParser = new JSONParser();
        JSONObject jsonTransaction = (JSONObject) transactionParser.parse(transaction);
        Boolean authorized = (Boolean) jsonTransaction.get("isAuthorized");
        Boolean heldforreview = (Boolean) jsonTransaction.get("isHeldForReview");
        model.addAttribute("authorized", authorized.toString());
        model.addAttribute("heldforreview", heldforreview.toString());
        model.addAttribute("response", transaction);
        return "epg/done";
    }

    // called in hpp integration when user cancels out of credit card page
    @GetMapping(value = "/epg/hpp/cancel")
    public String epgCancel(@RequestParam("sessionId") String sessionId, Model model) throws Exception
    {
        return "epg/hpp/epg-hpp-cancel";
    }
}