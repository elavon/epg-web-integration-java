package com.elavon.epg.web.integration;

import com.elavon.epg.web.integration.config.EPGProperties;
import com.elavon.epg.web.integration.epgmodel.LightboxData;
import com.elavon.epg.web.integration.epgmodel.Order;
import com.elavon.epg.web.integration.epgmodel.PaymentSession;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.util.Map;


@Controller
@Data
public class EPGLightboxController
{
    private final EPGApiClient apiClient;
    private final EPGProperties epgProperties;

    // sample page for entering amount and currency and purchasing
    @RequestMapping(value = "/epg/lightbox/epg-lightbox-merchant-demo", method = RequestMethod.GET)
    public String epgLightboxDemoPage(Model model)
    {
        model.addAttribute("src", epgProperties.getHpp() + "/client/library.js");
        return "epg/lightbox/epg-lb-merchant-demo";
    }

    @RequestMapping(value = "/epg/lightbox/epg-lb-form-data", method = RequestMethod.POST)
    public ResponseEntity<LightboxData> epgLightboxFormData(@RequestParam("amount") String amount, @RequestParam("currency-code") String currencyCode, Model model) throws com.fasterxml.jackson.core.JsonProcessingException
    {
        Order order = apiClient.createOrder(amount, currencyCode);
        PaymentSession response = apiClient.createPaymentSession(order);

        LightboxData lb = new LightboxData();
        lb.setSessionId(response.getId());

        return new ResponseEntity<LightboxData>(lb, HttpStatus.OK);
    }

    // process return from 3ds verification.  create a transaction
    @RequestMapping(value = "/epg/lightbox/epg-lb-3ds-results", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> epgPost3dsResults(@RequestParam("sessionId") String sessionId, Model model) throws Exception //, java.io.UnsupportedEncodingException
    {
        // retrieve the payment-session to get the order
        PaymentSession ps = apiClient.retrievePaymentSession(sessionId);
        String transactionJson = apiClient.retrieveTransaction(ps.getTransaction());

        // parse the transaction JSON to enforce output encoding and type safety
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> sanitizedResponse = objectMapper.readValue(transactionJson, new TypeReference<>() {});

        return ResponseEntity.ok(sanitizedResponse);
    }
}
