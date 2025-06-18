package com.elavon.epg.web.integration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(EPGProperties.class)
@RequiredArgsConstructor
public class RestTemplateConfig
{
    private final EPGProperties epgProperties;

    @Bean
    public RestTemplate apiRestTemplate(RestTemplateBuilder builder)
    {

        return builder
            //.requestFactory(HttpComponentsClientHttpRequestFactory::new)
            .basicAuthentication(epgProperties.getMerchantAlias(), epgProperties.getSecretKey())
            .defaultHeader("Accept-Version", "1")
            .rootUri(epgProperties.getApi())
            .build();
    }
}
