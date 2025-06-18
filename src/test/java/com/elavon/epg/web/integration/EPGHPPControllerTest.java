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
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;


class EPGHPPControllerTest
{

    @InjectMocks
    private EPGHPPController epgHPPController;

    @Mock
    private EPGApiClient apiClient;

    @Mock
    private EPGProperties epgProperties;

    //private WebTestClient webTestClient;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(epgHPPController).build();
    }


    @Test
    void testEPGOpenHPP() throws Exception {
        Order order = new Order();
        PaymentSession paymentSession = new PaymentSession();
        when(apiClient.createOrder(any(String.class), any(String.class))).thenReturn(order);
        when(apiClient.createPaymentSession(any(Order.class))).thenReturn(paymentSession);
        when(epgProperties.getHpp()).thenReturn("http://localhost:8080/hpp");

        mockMvc.perform(post("/epg/hpp/epg-hpp-open-hpp?amount=100&currency-code=USD"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testEPGReturn() throws Exception
    {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("create.transaction.response.json");
        String jsonRequest = IOUtils.toString(inputStream, "UTF-8");

        Order order = new Order();
        PaymentSession paymentSession = new PaymentSession();
        when(apiClient.createOrder(any(String.class), any(String.class))).thenReturn(order);
        when(apiClient.createPaymentSession(any(Order.class))).thenReturn(paymentSession);
        when(apiClient.retrievePaymentSession(any(String.class))).thenReturn(paymentSession);
        when(apiClient.retrieveTransaction(any(String.class))).thenReturn(jsonRequest);
        when(epgProperties.getApi()).thenReturn("http://localhost:8080/api");
        when(epgProperties.getHpp()).thenReturn("http://localhost:8080/hpp");
        paymentSession.setTransaction(apiClient.retrieveTransaction(""));

        mockMvc.perform(get("/epg/hpp/return?sessionId=123"))
                .andExpect(status().isOk());
    }

    @Test
    void testEPGCancel() throws Exception {
        mockMvc.perform(get("/epg/hpp/cancel?sessionId=123"))
                .andExpect(status().isOk());
    }
}