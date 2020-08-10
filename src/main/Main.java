package main;

import controllers.SignIn_SignUp_Controller;
import dataBase.DataBase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    Stage primaryStage;
    protected DataBase dataBase = new DataBase();

    @Override
    public void start(Stage Stage) throws Exception{
        //DataBase.printUsers();
        primaryStage = Stage;
        //Parent root = FXMLLoader.load(getClass().getResource("../fxmls/Main.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("../fxmls/profilesTableView.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
