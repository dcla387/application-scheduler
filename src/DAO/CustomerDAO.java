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

            String custLookup = "SELECT * FROM customers";
            PreparedStatement ps = connection.prepareStatement(custLookup);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");

                String divisionName = getDivisionName(connection, divisionId);

                Customer customer = new Customer(customerId, customerName, address, postalCode, phone, divisionId, divisionName);

                customerList.add(customer);

            }

        } catch (SQLException error) {
            System.out.println("Customers unable to load" + error.getMessage());
        }

        return customerList;


        }

        public static String getDivisionName(Connection connection, int divisionId){
            try {
                String divLookup = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";
                PreparedStatement ps = connection.prepareStatement(divLookup);
                ps.setInt(1, divisionId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()){ return rs.getString("Division");
                }
            } catch (SQLException error) {
                System.out.println("Division Name not loadin " + error.getMessage());

        }
            return "";
}
}