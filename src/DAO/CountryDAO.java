package DAO;

import Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * Data Access Object class for managing country data.
 *
 * <p>This class provides methods for retrieving country information from the database</p>
 *
 * @author Donna Clark
 * @version 1.0
 */
public class CountryDAO {

    /**
     * Retrieves all countries from the database.
     * @return ObservableList containing all countries in the database
     */

    public static ObservableList<Country> getCountries(){
        ObservableList<Country> countryList = FXCollections.observableArrayList();

        try {
            Connection connection = JDBC.getConnection();
            String countryLookup = "select * from countries";
            PreparedStatement preparedStatement = connection.prepareStatement(countryLookup);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int id = resultSet.getInt("Country_ID");
                String name = resultSet.getString("Country");

                Country country = new Country(id, name);
                countryList.add(country);
            }
        } catch (Exception error) {
            System.out.println("Error loading countries: " + error.getMessage());
        }

        return countryList;
    }

    /**
     * Retrieves the country associated with a specified division ID.
     *
     * @param divisionId The ID of the division whose country is determined
     * @return Country object associated with the division
     */

    public static Country getCountryByDivisionId(int divisionId) {
        try {
            Connection connection = JDBC.getConnection();

            String divisionLookup = "select Country_ID from first_level_divisions where Division_ID = " + divisionId;
            PreparedStatement preparedStatement = connection.prepareStatement(divisionLookup);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int countryID = resultSet.getInt("Country_ID");

                divisionLookup = "select * from countries where Country_ID = " + countryID;
                preparedStatement = connection.prepareStatement(divisionLookup);
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return new Country(
                            resultSet.getInt("Country_ID"),
                            resultSet.getString("Country")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Error finding country: " + e.getMessage());
        }

        return null;
    }
        }


