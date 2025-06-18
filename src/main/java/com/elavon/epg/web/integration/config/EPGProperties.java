package com.elavon.epg.web.integration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "epg")
public class EPGProperties
{
    private String api;
    private String hpp;
    private String merchantAlias;
    private String secretKey;
    private String merchantSite;
    private String publicApiKey;
}
