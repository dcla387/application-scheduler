package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import DAO.JDBC;

/**
 * Main application class for the customer and appointment management system.
 *
 * <p>This class serves as the entry point for the application. The application allows users
 * to manage customer records and their associated appointment information.</p>
 *
 * <p>The application uses JavaFX for the user interface and JDBC for database connectivity.
 * The initial view presented to the user is the login screen.</p>
 *
 * @author Donna Clark
 * @version 1.0
 */

public class Main extends Application {

    /**
     * Initiailizes and displays the primary stage of the JavaFX application.
     *
     * <p>this method loads the login screen, creates a new scene wit hthe loaded content, displays the stage to the User.</p>
     *
     * @param stage The primary stage for this application
     * @throws Exception If an error occurs in the loading process
     */

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Welcome");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * the Main entry point for the application.
     *
     * <p>this method is the one that establishes the database connection and launches the application.  Also closes the connection when application terminates.</p>
     *
     * @param args Command line arguments passed to the application
     */

    public static void main(String[] args) {
        JDBC.makeConnection();
        launch(args);
        JDBC.closeConnection();


    }
}
