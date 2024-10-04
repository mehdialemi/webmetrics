package ir.webmetrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class Dimension {
    private long app_id;
    private String country_code;
}
