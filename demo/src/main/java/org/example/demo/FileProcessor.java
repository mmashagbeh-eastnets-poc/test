package org.example.demo;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

public class FileProcessor {

    private static final Logger LOGGER = Logger.getLogger(FileProcessor.class.getName());
    private static final int THREAD_POOL_SIZE = 4;

    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private final BlockingQueue<Path> fileQueue = new LinkedBlockingQueue<>();

    public void startProcessing(String inputDir, String outputDir) {
        try {
            Files.walk(Paths.get(inputDir))
                    .filter(Files::isRegularFile)
                    .forEach(fileQueue::offer);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error scanning input directory", e);
            return;
        }

        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            executor.submit(() -> {
                while (true) {
                    Path file = fileQueue.poll();
                    if (file == null) break; // Stop when queue is empty
                    processFile(file, Paths.get(outputDir));
                }
            });
        }

        shutdownExecutor();
    }

    private void processFile(Path inputFile, Path outputDir) {
        try {
            List<String> lines = Files.readAllLines(inputFile);

            List<String> processedLines = new ArrayList<>();
            for (String line : lines) {
                processedLines.add(transformData(line));
            }

            Path outputFile = outputDir.resolve(inputFile.getFileName());
            Files.write(outputFile, processedLines);

            LOGGER.info("Processed file: " + inputFile.getFileName());

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error processing file: " + inputFile, e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error for file: " + inputFile, e);
        }
    }

    private String transformData(String data) {
        // Simulating complex data transformation logic
        return new StringBuilder(data).reverse().toString().toUpperCase();
    }

    private void shutdownExecutor() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(2, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java FileProcessor <inputDir> <outputDir>");
            return;
        }

        String inputDir = args[0];
        String outputDir = args[1];

        FileProcessor processor = new FileProcessor();
        processor.startProcessing(inputDir, outputDir);
    }
}
