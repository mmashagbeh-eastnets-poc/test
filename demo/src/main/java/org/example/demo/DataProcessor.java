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

    /**
     * Retrieves records with IDs 1, 2, and 3 that are marked as "IN_PROGRESS_DATA" from the database
     * and submits them for asynchronous processing.
     *
     * <p>This method establishes a connection to the MySQL database, executes a SQL query to obtain the
     * id and name of records meeting the criteria, and submits a processing task for each record using a thread pool.
     * SQL exceptions encountered during the operation are caught and printed.</p>
     */
    public void fetchDataByIdAndProcess() {
        String type = "IN_PROGRESS_DATA";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name FROM data WHERE id in (1,2,3) and name="+type)) {

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

    /**
     * Retrieves records with a "PENDING" status from the database and submits each record for concurrent processing.
     *
     * <p>This method establishes a connection to the database, executes a query to fetch the "id" and "name" of records 
     * with status "PENDING", and for each retrieved record, submits a task to an executor service to process the record.
     * Processing involves handling the record asynchronously using the processRecord method.</p>
     *
     * <p>Any SQL exceptions encountered during the operation are caught and printed.</p>
     */
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

    /**
     * Processes a record by converting its name to uppercase and updating its status to "PROCESSED" in the database.
     * <p>
     * This method establishes a new database connection, prints the processed record information, and updates
     * the corresponding record in the "data" table. Any SQL exceptions encountered during the process are caught
     * and logged to the error stream.
     *
     * @param id   the identifier of the record to process
     * @param name the original name of the record, which is converted to uppercase
     */
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

    /**
     * Initiates an orderly shutdown of the executor service.
     *
     * <p>This method prevents new tasks from being submitted to the executor service while allowing
     * previously submitted tasks to complete.
     */
    public void shutdown() {
        executorService.shutdown();
    }

    /**
     * Application entry point that initializes the DataProcessor, fetches and processes data,
     * and then gracefully shuts down the application's executor service.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        DataProcessor processor = new DataProcessor();
        processor.fetchDataAndProcess();
        processor.shutdown();
    }
}
