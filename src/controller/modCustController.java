package controller;

import DAO.CustomerDAO;
import Model.Customer;
import DAO.CountryDAO;
import Model.Country;
import DAO.DivisionDAO;
import Model.Division;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import javafx.collections.ObservableList;
import java.util.ResourceBundle;

import java.io.IOException;

/**
 * Controller class for modifying existing customer records.
 *
 * <p>This controller manages updating customer information.
 * It provides validation and data persistence through the CustomerDAO, as well as
 * country and division (state/province) selection with dynamic filtering.</p>
 *
 *
 * @author Donna Clark
 * @version 1.0
 */

public class modCustController implements Initializable {

    /**
     * TextField displaying the customer ID.
     * This field is disabled as IDs cannot be modified.
     */

    @FXML
    private TextField addCustIDTextField;

    /**
     * TextField for modifying the customer name.
     */

    @FXML
    private TextField addCustNameTextField;

    /**
     * TextField for modifying the customer's address.
     */

    @FXML
    private TextField addCustAddress1TextField;

    /**
     * TextField for modifying the customer's phone number.
     */

    @FXML
    private TextField addCustomerPhoneTextField;

    /**
     * ComboBox for selecting the customer's country.
     * When a country is selected, it filters the available divisions
     * in the state/province combo box.
     */

    @FXML
    private ComboBox<Country> addCustCountryComboBox;

    /**
     * ComboBox for selecting the customer's state/province (division).
     * The available options are dynamically updated based on the selected country.
     */

    @FXML
    private ComboBox<Division> addCustStateComboBox;

    /**
     * TextField for modifying the customer's postal code.
     */

    @FXML
    private TextField addCustZipTextField;

    /**
     * The customer being modified.
     * This contains the original values of the customer record before modification.
     */

    private Customer selectedCustomer;

    /**
     * Handles the save modified customer button upon click.
     *
     * <p>This method updates an existing customer record with the modified data from the form fields.
     * It performs validation to ensure all required fields are filled and shows appropriate alerts
     * based on the success or failure of the operation.</p>
     *
     * @param event The ActionEvent object generated when the save button is clicked
     * @throws IOException If an error occurs during FXML loading or scene switching
     */

    public void onClickToModCustomer(ActionEvent event) throws IOException {

        String name = addCustNameTextField.getText();
        String address = addCustAddress1TextField.getText();
        String postalCode = addCustZipTextField.getText();
        String phone = addCustomerPhoneTextField.getText();
        Division division = addCustStateComboBox.getValue();

        if (name.isEmpty() || address.isEmpty() || postalCode.isEmpty() ||
                phone.isEmpty() || division == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return;
        }

        boolean success = CustomerDAO.updateCustomer(
                selectedCustomer.getCustomerId(),
                name, address, postalCode, phone, division.getDivisionId());

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Completed");
            alert.setHeaderText("Customer has been updated");
            alert.setContentText("Customer  has been updated");
            alert.showAndWait();
        }

        Parent root = FXMLLoader.load(getClass().getResource("/view/CustMain.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Main Cust Page");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initializes the form with data from the selected customer for modification.
     *
     * <p>This method is called after the controller is initialized and populates all form fields
     * with the selected customer's data, including:</p>
     * <ul>
     *   <li>Basic information (ID, name, address, phone, postal code)</li>
     *   <li>Country and division (state/province) selection</li>
     * </ul>
     *
     * <p>The method performs the following tasks:</p>
     * <ol>
     *   <li>Sets the selectedCustomer field with the customer to be modified</li>
     *   <li>Populates text fields with customer data</li>
     *   <li>Determines the customer's country based on their division ID</li>
     *   <li>Selects the appropriate country in the country combo box</li>
     *   <li>Loads divisions for the selected country</li>
     *   <li>Selects the appropriate division in the division combo box</li>
     * </ol>
     *
     * @param customer The Customer object containing the data to be modified
     */

    public void initData(Customer customer) {
        this.selectedCustomer = customer;

        addCustIDTextField.setText(String.valueOf(customer.getCustomerId()));
        addCustNameTextField.setText(customer.getCustomerName());
        addCustAddress1TextField.setText(customer.getAddress());
        addCustomerPhoneTextField.setText(customer.getPhone());
        addCustZipTextField.setText(customer.getPostalCode());

        int divisionId = customer.getDivisionId();
        Country customerCountry = CountryDAO.getCountryByDivisionId(divisionId);

        for (Country country : addCustCountryComboBox.getItems()) {
            if (country.getCountryID() == customerCountry.getCountryID()) {
                addCustCountryComboBox.setValue(country);
                break;
            }
        }


        ObservableList<Division> divisions = DivisionDAO.getDivisionsByCountry(customerCountry.getCountryID());
        addCustStateComboBox.setItems(divisions);


        for (Division division : divisions) {
            if (division.getDivisionId() == divisionId) {
                addCustStateComboBox.setValue(division);
                break;
            }
        }
    }

    /**
     * Handles the cancel button click event.
     *
     * <p>This method cancels the customer modification process and returns
     * to the main customers screen without saving any changes.</p>
     *
     * @param event The ActionEvent object generated when the cancel button is clicked
     * @throws IOException If an error occurs during FXML loading or scene switching
     */

        public void onClickToCancelCustomer (ActionEvent event) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/view/CustMain.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.centerOnScreen();
            Scene scene = new Scene(root);
            stage.setTitle("Main Cust Page");
            stage.setScene(scene);
            stage.show();
        }

    /**
     * Initializes the controller class and sets up the UI components.
     *
     * <p>This method is automatically called after the FXML file has been loaded. It performs
     * the task of populating the country combo box with all available countries from the database.</p>
     *
     * <p>Note that the method does not populate fields with customer data,
     * as this happens later in the initData method when a customer is selected.</p>
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object, or null if not localized
     */
        @Override
        public void initialize (URL url, ResourceBundle resourceBundle){

            ObservableList<Country> countries = CountryDAO.getCountries();
            addCustCountryComboBox.setItems(countries);

        }

        public void onCountrySelected () {
            Country selectedCountry = addCustCountryComboBox.getValue();
            ObservableList<Division> divisions = DivisionDAO.getDivisionsByCountry(selectedCountry.getCountryID());
            addCustStateComboBox.setItems(divisions);
        }

        //Example 14-1 Creating a Combo Box with an Observable List
        //
        //ObservableList<String> options =
        //    FXCollections.observableArrayList(
        //        "Option 1",
        //        "Option 2",
        //        "Option 3"
        //    );
        //final ComboBox comboBox = new ComboBox(options);
    }
