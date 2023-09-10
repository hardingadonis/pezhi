package dao;

import model.Transaction;

import org.apache.logging.log4j.*;
import utils.DBConnector;

import java.sql.*;
import java.time.*;
import java.util.*;

public class TransactionDAO implements DAO<Transaction>, ITransaction<Transaction> {
    static final Logger logger = LogManager.getLogger(TransactionDAO.class);

    private static TransactionDAO instance = null;

    static {
        instance = new TransactionDAO();

        logger.info("Initialize TransactionDAO");
    }

    public static TransactionDAO getInstance() {
        return instance;
    }

    private List<Transaction> getAll(String sql) {
        List<Transaction> list = new ArrayList<>();

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Integer ID = result.getInt("id");
                LocalDateTime dateTime = result.getTimestamp("date_time").toLocalDateTime();
                String description = result.getString("description");
                Integer amount = result.getInt("amount");
                String type = result.getString("type");
                Integer sourceWallet = result.getInt("source_wallet");

                list.add(new Transaction(ID, dateTime, description, amount, type, sourceWallet));
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return list;
    }

    @Override
    public List<Transaction> getAll() {
        return this.getAll("SELECT * FROM trans");
    }

    @Override
    public Optional<Transaction> get(Integer ID) {
        Transaction transaction = null;

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM trans WHERE id = ?");

            statement.setInt(1, ID);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                LocalDateTime dateTime = result.getTimestamp("date_time").toLocalDateTime();
                String description = result.getString("description");
                Integer amount = result.getInt("amount");
                String type = result.getString("type");
                Integer sourceWallet = result.getInt("source_wallet");

                transaction = new Transaction(ID, dateTime, description, amount, type, sourceWallet);
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return Optional.ofNullable(transaction);
    }

    @Override
    public void insert(Transaction obj) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO trans(date_time, description, amount, type, source_wallet) VALUES (?, ?, ?, ?, ?)");

            statement.setString(1, obj.getDateTime().format(DBConnector.DATE_TIME_FORMAT));
            statement.setString(2, obj.getDescription());
            statement.setInt(3, obj.getAmount());
            statement.setString(4, obj.getType());
            statement.setInt(5, obj.getSourceWallet());

            int result = statement.executeUpdate();

            logger.info("Insert " + result + " Transaction to trans table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void update(Transaction obj) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("UPDATE trans SET date_time = ?, description = ?, amount = ?, type = ?, source_wallet = ? WHERE id = ?");

            statement.setString(1, obj.getDateTime().format(DBConnector.DATE_TIME_FORMAT));
            statement.setString(2, obj.getDescription());
            statement.setInt(3, obj.getAmount());
            statement.setString(4, obj.getType());
            statement.setInt(5, obj.getSourceWallet());
            statement.setInt(6, obj.getID());

            int result = statement.executeUpdate();

            logger.info("Update " + result + " Transaction in trans table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void delete(Integer ID) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("DELETE from trans WHERE id = ?");

            statement.setInt(1, ID);

            int result = statement.executeUpdate();

            logger.info("Delete " + result + " Transaction from trans table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public List<Transaction> listToday() {
        return this.getAll("SELECT * FROM trans WHERE DATE(date_time) = DATE('now', 'localtime')");
    }

    @Override
    public List<Transaction> listDate(Integer year, Integer month, Integer day) {
        return this.listDate(LocalDate.of(year, month, day));
    }

    @Override
    public List<Transaction> listDate(LocalDate date) {
        String sql = String.format("SELECT * FROM trans WHERE date(date_time) = '%s'", date.format(DBConnector.DATE_FORMAT));
        return this.getAll(sql);
    }

    @Override
    public List<Transaction> listMonth(Integer year, Integer month) {
        String sql = String.format("SELECT * FROM trans WHERE STRFTIME('%%Y', date_time) = '%d' AND STRFTIME('%%m', date_time) = '%02d'", year, month);
        return this.getAll(sql);
    }

    @Override
    public List<Transaction> listYear(Integer year) {
        String sql = String.format("SELECT * FROM trans WHERE STRFTIME('%%Y', date_time) = '%d'", year);
        return this.getAll(sql);
    }

    @Override
    public List<Transaction> listPeriod(LocalDate begin, LocalDate end) {
        String sql = String.format("SELECT * FROM trans WHERE DATE(date_time) >= '%s' AND DATE(date_time) <= '%s'", begin.format(DBConnector.DATE_FORMAT), end.format(DBConnector.DATE_FORMAT));
        return this.getAll(sql);
    }
}
