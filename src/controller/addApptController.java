package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.CustomerDAO;
import DAO.JDBC;
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

import java.time.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.time.format.DateTimeFormatter;


import java.awt.*;
import java.io.IOException;

/**
 * This class is a controller for the adding of Appointments.
 *
 * <p>This controller manages the UI for appointment handling while adding appointments.
 * It performs validations and enforces business time scheduling and performs overlapping appointment checks.</p>
 *
 *
 * <p>Handles time zone conversions as well.</p>
 *
 * @author Donna Clark
 * @version  1.0
 * */

public class addApptController implements Initializable {

    /**
     * ComboBox for selecting a customer for the appointment.
     * Populated with all available customers from the database.
     */

    @FXML
    private ComboBox<Customer> customerComboBox;

    /**
     * TExt Field for displaying the appt ID
     */

    @FXML
    private TextField apptIDTextField;

    /**
     * TextField for entering the appointment title.
     */

    @FXML

    private TextField titleTextField;

    /**
     * TextField for entering the description.
     */

    @FXML
    private TextField descriptionTextField;

    /**
     * TextField for entering the location.
     */

    @FXML
    private TextField locationTextField;

    /**
     * ComboBox for selecting a contact person for the appointment.
     * Populated with all available contacts from the database.
     */

    @FXML
    private ComboBox<Contact> contactComboBox;

    /**
     * TextField for entering the appointment type.
     */

    @FXML
    private TextField typeTextField;

    /**
     * Date picker for selecting the start of the appointment
     */

    @FXML
    private DatePicker startDatePicker;

    /**
     * ComboBox for selecting the start time of the appointment.
     * Populated with 15-minute increments.
     */

    @FXML
    private ComboBox<String> startTimeComboBox;

    /**
     * Date picker for selecting the end of the appointment
     */

    @FXML
    private DatePicker endDatePicker;

    /**
     * ComboBox for selecting the end time of the appointment.
     * Populated with 15-minute increments.
     */

    @FXML

    private ComboBox<String> endTimeComboBox;

    /**
     * ComboBox for selecting the user Id associated with the appointment.
     * Supporting 2 users at the moment 1 and 2.
     */

    @FXML
    private ComboBox userIDComboBox;

    /**
     * This Initializes the controller class to set up the interface.
     *
     * <p>This is called after FXML is loaded and does the following:</p>
     * <ul>
     *     <li>Populates the customer combo boxes with customers</li>
     *     <li>Populates contact combo box with contacts</li>
     *     <li>Populates the time combo boxes</li>
     *     <li>Sets default values for dates and times</li>
     *     <li>Populates the combo boxes with available user IDs</li>
     * </ul>
     * @param url The location for resolving relative paths
     * @param resourceBundle for localizing the code. In this case looking to see if French needs to be triggered
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerComboBox.setItems(CustomerDAO.getAllCustomers());
        contactComboBox.setItems(ContactDAO.getAllContacts());
        populateTimeComboBoxes();

        userIDComboBox.getItems().addAll(1, 2);
        userIDComboBox.setValue(1);

        LocalDate today = LocalDate.now();
        startDatePicker.setValue(today);
        endDatePicker.setValue(today);

        LocalTime defaultStartTime = LocalTime.of(9, 0);
        if (startTimeComboBox.getItems().contains(defaultStartTime.toString())) {
            startTimeComboBox.setValue(defaultStartTime.toString());

        }

        LocalTime defaultEndTime = LocalTime.of(10, 0);
        if (endTimeComboBox.getItems().contains(defaultEndTime.toString())) {
            endTimeComboBox.setValue(defaultEndTime.toString());
        }

    }

    private boolean validateAppointment (String title, String description, LocalDateTime start, LocalDateTime end, int customerId) {
        if (title.isEmpty() || description.isEmpty() || start == null || end == null){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fields cannot be blank");
            alert.showAndWait();
            return false;
        }

        if (!AppointmentDAO.isInBizHours(start, end)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Out of Range");
            alert.setContentText("This appointment is not within Business Hours");
            alert.showAndWait();
            return false;
        }

        int appointmentId = 0;

        if (AppointmentDAO.appointIsOverlapping(customerId, start, end, appointmentId)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment cannot be booked");
            alert.setContentText("There is already a booking at this time for this customer");
            alert.showAndWait();
            return false;
        }
        return true;
    }



    private void populateTimeComboBoxes() {
        LocalTime start = LocalTime.of(0, 0);
        LocalTime end = LocalTime.of(23, 30);


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

        // Convert to UTC before saving

        ZonedDateTime utcStart = startDateTime.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime utcEnd = endDateTime.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"));

        if (endDateTime.isBefore(startDateTime) || endDateTime.isEqual(startDateTime)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("You have an Error");
            alert.setContentText("End Time needs to be later than Start Time");
            alert.showAndWait();
            return;
        }

        if (!validateAppointment(title, description, startDateTime, endDateTime, customer.getCustomerId())) {
            return;
        }

        AppointmentDAO.addAppointment(
                title,
                description,
                location,
                contact.getContactId(),
                type,
                utcStart.toLocalDateTime(),
                utcEnd.toLocalDateTime(),
                customer.getCustomerId(),
                userID);

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
