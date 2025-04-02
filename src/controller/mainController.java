package controller;

import DAO.AppointmentDAO;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class mainController implements Initializable {

    @FXML
    private Button mainAppointments;

    @FXML
    private Button mainCustomers;

    @FXML
    private Button mainReports;

    @FXML
    private Button mainExit;

    private int userId;

    public void onActionExit(ActionEvent event) {System.exit(0);}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }

    public void setUserId (int userId) {
        this.userId = userId;

        checkForAppointments(userId);
    }

    private void checkForAppointments(int userId) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextFifteen = now.plusMinutes(15);

        System.out.println("Checking appointments for user ID: " + userId);
        System.out.println("Current time: " + now);
        System.out.println("15 minutes from now: " + nextFifteen);

        ObservableList<Appointment> allAppointments = AppointmentDAO.getAllAppointments();

        System.out.println("Total appointments in system: " + allAppointments.size());

        for (Appointment a : allAppointments){
            System.out.println("Appt ID: " + a.getAppointmentId() +
                                ", User ID: " + a.getUserId() +
                                ", Start: " + a.getStart() +
                                ", Customer: " + a.getCustomerName());

        }

        ObservableList<Appointment> upcomingAppointments = FXCollections.observableArrayList();

        for (Appointment appointment : allAppointments) {

            System.out.println("Checking appt ID: " + appointment.getAppointmentId());
            System.out.println("Appt user ID: " + appointment.getUserId() + ", Login user ID: " + userId);

            if(appointment.getUserId() == userId) {
                LocalDateTime appointmentStart = appointment.getStart();

                System.out.println("Appointment start: " + appointmentStart);
                System.out.println("Is after now: " + appointmentStart.isAfter(now));
                System.out.println("Is before nextFifteen: " + appointmentStart.isBefore(nextFifteen));

                if (appointmentStart.isAfter(now) && appointmentStart.isBefore(nextFifteen)) {
                    System.out.println("Found upcoming appointment: " + appointment.getAppointmentId());
                    upcomingAppointments.add(appointment);
                }
            }
        }

        System.out.println("Found " + upcomingAppointments.size() + " upcoming appointments");

        if (upcomingAppointments.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointments Alert");
            alert.setContentText("You have no appointments in hte next 15 minutes");
            alert.showAndWait();

        } else {

            StringBuilder message = new StringBuilder();

            for (Appointment appointment : upcomingAppointments) {

                message.append("Appointment ID: ").append(appointment.getAppointmentId())
                        .append("\nDate: ").append(appointment.getStart().toLocalDate())
                        .append("\nTime: ").append(appointment.getStart().toLocalTime())
                        .append("\n\n");

            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upcoming Appointments");
            alert.setHeaderText("You have this appointment within the next 15 mins");
            alert.setContentText(message.toString());
            alert.showAndWait();
        }
        }

    public void onClickAppts(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ApptMain.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Appt Page");
        stage.setScene(scene);
        stage.show();
    }


    public void onClickCusts(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustMain.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Cust Page");
        stage.setScene(scene);
        stage.show();
    }


    public void onClickToReports(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Report.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Report Page");
        stage.setScene(scene);
        stage.show();
    }
}
