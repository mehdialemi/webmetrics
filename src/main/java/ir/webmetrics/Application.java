package ir.webmetrics;

import ir.webmetrics.model.*;
import ir.webmetrics.service.InputLoader;
import ir.webmetrics.service.MetricCalculator;
import ir.webmetrics.service.MetricWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputLoader loader = new InputLoader();

        List<Click> clicks = loader.getClicks(classLoader.getResourceAsStream("clicks.json"));
        logger.info("Loaded {} clicks", clicks.size());

        List<Impression> impressions = loader.getImpression(classLoader.getResourceAsStream("impressions.json"));
        logger.info("Loaded {} impressions", impressions.size());

        MetricCalculator metricCalculator = new MetricCalculator();
        metricCalculator.init(clicks, impressions);
        Map<Dimension, Metric> metrics = metricCalculator.getMetrics();
        MetricWriter metricWriter = new MetricWriter("/tmp/metrics.json");
        metricWriter.writeMetrics(metrics.values());

        Map<Dimension, List<Long>> recommendation = metricCalculator.getRecommendations();
        metricWriter = new MetricWriter("/tmp/recommendations.json");
        List<Recommendation> recommendations = recommendation.entrySet()
                .stream().map(e -> new Recommendation(e.getKey().getApp_id(), e.getKey().getCountry_code(), e.getValue()))
                .collect(Collectors.toList());
        metricWriter.writeRecommendations(recommendations);
    }
}
