package controller;

import DAO.AppointmentDAO;
import DAO.JDBC;
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
}
