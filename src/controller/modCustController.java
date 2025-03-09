package controller;

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
    private ComboBox<String> addCustCountryTextField;

    @FXML
    private ComboBox<String> addCustStateTextField;

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

    }
}
