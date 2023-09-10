package dao;

import model.ExpenseTransaction;

import org.apache.logging.log4j.*;
import utils.DBConnector;

import java.sql.*;
import java.time.*;
import java.util.*;

public class ExpenseTransactionDAO implements DAO<ExpenseTransaction>, ITransaction<ExpenseTransaction> {
    static final Logger logger = LogManager.getLogger(ExpenseTransactionDAO.class);

    private static ExpenseTransactionDAO instance = null;

    static {
        instance = new ExpenseTransactionDAO();

        logger.info("Initialize ExpenseTransactionDAO");
    }

    public static ExpenseTransactionDAO getInstance() {
        return instance;
    }

    private List<ExpenseTransaction> getAll(String sql) {
        List<ExpenseTransaction> list = new ArrayList<>();

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
                Integer categoryID = result.getInt("category_id");
                Integer targetID = result.getInt("target_id");
                Integer debtID = result.getInt("debt_id");

                if (targetID == 0) {
                    targetID = null;
                }

                if (debtID == 0) {
                    debtID = null;
                }

                list.add(ExpenseTransaction.builder().ID(ID).dateTime(dateTime).description(description).amount(amount).type(type).sourceWallet(sourceWallet).categoryID(categoryID).targetID(targetID).debtID(debtID).build());
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return list;
    }

    @Override
    public List<ExpenseTransaction> getAll() {
        return this.getAll("SELECT * FROM trans NATURAL JOIN expense_trans");
    }

    @Override
    public Optional<ExpenseTransaction> get(Integer ID) {
        ExpenseTransaction transaction = null;

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM trans NATURAL JOIN expense_trans WHERE id = ?");

            statement.setInt(1, ID);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                LocalDateTime dateTime = result.getTimestamp("date_time").toLocalDateTime();
                String description = result.getString("description");
                Integer amount = result.getInt("amount");
                String type = result.getString("type");
                Integer sourceWallet = result.getInt("source_wallet");
                Integer categoryID = result.getInt("category_id");
                Integer targetID = result.getInt("target_id");
                Integer debtID = result.getInt("debt_id");

                if (targetID == 0) {
                    targetID = null;
                }

                if (debtID == 0) {
                    debtID = null;
                }

                transaction = ExpenseTransaction.builder().ID(ID).dateTime(dateTime).description(description).amount(amount).type(type).sourceWallet(sourceWallet).categoryID(categoryID).targetID(targetID).debtID(debtID).build();
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return Optional.ofNullable(transaction);
    }

    @Override
    public void insert(ExpenseTransaction obj) {
        TransactionDAO.getInstance().insert(obj);

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO expense_trans(id, category_id, target_id, debt_id) VALUES (LAST_INSERT_ROWID(), ?, ?, ?)");

            statement.setInt(1, obj.getCategoryID());
            statement.setObject(2, obj.getTargetID(), Types.INTEGER);
            statement.setObject(3, obj.getDebtID(), Types.INTEGER);

            int result = statement.executeUpdate();

            logger.info("Insert " + result + " ExpenseTransaction to expense_trans table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void update(ExpenseTransaction obj) {
        TransactionDAO.getInstance().update(obj);

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("UPDATE expense_trans SET category_id = ?, target_id = ?, debt_id = ? WHERE id = ?");

            statement.setInt(1, obj.getCategoryID());
            statement.setObject(2, obj.getTargetID(), Types.INTEGER);
            statement.setObject(3, obj.getDebtID(), Types.INTEGER);
            statement.setInt(4, obj.getID());

            int result = statement.executeUpdate();

            logger.info("Update " + result + " ExpenseTransaction in expense_trans table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void delete(Integer ID) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("DELETE from expense_trans WHERE id = ?");

            statement.setInt(1, ID);

            int result = statement.executeUpdate();

            logger.info("Delete " + result + " ExpenseTransaction from expense_trans table");

            statement.close();

            TransactionDAO.getInstance().delete(ID);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public List<ExpenseTransaction> listToday() {
        return this.getAll("SELECT * FROM trans NATURAL JOIN expense_trans WHERE DATE(date_time) = DATE('now', 'localtime')");
    }

    @Override
    public List<ExpenseTransaction> listDate(Integer year, Integer month, Integer day) {
        return this.listDate(LocalDate.of(year, month, day));
    }

    @Override
    public List<ExpenseTransaction> listDate(LocalDate date) {
        String sql = String.format("SELECT * FROM trans NATURAL JOIN expense_trans WHERE date(date_time) = '%s'", date.format(DBConnector.DATE_FORMAT));
        return this.getAll(sql);
    }

    @Override
    public List<ExpenseTransaction> listMonth(Integer year, Integer month) {
        String sql = String.format("SELECT * FROM trans NATURAL JOIN expense_trans WHERE STRFTIME('%%Y', date_time) = '%d' AND STRFTIME('%%m', date_time) = '%02d'", year, month);
        return this.getAll(sql);
    }

    @Override
    public List<ExpenseTransaction> listYear(Integer year) {
        String sql = String.format("SELECT * FROM trans NATURAL JOIN expense_trans WHERE STRFTIME('%%Y', date_time) = '%d'", year);
        return this.getAll(sql);
    }

    @Override
    public List<ExpenseTransaction> listPeriod(LocalDate begin, LocalDate end) {
        String sql = String.format("SELECT * FROM trans NATURAL JOIN expense_trans WHERE DATE(date_time) >= '%s' AND DATE(date_time) <= '%s'", begin.format(DBConnector.DATE_FORMAT), end.format(DBConnector.DATE_FORMAT));
        return this.getAll(sql);
    }
}
