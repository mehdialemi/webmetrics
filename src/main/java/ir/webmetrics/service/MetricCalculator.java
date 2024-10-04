package ir.webmetrics.service;

import ir.webmetrics.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class MetricCalculator {
    private static final Logger logger = LoggerFactory.getLogger(MetricCalculator.class);

    private List<Click> clicks = new ArrayList<>();
    private List<Impression> impressions = new ArrayList<>();

    private Map<String, RevCounter> clickMap = new HashMap<>();

    private Map<Dimension, List<Impression>> dimensionMap = new HashMap<>();

    public void init(List<Click> clicks, List<Impression> impressions) {
        this.clicks.addAll(clicks);
        this.impressions.addAll(impressions);
        for (Click click : clicks) {
            clickMap.merge(click.getImpression_id(), new RevCounter(click.getRevenue()), RevCounter::sum);
        }
        logger.info("Calculated clicks for {} impression ids", clickMap.size());

        for (Impression impression : impressions) {
            Dimension dimension = new Dimension(impression.getApp_id(), impression.getCountry_code());
            List<Impression> list = dimensionMap.computeIfAbsent(dimension, k -> new ArrayList<>());
            list.add(impression);
        }
    }

    public Map<Dimension, List<Long>> getRecommendations() {
        Map<Dimension, List<Long>> recommendations = new HashMap<>();
        for (Map.Entry<Dimension, List<Impression>> e : dimensionMap.entrySet()) {
            Map<Long, Metric> metricMap = new HashMap<>();
            for (Impression impression : e.getValue()) {
                Metric metric = metricMap.get(impression.getAdvertiser_id());
                if (metric == null) {
                    metric = new Metric();
                    metricMap.put(impression.getAdvertiser_id(), metric);
                    updateMetric(impression, metric);
                    metric.setImpressions(1);
                } else {
                    updateMetric(impression, metric);
                    metric.setImpressions(metric.getImpressions() + 1);
                }
            }
            List<Long> advertiserIds = metricMap.entrySet().stream().sorted(
                    Comparator.comparingDouble(o -> o.getValue().getRevenue() / o.getValue().getImpressions()))
                    .limit(5)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            recommendations.put(e.getKey(), advertiserIds);
        }
        return recommendations;
    }

    public Map<Dimension, Metric> getMetrics() {
        Map<Dimension, Metric> metricMap = new HashMap<>();
        for (Map.Entry<Dimension, List<Impression>> entry : dimensionMap.entrySet()) {
            Dimension dimension = entry.getKey();
            for (Impression impression : entry.getValue()) {
                Metric metric = metricMap.get(dimension);
                if (metric == null) {
                    metric = new Metric();
                    metricMap.put(dimension, metric);
                    metric.setApp_id(impression.getApp_id());
                    metric.setCountry_code(impression.getCountry_code());
                    updateMetric(impression, metric);
                    metric.setImpressions(1);
                } else {
                    updateMetric(impression, metric);
                    metric.setImpressions(metric.getImpressions() + 1);
                }
            }
        }
        logger.info("Found {} metrics", metricMap.size());
        return metricMap;
    }

    private void updateMetric(Impression impression, Metric metric) {
        RevCounter revCounter = clickMap.get(impression.getId());
        if (revCounter != null) {
            metric.setClicks(revCounter.getCounter());
            metric.setRevenue(revCounter.getRevenue());
        }
    }
}
