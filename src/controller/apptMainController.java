package controller;

import Model.Appointment;
import DAO.AppointmentDAO;
import DAO.CustomerDAO;
import DAO.JDBC;

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
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.net.URL;

import java.time.ZonedDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZoneId;

/**
 * This class is a Main Customer controller for Customers.
 *
 * <p>This controller manages the appointments table view and provides viewing,
 * adding, modifying, and deleting appointments. It includes filtering capabilities by time frame
 * (all, month, week) and by customer.</p>
 *
 *
 * @author Donna Clark
 * @version  1.0
 * */


public class apptMainController implements Initializable {

    /**
     * TableView displaying the list of appointments.
     */

    @FXML
    private TableView<Appointment> appointmentTableView;

    /**
     * TableColumn for displaying appointment IDs.
     */

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;

    /**
     * TableColumn for displaying appointment titles.
     */

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    /**
     * TableColumn for displaying appointment descriptions.
     */

    @FXML
    private TableColumn<Appointment, String> descriptionColumn;

    /**
     * TableColumn for displaying appointment locations.
     */

    @FXML
    private TableColumn<Appointment, String> locationColumn;

    /**
     * TableColumn for displaying appointment contact information.
     */

    @FXML
    private TableColumn<Appointment, Integer> contactColumn;

    /**
     * TableColumn for displaying appointment types.
     */

    @FXML
    private TableColumn<Appointment, String > typeColumn;

    /**
     * TableColumn for displaying appointment start date and time.
     */

    @FXML
    private TableColumn<Appointment, LocalDateTime> startColumn;

    /**
     * TableColumn for displaying appointment end date and time.
     */

    @FXML
    private TableColumn<Appointment, LocalDateTime> endColumn;

    /**
     * TableColumn for displaying customer IDs associated with appointments.
     */

    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;

    /**
     * TableColumn for displaying user IDs associated with appointments.
     */

    @FXML
    private TableColumn<Appointment, Integer> userIdColumn;

    /**
     * ComboBox for selecting customers to filter appointments.
     */

    @FXML
    private ComboBox<String> customerComboBox;

    /**
     * RadioButton for filtering appointments by month.
     */

    @FXML
    private RadioButton monthRadioButton;

    /**
     * RadioButton for filtering appointments by week.
     */

    @FXML
    private RadioButton weekRadioButton;

    /**
     * RadioButton for displaying all appointments.
     */

    @FXML
    private RadioButton allRadioButton;

    /**
     * ToggleGroup for managing the radio button selection state.
     */

    private ToggleGroup viewToggleGroup;

    /**
     * ObservableList containing all appointments from the database.
     */

    private ObservableList<Appointment> allAppointments;

    /**
     * Initializes the controller class.
     *
     * <p>This method is automatically called after the FXML file has been loaded. It performs
     * the following tasks:</p>
     * <ul>
     *   <li>Sets up table columns</li>
     *   <li>Configures date/time formatting</li>
     *   <li>Creates and configures a toggle group for the radio buttons</li>
     *   <li>Loads all appointments from the database into the table</li>
     *   <li>Populates the customer combo box with all customer names</li>
     * </ul>
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object, or null if not localized
     */

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

        startColumn.setCellFactory(column -> new TableCell<Appointment, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {

                    setText(item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                }
            }
        });


        endColumn.setCellFactory(column -> new TableCell<Appointment, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {

                    setText(item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                }
            }
        });


        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        viewToggleGroup = new ToggleGroup();
        allRadioButton.setToggleGroup(viewToggleGroup);
        monthRadioButton.setToggleGroup(viewToggleGroup);
        weekRadioButton.setToggleGroup(viewToggleGroup);

        allRadioButton.setOnAction(event -> filterAppointmentsByTimeFrame());
        monthRadioButton.setOnAction(event -> filterAppointmentsByTimeFrame());
        weekRadioButton.setOnAction(event -> filterAppointmentsByTimeFrame());

        allRadioButton.setSelected(true);

        allAppointments = AppointmentDAO.getAllAppointments();
        appointmentTableView.setItems(allAppointments);


        customerComboBox.getItems().add("All Customers");

        for (Customer customer : CustomerDAO.getAllCustomers()) {
            customerComboBox.getItems().add(customer.getCustomerName());
        }

        customerComboBox.setValue("All Customers");

        customerComboBox.setOnAction(actionEvent -> filterAppointmentsByCustomer());

    }

    /**
     * Filters appointments based on the selected customer.
     *
     * <p>This method is called when a customer is selected in the customer combo box.</p>
     *
     */

    private void filterAppointmentsByCustomer() {
        String selectedCustomer = customerComboBox.getValue();

        filterAppointmentsByTimeFrame();

        if (selectedCustomer == null || selectedCustomer.equals("All Customers")) {

            return;

        }

            ObservableList<Appointment> currentAppointments = appointmentTableView.getItems();
            ObservableList<Appointment> customerFiltered = FXCollections.observableArrayList();
        }

    /**
     * Filters appointments based on the selected time frame (all, month, week) and customer.
     *
     *
     * <p>Time frames include:</p>
     * <ul>
     *   <li>All: Shows all appointments</li>
     *   <li>Month: Shows appointments in the current month and year</li>
     *   <li>Week: Shows appointments in the next 7 days</li>
     * </ul>
     */

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

            } else if (weekRadioButton.isSelected()){

                LocalDateTime oneWeek = now.plusDays(7);

                for (Appointment appointment : allAppointments) {
                    LocalDateTime appointmentStart = appointment.getStart();

                    if ((appointmentStart.isEqual(now) || appointmentStart.isAfter(now)) && (appointmentStart.isBefore(oneWeek)) || (appointmentStart.isEqual(oneWeek))) {

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

    /**
     * Handles the exit application action.
     * Terminates the application immediately when the exit button is clicked.
     *
     * @param event The ActionEvent object generated when the exit button is clicked
     */

    public void onActionExitAppt(ActionEvent event) {System.exit(0);}

    /**
     * Navigates to the main menu screen when the main button is clicked.
     *
     * @param event The ActionEvent object generated when the main button is clicked
     * @throws IOException If an error occurs during FXML loading or scene switching
     */

    public void toMainButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Page");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates to the customer records screen when the customer records button is clicked.
     *
     * @param event The ActionEvent object generated when the customer records button is clicked
     * @throws IOException If an error occurs during FXML loading or scene switching
     */

    public void onActionToCustRecs(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustMain.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customer Recs Page");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates to the add appointment screen when the add appointment button is clicked.
     *
     * @param event The ActionEvent object generated when the add appointment button is clicked
     * @throws IOException If an error occurs during FXML loading or scene switching
     */

    public void onClickAddAppts(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddAppt.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Add Appt Page");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates to the modify appointment screen.
     *
     * <p>This method checks if an appointment is selected. If not,
     * it displays an error message.</p>
     *
     * @param event The ActionEvent object generated when the modify appointment button is clicked
     * @throws IOException If an error occurs during FXML loading or scene switching
     */

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

    /**
     * Handles the delete appointment action.
     *
     * <p>If an appointment is selected, it displays a
     * confirmation dialog and, if confirmed, deletes the appointment.</p>
     *
     * @param event The ActionEvent object generated when the delete appointment button is clicked
     */

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
