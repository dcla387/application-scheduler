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

public class reportController {


    @FXML
    private Button countAppointments;

    @FXML
    private Button byContact;

    @FXML
    private Button filterByCountry;

    @FXML
    private Button backToMain;

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



    public void onClickBackToMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Main Menu Page");
        stage.setScene(scene);
        stage.show();
    }

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
}
