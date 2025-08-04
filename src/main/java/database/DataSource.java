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
 * Student Number: 041170924013
 * Lab Section: 013 CST8288
 * @author Chester Don Valencerina
 * 
 * Singleton class for database connection management
 */
public class DataSource {
    private static DataSource instance;
    private Connection conn = null;
    
    private DataSource()  {    
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }
    /**
     * Gets the singleton instance of DataSource
     * @return DataSource instance
     */
    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }

    /**
     * Creates a new database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public Connection getConnection() throws SQLException {
        Properties props = new Properties();
        try(InputStream in = Thread.currentThread()
                                   .getContextClassLoader()
                                   .getResourceAsStream("database.properties")){
            props.load(in);
        }catch(IOException e){
            e.printStackTrace();
        }
        String url = props.getProperty("jdbc.url");
        String usr = props.getProperty("jdbc.username");
        String pass = props.getProperty("jdbc.password");
        conn = DriverManager.getConnection(url, usr, pass);
        return conn;
    }
}

