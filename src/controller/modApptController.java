package controller;

import DAO.*;
import Model.*;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import javafx.collections.ObservableList;

import java.time.*;

import java.util.ResourceBundle;


/**
 * Controller class for modifying existing appointments in the scheduling system.
 *
 * <p>This controller manages the updating appointment records. It provides
 * form validation, time selection with business hours enforcement, overlap detection,
 * and time zone conversion to UTC for database storage.</p>
 *
 *
 * @author Donna Clark
 * @version 1.0
 */

public class modApptController implements Initializable {

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private TextField addCustNameTextField;

    /**
     * TextField displaying the appointment ID.
     * This field is read-only as IDs cannot be modified.
     */

    @FXML
    private TextField apptIDTextField;

    /**
     * TextField for modifying the appointment title.
     */

    @FXML

    private TextField titleTextField;

    /**
     * TextField for modifying the appointment description.
     */

    @FXML
    private TextField descriptionTextField;

    /**
     * TextField for modifying the appointment location.
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
     * TextField for modifying the appointment type.
     */

    @FXML
    private TextField typeTextField;

    /**
     * DatePicker for selecting the start date of the appointment.
     */

    @FXML
    private DatePicker startDatePicker;

    /**
     * ComboBox for selecting the start time of the appointment.
     * Populated with 15-minute increments throughout the day.
     */

    @FXML
    private ComboBox<String> startTimeComboBox;

    /**
     * DatePicker for selecting the end date of the appointment.
     */

    @FXML
    private DatePicker endDatePicker;

    /**
     * ComboBox for selecting the end time of the appointment.
     * Populated with 15-minute increments throughout the day.
     */

    @FXML

    private ComboBox<String> endTimeComboBox;

    /**
     * ComboBox for selecting the user ID associated with the appointment.
     */

    @FXML
    private ComboBox userIDComboBox;

    private Appointment selectedAppointment;

    /**
     * Initializes the controller class and sets up the UI components.
     *
     * <p>This method is automatically called after the FXML file has been loaded. It performs
     * the following tasks:</p>
     * <ul>
     *   <li>Populates the contact combo box with all available contacts</li>
     *   <li>Populates the time combo boxes with 15-minute increments</li>
     *   <li>Sets up the user ID combo box with available user IDs</li>
     * </ul>
     *
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object, or null if not localized
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        contactComboBox.setItems(ContactDAO.getAllContacts());
        populateTimeComboBoxes();

        userIDComboBox.getItems().addAll(1, 2);
        userIDComboBox.setValue(1);

    }

    /**
     * Populates the start and end time combo boxes with time slots.
     *
     */

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

    /**
     * Initializes the form with data from the selected appointment for modification.
     *
     * <p>This method is called after the controller is initialized and populates all form fields
     * with the selected appointment's data, including:</p>
     * <ul>
     *   <li>Basic information (ID, title, description, location, type)</li>
     *   <li>Associated customer name</li>
     *   <li>Associated contact</li>
     *   <li>Start and end dates and times</li>
     *   <li>User ID</li>
     * </ul>
     *
     * @param appointment The Appointment object containing the data to be modified
     */

    public void initData(Appointment appointment) {
        this.selectedAppointment = appointment;

        addCustNameTextField.setText(appointment.getCustomerName());
        apptIDTextField.setText(String.valueOf(appointment.getAppointmentId()));
        titleTextField.setText(appointment.getTitle());
        descriptionTextField.setText(appointment.getDescription());
        locationTextField.setText(appointment.getLocation());
        typeTextField.setText(appointment.getType());


        for (Contact contact : contactComboBox.getItems()) {
            if (contact.getContactName().equals(appointment.getContactName())) {
                contactComboBox.setValue(contact);
                break;
            }
        }





        LocalDateTime start = appointment.getStart();
        LocalDateTime end = appointment.getEnd();

        startDatePicker.setValue(start.toLocalDate());
        endDatePicker.setValue(end.toLocalDate());

        startTimeComboBox.setValue(start.toLocalTime().toString());
        endTimeComboBox.setValue(end.toLocalTime().toString());

        userIDComboBox.setValue(appointment.getUserId());


    }

    /**
     * Validates an appointment.
     *
     * <p>This method performs several validation checks:</p>
     * <ul>
     *   <li>Ensures required fields are not empty</li>
     *   <li>Verifies appointment times fall within business hours</li>
     *   <li>Checks for scheduling conflicts (overlapping appointments), excluding the current appointment being modified</li>
     * </ul>
     *
     * <p>If any validation check fails, an appropriate error alert is displayed
     * to inform the user of the issue.</p>
     *
     * @param title The appointment title
     * @param description The appointment description
     * @param start The appointment start date/time
     * @param end The appointment end date/time
     * @param customerId The customer ID associated with the appointment
     * @return true if appointment passes all validation checks, false otherwise
     */

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

        int appointmentId = selectedAppointment.getAppointmentId();

        if (AppointmentDAO.appointIsOverlapping(customerId, start, end, appointmentId)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment cannot be booked");
            alert.setContentText("There is already a booking at this time for this customer");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Handles the cancel button click event.
     *
     * <p>This method cancels the appointment modification process and returns
     * to the main appointments screen without saving any changes.</p>
     *
     * @param event The ActionEvent object generated when the cancel button is clicked
     * @throws IOException If an error occurs during FXML loading or scene switching
     */

    public void onClickToCancelAppointment(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ApptMain.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Main Appt Page");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Handles the main menu button click event.
     *
     * <p>This method navigates back to the main menu screen of the application
     * without saving any appointment changes.</p>
     *
     * @param event The ActionEvent object generated when the main menu button is clicked
     * @throws IOException If an error occurs during FXML loading or scene switching
     */

    public void onClickToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Main Page");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the save modified appointment button click event.
     *
     * <p>This method performs the following tasks:</p>
     * <ul>
     *   <li>Retrieves all input values from form fields</li>
     *   <li>Validates that all required fields are filled</li>
     *   <li>Checks that end time is after start time</li>
     *   <li>Converts local times to UTC for database storage</li>
     *   <li>Validates appointment business rules (via validateAppointment)</li>
     *   <li>Updates the appointment in the database</li>
     *   <li>Returns to the main appointments screen</li>
     * </ul>
     *
     * <p>If any validation fails, appropriate error alerts are displayed
     * and the save operation is aborted.</p>
     *
     * @param event The ActionEvent object generated when the save button is clicked
     * @throws IOException If an error occurs during FXML loading or scene switching
     */

    public void onClickModAppt(ActionEvent event) throws IOException {

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
            alert.setTitle("Error");
            alert.setContentText("Fill in All Fields to Save");
            alert.showAndWait();
            return;
        }

        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.parse(startTime));
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.parse(endTime));

        // Convert to UTC before saving

//        ZonedDateTime utcStart = startDateTime.atZone(ZoneId.systemDefault())
//
//                .withZoneSameInstant(ZoneId.of("UTC"));
//
//        ZonedDateTime utcEnd = endDateTime.atZone(ZoneId.systemDefault())
//
//                .withZoneSameInstant(ZoneId.of("UTC"));

        if (endDateTime.isBefore(startDateTime) || endDateTime.isEqual(startDateTime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("End time must be after start time");
            alert.showAndWait();
            return;
        }

        int appointmentId = selectedAppointment.getAppointmentId();
        int customerId = selectedAppointment.getCustomerId();
        int userId = (Integer) userIDComboBox.getValue();

        if (!validateAppointment(title, description, startDateTime, endDateTime, customerId)) {
            return;
        }

        AppointmentDAO.updateAppointment(
                appointmentId,
                title,
                description,
                location,
                contact.getContactId(),
                type,
                startDateTime, // Convert back to LocalDateTime but in UTC
                endDateTime,   // Convert back to LocalDateTime but in UTC
                customerId,
                userId
        );

        /*System.out.println("Modifying appointment: Local time values:");
        System.out.println("Start: " + startDateTime);
        System.out.println("End: " + endDateTime);*/

        Parent root = FXMLLoader.load(getClass().getResource("/view/ApptMain.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Main Appt Page");
        stage.setScene(scene);
        stage.show();
    }


    }
