package DAO;

import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DivisionDAO {

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
