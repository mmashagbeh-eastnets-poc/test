package org.example.demo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class DataAggregator {

    private static final int THREAD_POOL_SIZE = 10;
    private static final String[] API_ENDPOINTS = {
            "https://api.service1.com/data",
            "https://api.service2.com/data",
            "https://api.service3.com/data"
    };

    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public Map<String, String> fetchDataConcurrently() {
        Map<String, Future<String>> futureResults = new HashMap<>();

        for (String endpoint : API_ENDPOINTS) {
            futureResults.put(endpoint, executor.submit(() -> fetchDataFromApi(endpoint)));
        }

        Map<String, String> aggregatedData = new HashMap<>();
        for (Map.Entry<String, Future<String>> entry : futureResults.entrySet()) {
            try {
                aggregatedData.put(entry.getKey(), entry.getValue().get(5, TimeUnit.SECONDS));
            } catch (TimeoutException e) {
                System.err.println("Timeout for endpoint: " + entry.getKey());
                entry.getValue().cancel(true);
            } catch (Exception e) {
                System.err.println("Failed to fetch data from: " + entry.getKey() + " - " + e.getMessage());
            }
        }

        shutdownExecutor();
        return aggregatedData;
    }

    private String fetchDataFromApi(String apiUrl) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            if (connection.getResponseCode() == 200) {
                try (Scanner scanner = new Scanner(connection.getInputStream())) {
                    return scanner.useDelimiter("\\A").next();
                }
            } else {
                throw new IOException("Unexpected response code: " + connection.getResponseCode());
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void shutdownExecutor() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        DataAggregator aggregator = new DataAggregator();
        Map<String, String> data = aggregator.fetchDataConcurrently();

        System.out.println("Aggregated Data:");
        data.forEach((url, result) -> System.out.println(url + " -> " + result.substring(0, Math.min(100, result.length())) + "..."));
    }
}
