package controller;

import Model.Appointment;
import DAO.AppointmentDAO;
import DAO.CustomerDAO;

import Model.Customer;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.net.URL;

public class apptMainController implements Initializable {

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> descriptionColumn;

    @FXML
    private TableColumn<Appointment, String> locationColumn;

    @FXML
    private TableColumn<Appointment, Integer> contactColumn;

    @FXML
    private TableColumn<Appointment, String > typeColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> startColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> endColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;

    @FXML
    private TableColumn<Appointment, Integer> userIdColumn;

    @FXML
    private ComboBox<String> customerComboBox;

    @FXML
    private RadioButton monthRadioButton;

    @FXML
    private RadioButton weekRadioButton;

    private ToggleGroup viewToggleGroup;

    private ObservableList<Appointment> allAppointments;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        viewToggleGroup = new ToggleGroup();
        monthRadioButton.setToggleGroup(viewToggleGroup);
        weekRadioButton.setToggleGroup(viewToggleGroup);

        monthRadioButton.setOnAction(event -> filterAppointmentsByTimeFrame());
        weekRadioButton.setOnAction(event -> filterAppointmentsByTimeFrame());



        allAppointments = AppointmentDAO.getAllAppointments();
        appointmentTableView.setItems(allAppointments);


        customerComboBox.getItems().add("All Customers");

        for (Customer customer : CustomerDAO.getAllCustomers()) {
            customerComboBox.getItems().add(customer.getCustomerName());
        }

        customerComboBox.setValue("All Customers");

        customerComboBox.setOnAction(actionEvent -> filterAppointmentsByCustomer());

    }

    private void filterAppointmentsByCustomer() {
        String selectedCustomer = customerComboBox.getValue();

        filterAppointmentsByTimeFrame();

        if (selectedCustomer == null || selectedCustomer.equals("All Customers")) {

            return;

        }

            ObservableList<Appointment> currentAppointments = appointmentTableView.getItems();
            ObservableList<Appointment> customerFiltered = FXCollections.observableArrayList();
        }

    private void filterAppointmentsByTimeFrame() {

            ObservableList<Appointment> allAppointments = AppointmentDAO.getAllAppointments();
            ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

            LocalDateTime now = LocalDateTime.now();

            if (monthRadioButton.isSelected()) {

                int currentMonth = now.getMonthValue();
                int currentYear = now.getYear();

                for (Appointment appointment : allAppointments) {

                    LocalDateTime appointmentStart = appointment.getStart();

                    if (appointmentStart.getMonthValue() == currentMonth && appointmentStart.getYear() == currentYear) {

                        filteredAppointments.add(appointment);
                    }
                }

            } else {
                filteredAppointments = allAppointments;
            }

            String selectedCustomer = customerComboBox.getValue();

            if (selectedCustomer != null && !selectedCustomer.equals("All Customers")) {

                ObservableList<Appointment> customerFiltered = FXCollections.observableArrayList();

                for (Appointment appointment : filteredAppointments) {
                    if (appointment.getCustomerName().equals(selectedCustomer)) {
                        customerFiltered.add(appointment);
                    }

                }
                appointmentTableView.setItems(customerFiltered);
            } else {
                appointmentTableView.setItems(filteredAppointments);
            }

        }



    public void onActionExitAppt(ActionEvent event) {System.exit(0);}

    public void toMainButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Page");
        stage.setScene(scene);
        stage.show();
    }

    public void onActionToCustRecs(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustMain.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customer Recs Page");
        stage.setScene(scene);
        stage.show();
    }

    public void onClickAddAppts(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddAppt.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Add Appt Page");
        stage.setScene(scene);
        stage.show();
    }

    public void onClickToModAppt(ActionEvent event) throws IOException {

        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please Selet an Appointment");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModAppt.fxml"));
        Parent root = loader.load();

        modApptController controller = loader.getController();
        controller.initData(selectedAppointment);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Appt Cust Page");
        stage.setScene(scene);
        stage.show();
    }

    public void onClickToDelAppt(ActionEvent event) {

        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Must Select An Appointment");
            alert.showAndWait();
            return;
        }

        int appointmentId = selectedAppointment.getAppointmentId();
        String appointmentType = selectedAppointment.getType();

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText("Are you SURE you want to Delete this Appointment?");

        if (confirm.showAndWait().get() == javafx.scene.control.ButtonType.OK) {

            AppointmentDAO.deleteAppointment(appointmentId);

            Alert doneAlert = new Alert(Alert.AlertType.INFORMATION);
            doneAlert.setTitle("This Appointment was Deleted");
            doneAlert.setHeaderText("Successful");
            doneAlert.setContentText("Appointment ID: " + appointmentId + "\nType: " + appointmentType);
            doneAlert.showAndWait();

            allAppointments = AppointmentDAO.getAllAppointments();
            appointmentTableView.setItems(allAppointments);

            filterAppointmentsByCustomer();
        }


    }

}
