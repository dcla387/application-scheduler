package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainController implements Initializable {

    @FXML
    private Button mainAppointments;

    @FXML
    private Button mainCustomers;

    @FXML
    private Button mainReports;

    @FXML
    private Button mainExit;

    public void onActionExit(ActionEvent event) {System.exit(0);}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){


    }

    public void onClickAppts(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ApptMain.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appt Page");
        stage.setScene(scene);
        stage.show();
    }

}
