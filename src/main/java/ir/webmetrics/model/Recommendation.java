package ir.webmetrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation {
    private long app_id;
    private String country_code;
    private List<Long> recommended_advertiser_ids = new ArrayList<>();
}
