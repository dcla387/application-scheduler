package controller;

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

    public void onClickToModCustomer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustMain.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Main Cust Page");
        stage.setScene(scene);
        stage.show();
    }

    public void onClickToCancelCustomer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustMain.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        Scene scene = new Scene(root);
        stage.setTitle("Main Cust Page");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Country> countries = CountryDAO.getCountries();
        addCustCountryComboBox.setItems(countries);

   }

   public void onCountrySelected(){
        Country selectedCountry = addCustCountryComboBox.getValue();
        ObservableList<Division> divisions = DivisionDAO.getDivisions();
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
