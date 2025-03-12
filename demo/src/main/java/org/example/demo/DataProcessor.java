package org.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataProcessor {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sampledb";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void fetchDataByIdAndProcess() {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name FROM data WHERE id in (1,2,3)")) {

            preparedStatement.setString(1, "PENDING");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                executorService.submit(() -> processRecord(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fetchDataAndProcess() {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name FROM data WHERE status = ?")) {

            preparedStatement.setString(1, "PENDING");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                executorService.submit(() -> processRecord(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void processRecord(int id, String name) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE data SET status = ? WHERE id = ?")) {

            String processedName = name.toUpperCase();  // Sample processing logic
            System.out.println("Processed record " + id + ": " + processedName);

            preparedStatement.setString(1, "PROCESSED");
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error processing record " + id + ": " + e.getMessage());
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public static void main(String[] args) {
        DataProcessor processor = new DataProcessor();
        processor.fetchDataAndProcess();
        processor.shutdown();
    }
}
