package ir.webmetrics.model;

import lombok.Data;

@Data
public class RevCounter {
    private double revenue;
    private long counter;

    public RevCounter(double revenue) {
        this.revenue = revenue;
        this.counter = 1;
    }

    public static RevCounter sum(RevCounter a, RevCounter b) {
        a.counter ++;
        a.revenue += b.revenue;
        return a;
    }
}
