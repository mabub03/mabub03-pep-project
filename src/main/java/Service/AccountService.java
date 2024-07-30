package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    // constructor for creating a new AccountService with a new AccountDAO
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    // Use the AccountDAO to create a new Account
    public Account createAccount(Account account) throws Exception {
        // Check if the username is not blank
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new Exception("Username can not be blank");
        }

        // Check if the password is at least 4 characters long
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new Exception("Password must be at least 4 characters long");
        }

        // If all conditions are met, commit the account to the database
        return accountDAO.createAccount(account);
    }
    
    // Use the Account DAO to log the user in
    public Account login(String username, String password) throws Exception {
        Account account = accountDAO.findByUsername(username);

        if (account == null || !account.getUsername().equals(username)|| !account.getPassword().equals(password)) {
            throw new Exception("Invalid username or password");
        }

        return account;
    }
}
