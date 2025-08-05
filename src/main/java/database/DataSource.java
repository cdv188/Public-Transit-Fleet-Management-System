package database;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton for managing database connections.
 */
public class DataSource {

    private static DataSource instance;
    private Connection conn = null;

    /** Loads the MySQL JDBC driver. */
    private DataSource() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    /**
     * @return the singleton instance of {@link DataSource}
     */
    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }

    /**
     * Creates and returns a new database connection.
     *
     * @return a new {@link Connection}
     * @throws SQLException if connection fails
     */
    public Connection getConnection() throws SQLException {
        Properties props = new Properties();
        try (InputStream in = Thread.currentThread()
                                   .getContextClassLoader()
                                   .getResourceAsStream("database.properties")) {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String url = props.getProperty("jdbc.url");
        String usr = props.getProperty("jdbc.username");
        String pass = props.getProperty("jdbc.password");

        conn = DriverManager.getConnection(url, usr, pass);
        return conn;
    }
}
