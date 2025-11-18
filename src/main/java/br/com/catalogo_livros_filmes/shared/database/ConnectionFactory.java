package br.com.catalogo_livros_filmes.shared.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static final Properties PROPERTIES = new Properties();
    private static String jdbcUrl;
    private static String jdbcUser;
    private static String jdbcPassword;

    static {
        loadProperties();
        loadDriver();
    }

    private static void loadProperties() {
        try (InputStream inputStream = ConnectionFactory.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (inputStream == null) {
                throw new RuntimeException("db.properties file not found in classpath");
            }

            PROPERTIES.load(inputStream);

            jdbcUrl = getEnvOrProperty("DB_URL", "db.url");
            jdbcUser = getEnvOrProperty("DB_USERNAME", "db.username");
            jdbcPassword = getEnvOrProperty("DB_PASSWORD", "db.password");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    private static String getEnvOrProperty(String envName, String propName) {
        String envValue = System.getenv(envName);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        String propValue = PROPERTIES.getProperty(propName);
        if (propValue == null) {
            throw new RuntimeException("Missing property: " + propName);
        }
        return propValue;
    }

    private static void loadDriver() {
        String driverClassName = PROPERTIES.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load JDBC driver: " + driverClassName, e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
    }
}
