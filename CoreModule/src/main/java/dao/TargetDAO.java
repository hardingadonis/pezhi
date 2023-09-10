package dao;

import model.Target;

import org.apache.logging.log4j.*;
import utils.DBConnector;

import java.sql.*;
import java.util.*;

public class TargetDAO implements DAO<Target> {
    static final Logger logger = LogManager.getLogger(TargetDAO.class);

    private static TargetDAO instance = null;

    static {
        instance = new TargetDAO();

        logger.info("Initialize TargetDAO");
    }

    public static TargetDAO getInstance() {
        return instance;
    }

    @Override
    public List<Target> getAll() {
        List<Target> list = new ArrayList<>();

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM target");

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Integer ID = result.getInt("id");
                String name = result.getString("name");
                String description = result.getString("description");
                Integer targetAmount = result.getInt("target_amount");
                Integer currentBalance = result.getInt("current_balance");

                list.add(new Target(ID, name, description, targetAmount, currentBalance));
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return list;
    }

    @Override
    public Optional<Target> get(Integer ID) {
        Target target = null;

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM target WHERE id = ?");

            statement.setInt(1, ID);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String name = result.getString("name");
                String description = result.getString("description");
                Integer targetAmount = result.getInt("target_amount");
                Integer currentBalance = result.getInt("current_balance");

                target = new Target(ID, name, description, targetAmount, currentBalance);
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return Optional.ofNullable(target);
    }

    @Override
    public void insert(Target obj) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO target(name, description, target_amount, current_balance) VALUES (?, ?, ?, ?)");

            statement.setString(1, obj.getName());
            statement.setString(2, obj.getDescription());
            statement.setInt(3, obj.getTargetAmount());
            statement.setInt(4, obj.getCurrentBalance());

            int result = statement.executeUpdate();

            logger.info("Insert " + result + " Target to target table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void update(Target obj) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("UPDATE target SET name = ?, description = ?, target_amount = ?, current_balance = ? WHERE id = ?");

            statement.setString(1, obj.getName());
            statement.setString(2, obj.getDescription());
            statement.setInt(3, obj.getTargetAmount());
            statement.setInt(4, obj.getCurrentBalance());
            statement.setInt(5, obj.getID());

            int result = statement.executeUpdate();

            logger.info("Update " + result + " Target in target table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void delete(Integer ID) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("DELETE from target WHERE id = ?");

            statement.setInt(1, ID);

            int result = statement.executeUpdate();

            logger.info("Delete " + result + " Target from target table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }
}
