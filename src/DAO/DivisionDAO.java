package DAO;

import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DivisionDAO {

    public static ObservableList<Division> getDivisions(){
        ObservableList<Division> divisionsList = FXCollections.observableArrayList();

        try {
            Connection connection = JDBC.getConnection();
            String divisionLookup = "select * from first_level_divisions";
            PreparedStatement preparedStatement = connection.prepareStatement(divisionLookup);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String name = resultSet.getString("Division");

                Division division = new Division(name);
                divisionsList.add(division);
            }
        } catch (Exception error) {
            System.out.println("Error loading domiciles: " + error.getMessage());
        }

        return divisionsList;
    }

    public static Division getCountryByDivisionId(int divisionId) {
        try {
            Connection connection = JDBC.getConnection();

            String divisionLookup = "select Country_ID from first_level_divisions where Division_ID = " + divisionId;
            PreparedStatement preparedStatement = connection.prepareStatement(divisionLookup);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int countryId = resultSet.getInt("Country_ID");

                divisionLookup = "select * from countries where Country_ID = " + countryId;
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

