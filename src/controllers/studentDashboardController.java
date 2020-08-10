package controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class studentDashboardController implements Initializable {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    Scene scene;


    @FXML
    void exit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxmls/Main.fxml"));
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    private void toUserDataBase(Event event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../fxmls/profilesTableView.fxml"));
        //Stage stage = (Stage) anchorPane.getScene().getWindow(); //method is right for close/minimize but not working here.
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    @FXML
    void openTableView(ActionEvent event) throws Exception {
        toUserDataBase(event);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawer.setSidePane();
        HamburgerBackArrowBasicTransition hamburgerBackArrowBasicTransition = new HamburgerBackArrowBasicTransition(hamburger);
        hamburgerBackArrowBasicTransition.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            hamburgerBackArrowBasicTransition.setRate(hamburgerBackArrowBasicTransition.getRate() * -1);
            hamburgerBackArrowBasicTransition.play();

        });
    }
}
