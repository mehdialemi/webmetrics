package ir.webmetrics;

import ir.webmetrics.model.*;
import ir.webmetrics.service.InputLoader;
import ir.webmetrics.service.MetricCalculator;
import ir.webmetrics.service.MetricWriter;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException {
        Options options = new Options();

        Option option = new Option("c", "clicks", true, "clicks.json file path");
//        option.setRequired(true);
        options.addOption(option);

        option = new Option("i", "impressions", true, "impressions.json file path");
//        option.setRequired(true);
        options.addOption(option);

        option = new Option("o", "output", true, "output directory for metrics and recommendation");
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        String clicksFileName = cmd.getOptionValue("clicks");
        String impressionsFileName = cmd.getOptionValue("impressions");
        String outputDirectory = cmd.getOptionValue("output", "/tmp");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputLoader loader = new InputLoader();

        InputStream inputStream = clicksFileName == null ? classLoader.getResourceAsStream("clicks.json")
                : new FileInputStream(clicksFileName);
        List<Click> clicks = loader.getClicks(inputStream);
        logger.info("Loaded {} clicks", clicks.size());

        inputStream = impressionsFileName == null ? classLoader.getResourceAsStream("impressions.json")
                : new FileInputStream(impressionsFileName);
        List<Impression> impressions = loader.getImpression(inputStream);
        logger.info("Loaded {} impressions", impressions.size());

        MetricCalculator metricCalculator = new MetricCalculator();
        metricCalculator.init(clicks, impressions);
        Map<Dimension, Metric> metrics = metricCalculator.getMetrics();
        MetricWriter metricWriter = new MetricWriter(outputDirectory + "/metrics.json");
        metricWriter.writeMetrics(metrics.values());

        Map<Dimension, List<Long>> recommendation = metricCalculator.getRecommendations();
        metricWriter = new MetricWriter(outputDirectory + "/recommendations.json");
        List<Recommendation> recommendations = recommendation.entrySet()
                .stream().map(e -> new Recommendation(e.getKey().getApp_id(), e.getKey().getCountry_code(), e.getValue()))
                .collect(Collectors.toList());
        metricWriter.writeRecommendations(recommendations);
    }
}
