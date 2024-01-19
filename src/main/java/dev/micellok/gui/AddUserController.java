package dev.micellok.gui;

import dev.micellok.model.Calendar;
import dev.micellok.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddUserController implements Initializable {
    @FXML
    private TextField emailTextField, firstNameTextField, lastNameTextField;
    @FXML
    private Button addUserBtn;

    private User user = null;
    private MeetingsController parentController;
    private Stage stage;
    private String email, firstName, lastName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addUserBtn.setOnAction(this::addUser);
    }

    public void addCalendar(MeetingsController parentController, Stage stage) {
        this.parentController = parentController;
        this.stage = stage;
    }

    private void addUser(ActionEvent event) {
        email = emailTextField.getText();
        firstName = firstNameTextField.getText();
        lastName = lastNameTextField.getText();

        if(email.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("error :c");
            alert.setHeaderText("Zła konfiguracja użytkownika!");
            alert.setContentText("Pole email nie może być puste.");
            alert.showAndWait();
        } else {
            User newUser = new User(email, firstName, lastName);
            parentController.userUpdated(newUser);
            stage.close();
        }
    }

    public void initUser(User user) {
        emailTextField.setText(user.getEmail());
        emailTextField.commitValue();
        firstNameTextField.setText(user.getFirstName());
        firstNameTextField.commitValue();
        lastNameTextField.setText(user.getLastName());
        lastNameTextField.commitValue();
        emailTextField.setEditable(false);
    }
}
