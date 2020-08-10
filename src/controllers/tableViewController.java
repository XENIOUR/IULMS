package controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import dataBase.DataBase;
import dataBase.Profile;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;


public class tableViewController implements Initializable {
    @FXML
    private TableView<Profile> tableView;

    @FXML
    private TableColumn<Profile, String> idColumn;

    @FXML
    private TableColumn<Profile, String> nameColumn;

    @FXML
    private TableColumn<Profile, String> emailColumn;

    @FXML
    private TableColumn<Profile, String> passColumn;

    @FXML
    private TableColumn<Profile, String> genderColumn;

    @FXML
    private TableColumn<Profile, String> adminColumn;

    @FXML
    private JFXToggleButton showPass;

    Dialog<Pair<String, String>> inputDialog;
    TitledPane tp;
    Label label;


    @FXML
    void addBtnClicked() throws SQLException, ClassNotFoundException {
        // Create the custom dialog.

        inputDialog = new Dialog<>();
        inputDialog.setTitle("Add User");
        inputDialog.setHeaderText("Add User Information");
        inputDialog.setContentText("Name");

        // Set the icon
        inputDialog.setGraphic(new ImageView(new Image("images/add-user-icon(256x256).png")));

        // Get the Stage.
        Stage stage = (Stage) inputDialog.getDialogPane().getScene().getWindow();

        // Add a custom icon.
        stage.getIcons().add(new Image("images/add-user-icon(small).png"));

        // Set the button types.
        ButtonType addButton = new ButtonType("Add User", ButtonBar.ButtonData.FINISH);
        inputDialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);


        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.setPrefHeight(260);

        //TextFields
        JFXTextField username = new JFXTextField();
        username.setPromptText("Username");
        username.setLabelFloat(true);
        JFXPasswordField password = new JFXPasswordField();
        password.setPromptText("Password");
        password.setLabelFloat(true);
        JFXTextField email = new JFXTextField();
        email.setPromptText("Email");
        email.setLabelFloat(true);
        JFXTextField id = new JFXTextField();
        id.setPromptText("Id");
        id.setLabelFloat(true);

        //RadioButtons
        RadioButton male = new RadioButton("Male");
        RadioButton feMale = new RadioButton("Female");
        ToggleGroup tg = new ToggleGroup();
        male.setToggleGroup(tg);
        feMale.setToggleGroup(tg);

        //making a tooltip
        Tooltip tooltip = new Tooltip("Id is auto generated.");
        tooltip.setStyle("-fx-underline: true; -fx-font-size: 12px; -fx-font-weight: bold;");

        //help image
        Image image = new Image("images/help.png");
        ImageView imageView = new ImageView(image);
        Tooltip.install(imageView, tooltip);


        //TitledPane (collapse/Expand)
        tp = new TitledPane();
        tp.setText("Add Id.");
        tp.setContent(id);
        tp.setExpanded(false);

        //CheckBox
        CheckBox admin = new CheckBox("Admin");

        //Label
        label = new Label("Id is auto generated.\nLeave it empty unless you know what you are doing.");
        label.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-effect: dropshadow( gaussian , rgba(255,255,255,0.5) , 0,0,0,1 ); -fx-underline: true;");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);
        grid.add(new Label("E-mail"), 0, 2);
        grid.add(email, 1, 2);
        grid.add(male, 0, 3);
        grid.add(feMale, 1, 3);
        grid.add(admin, 0, 4);
        grid.add(tp, 1, 4);
        grid.add(imageView, 2, 4);
        grid.add(label, 1, 5);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = inputDialog.getDialogPane().lookupButton(addButton);
        loginButton.setDisable(true);

        // Do some validation
        username.textProperty().addListener((observable, oldValue, newValue) -> loginButton.setDisable(newValue.trim().isEmpty()));

        inputDialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(username::requestFocus);

        //to check that add or cancel button is pressed because it is not Button(type)
        AtomicBoolean checkCancelOrAdd = new AtomicBoolean(false);

        // Convert the result to a username-password-pair when the login button is clicked.
        inputDialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                return new Pair<>(username.getText(), password.getText());
            }else {
                checkCancelOrAdd.set(true);
            }
            return null;
        });

        Optional<Pair<String, String>> result = inputDialog.showAndWait();

        Profile newProfile = new Profile();
        result.ifPresent(usernamePassword -> System.out.println(createProfile(newProfile, username, id, email, password, male, admin)));

        if (checkCancelOrAdd.get()){
            inputDialog.close();
            checkCancelOrAdd.set(false);
        }else {
            if (username.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("Username cannot be empty.");
                alert.show();
            }else {
                if (password.getText().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setContentText("Password cannot be empty.");
                    alert.show();
                }else {
                    addUser(newProfile);
                }
            }
        }
    }

    public String createProfile(Profile profile, TextField username, TextField id, TextField email, TextField password, RadioButton male, CheckBox admin) {
        profile.setName(username.getText());
        profile.setEmail(email.getText());
        profile.setPass(password.getText());
        profile.setGender(male.isSelected());
        profile.setAdmin(admin.isSelected());
        if (id.getText().isEmpty()){
            return "";
        }else {
            profile.setId(Integer.parseInt(id.getText()));
        }
        return "";
    }

    //for adding user by tableView
    private void addUser(Profile profile) throws SQLException, ClassNotFoundException {
        if (DataBase.searchUsername(profile.getName(), profile.getEmail())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Username or Email Already exist.");
            alert.show();
        }else {
            //TableView Input
            //Add to TableView
            tableView.getItems().add(profile);

            //Database input
            PreparedStatement ps;
            //INSERT INTO `user` (`id`, `name`, `email`, `pass`, `gender`, `admin`) VALUES (NULL, 'Ayesha', 'ayesha@gmail.com', '7721', '1', '0');
            ps = DataBase.getConnection().prepareStatement("INSERT INTO `user`(`id`, `name`, `email`, `pass`, `gender`, `admin`) VALUES (?,?,?,?,?,?)");

            ps.setInt(1, profile.getId());
            ps.setString(2, profile.getName());
            ps.setString(3, profile.getEmail());
            ps.setString(4, profile.getPass());
            ps.setBoolean(5, profile.isGender());
            ps.setBoolean(6, profile.isAdmin());

            ps.executeUpdate();
            tableView.setItems(getProfiles());
        }
    }

    @FXML
    void updateBtnClicked() throws SQLException, ClassNotFoundException {
        try {
            // Create the custom dialog.

            Dialog<Pair<String, String>> inputDialog = new Dialog<>();
            inputDialog.setTitle("Update User");
            inputDialog.setHeaderText("Edit User Information");
            //inputDialog.setContentText("Name");

            // Set the icon
            inputDialog.setGraphic(new ImageView(new Image("images/user-management-icon.png")));

            // Get the Stage.
            Stage stage = (Stage) inputDialog.getDialogPane().getScene().getWindow();

            // Add a custom icon.
            stage.getIcons().add(new Image("images/user-management-icon(Small).png"));

            // Set the button types.
            ButtonType updateButton = new ButtonType("Update", ButtonBar.ButtonData.FINISH);
            inputDialog.getDialogPane().getButtonTypes().addAll(updateButton, ButtonType.CANCEL);


            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            //selected Profile
            Profile profile = tableView.getSelectionModel().getSelectedItem();

            //TextFields
            TextField username = new TextField();
            username.setPromptText("Username");
            username.setText(profile.getName());
            PasswordField password = new PasswordField();
            password.setPromptText("Password");
            password.setText(profile.getPass());
            TextField email = new TextField();
            email.setPromptText("Email");
            email.setText(profile.getEmail());
            TextField id = new TextField();
            id.setPromptText("Id");
            id.setText(String.valueOf(profile.getId()));
            id.setEditable(false);


            //RadioButtons
            RadioButton male = new RadioButton("Male");
            RadioButton feMale = new RadioButton("Female");
            ToggleGroup tg = new ToggleGroup();
            male.setToggleGroup(tg);
            feMale.setToggleGroup(tg);
            if (profile.isGender()){
                male.setSelected(true);
            }else {
                feMale.setSelected(true);
            }

            //CheckBox
            CheckBox admin = new CheckBox("Admin");
            admin.setSelected(profile.isAdmin());

            grid.add(new Label("Id:"), 0, 0);
            grid.add(id, 1, 0);
            grid.add(new Label("Username:"), 0, 1);
            grid.add(username, 1, 1);
            grid.add(new Label("Password:"), 0, 2);
            grid.add(password, 1, 2);
            grid.add(new Label("Email:"), 0, 3);
            grid.add(email, 1, 3);
            grid.add(male, 1, 4);
            grid.add(feMale, 2, 4);
            grid.add(admin, 1, 5);

            // Enable/Disable login button depending on whether a username was entered.
            Node updateBtn = inputDialog.getDialogPane().lookupButton(updateButton);
            updateBtn.setDisable(true);

            // Do some validation
            username.textProperty().addListener((observable, oldValue, newValue) -> updateBtn.setDisable(newValue.trim().isEmpty()));

            inputDialog.getDialogPane().setContent(grid);

            // Request focus on the username field by default.
            Platform.runLater(username::requestFocus);

            // Convert the result to a username-password-pair when the login button is clicked.
            inputDialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButton) {
                    return new Pair<>(username.getText(), password.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = inputDialog.showAndWait();

            Profile newProfile = new Profile();
            result.ifPresent(usernamePassword -> System.out.println(createProfile(newProfile, username, id, email, password, male, admin)));

            updateUser(newProfile);
        }catch (NullPointerException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Select a User.");
            alert.show();
        }
    }

    public void updateUser(Profile profile) throws SQLException, ClassNotFoundException {
        if (DataBase.searchUsername(profile.getName(), profile.getEmail())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Username or Email Already exist.");
            alert.show();
        }else {
            int id = tableView.getSelectionModel().getSelectedItem().getId();
            //UPDATE `user` SET `name` = ?, `email` = ?, `pass` = ?, `gender` = ?, `admin` = ? WHERE `user`.`id` = ?;

            PreparedStatement ps = DataBase.getConnection().prepareStatement("UPDATE `user` SET `name` = ?, `email` = ?, `pass` = ?, `gender` = ?, `admin` = ? WHERE `user`.`id` = ?");
            ps.setString(1, profile.getName());
            ps.setString(2, profile.getEmail());
            ps.setString(3, profile.getPass());
            ps.setBoolean(4, profile.isGender());
            ps.setBoolean(5, profile.isAdmin());
            ps.setInt(6, id);
            ps.executeUpdate();
            tableView.setItems(getProfiles());
        }
    }

    @FXML
    void deleteBtnClicked() throws SQLException, ClassNotFoundException {
        try {
            deleteButtonClicked();
        }catch (NullPointerException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Select a User.");
            alert.show();
        }
    }

    private void deleteButtonClicked() throws SQLException, ClassNotFoundException {
        int id = tableView.getSelectionModel().getSelectedItem().getId();
        ObservableList<Profile> profileSelected, allProfiles;
        allProfiles = tableView.getItems();
        profileSelected = tableView.getSelectionModel().getSelectedItems();
        profileSelected.forEach(allProfiles::remove);

        PreparedStatement ps = DataBase.getConnection().prepareStatement("DELETE FROM user " + "WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @FXML
    void backBtnClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxmls/StudentPanel.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public ObservableList<Profile> getProfiles() throws SQLException, ClassNotFoundException {
        Connection connection = DataBase.getConnection();
        String query = "select * from user";
        assert connection != null;
        var statement = connection.prepareStatement(query);
        ResultSet r = statement.executeQuery();

        ObservableList<Profile> profiles = FXCollections.observableArrayList();
        while (r.next()) {
            int id = r.getInt("id");
            String name = r.getString("name");
            String email = r.getString("email");
            String pass = r.getString("pass");
            boolean gender = r.getBoolean("gender");
            boolean admin = r.getBoolean("admin");
            profiles.add(new Profile(id, name, email, pass, gender, admin));
        }
        return profiles;
    }

    @FXML
    public void changeNameCellEvent(TableColumn.CellEditEvent<Profile, String> editedCell) throws SQLException, ClassNotFoundException {
        int id = tableView.getSelectionModel().getSelectedItem().getId();

        if (DataBase.searchUsername(editedCell.getNewValue(), null)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Username Already exist.");
            alert.show();
        }else {
            //UPDATE `user` SET `name` = ? WHERE `user`.`id` = ?;
            PreparedStatement ps = DataBase.getConnection().prepareStatement("UPDATE `user` SET `name` = ? WHERE `user`.`id` = ?");
            ps.setString(1, editedCell.getNewValue());
            ps.setInt(2, id);
            ps.executeUpdate();
            tableView.setItems(getProfiles());
        }
    }

    @FXML
    public void changeEmailCellEvent(TableColumn.CellEditEvent<Profile, String> editedCell) throws SQLException, ClassNotFoundException {
        int id = tableView.getSelectionModel().getSelectedItem().getId();

        if (DataBase.searchUsername(null, editedCell.getNewValue())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Email Already exist.");
            alert.show();
        }else {
            //UPDATE `user` SET `email` = ? WHERE `user`.`id` = ?;
            PreparedStatement ps = DataBase.getConnection().prepareStatement("UPDATE `user` SET `email` = ? WHERE `user`.`id` = ?");
            ps.setString(1, editedCell.getNewValue());
            ps.setInt(2, id);
            ps.executeUpdate();
            tableView.setItems(getProfiles());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        passColumn.setCellValueFactory(new PropertyValueFactory<>("pass"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        adminColumn.setCellValueFactory(new PropertyValueFactory<>("admin"));

        tableView.setEditable(true);

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passColumn.setCellFactory(cell -> new PasswordLabelCell());

        try {
            tableView.setItems(getProfiles());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}

class PasswordLabelCell extends TableCell<Profile, String> {
    private final Label label;

    public PasswordLabelCell() {
        label = new Label();
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 17px;");
        //label.setFont(new Font("Arial", 30));
        this.setGraphic(null);
    }

    private String genDotString(int len) {

        return "\u2022".repeat(Math.max(0, len));
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            label.setText(genDotString(item.length()));
            setGraphic(label);
        } else {
            setGraphic(null);
        }
    }
}
