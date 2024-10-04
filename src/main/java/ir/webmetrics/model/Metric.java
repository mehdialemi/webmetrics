package ir.webmetrics.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Metric {
    private long app_id;
    private String country_code;
    private long impressions;
    private long clicks;
    private double revenue;
}
