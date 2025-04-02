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

    public static int getUserId(String username) {

        int userId = 1;

        try {
            Connection connection = JDBC.getConnection();

            String search = "Select User_ID From users Where User_Name = ?";
            PreparedStatement statement = connection.prepareStatement(search);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                userId = resultSet.getInt("User_ID");

            }

            resultSet.close();
            statement.close();

        }catch (SQLException error) {

            System.out.println("Error in processing User_ID: " + error.getMessage());
        }

        return userId;

    }

}
