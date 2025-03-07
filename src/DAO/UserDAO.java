package DAO;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static boolean userValidation(String userName, String password) {
        try {
            Connection connection = JDBC.getConnection();

            String userLookup = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
            PreparedStatement ps = connection.prepareStatement(userLookup);

            ps.setString(1, userName);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Database error during authentication: " + e.getMessage());
            return false;
        }
    }

}
