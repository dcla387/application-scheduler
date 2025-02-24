package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;


public class loginController implements Initializable {

    private static final String ADMIN_TEST_USER = "admin";
    private static final String ADMIN_TEST_PASS = "password123";

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Button login;

    @FXML
    private Button exit;

    @FXML
    private Label location;

        public void onActionExit(ActionEvent event) {System.exit(0);}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        Locale locale = Locale.getDefault();
        String countryName = locale.getDisplayCountry();

        location.setText(countryName);

    }

    public void toMainButtonClick(ActionEvent event) throws IOException {

            String userInput = userName.getText();
            String passInput = password.getText();

            if (userInput.equals(ADMIN_TEST_USER) && passInput.equals(ADMIN_TEST_PASS)) {


                Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Main Page");
                stage.setScene(scene);
                stage.show();

            } else {
                error("Login Error", "Bad Credntials", "MUST USE PROVIDED TEST CREDENTIALS" );
            }
    }

    private void error(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
}

}
