package controller;

import DAO.AppointmentDAO;
import DAO.JDBC;
import Model.Appointment;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Map;

/**
 * Controller class for generating and dispalying various types of reports.
 *
 * <p>This controller generates three different ty[es of reports</p>
 * <ul>
 *     <li>Appointment counts by ty[e and month</li>
 *     <li>Contact schedule reports showing appointment details for each contact</li>
 *     <li>customer counts by country</li>
 * </ul>
 *
 * <p>Reports are generated by pulling data from the database and displayed in a pop up dialog box</p>
 *
 * @author Donna Clark
 * @version 1.0
 * */

public class reportController {

    /**
     * Button that triggers the appointment count by ty[e and month report.
     */


    @FXML
    private Button countAppointments;

    /**
     * Button that triggers the contact schedule report.
     */

    @FXML
    private Button byContact;

    /**
     * Button tha ttriggers the customer count by country
     */

    @FXML
    private Button filterByCountry;

    /**
     * Button that navigates back to the main menu
     */

    @FXML
    private Button backToMain;

    /**
     * Generates and displays a report a appointment counts by type and month.
     *
     * <p>This method retrieves data about appointment types grouped by month displayed in a User friendly report</p>
     *
     * <p>The report shows each month followed by a list of appointment types and their respective counts for that month</p>
     *
     *
     * @param event Object generated when the report button is clicked
     */

    @FXML
    private void onClickTypeMonthReport(ActionEvent event) {

        StringBuilder report = new StringBuilder();
        report.append("Appointment Types by Month\n\n");

        Map<String, Map<String, Integer>> reportData = AppointmentDAO.getAppointmentsByMonthAndType();

        for (String month : reportData.keySet()) {
            report.append("\nMonth: ").append(month).append("\n");
            report.append("************************\n");

            Map<String, Integer> typeData = reportData.get(month);

            for (String type : typeData.keySet()) {
                int count = typeData.get(type);
                report.append(type).append(" : ").append(count).append("\n");

            }
        }





        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointments Report");
        alert.setHeaderText("by Month and by Type with count");
        TextArea textArea = new TextArea(report.toString());
        textArea.setEditable(false);
        textArea.setPrefHeight(400);
        textArea.setPrefWidth(400);

        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();

    }

    /**
     * Navigates back to the Main Menu screen.
     *
     * @param event Object generated when the back button is clicked
     * @throws IOException If an error occures during the loading
     */


    public void onClickBackToMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Main Menu Page");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Generates and displays a report of appointment schedules for each contact.
     *
     * <p>this method retrieves all contact names from the AppointmentDAO, then for each contact
     * retrieves their assocaiated appointments.</p>
     *
     * <p>This report is displayed in a User friendly alert dialog</p>
     *
     * <p>For each contact, the report shows a list of appointments with these details:</p>
     * <ul>
     *     <li>Appointment ID</li>
     *     <li>Title</li>
     *     <li>Type</li>
     *     <li>Description</li>
     *     <li>Start date and time</li>
     *     <li>End date and Time</li>
     *     <li>Customer ID</li>
     * </ul>
     *
     * <p>If a contact has no scheduled appointment, the report will display that message.</p>
     *
     * @param event Object generated when the contactreport button is clicked.
     */

    @FXML
    private void onClickContactReport(ActionEvent event) {

        ObservableList<String> contactNames = AppointmentDAO.getAllContactNames();

        StringBuilder report = new StringBuilder();
        report.append("Contacts Schedule\n\n");

        for (String contactName : contactNames) {
            report.append("\nContact: ").append(contactName).append("\n");
            report.append("************************\n");

        int contactId = AppointmentDAO.getContactIdFromName(contactName);

        ObservableList<Appointment> appointments = AppointmentDAO.getAppointmentByContactId(contactId);

        if (appointments.isEmpty()) {
            report.append("Did not find an scheduled appointment\n\n");
        } else {

            for (Appointment appointment : appointments) {

                report.append("Appointment ID: ").append(appointment.getAppointmentId()).append("\n");
                report.append("Title: ").append(appointment.getTitle()).append("\n");
                report.append("Type: ").append(appointment.getType()).append("\n");
                report.append("Description: ").append(appointment.getDescription()).append("\n");
                report.append("Start: ").append(appointment.getStart()).append("\n");
                report.append("End: ").append(appointment.getEnd()).append("\n");
                report.append("Customer ID: ").append(appointment.getCustomerId()).append("\n");
                report.append("*******************************\n");

            }
            report.append("\n");

            }


        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Schedules for Contact");
        alert.setHeaderText("Break Down");
        TextArea textArea = new TextArea(report.toString());
        textArea.setEditable(false);
        textArea.setPrefHeight(400);
        textArea.setPrefWidth(400);

        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();


        }

    /**
     * Generates and displays a report of customer count by country.
     *
     * <p>this method pulls the information from the AppointmentDAO and appends it to a String Builder.</p>
     *
     * <p>the report shows each country and the number of customers associated with that country</p>
     *
     * @param event Object generated when the contactreport button is clicked.
     */
    @FXML
   private void onClickCountryReport(ActionEvent event) {

        StringBuilder report = new StringBuilder();

        AppointmentDAO.getCustomerCountByCountry(report);

       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setTitle("Customer Count By Country");
       alert.setHeaderText("Break Down");
       TextArea textArea = new TextArea(report.toString());
       textArea.setEditable(false);
       textArea.setPrefHeight(400);
       textArea.setPrefWidth(400);

       alert.getDialogPane().setContent(textArea);
       alert.showAndWait();



   }
}
