package controllers;

import dataBase.DataBase;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public class SignIn_SignUp_Controller {

    DataBase dataBase = new DataBase();

    @FXML
    private TextField login_Username;

    @FXML
    private PasswordField login_Pass;

    @FXML
    public TextField signup_Name;

    @FXML
    public TextField signup_Email;

    @FXML
    public PasswordField signup_Pass;

    @FXML
    private Button signup_btn_id;

    @FXML
    public Label acc_Label;

    @FXML
    public Label loginError;

    @FXML
    protected RadioButton radioMale;

    @FXML
    protected RadioButton radioFemale;

    @FXML
    private ImageView image;

    String user, pass, email;
    public boolean acc_Label_Check = false, gender;
    ToggleGroup group = new ToggleGroup();
    FileInputStream fileInputStream;

    @FXML
    void login_btn(ActionEvent event) throws Exception{

        String userName = login_Username.getText().toLowerCase();
        String pass = login_Pass.getText();

        if (dataBase.getUser(userName, pass)) {
            welcomeStage(event);
        } else {
            loginError.setVisible(true);
        }

        if (login_Username.getText().isBlank()){
            login_Username.setPromptText("Enter a Username");
            login_Username.setStyle("-fx-prompt-text-fill: red;");
        }

        if (login_Pass.getText().isBlank()){
            login_Pass.setPromptText("Enter a Password");
            login_Pass.setStyle("-fx-prompt-text-fill: red;");
        }
    }

    @FXML
    void choseImage(MouseEvent event) throws FileNotFoundException {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(image.getScene().getWindow());
        fileInputStream = new FileInputStream(file);
        Image imageChoosen = new Image(fileInputStream);
        image.setImage(imageChoosen);
    }

    @FXML
    void signUp_btn() throws SQLException, ClassNotFoundException, IOException {
        if( signup_Name.getText().isEmpty()){
            signup_Name.setPromptText("Enter a Username");
            signup_Name.setStyle("-fx-prompt-text-fill: red;");
        }
        if (signup_Email.getText().isEmpty()){
            signup_Email.setPromptText("Enter an E-mail");
            signup_Email.setStyle("-fx-prompt-text-fill: red;");
        }
        if (signup_Pass.getText().isEmpty()){
            signup_Pass.setPromptText("Enter a Password");
            signup_Pass.setStyle("-fx-prompt-text-fill: red;");
        } else {

            user = signup_Name.getText().toLowerCase();
            email = signup_Email.getText();
            pass = signup_Pass.getText();

            if (radioMale.isSelected()) {
                gender = true;
            } else if (radioFemale.isSelected()) {
                gender = false;
            }

            if (dataBase.addUser(user, email, pass, gender)) {
                acc_Label.setText("Username or Email already exist.");
                acc_Label.setVisible(true);
                acc_Label_Check = true;
                signup_Pass.clear();
            } else {
                acc_Label_Check = false;
                acc_Label.setVisible(true);
                DataBase.printUsers();
                signup_btn_id.setDisable(true);
                signup_Name.clear();
                signup_Pass.clear();
                signup_Email.clear();
                signup_Name.setEditable(false);
                signup_Pass.setEditable(false);
                signup_Email.setEditable(false);
                radioMale.setDisable(true);
                radioFemale.setDisable(true);
            }
        }
    }

    @FXML
    void setRadioButtons(ActionEvent event) {
        radioMale.setToggleGroup(group);
        radioFemale.setToggleGroup(group);
    }

    @FXML
    void login_tf_focused() {
        if (login_Username.isFocused()){
            login_Username.setPromptText("Username");
            login_Username.setStyle("-fx-prompt-text-fill: gray;");
            loginError.setVisible(false);
        }

        if (login_Pass.isFocused()){
            login_Pass.setPromptText("Password");
            login_Pass.setStyle("-fx-prompt-text-fill: gray;");
            loginError.setVisible(false);
        }
    }

    @FXML
    void tf_focused() {
        if(signup_Name.isFocused()){
            signup_Name.setStyle("-fx-prompt-text-fill: gray;");
            signup_Name.setPromptText("Full Name");
            //signup_Name.addEventFilter(KeyEvent.KEY_TYPED , letter_Validation(10));
            acc_Label.setVisible(false);
            acc_Label.setText("Account successfully created.");
        }

        if (signup_Email.isFocused()){
            signup_Email.setStyle("-fx-prompt-text-fill: gray;");
            signup_Email.setPromptText("Email");
            if (acc_Label_Check) {
                acc_Label.setVisible(false);
            }
            acc_Label.setText("Account successfully created.");
        }

        if(signup_Pass.isFocused()){
            signup_Pass.setStyle("-fx-prompt-text-fill: gray;");
            signup_Pass.setPromptText("Password");
            //signup_Pass.addEventFilter(KeyEvent.KEY_TYPED , letter_Validation(10));
            if (acc_Label_Check) {
                acc_Label.setVisible(false);
            }
            acc_Label.setText("Account successfully created.");
        }
    }

    @FXML
    void logout(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../fxmls/Main.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    private void welcomeStage(Event event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../fxmls/StudentPanel.fxml"));
        //Stage stage = (Stage) anchorPane.getScene().getWindow(); //method is right for close/minimize but not working here.
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public EventHandler<KeyEvent> letter_Validation(final Integer max_Length) {
        return e -> {
            TextField txt_TextField = (TextField) e.getSource();
            if (txt_TextField.getText().length() >= max_Length) {
                e.consume();
            }
            if (e.getCharacter().matches("[A-Za-z]")) {

            } else {
                e.consume();
            }
        };
    }
}
