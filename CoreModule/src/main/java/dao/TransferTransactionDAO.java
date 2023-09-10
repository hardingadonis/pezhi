package dao;

import model.TransferTransaction;

import org.apache.logging.log4j.*;
import utils.DBConnector;

import java.sql.*;
import java.time.*;
import java.util.*;

public class TransferTransactionDAO implements DAO<TransferTransaction>, ITransaction<TransferTransaction> {
    static final Logger logger = LogManager.getLogger(TransferTransactionDAO.class);

    private static TransferTransactionDAO instance = null;

    static {
        instance = new TransferTransactionDAO();

        logger.info("Initialize TransactionDAO");
    }

    public static TransferTransactionDAO getInstance() {
        return instance;
    }

    private List<TransferTransaction> getAll(String sql) {
        List<TransferTransaction> list = new ArrayList<>();

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
                Integer targetWallet = result.getInt("target_wallet");

                list.add(TransferTransaction.builder().ID(ID).dateTime(dateTime).description(description).amount(amount).type(type).sourceWallet(sourceWallet).targetWallet(targetWallet).build());
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return list;
    }

    @Override
    public List<TransferTransaction> getAll() {
        return this.getAll("SELECT * FROM trans NATURAL JOIN transfer_trans");
    }

    @Override
    public Optional<TransferTransaction> get(Integer ID) {
        TransferTransaction transaction = null;

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM trans NATURAL JOIN transfer_trans WHERE id = ?");

            statement.setInt(1, ID);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                LocalDateTime dateTime = result.getTimestamp("date_time").toLocalDateTime();
                String description = result.getString("description");
                Integer amount = result.getInt("amount");
                String type = result.getString("type");
                Integer sourceWallet = result.getInt("source_wallet");
                Integer targetWallet = result.getInt("target_wallet");

                transaction = TransferTransaction.builder().ID(ID).dateTime(dateTime).description(description).amount(amount).type(type).sourceWallet(sourceWallet).targetWallet(targetWallet).build();
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return Optional.ofNullable(transaction);
    }

    @Override
    public void insert(TransferTransaction obj) {
        TransactionDAO.getInstance().insert(obj);

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO transfer_trans(id, target_wallet) VALUES (LAST_INSERT_ROWID(), ?)");

            statement.setInt(1, obj.getTargetWallet());

            int result = statement.executeUpdate();

            logger.info("Insert " + result + " TransferTransaction to transfer_trans table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void update(TransferTransaction obj) {
        TransactionDAO.getInstance().update(obj);

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("UPDATE transfer_trans SET target_wallet = ? WHERE id = ?");

            statement.setInt(1, obj.getTargetWallet());
            statement.setInt(2, obj.getID());

            int result = statement.executeUpdate();

            logger.info("Update " + result + " TransferTransaction in transfer_trans table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void delete(Integer ID) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("DELETE from transfer_trans WHERE id = ?");

            statement.setInt(1, ID);

            int result = statement.executeUpdate();

            logger.info("Delete " + result + " TransferTransaction from transfer_trans table");

            statement.close();

            TransactionDAO.getInstance().delete(ID);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public List<TransferTransaction> listToday() {
        return this.getAll("SELECT * FROM trans NATURAL JOIN transfer_trans WHERE DATE(date_time) = DATE('now', 'localtime')");
    }

    @Override
    public List<TransferTransaction> listDate(Integer year, Integer month, Integer day) {
        return this.listDate(LocalDate.of(year, month, day));
    }

    @Override
    public List<TransferTransaction> listDate(LocalDate date) {
        String sql = String.format("SELECT * FROM trans NATURAL JOIN transfer_trans WHERE date(date_time) = '%s'", date.format(DBConnector.DATE_FORMAT));
        return this.getAll(sql);
    }

    @Override
    public List<TransferTransaction> listMonth(Integer year, Integer month) {
        String sql = String.format("SELECT * FROM trans NATURAL JOIN transfer_trans WHERE STRFTIME('%%Y', date_time) = '%d' AND STRFTIME('%%m', date_time) = '%02d'", year, month);
        return this.getAll(sql);
    }

    @Override
    public List<TransferTransaction> listYear(Integer year) {
        String sql = String.format("SELECT * FROM trans NATURAL JOIN transfer_trans WHERE STRFTIME('%%Y', date_time) = '%d'", year);
        return this.getAll(sql);
    }

    @Override
    public List<TransferTransaction> listPeriod(LocalDate begin, LocalDate end) {
        String sql = String.format("SELECT * FROM trans NATURAL JOIN transfer_trans WHERE DATE(date_time) >= '%s' AND DATE(date_time) <= '%s'", begin.format(DBConnector.DATE_FORMAT), end.format(DBConnector.DATE_FORMAT));
        return this.getAll(sql);
    }
}
