package utils;

import org.apache.logging.log4j.*;

import java.io.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class DBConnector {
    static final Logger logger = LogManager.getLogger(DBConnector.class);

    private static final String[] TABLES = {
            "CREATE TABLE IF NOT EXISTS wallet (id INTEGER, name TEXT NOT NULL, type TEXT NOT NULL, balance INTEGER NOT NULL, PRIMARY KEY(id AUTOINCREMENT));",
            "CREATE TABLE IF NOT EXISTS trans (id INTEGER, date_time TEXT NOT NULL, description TEXT NOT NULL, amount INTEGER NOT NULL, type TEXT NOT NULL, source_wallet INTEGER NOT NULL, PRIMARY KEY(id AUTOINCREMENT), FOREIGN KEY (source_wallet) REFERENCES wallet (id));",
            "CREATE TABLE IF NOT EXISTS income_category (id INTEGER, name TEXT NOT NULL, description TEXT NOT NULL, PRIMARY KEY(id AUTOINCREMENT));",
            "CREATE TABLE IF NOT EXISTS expense_category (id INTEGER, name TEXT NOT NULL, description TEXT NOT NULL, limit_amount INTEGER, PRIMARY KEY(id AUTOINCREMENT));",
            "CREATE TABLE IF NOT EXISTS target (id INTEGER, name TEXT NOT NULL, description TEXT NOT NULL, target_amount INTEGER NOT NULL, current_balance INTEGER NOT NULL, PRIMARY KEY(id AUTOINCREMENT));",
            "CREATE TABLE IF NOT EXISTS debt (id INTEGER, full_name TEXT NOT NULL, description TEXT NOT NULL, debt_amount INTEGER NOT NULL, phone TEXT, PRIMARY KEY(id AUTOINCREMENT));",
            "CREATE TABLE IF NOT EXISTS transfer_trans (id INTEGER, target_wallet INTEGER NOT NULL, PRIMARY KEY(id), FOREIGN KEY (id) REFERENCES trans (id), FOREIGN KEY (target_wallet) REFERENCES wallet (id));",
            "CREATE TABLE IF NOT EXISTS income_trans (id INTEGER, category_id INTEGER NOT NULL, target_id INTEGER, debt_id INTEGER, PRIMARY KEY(id), FOREIGN KEY (id) REFERENCES trans (id), FOREIGN KEY (category_id) REFERENCES income_category (id), FOREIGN KEY (target_id) REFERENCES target (id), FOREIGN KEY (debt_id) REFERENCES debt (id));",
            "CREATE TABLE IF NOT EXISTS expense_trans (id INTEGER, category_id INTEGER NOT NULL, target_id INTEGER, debt_id INTEGER, PRIMARY KEY(id), FOREIGN KEY (id) REFERENCES trans (id), FOREIGN KEY (category_id) REFERENCES expense_category (id), FOREIGN KEY (target_id) REFERENCES target (id), FOREIGN KEY (debt_id) REFERENCES debt (id));"
    };

    private static Connection conn = null;

    public static DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static {
        String databaseLocation = ConfigManager.getInstance().getConfig().getDatabaseLocation();

        if (databaseLocation == null) {
            databaseLocation = "database.sqlite";

            ConfigManager.getInstance().setConfig(new Config(databaseLocation));
            ConfigManager.getInstance().save();
        }

        File databaseFile = new File(databaseLocation);
        boolean isExist = databaseFile.exists();

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + databaseLocation);
            logger.info("Connect SQLite successfully!");

            if (!isExist) {
                for (var table : TABLES) {
                    PreparedStatement statement = conn.prepareStatement(table);

                    int result = statement.executeUpdate();

                    if (result == 0) {
                        logger.info("Create table successfully!");
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static Connection getConnection() {
        return conn;
    }

    public static void closeConnection() {
        try {
            if (conn != null) {
                conn.close();

                logger.info("Close SQLite successfully!");
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }
}
