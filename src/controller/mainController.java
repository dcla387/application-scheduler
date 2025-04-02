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

/**
 * Controller class for the Main Controller.
 *
 * <p>This controller is a navigation agent to the following screens and functions</p>
 * <ul>
 *     <li>Appointments</li>
 *     <li>Customers</li>
 *     <li>Reports</li>
 *     <li>Provides an alert to appointements within the next 15 mins</li>
 * </ul>
 *
 * @author Donna Clark
 * @version 1.0
 *
 * */

public class mainController implements Initializable {

    /**
     * Button that navigates to the Appointment screen upon Click
     *
     */

    @FXML
    private Button mainAppointments;

    /**
     * Button that navigates to the Customer screen upon Click
     *
     */

    @FXML
    private Button mainCustomers;

    /**
     * Button that navigates to the Reports screen upon Click
     *
     */

    @FXML
    private Button mainReports;

    /**
     * Button that exits the Controller upon Click
     *
     */

    @FXML
    private Button mainExit;

    /**
     * The ID of the currently logged-in user.
     *
     * This ID is used to filter appointments specific to the user.
     */

    private int userId;

    /**
     * Terminates the program action.
     *
     */

    public void onActionExit(ActionEvent event) {System.exit(0);}

    /**
     * This Initializes the controller class.
     *
     * <p>This is called after FXML is loaded and does the following:</p>
     *
     * @param url The location for resolving relative paths
     * @param resourceBundle for localizing the code.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }

    /**
     * Sets the user ID for the session and checks for upcoming appointments.
     *
     * <p>This method is called after successful login and checks for any upcoming appointments for the user.</p>
     *
     * @param userId The ID of the user
     */

    public void setUserId (int userId) {
        this.userId = userId;

        checkForAppointments(userId);
    }

    /**
     * Checks for upcoming appointments within the next 15 minutes for the specified user.
     *
     * <p>This method gets all appointments from the database, filters them for current user
     * and for the appointments that are scheduled to start within the next 15 minutes,
     * and displays an alert.</p>
     *
     * <p>If no upcoming appointments are found, an alert is displayed.
     * If one or more upcoming appointments are found, an alert shows appointment
     * IDs, dates, and times is displayed.</p>
     *
     * @param userId The ID of the user to check appointments for
     */

    private void checkForAppointments(int userId) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextFifteen = now.plusMinutes(15);



        ObservableList<Appointment> allAppointments = AppointmentDAO.getAllAppointments();



        for (Appointment a : allAppointments){

        }

        ObservableList<Appointment> upcomingAppointments = FXCollections.observableArrayList();

        for (Appointment appointment : allAppointments) {



            if(appointment.getUserId() == userId) {
                LocalDateTime appointmentStart = appointment.getStart();



                if (appointmentStart.isAfter(now) && appointmentStart.isBefore(nextFifteen)) {

                    upcomingAppointments.add(appointment);
                }
            }
        }



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
