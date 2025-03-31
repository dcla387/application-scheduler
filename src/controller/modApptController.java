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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;


public class modApptController implements Initializable {

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private TextField addCustNameTextField;

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
    private Appointment selectedAppointment;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        contactComboBox.setItems(ContactDAO.getAllContacts());
        populateTimeComboBoxes();

        userIDComboBox.getItems().addAll(1, 2);
        userIDComboBox.setValue(1);

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

        /*private void populateTimeComboBoxes() {
            LocalTime start = LocalTime.of(0, 0);
            LocalTime end = LocalTime.of(23, 30);

            while (!start.isAfter(end)) {
                String timestr = start.toString();
                startTimeComboBox.getItems().add(timestr);
                endTimeComboBox.getItems().add(timestr);

                start = start.plusMinutes(15);
            }
        }*/

        startDatePicker.setValue(appointment.getStart().toLocalDate());
        endDatePicker.setValue(appointment.getEnd().toLocalDate());

        startTimeComboBox.setValue(appointment.getStart().toLocalTime().toString());
        endTimeComboBox.setValue(appointment.getEnd().toLocalTime().toString());

        userIDComboBox.setValue(appointment.getUserId());


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

        int appointmentId = selectedAppointment.getAppointmentId();

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

    private boolean validateAppointment (String title, String description, LocalDateTime start, LocalDateTime end, int customerId) {
        if (title.isEmpty() || description.isEmpty() || start == null || end == null){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fields cannot be blank");
            alert.showAndWait();
            return false;
        }

        if (AppointmentDAO.isInBizHours(start, end)) {

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

        AppointmentDAO.updateAppointment(
                appointmentId,
                title,
                description,
                location,
                contact.getContactId(),
                type,  // This should now get updated
                startDateTime,
                endDateTime,
                customerId,
                userId
        );



        Parent root = FXMLLoader.load(getClass().getResource("/view/ApptMain.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Main Appt Page");
        stage.setScene(scene);
        stage.show();
    }


    }
