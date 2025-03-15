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

public class modCustController implements Initializable {

    @FXML
    private TextField addCustIDTextField;

    @FXML
    private TextField addCustNameTextField;

    @FXML
    private TextField addCustAddress1TextField;

    @FXML
    private TextField addCustomerPhoneTextField;

    @FXML
    private ComboBox<Country> addCustCountryComboBox;

    @FXML
    private ComboBox<Division> addCustStateComboBox;

    @FXML
    private TextField addCustZipTextField;

    private Customer selectedCustomer;

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
