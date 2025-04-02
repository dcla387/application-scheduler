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
 * This class is a controller for the adding of Customers.
 *
 * <p>This controller manages the UI for customer handling while adding new and updating customers.
 * It performs validations.</p>
 *
 *
 *
 * @author Donna Clark
 * @version  1.0
 * */

public class addCustController implements Initializable {

    /**
     * TextField displaying the customer ID.
     * This field is disabled as IDs are auto-generated for new customers
     * or pre-filled for customer modifications.
     */

    @FXML
    private TextField newCustIDTextField;

    /**
     * TextField for entering the customer name.
     */

    @FXML
    private TextField newCustNameTextField;

    /**
     * TextField for entering the customer address.
     */

    @FXML
    private TextField newCustAddress1TextField;

    /**
     * TextField for entering the customer phone.
     */

    @FXML
    private TextField newCustomerPhoneTextField;

    /**
     * Combo box for selecting customers county.
     * When a country is selected there is a filter for divisions.
     */

    @FXML
    private ComboBox<Country> addCustCountryComboBox;

    /**
     * Combo box for selecting customers state.
     */

    @FXML
    private ComboBox<Division> addCustStateComboBox;

    /**
     * TextField for entering the customer's zip code.
     */

    @FXML
    private TextField newCustZipTextField;

    /**
     * Customer being modified.
     */

    private Customer selectedCustomer;

    /**
     * Handles the mod customer button click event.
     *
     * <p>This method performs the following:</p>
     * <ul>
     *   <li>Upadtes an existing customer</li>
     *   <li>Validates that all required fields are filled</li>
     * </ul>
     *
     * <p>If any validation fails, appropriate error alerts are displayed
     * and the save operation is aborted.</p>
     *
     * @param event The ActionEvent object generated when the mod button is clicked
     * @throws IOException If an error occurs during FXML loading or scene switching
     */

    public void onClickToModCustomer(ActionEvent event) throws IOException {

        String name = newCustNameTextField.getText();
        String address = newCustAddress1TextField.getText();
        String postalCode = newCustZipTextField.getText();
        String phone = newCustomerPhoneTextField.getText();
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
     * This Initializes the controller class to set up the interface.
     *
     * <p>This is called after FXML is loaded and does the following:</p>
     * <ul>
     *     <li>Populates the customer current information</li>
     *
     * </ul>
     *
     * @param customer The Customer object containing the data to be modified
     *
     */

    public void initData(Customer customer) {
        this.selectedCustomer = customer;

        newCustIDTextField.setText(String.valueOf(customer.getCustomerId()));
        newCustNameTextField.setText(customer.getCustomerName());
        newCustAddress1TextField.setText(customer.getAddress());
        newCustomerPhoneTextField.setText(customer.getPhone());
        newCustZipTextField.setText(customer.getPostalCode());

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
     * Handles the click to cancel click event.
     *
     * <p>This method cancels the customer creation or modification process and returns
     *     to the main customers screen without saving any data</p>
     *
     * @param event The ActionEvent object generated when the mod button is clicked
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

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){

        ObservableList<Country> countries = CountryDAO.getCountries();
        addCustCountryComboBox.setItems(countries);

    }

    /**
     * Handles the country selection event.
     *
     * <p>This method is triggered when a country is selected in the country combo box.
     * It updates the division (state/province) combo box with only the divisions
     * associated with the selected country.</p>
     */

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


    public void onClickToSaveCustomer(ActionEvent event) throws IOException {

        String name = newCustNameTextField.getText();
        String address = newCustAddress1TextField.getText();
        String postalCode = newCustZipTextField.getText();
        String phone = newCustomerPhoneTextField.getText();
        Division division = addCustStateComboBox.getValue();

        if (name.isEmpty() || address.isEmpty() || postalCode.isEmpty() ||
                phone.isEmpty() || division == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eror");
            alert.setHeaderText("It's not completed");
            alert.setContentText("All fields need input");
            alert.showAndWait();
            return;
        }

        CustomerDAO.addCustomer(name, address, postalCode, phone, division.getDivisionId());


        Parent root = FXMLLoader.load(getClass().getResource("/view/CustMain.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Add Cust Page");
        stage.setScene(scene);
        stage.show();
    }


}
