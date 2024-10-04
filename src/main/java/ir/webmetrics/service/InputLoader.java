package ir.webmetrics.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.webmetrics.model.Click;
import ir.webmetrics.model.Impression;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class InputLoader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Click> getClicks(InputStream inputStream) throws IOException {
        String input = readFromInputStream(inputStream);
        return objectMapper.readValue(input, new TypeReference<>() {});
    }

    public List<Impression> getImpression(InputStream inputStream) throws IOException {
        String input = readFromInputStream(inputStream);
        return objectMapper.readValue(input, new TypeReference<>() {});
    }

    private static String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

}
