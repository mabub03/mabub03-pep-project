package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDAO {
    // Create an Account
    public Account createAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            // Create the sql string
            String sql = "INSERT INTO account(username, password) VALUES(?, ?);";
            
            // Create the prepared statement and setString methods
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            int affectedRows = preparedStatement.executeUpdate();

            // if there are affected rows then get generated keys and set the generated key to the account id of the account object
            if (affectedRows > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                
                if (generatedKeys.next()) {
                    account.setAccount_id(generatedKeys.getInt(1));
                }
            }

            return account;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return null;
    }

    public Account findByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;

        try {
            // sql statement
            String sql = "SELECT * FROM account WHERE username = ?";
                        
            // prepared statement and its setString, setInt, or setLong to run the sql
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            // if there is a result set then assign the values from the database to an account object to return
            if (resultSet.next()) {
                account = new Account(
                    resultSet.getInt("account_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
                );
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return account;
    }

    public boolean findById(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            // sql statement
            String sql = "SELECT * FROM account WHERE account_id = ?";
            
            // prepared statement and its setString, setInt, or setLong to run the sql
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            // if there is an result set then get the count and if there is an instance of it return it to return true
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return false;
    }
}
