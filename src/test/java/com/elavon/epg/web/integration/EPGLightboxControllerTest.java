package com.elavon.epg.web.integration;

import com.elavon.epg.web.integration.config.EPGProperties;
import com.elavon.epg.web.integration.epgmodel.Order;
import com.elavon.epg.web.integration.epgmodel.PaymentSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EPGLightboxControllerTest
{

    @InjectMocks
    private EPGLightboxController epgLightboxController;

    @Mock
    private EPGApiClient apiClient;

    @Mock
    private EPGProperties epgProperties;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(epgLightboxController).build();
    }

    @Test
    void testEPGLightboxDemoPage() throws Exception {
        mockMvc.perform(get("/epg/lightbox/epg-lightbox-merchant-demo"))
                .andExpect(status().isOk());
    }

    @Test
    void testEPGLightboxFormData() throws Exception {
        Order order = new Order();
        PaymentSession paymentSession = new PaymentSession();
        when(apiClient.createOrder(any(String.class), any(String.class))).thenReturn(order);
        when(apiClient.createPaymentSession(any(Order.class))).thenReturn(paymentSession);

        mockMvc.perform(post("/epg/lightbox/epg-lb-form-data")
                .param("amount", "100")
                .param("currency-code", "USD"))
                .andExpect(status().isOk());
    }

    @Test
    void testEPGPost3dsResults() throws Exception {
        Order order = new Order();
        PaymentSession paymentSession = new PaymentSession();
        paymentSession.setTransaction("transaction-uri");
        when(apiClient.createOrder(any(String.class), any(String.class))).thenReturn(order);
        when(apiClient.createPaymentSession(any(Order.class))).thenReturn(paymentSession);
        when(apiClient.retrievePaymentSession(any(String.class))).thenReturn(paymentSession);

        // Mock transaction JSON response
        String transactionJson = """
        {
            "href": "https://uat.api.converge.eu.elavonaws.com/transactions/t48p28ggk4hj7w7d23qvtwt7ktc9",
            "id": "t48p28ggk4hj7w7d23qvtwt7ktc9",
            "createdAt": "2025-05-16T18:40:58.861Z"
        }
    """;
        //when(apiClient.retrieveTransaction(any(String.class))).thenReturn(transactionJson);
        when(apiClient.retrieveTransaction(eq("transaction-uri"))).thenReturn(transactionJson);

        // Mock API property
        when(epgProperties.getApi()).thenReturn("http://localhost:8080/api");


        mockMvc.perform(post("/epg/lightbox/epg-lb-3ds-results")
                        .param("sessionId", "123"))
                .andExpect(status().isOk());
    }}