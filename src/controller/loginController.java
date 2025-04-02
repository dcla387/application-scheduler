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

import DAO.JDBC;
import DAO.UserDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * This class is a controller for the Login Screen.
 * <p>It is a standard username and password login authentication.
 * Has localization support for a French language login prompts.
 * At login it will check for appointments within 15 mins of login</p>
 *
 * <p>All login attempts will be logged as Successful or Failed.</p>
 *
 * @author Donna Clark
 * @version  1.0
 * */

public class loginController implements Initializable {


    /**
     * Text field for entering username
     */

    @FXML
    private TextField userName;

    /**
     * Password field for entering password
     */

    @FXML
    private PasswordField password;

    /**
     * Label for the title of the login form
     */

    @FXML
    private Label title;

    /**
     * Button to submit login credentials
     */

    @FXML
    private Button login;

    /**
     * Button to submit logoff trigger
     */

    @FXML
    private Button logoff;

    /**
     * Label for to display User's location
     */

    @FXML
    private Label location;

    /**
     * Label for toheuser
     */


    @FXML
    private Label userLabel;

    /**
     * Label for the password
     */

    @FXML
    private Label passLabel;


    /**
     * Button for exiting the application upon Click.
     * @param event This is the trigger that sets off the action for this method
     * */

        public void onActionExit(ActionEvent event) {System.exit(0);}


    /**
     * This Initializes the controller class to set up the interface.
     *
     * <p>This is called after FXML is loaded and does the following:</p>
     * <ul>
     *     <li>Sets the location of the User</li>
     *     <li>Detects if we need the French promps</li>
     *     <li>Updates the labels to French</li>
     * </ul>
     * @param url The location for resolving relative paths
     * @param resourceBundle for localizing the code. In this case looking to see if French needs to be triggered
     */


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

    /**
     * Handler for the Button Click to take a User to the Main Screen.
     * <p>This method does the following:</p>
     * <ul>
     *      <li>Will handle validation of login credentials true or false and log the findings</li>
     *      <li>Logs login attempts</li>
     *      <li>On good login navigates to Main Controller</li>
     *      <li>This also will search for any appointments that are occurring in the next 15 mins after login</li>
     *</ul>
     *
     * @param event Object generated when clicked login
     *
     */

    public void toMainButtonClick(ActionEvent event) throws IOException {

            String userInput = userName.getText();
            String passInput = password.getText();

            if (UserDAO.userValidation(userInput, passInput)) {

                /**
                 * Logs succesesful login to log
                 * */
                new PrintWriter(new FileOutputStream("login_activity.txt", true), true)
                        .println(new Date().toString() + " - ✔ Good Login: " + userInput);

                int userId = UserDAO.getUserId(userInput);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
                Parent root = loader.load();

                mainController mainController = loader.getController();
                mainController.setUserId(userId);

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

            /**
             * Displays an Error alert for the Login Validation.
             *
             * <p>This method shows an Alert dialog to the user with the verbiage defined in the above catch. It provides a title, header and content in the message.</p>
             *
             * @param title Title text for error dialog
             * @param header  Header text for error dialog
             * @param content Content text for error dialog
             *
             * */

    private void error(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
}

}
