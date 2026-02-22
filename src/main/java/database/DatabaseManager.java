package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:finance.db";

    public static Connection connection() throws Exception {
        return DriverManager.getConnection(URL);
    }

    public static void init() {
        try (Connection conn = connection();
             Statement stmt = conn.createStatement()) {
            String userTable = """
                    CREATE TABLE IF NOT EXISTS USERS(id INTEGER PRIMARY KEY AUTOINCREMENT,
                                                                            telegram_id INTEGER UNIQUE,
                                                                            username TEXT);
                    """;
            String transactionTable = """
                    CREATE TABLE IF NOT EXISTS transactions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        user_id INTEGER,
                        type TEXT,
                        amount REAL,
                        category TEXT,
                        date TEXT
                    );
                    """;
            stmt.execute(userTable);
            stmt.execute(transactionTable);

            System.out.println("База данных и таблицы созданы");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
