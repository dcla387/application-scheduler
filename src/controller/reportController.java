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
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class reportController {


    @FXML
    private Button countAppointments;

    @FXML
    private Button byContact;

    @FXML
    private Button filterByCountry;

    @FXML
    private Button backToMain;

    private void onClickTypeMonthReport(ActionEvent event){

        StringBuilder report = new StringBuilder();
        report.append("Appointment Types by Month\n\n");

        try {

            Connection connection = JDBC.getConnection();

            String getInfo = "Select MONTHNAME(Start) as Month, type, COUNT(*) as COUNT " +
                            "From Appointments " +
                            "Group by MONTHNAME(Start), Type " +
                            "Order by MONTH(Start), Type ";

            PreparedStatement preparedStatement = connection.prepareStatement(getInfo);
            ResultSet resultSet = preparedStatement.executeQuery();

            String currentMonth = "";

            while (resultSet.next()){

                String month = resultSet.getString("Month");
                String type = resultSet.getString( "Type");
                int count = resultSet.getInt("Count");

                if(!month.equals(currentMonth));
                    currentMonth = month;
                    report.append("\nMonth: ").append(month).append("\n");


            }

        }



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
