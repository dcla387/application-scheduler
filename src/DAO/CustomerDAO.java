package DAO;

import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * Data Access Object for managing customer data.
 *
 * <p>This class provides methods for retrieveing, creating, updating customers in the database.</p>
 *
 * @author Donna Clark
 * @version 1.0
 */

public class CustomerDAO {

    /**
     * Retrieves all customers from the database
     *
     * <p>this method gets all customer records from the database - performs lookup of division names associated with customer records</p>
     * @return ObservableList containing all customer with associated division names
     */
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

    /**
     * Updates an existing customer record in the database.
     *
     * @param customerId The ID of the customer to update
     * @param name The updated customer name
     * @param address The updated address
     * @param postalCode The updated postal code
     * @param phone The updated phone number
     * @param divisionId The updated division ID
     * @return true if the update was a success
     */

        public static boolean updateCustomer(int customerId, String name, String address, String postalCode, String phone, int divisionId) {
            try {
                Connection connection = JDBC.getConnection();
                String update = "Update Customers Set Customer_Name = '" + name + "', Address = '" + address + "', Postal_Code = '" + postalCode + "', Phone = '" + phone + "', Division_ID = " + divisionId + " where Customer_ID = " + customerId;

                PreparedStatement preparedStatement = connection.prepareStatement(update);
                int rowsUpdated = preparedStatement.executeUpdate();
                return rowsUpdated > 0;
            } catch (SQLException error) {
                System.out.println("Error in updating Customer " + error.getMessage());
                return false;
            }


        }

    /**
     * Adds a new customer to the database
     *
     * @param name the customer name
     * @param address The customer address
     * @param postalCode  The customer postal code
     * @param phone The customer phone number
     * @param divisionId The division ID associated wit hthe customer
     * @return True if the customer was successfully added, false if otherwise
     */

    public static boolean addCustomer(String name, String address, String postalCode, String phone, int divisionId) {
        try {
            Connection connection = JDBC.getConnection();
            String newCust = "Insert Into customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) " +
                    "VALUES ('" + name + "', '" + address + "', '" + postalCode + "', '" + phone + "', " + divisionId + ")";

            PreparedStatement preparedStatement = connection.prepareStatement(newCust);
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException error) {
            System.out.println("Error in Adding New Customer " + error.getMessage());
            return false;
        }
    }

    /**
     * Deletes a customer from the database.
     *
     * <p>This method removes a customer record based on its ID</p>
     *
     * @param customerId The ID of the customer to delete
     * @return true If the customer was succesfully deleted, false if otherwise
     */

        public static boolean delCustomer(int customerId) {
            try {
                Connection connection = JDBC.getConnection();
                String delete = "Delete From customers Where Customer_ID = " + customerId;
                PreparedStatement preparedStatement = connection.prepareStatement(delete);
                int rowsDeleted = preparedStatement.executeUpdate();
                return rowsDeleted > 0;
            } catch (SQLException error) {
                System.out.println("Error in Deleting Customer " + error.getMessage());
                return false;
            }
        }

    /**
     * Retrieves the division name associated with a division ID.
     *
     * @param connection The active database connection
     * @param divisionId The ID of the division to look up
     * @return The name of the division, or empty string if not found
     */

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

    /**
     * Retrieves a customer name based on the customer ID.
     *
     * <p>This method looks up a customer's name using their ID, useing Java Stream to filter and extract the name from all customer.</p>
     *
     * <p>A LAMBDA expression is used here to filter the stream of customers by ID and extract the customer name.  This is more
     * precise than with a For-loop. It provides a functional programming style that clarifies the intent.</p>
     *
     * @param customerId The ID of the customer to look up
     * @return The name of the customer, or ERROR if not found.
     */

    public static String getCustomerNameFromId(int customerId) {
        ObservableList<Customer> customers = getAllCustomers();

        return customers.stream()
                .filter(customer -> customer.getCustomerId() == customerId)
                .map(Customer::getCustomerName)
                .findFirst()
                .orElse("Error");

        /*Lambda Expression swap out
        for (Customer customer : customers) {
            if (customer.getCustomerId() == customerId) {
                return customer.getCustomerName();
            }
        }
        return "Error"; */
    }


}