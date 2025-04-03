package DAO;

import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Data Access Object class for managing division data.
 *
 * <p>This class provides methods for retrieveing division information from the database.
 * Divisions of the first-level admin divisions like states and provinces that are associated with a particular country.</p>
 *
 * @author Donna Clark
 * @version 1.0
 */
public class DivisionDAO {

    /**
     * Retrieves all division from the database.
     *
     * <p>This method executes a SQL query to get all division records from the database for use in the application.</p>
     *
     * @return ObservableList containing all divisions in the database
     */

    public static ObservableList<Division> getDivisions() {
        ObservableList<Division> divisionsList = FXCollections.observableArrayList();

        try {
            Connection connection = JDBC.getConnection();
            String divisionLookup = "select * from first_level_divisions";
            PreparedStatement preparedStatement = connection.prepareStatement(divisionLookup);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int divisionId = resultSet.getInt("Division_ID");
                String name = resultSet.getString("Division");
                int countryID = resultSet.getInt("Country_ID");

                Division division = new Division(divisionId, name, countryID);
                divisionsList.add(division);
            }
        } catch (Exception error) {
            System.out.println("Error loading domiciles: " + error.getMessage());
        }

        return divisionsList;
    }

    /**
     * Retrieves all divisions associated with a specific country.
     *
     * <p>This method is used to populate combo boxes for the User to make accurate associations of the customer divisions.</p>
     *
     * @param countryID The ID of the country to filet the divisions
     * @return ObservableList containing divisions associated with the specified country
     */

    public static ObservableList<Division> getDivisionsByCountry(int countryID) {
        ObservableList<Division> divisionsList = FXCollections.observableArrayList();

        try {
            Connection connection = JDBC.getConnection();
            String divisionLookup = "SELECT * FROM first_level_divisions where Country_ID = " + countryID + " ORDER BY Division";
            PreparedStatement preparedStatement = connection.prepareStatement(divisionLookup);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int divisionId = resultSet.getInt("Division_ID");
                String name = resultSet.getString("Division");

                Division division = new Division(divisionId, name, countryID);
                divisionsList.add(division);
            }

        } catch (Exception error) {
            System.out.println("Error loading domiciles: " + error.getMessage());
        }
        return divisionsList;
    }

    public int getDivisionId() {
        int divisionId = 0;
        return divisionId;
    }
}
