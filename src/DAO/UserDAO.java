package DAO;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**This is a Data Access Object for User related functions.
 * The serves as user authentication and also gets information
 * about the user to be used in another part of this project.
 */

public class UserDAO {

/**
 * This class is used to validate the user against the information stored in the SQL
 * Database.  It's to validate the User and the PAssword.
 * @param userName username to validate
 * @param password password validation
 * @return true if true and false if it fails validation
 */

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

    /**
     * This will get the user information from the database
     * @param username This is used to lookup the User
     * @return username if it is found in the database, 1 is used as a default value
     *
     */

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
