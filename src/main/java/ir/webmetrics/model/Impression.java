package ir.webmetrics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Impression {
    private long app_id;
    private long advertiser_id;
    private String country_code;
    private String id;
}
