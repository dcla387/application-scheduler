package DAO;

import Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class CountryDAO {

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

    public static Country getCountryByDivisionId(int divisionId) {
        try {
            Connection connection = JDBC.getConnection();

            // First get the country ID from the division
            String divisionLookup = "SELECT Country_ID FROM first_level_divisions WHERE Division_ID = " + divisionId;
            PreparedStatement preparedStatement = connection.prepareStatement(divisionLookup);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int countryId = resultSet.getInt("Country_ID");

                // Then get the country info
                divisionLookup = "SELECT * FROM countries WHERE Country_ID = " + countryId;
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


