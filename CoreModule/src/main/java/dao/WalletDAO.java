package dao;

import model.Wallet;

import org.apache.logging.log4j.*;
import utils.DBConnector;

import java.sql.*;
import java.util.*;

public class WalletDAO implements DAO<Wallet> {
    static final Logger logger = LogManager.getLogger(WalletDAO.class);

    private static WalletDAO instance = null;

    static {
        instance = new WalletDAO();

        logger.info("Initialize WalletDAO");
    }

    public static WalletDAO getInstance() {
        return instance;
    }

    @Override
    public List<Wallet> getAll() {
        List<Wallet> list = new ArrayList<>();

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM wallet");

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Integer ID = result.getInt("id");
                String name = result.getString("name");
                String type = result.getString("type");
                Integer balance = result.getInt("balance");

                list.add(new Wallet(ID, name, type, balance));
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return list;
    }

    @Override
    public Optional<Wallet> get(Integer ID) {
        Wallet wallet = null;

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM wallet WHERE id = ?");

            statement.setInt(1, ID);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String name = result.getString("name");
                String type = result.getString("type");
                Integer balance = result.getInt("balance");

                wallet = new Wallet(ID, name, type, balance);
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return Optional.ofNullable(wallet);
    }

    @Override
    public void insert(Wallet obj) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO wallet(name, type, balance) VALUES (?, ?, ?)");

            statement.setString(1, obj.getName());
            statement.setString(2, obj.getType());
            statement.setInt(3, obj.getBalance());

            int result = statement.executeUpdate();

            logger.info("Insert " + result + " Wallet to wallet table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void update(Wallet obj) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("UPDATE wallet SET name = ?, type = ?, balance = ? WHERE id = ?");

            statement.setString(1, obj.getName());
            statement.setString(2, obj.getType());
            statement.setInt(3, obj.getBalance());
            statement.setInt(4, obj.getID());

            int result = statement.executeUpdate();

            logger.info("Update " + result + " Wallet in wallet table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void delete(Integer ID) {
        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement("DELETE from wallet WHERE id = ?");

            statement.setInt(1, ID);

            int result = statement.executeUpdate();

            logger.info("Delete " + result + " Wallet from wallet table");

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }
}
