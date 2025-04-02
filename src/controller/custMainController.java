package controller;

import DAO.AppointmentDAO;
import DAO.CustomerDAO;
import DAO.JDBC;
import Model.Appointment;
import Model.Customer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;

import java.io.IOException;

/**
 * Controller class for the main customer screen of the application.
 *
 * <p>This controller manages the customer table and provides functionality for viewing,
 * adding, modifying, and deleting customer records. It includes navigation to other screens
 * and proper handling of customer-appointment relationships when performing deletions.</p>
 *
 *
 * @author Donna Clark
 * @version 1.0
 */

public class custMainController implements Initializable {

    /**
     * TableView displaying the list of customers.
     */

    @FXML
    private TableView<Customer> customerTableView;

    /**
     * TableColumn for displaying customer names.
     */

    @FXML
    private TableColumn<Customer, String> nameColumn;

    /**
     * TableColumn for displaying customer addresses.
     */

    @FXML
    private TableColumn<Customer, String> addressColumn;

    /**
     * TableColumn for displaying customer postal codes.
     */

    @FXML
    private TableColumn<Customer, String> postalCodeColumn;


    /**
     * TableColumn for displaying customer divisions (state/province).
     */

    @FXML
    private TableColumn<Customer, String> divisionColumn;

    /**
     * TableColumn for displaying customer phone numbers.
     */

    @FXML
    private TableColumn<Customer, String> phoneColumn;


    /**
     * Initializes the controller class.
     *
     * <p>This method is automatically called after the FXML file has been loaded. It performs
     * the following tasks:</p>
     * <ul>
     *   <li>Sets up table columns with their corresponding Customer properties</li>
     *   <li>Loads customer data from the database and populates the table</li>
     * </ul>
     *
     * @param url The location used to resolve relative paths for the root object
     * @param rb The resources used to localize the root object, or null if not localized
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));


        loadCustomerData();
    }

/**
 * Loads all customer data from the database and displays it in the table.
 *
 * <p>This method retrieves all customers, sorts them alphabetically by name,
 * and populates the table view with the sorted list.</p>
 *
 * <p>A lambda expression is used for sorting customers alphabetically by name.
 * This approach is chosen because it provides a concise, readable way to define
 * the comparison logic inline. Using a lambda instead of a traditional Comparator
 * reduces code verbosity while maintaining clarity about the sorting criteria.
 * The lambda efficiently encapsulates the string comparison in a single line
 * without requiring a separate class definition.</p>
 */

    //Lamba expression usage

    private void loadCustomerData(){
        ObservableList<Customer> customerList = CustomerDAO.getAllCustomers();

        //Lambda - sort customers on Table View alphabetically

        customerList.sort((c1, c2) -> c1.getCustomerName().compareTo(c2.getCustomerName()));

        customerTableView.setItems(customerList);
    }

    /**
     * Handles the exit application action.
     * Terminates the application immediately when the exit button is clicked.
     *
     * @param event The object generated when the exit button is clicked
     */

    public void onActionExitCust(ActionEvent event) {
        System.exit(0);
    }

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
     * Navigates to the appointments screen when the appointments button is clicked.
     *
     * @param event The ActionEvent object generated when the appointments button is clicked
     * @throws IOException If an error occurs during FXML loading or scene switching
     */

    public void onClickAppts(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ApptMain.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Appt Page");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates to the add customer screen when the add customer button is clicked.
     *
     * @param event The ActionEvent object generated when the add customer button is clicked
     * @throws IOException If an error occurs during FXML loading or scene switching
     */

    public void onClickAddCustomer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddCust.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Add Cust Page");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates to the modify customer screen with the selected customer data.
     *
     * <p>This method checks if a customer is selected in the table view. If not,
     * it displays an error message. If a customer is selected, it loads the
     * modification screen and initializes it with the selected customer's data.</p>
     *
     * @param event The ActionEvent object generated when the modify customer button is clicked
     * @throws IOException If an error occurs during FXML loading or scene switching
     */

    public void onClickToModCust(ActionEvent event) throws IOException {


        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please Selet a Customer");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModCust.fxml"));
        Parent root = loader.load();

        modCustController controller = loader.getController();
        controller.initData(selectedCustomer);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Mod Cust Page");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the delete customer action.
     *
     * <p>This method performs the following tasks:</p>
     * <ul>
     *   <li>Checks if a customer is selected in the table view</li>
     *   <li>Checks if the selected customer has associated appointments</li>
     *   <li>If appointments exist, warns the user and asks for confirmation</li>
     *   <li>If confirmed, deletes all associated appointments and the customer</li>
     * </ul>
     *
     * @param event The ActionEvent object generated when the delete customer button is clicked
     * @throws IOException If an error occurs during data operations
     */

    public void onClickToDelCust(ActionEvent event) throws IOException {

        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please Selet a Customer");
            alert.showAndWait();
            return;
        }

        int customerId = selectedCustomer.getCustomerId();
        ObservableList<Appointment> customerAppointments = AppointmentDAO.getAppointmentsByCustomerId(customerId);

        if (!customerAppointments.isEmpty()) {
            Alert appointmentAlert = new Alert(Alert.AlertType.WARNING);
            appointmentAlert.setTitle("This is a Warning");
            appointmentAlert.setHeaderText("Selected Customer has Assocaited Appointments");
            appointmentAlert.setContentText("The appointments will be canceld if this customer is deleted. Do you want to continue?");

            if(appointmentAlert.showAndWait().get() !=javafx.scene.control.ButtonType.OK) {
                return;
            }
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText("Are you sure you want to Delete this Customer");


        if (confirm.showAndWait().get() == javafx.scene.control.ButtonType.OK) {

            if(!customerAppointments.isEmpty()) {
                for (Appointment appointment : customerAppointments) {
                    AppointmentDAO.deleteAppointment(appointment.getAppointmentId());
                }
            }

            CustomerDAO.delCustomer(customerId);

            customerTableView.setItems(CustomerDAO.getAllCustomers());
        }

    }

}
