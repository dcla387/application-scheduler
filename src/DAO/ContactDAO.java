package DAO;

import Model.Contact;
import Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactDAO {


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
}
