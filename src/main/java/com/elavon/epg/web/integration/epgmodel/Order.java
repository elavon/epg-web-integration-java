package com.elavon.epg.web.integration.epgmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Order
{
    private String id;
    private Total total;
}
