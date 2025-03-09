package DAO;

import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            Connection connection = JDBC.getConnection();
            String custLookup = "select * from customers";
            PreparedStatement preparedStatement = connection.prepareStatement(custLookup);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Customer customer = new Customer(
                resultSet.getInt("Customer_ID"),
                resultSet.getString("Customer_Name"),
                resultSet.getString("Address"),
                resultSet.getString("Postal_Code"),
                resultSet.getString("Phone"),
                resultSet.getInt("Division_ID"),
                getDivisionName(connection, resultSet.getInt("Division_ID"))
            );
                customerList.add(customer);
            }

        } catch (SQLException error) {
            System.out.println("Customers unable to load" + error.getMessage());
        }

        return customerList;


        }

        public static boolean updateCustomer(int customerId, String name, String address, String postalCode, String phone, int divisionId) {
            try {
                Connection connection = JDBC.getConnection();
                String update = "Update Customers Set Customer_Name = '" + name + "', Address = '" + address + "', Postal_Code = '" + postalCode + "', Phone = '" + phone + "', Division_ID = " + divisionId + "where Customer_ID = " + customerId;

                PreparedStatement preparedStatement = connection.prepareStatement(update);
                int rowsUpdated = preparedStatement.executeUpdate();
                return rowsUpdated > 0;
            } catch (SQLException error) {
                System.out.println("Error in updating Customer " + error.getMessage());
                return false;
            }


        }

        public static String getDivisionName(Connection connection, int divisionId){
            try {
                String divLookup = "select Division from first_level_divisions where Division_ID = " + divisionId;
                PreparedStatement preparedStatement = connection.prepareStatement(divLookup);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){ return resultSet.getString("Division");
                }
            } catch (SQLException error) {
                System.out.println("Division Name not loadin " + error.getMessage());

        }
            return "";
}
}