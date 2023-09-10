package dao;

import model.IncomeTransaction;

import org.apache.logging.log4j.*;
import utils.DBConnector;

import java.sql.*;
import java.time.*;
import java.util.*;

public class IncomeTransactionDAO implements DAO<IncomeTransaction>, ITransaction<IncomeTransaction> {
    static final Logger logger = LogManager.getLogger(IncomeTransactionDAO.class);

    private static IncomeTransactionDAO instance = null;

    static {
        instance = new IncomeTransactionDAO();

        logger.info("Initialize IncomeTransactionDAO");
    }

    public static IncomeTransactionDAO getInstance() {
        return instance;
    }

    private List<IncomeTransaction> getAll(String sql) {
        List<IncomeTransaction> list = new ArrayList<>();

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

                list.add(IncomeTransaction.builder().ID(ID).dateTime(dateTime).description(description).amount(amount).type(type).sourceWallet(sourceWallet).categoryID(categoryID).targetID(targetID).debtID(debtID).build());
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return list;
    }

    @Override
    public List<IncomeTransaction> getAll() {
        return this.getAll("SELECT * FROM trans NATURAL JOIN income_trans");
    }

    @Override
    public Optional<IncomeTransaction> get(Integer ID) {
        IncomeTransaction transaction = null;

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM trans NATURAL JOIN income_trans WHERE id = ?");

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

                transaction = IncomeTransaction.builder().ID(ID).dateTime(dateTime).description(description).amount(amount).type(type).sourceWallet(sourceWallet).categoryID(categoryID).targetID(targetID).debtID(debtID).build();
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return Optional.ofNullable(transaction);
    }

    @Override
    public void insert(IncomeTransaction obj) {
        TransactionDAO.getInstance().insert(obj);

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO income_trans(id, category_id, target_id, debt_id) VALUES (LAST_INSERT_ROWID(), ?, ?, ?)");

            statement.setInt(1, obj.getCategoryID());
            statement.setObject(2, obj.getTargetID(), Types.INTEGER);
            statement.setObject(3, obj.getDebtID(), Types.INTEGER);

            int result = statement.executeUpdate();

            logger.info("Insert " + result + " IncomeTransaction to income_trans table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void update(IncomeTransaction obj) {
        TransactionDAO.getInstance().update(obj);

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("UPDATE income_trans SET category_id = ?, target_id = ?, debt_id = ? WHERE id = ?");

            statement.setInt(1, obj.getCategoryID());
            statement.setObject(2, obj.getTargetID(), Types.INTEGER);
            statement.setObject(3, obj.getDebtID(), Types.INTEGER);
            statement.setInt(4, obj.getID());

            int result = statement.executeUpdate();

            logger.info("Update " + result + " IncomeTransaction in income_trans table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void delete(Integer ID) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("DELETE from income_trans WHERE id = ?");

            statement.setInt(1, ID);

            int result = statement.executeUpdate();

            logger.info("Delete " + result + " IncomeTransaction from income_trans table");

            statement.close();

            TransactionDAO.getInstance().delete(ID);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public List<IncomeTransaction> listToday() {
        return this.getAll("SELECT * FROM trans NATURAL JOIN income_trans WHERE DATE(date_time) = DATE('now', 'localtime')");
    }

    @Override
    public List<IncomeTransaction> listDate(Integer year, Integer month, Integer day) {
        return this.listDate(LocalDate.of(year, month, day));
    }

    @Override
    public List<IncomeTransaction> listDate(LocalDate date) {
        String sql = String.format("SELECT * FROM trans NATURAL JOIN income_trans WHERE date(date_time) = '%s'", date.format(DBConnector.DATE_FORMAT));
        return this.getAll(sql);
    }

    @Override
    public List<IncomeTransaction> listMonth(Integer year, Integer month) {
        String sql = String.format("SELECT * FROM trans NATURAL JOIN income_trans WHERE STRFTIME('%%Y', date_time) = '%d' AND STRFTIME('%%m', date_time) = '%02d'", year, month);
        return this.getAll(sql);
    }

    @Override
    public List<IncomeTransaction> listYear(Integer year) {
        String sql = String.format("SELECT * FROM trans NATURAL JOIN income_trans WHERE STRFTIME('%%Y', date_time) = '%d'", year);
        return this.getAll(sql);
    }

    @Override
    public List<IncomeTransaction> listPeriod(LocalDate begin, LocalDate end) {
        String sql = String.format("SELECT * FROM trans NATURAL JOIN income_trans WHERE DATE(date_time) >= '%s' AND DATE(date_time) <= '%s'", begin.format(DBConnector.DATE_FORMAT), end.format(DBConnector.DATE_FORMAT));
        return this.getAll(sql);
    }
}
