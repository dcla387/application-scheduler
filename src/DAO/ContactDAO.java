package DAO;

import Model.Contact;
import Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object class for managing contact Data
 *
 * <p>This class provides methods for retrieving contact information from the database. It handles all contact related functions</p>
 *
 * @author Donna Clark
 * @version 1.0
 */

public class ContactDAO {

    /**
     * Retrieves all contacts from the database.
     *
     * <p>Executes a SQL query to fetch all contact records for use in the application</p>
     *
     * @return ObservableList containing all contacts in the database
     */


    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> contactList = FXCollections.observableArrayList();

        try {
            Connection connection = JDBC.getConnection();

            String contactLookup = "select Contact_ID, Contact_Name from contacts";
            PreparedStatement preparedStatement = connection.prepareStatement(contactLookup);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Contact contact = new Contact(
                        resultSet.getInt("Contact_ID"),
                        resultSet.getString("Contact_Name")
                );
                contactList.add(contact);
            }
        } catch (SQLException error) {
            System.out.println("There has been an error: " + error.getMessage());
        }
        return contactList;
    }

    /**
     * Retrieves the contact name associated with a specified contact ID
     *
     * @param contactId The ID of the contact to be looked up
     * @return The name of the contact with the specified ID
     */

    public static String getContactNameFromId(int contactId) {

        ObservableList<Contact> contacts = getAllContacts();

        for (Contact contact : contacts) {
            if (contact.getContactId() == contactId) {
                return contact.getContactName();
            }

        }
        return "Error";


    }
}

