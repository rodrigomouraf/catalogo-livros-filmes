package br.com.catalogo_livros_filmes.shared.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMigrator {
    public static void main(String[] args) {
        try {
            runSqlFile("db/migrations/V1__create_catalog_table.sql");
            runSqlFile("db/seeds/S1__seed_catalog.sql");

            System.out.println("Database migrations and seeds executed successfully.");
        } catch (Exception e) {
            System.err.println("Error executing migrations/seeds: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void runSqlFile(String classpathSqlFile) throws IOException, SQLException {
        List<String> statements = loadSqlStatementsFromClasspath(classpathSqlFile);

        try (Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {

            for (String sql : statements) {
                if (sql == null || sql.isBlank()) continue;
                statement.execute(sql);
            }
        }
    }

    private static List<String> loadSqlStatementsFromClasspath(String classpathSqlFile)
            throws IOException {

        ClassLoader classLoader = DatabaseMigrator.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(classpathSqlFile)) {
            if (inputStream == null) {
                throw new IOException("SQL file not found in classpath: " + classpathSqlFile);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }

                String[] rawStatements = sb.toString().split(";");
                List<String> statements = new ArrayList<>();
                for (String raw : rawStatements) {
                    String trimmed = raw.trim();
                    if (!trimmed.isEmpty()) {
                        statements.add(trimmed);
                    }
                }
                return statements;
            }
        }
    }
}
