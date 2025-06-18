package com.elavon.epg.web.integration.epgmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OrderRequest
{
    private Total total;
}
