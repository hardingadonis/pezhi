package dao;

import model.Debt;

import org.apache.logging.log4j.*;
import utils.DBConnector;

import java.sql.*;
import java.util.*;

public class DebtDAO implements DAO<Debt> {
    static final Logger logger = LogManager.getLogger(DebtDAO.class);

    private static DebtDAO instance = null;

    static {
        instance = new DebtDAO();

        logger.info("Initialize DebtDAO");
    }

    public static DebtDAO getInstance() {
        return instance;
    }

    @Override
    public List<Debt> getAll() {
        List<Debt> list = new ArrayList<>();

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM debt");

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Integer ID = result.getInt("id");
                String fullName = result.getString("full_name");
                String description = result.getString("description");
                Integer debtAmount = result.getInt("debt_amount");
                String phone = result.getString("phone");

                list.add(new Debt(ID, fullName, description, debtAmount, phone));
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return list;
    }

    @Override
    public Optional<Debt> get(Integer ID) {
        Debt debt = null;

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM debt WHERE id = ?");

            statement.setInt(1, ID);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String fullName = result.getString("full_name");
                String description = result.getString("description");
                Integer debtAmount = result.getInt("debt_amount");
                String phone = result.getString("phone");

                if (result.wasNull()) {
                    phone = null;
                }

                debt = new Debt(ID, fullName, description, debtAmount, phone);
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return Optional.ofNullable(debt);
    }

    @Override
    public void insert(Debt obj) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO debt(full_name, description, debt_amount, phone) VALUES (?, ?, ?, ?)");

            statement.setString(1, obj.getFullName());
            statement.setString(2, obj.getDescription());
            statement.setInt(3, obj.getDebtAmount());
            statement.setObject(4, obj.getPhone(), Types.NVARCHAR);

            int result = statement.executeUpdate();

            logger.info("Insert " + result + " Debt to debt table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void update(Debt obj) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("UPDATE debt SET full_name = ?, description = ?, debt_amount = ?, phone = ? WHERE id = ?");

            statement.setString(1, obj.getFullName());
            statement.setString(2, obj.getDescription());
            statement.setInt(3, obj.getDebtAmount());
            statement.setObject(4, obj.getPhone(), Types.NVARCHAR);
            statement.setInt(5, obj.getID());

            int result = statement.executeUpdate();

            logger.info("Update " + result + " Debt in debt table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void delete(Integer ID) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("DELETE from debt WHERE id = ?");

            statement.setInt(1, ID);

            int result = statement.executeUpdate();

            logger.info("Delete " + result + " Debt from debt table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }
}
