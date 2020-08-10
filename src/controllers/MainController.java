package controllers;

import dataBase.DataBase;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private VBox vBox;

    @FXML
    private AnchorPane aPane;

    private Parent fxml;
    protected SignIn_SignUp_Controller obj = new SignIn_SignUp_Controller();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vBox);
        t.setToX(vBox.getLayoutX() * 20);
        t.play();
        t.setOnFinished((e) -> {
            try {
                fxml = FXMLLoader.load(getClass().getResource("../fxmls/SignIn.fxml"));
                vBox.getChildren().removeAll();
                vBox.getChildren().setAll(fxml);
            }catch (IOException ignored){}
        });
    }

    @FXML
    void stage_closed(ActionEvent event) {
        Stage stage = (Stage) aPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void stage_minimized(ActionEvent event) {
        Stage stage = (Stage) aPane.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void open_signin(ActionEvent event){
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vBox);
        t.setToX(vBox.getLayoutX() * 20);
        t.play();
        t.setOnFinished((e) -> {
            try {
                fxml = FXMLLoader.load(getClass().getResource("../fxmls/SignIn.fxml"));
                vBox.getChildren().removeAll();
                vBox.getChildren().setAll(fxml);
            }catch (IOException ignored){

            }
        });
    }

    @FXML
    private void open_signup(ActionEvent event) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vBox);
        t.setToX(0);
        t.play();
        t.setOnFinished((e) -> {
            try {
                fxml = FXMLLoader.load(getClass().getResource("../fxmls/SignUp.fxml"));
                vBox.getChildren().removeAll();
                vBox.getChildren().setAll(fxml);
            }catch (IOException ignored){

            }
        });
    }
}
