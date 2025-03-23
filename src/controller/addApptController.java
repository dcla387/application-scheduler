package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.CustomerDAO;
import Model.Contact;
import Model.Customer;
import Model.Appointment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.net.URL;
import javafx.collections.ObservableList;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.Locale;
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
    private ComboBox<String> startTimeComboBox;

    @FXML
    private DatePicker endDatePicker;

    @FXML

    private ComboBox<String> endTimeComboBox;

    @FXML
    private ComboBox userIDComboBox;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerComboBox.setItems(CustomerDAO.getAllCustomers());
        contactComboBox.setItems(ContactDAO.getAllContacts());
        populateTimeComboBoxes();

        userIDComboBox.getItems().addAll(1, 2);
        userIDComboBox.setValue(1);

    }



    private void populateTimeComboBoxes() {
        LocalTime start = LocalTime.of(0, 0);
        LocalTime end = LocalTime.of(23, 45);


        while (!start.isAfter(end)) {
            String timestr = start.toString();
            startTimeComboBox.getItems().add(timestr);
            endTimeComboBox.getItems().add(timestr);
            start = start.plusMinutes(15);
        }
    }

    @FXML
    private void onClickSaveAppt(ActionEvent event) throws IOException {
        Customer customer = customerComboBox.getValue();

        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setContentText("You will need to make a selection");
            alert.showAndWait();
            return;
        }


        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        Contact contact = contactComboBox.getValue();
        String type = typeTextField.getText();
        LocalDate startDate = startDatePicker.getValue();
        String startTime = startTimeComboBox.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String endTime = endTimeComboBox.getValue();



        if (title.isEmpty() || description.isEmpty() || location.isEmpty() ||
            contact == null || type.isEmpty() || startDate == null ||
                startTime == null || endDate == null || endTime == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("There is an Error");
            alert.setContentText("All fields must be filled in");
            alert.showAndWait();
            return;
        }

        int userID = (Integer) userIDComboBox.getValue();

        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.parse(startTime));
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.parse(endTime));

        if (endDateTime.isBefore(startDateTime) || endDateTime.isEqual(startDateTime)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("You have an Error");
            alert.setContentText("End Time needs to be later than Start Time");
            alert.showAndWait();
            return;
        }

        AppointmentDAO.addAppointment(title, description, location, contact.getContactId(), type, startDateTime, endDateTime,
                customer.getCustomerId(), userID);

        Parent root = FXMLLoader.load(getClass().getResource("/view/ApptMain.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Main Appt Page");
        stage.setScene(scene);
        stage.show();
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

}
