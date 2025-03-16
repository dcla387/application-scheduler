package controller;

import DAO.CustomerDAO;
import Model.Customer;
import Model.Appointment;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import javafx.collections.ObservableList;
import org.w3c.dom.Text;

import java.util.ResourceBundle;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.awt.*;
import java.io.IOException;


public class addApptController implements Initializable {

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private TextField apptIDTextField;

    @FXML

    private TextField titleTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField locationTextField;

    @FXML
    private ComboBox<Contact> contactComboBox;

    @FXML
    private TextField typeTextField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<String> endTimeComboBox;

    @FXML
    private TextField userIDTextField;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerComboBox.setItems(CustomerDAO.getAllCustomers());
        contactComboBox.setItems(ContactDAO.getAllContacts());
        populateTimeComboBoxes();
        }

        String searchName = name.toLowerCase().trim();

        for (Customer customer : allCustomers) {
            if (customer.getCustomerName().toLowerCase().contains(searchName)) {
                selectedCustomer = customer;
                customerIdField.setText(String.valueOf(customer.getCustomerId()));
                return;
            }
        }

        customerIdField.clear();
        selectedCustomer = null;
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public void onClickToCancelAppointment(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ApptMain.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Main Appt Page");
        stage.setScene(scene);
        stage.show();
    }

    public void onClickToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Main Page");
        stage.setScene(scene);
        stage.show();
    }

    public void onClickSaveAppt(ActionEvent actionEvent) {
    }
}
