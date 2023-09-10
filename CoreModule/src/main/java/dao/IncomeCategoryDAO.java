package dao;

import model.IncomeCategory;

import org.apache.logging.log4j.*;
import utils.DBConnector;

import java.sql.*;
import java.util.*;

public class IncomeCategoryDAO implements DAO<IncomeCategory> {
    static final Logger logger = LogManager.getLogger(IncomeCategoryDAO.class);

    private static IncomeCategoryDAO instance = null;

    static {
        instance = new IncomeCategoryDAO();

        logger.info("Initialize IncomeCategoryDAO");
    }

    public static IncomeCategoryDAO getInstance() {
        return instance;
    }

    @Override
    public List<IncomeCategory> getAll() {
        List<IncomeCategory> list = new ArrayList<>();

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM income_category");

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Integer ID = result.getInt("id");
                String name = result.getString("name");
                String description = result.getString("description");

                list.add(new IncomeCategory(ID, name, description));
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return list;
    }

    @Override
    public Optional<IncomeCategory> get(Integer ID) {
        IncomeCategory category = null;

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM income_category WHERE id = ?");

            statement.setInt(1, ID);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String name = result.getString("name");
                String description = result.getString("description");

                category = new IncomeCategory(ID, name, description);
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return Optional.ofNullable(category);
    }

    @Override
    public void insert(IncomeCategory obj) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO income_category(name, description) VALUES (?, ?)");

            statement.setString(1, obj.getName());
            statement.setString(2, obj.getDescription());

            int result = statement.executeUpdate();

            logger.info("Insert " + result + " IncomeCategory to income_category table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void update(IncomeCategory obj) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("UPDATE income_category SET name = ?, description = ? WHERE id = ?");

            statement.setString(1, obj.getName());
            statement.setString(2, obj.getDescription());
            statement.setInt(3, obj.getID());

            int result = statement.executeUpdate();

            logger.info("Update " + result + " IncomeCategory in income_category table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void delete(Integer ID) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("DELETE from income_category WHERE id = ?");

            statement.setInt(1, ID);

            int result = statement.executeUpdate();

            logger.info("Delete " + result + " IncomeCategoryDAO from income_category table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }
}
