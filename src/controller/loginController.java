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

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.Date;



public class loginController implements Initializable {
//need to pull 2 users from database add passwords to those profiles in the database - 1 - ADMIN 2 - TEST
    //then take this hardcoded username/password out
    private static final String ADMIN_TEST_USER = "admin";
    private static final String ADMIN_TEST_PASS = "password123";

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Label title;

    @FXML
    private Button login;

    @FXML
    private Button logoff;

    @FXML
    private Label location;

    @FXML
    private Label userLabel;
    @FXML
    private Label passLabel;


        public void onActionExit(ActionEvent event) {System.exit(0);}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        ZoneId zoneId = ZoneId.systemDefault();
        location.setText(zoneId.toString());

        // Locale.setDefault(new Locale("fr"));
        boolean isFrench = Locale.getDefault().getLanguage().equals("fr");

        System.out.println("Current locale: " + Locale.getDefault());
        System.out.println("Is French: " + isFrench);

        if (isFrench) {
            try {

                ResourceBundle frMessages = ResourceBundle.getBundle("view.login_messages", Locale.FRENCH);


                title.setText(frMessages.getString("login.title"));
                userName.setPromptText(frMessages.getString("login.username"));
                password.setPromptText(frMessages.getString("login.password"));
                userLabel.setText(frMessages.getString("login.userLabel"));
                passLabel.setText(frMessages.getString("login.passLabel"));
                login.setText(frMessages.getString("login.login"));
                logoff.setText(frMessages.getString("login.logoff"));


            } catch (Exception e) {

                System.out.println("Could not load French resources: " + e.getMessage());
            }
        }

    }

    public void toMainButtonClick(ActionEvent event) throws IOException {

            String userInput = userName.getText();
            String passInput = password.getText();

            if (userInput.equals(ADMIN_TEST_USER) && passInput.equals(ADMIN_TEST_PASS)) {

                new PrintWriter(new FileOutputStream("login_activity.txt", true), true)
                        .println(new Date().toString() + " - ✔ Good Login: " + userInput);

                Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Main Page");
                stage.setScene(scene);
                stage.show();

            } else {

                new PrintWriter(new FileOutputStream("login_activity.txt", true), true)
                        .println(new Date().toString() + " - 🛑  FAILED Login: " + "user Name: " + userInput + "  Password: " + passInput);

                Locale currentLocale = Locale.getDefault();
                ResourceBundle messages = ResourceBundle.getBundle("view.login_messages", currentLocale);

                try {
                    String errorTitle = messages.getString("error.title");
                    String errorHeader = messages.getString("error.header");
                    String errorContent = messages.getString("error.content");

                    error(errorTitle, errorHeader, errorContent);
                } catch (Exception e) {

                    error("Login Error", "Bad Credentials", "MUST USE PROVIDED TEST CREDENTIALS");
                }
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
