package ir.webmetrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class DimensionAdvertiseId  {
    private Dimension dimension;
    private long advertiserId;
}
