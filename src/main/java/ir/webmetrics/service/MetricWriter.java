package ir.webmetrics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.webmetrics.model.Metric;
import ir.webmetrics.model.Recommendation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

public class MetricWriter {

    private final String fileName;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MetricWriter(String fileName) {
        this.fileName = fileName;
    }

    public void writeMetrics(Collection<Metric> metrics) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        objectMapper.writeValue(fileOutputStream, metrics);
    }

    public void writeRecommendations(Collection<Recommendation> metrics) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        objectMapper.writeValue(fileOutputStream, metrics);
    }
}
