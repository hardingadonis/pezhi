package dao;

import model.ExpenseCategory;

import org.apache.logging.log4j.*;
import utils.DBConnector;

import java.sql.*;
import java.util.*;

public class ExpenseCategoryDAO implements DAO<ExpenseCategory> {
    static final Logger logger = LogManager.getLogger(ExpenseCategoryDAO.class);

    private static ExpenseCategoryDAO instance = null;

    static {
        instance = new ExpenseCategoryDAO();

        logger.info("Initialize ExpenseCategoryDAO");
    }

    public static ExpenseCategoryDAO getInstance() {
        return instance;
    }

    @Override
    public List<ExpenseCategory> getAll() {
        List<ExpenseCategory> list = new ArrayList<>();

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM expense_category");

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Integer ID = result.getInt("id");
                String name = result.getString("name");
                String description = result.getString("description");
                Integer limitAmount = result.getInt("limit_amount");

                if (result.wasNull()) {
                    limitAmount = null;
                }

                list.add(new ExpenseCategory(ID, name, description, limitAmount));
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return list;
    }

    @Override
    public Optional<ExpenseCategory> get(Integer ID) {
        ExpenseCategory category = null;

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM expense_category WHERE id = ?");

            statement.setInt(1, ID);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String name = result.getString("name");
                String description = result.getString("description");
                Integer limitAmount = result.getInt("limit_amount");

                if (limitAmount == 0) {
                    limitAmount = null;
                }

                category = new ExpenseCategory(ID, name, description, limitAmount);
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return Optional.ofNullable(category);
    }

    @Override
    public void insert(ExpenseCategory obj) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO expense_category(name, description, limit_amount) VALUES (?, ?, ?)");

            statement.setString(1, obj.getName());
            statement.setString(2, obj.getDescription());
            statement.setObject(3, obj.getLimitAmount(), Types.INTEGER);

            int result = statement.executeUpdate();

            logger.info("Insert " + result + " ExpenseCategory to expense_category table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void update(ExpenseCategory obj) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("UPDATE expense_category SET name = ?, description = ?, limit_amount = ? WHERE id = ?");

            statement.setString(1, obj.getName());
            statement.setString(2, obj.getDescription());
            statement.setObject(3, obj.getLimitAmount(), Types.INTEGER);
            statement.setInt(4, obj.getID());

            int result = statement.executeUpdate();

            logger.info("Update " + result + " ExpenseCategory in expense_category table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void delete(Integer ID) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("DELETE from expense_category WHERE id = ?");

            statement.setInt(1, ID);

            int result = statement.executeUpdate();

            logger.info("Delete " + result + " ExpenseCategory from expense_category table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }
}
