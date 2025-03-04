package DAO;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class UserDAO {
    public UserDAO(int userNumber, String userName, String password) {
        try {
            String userLookup = "SELECT * FROM users WHERE userName = ? AND password = ?";
            PreparedStatement ps = ps.prepareStatement(sql);

            ps.setString(1, userName);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Database error during authentication: " + e.getMessage());
        }
    }
}
