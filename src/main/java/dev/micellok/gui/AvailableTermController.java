package dev.micellok.gui;

import dev.micellok.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class AvailableTermController implements Initializable {
    @FXML
    private DatePicker meetingDatePicker;
    @FXML
    private TextField meetingLengthTextField;
    @FXML
    private Label availableMeetingsLabel;
    @FXML
    private Button closeButton, addParticipants, findBtn;

    private MeetingsController parentController;
    private Stage stage;
    private int meetingLength;
    private LinkedList<User> participants = new LinkedList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        closeButton.setOnAction(this::closeStage);
        addParticipants.setOnAction(event -> {
            try {
                addParticipantHandle(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        findBtn.setOnAction(this::findTerms);
    }

    public void initController(MeetingsController controller, Stage stage) {
        parentController = controller;
        this.stage = stage;
    }

    public void findTerms(ActionEvent event) {
        LocalDate date = meetingDatePicker.getValue();
        meetingLength = Integer.parseInt(meetingLengthTextField.getText());

        if(date != null && meetingLength > 0) {
            String terms = parentController.getAvailableTerms(date, participants, meetingLength);
            availableMeetingsLabel.setText(terms);
        }
    }

    private void addParticipantHandle(ActionEvent event) throws IOException {
        Stage editParticipantsStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("find-add-participants-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);

        FindTermParticipantsController findTermParticipantsController = fxmlLoader.getController();
        findTermParticipantsController.controllerInit(parentController.getCalendarInstance(), this);

        editParticipantsStage.initModality(Modality.APPLICATION_MODAL);
        editParticipantsStage.setResizable(false);
        editParticipantsStage.setTitle("Edit participants");
        editParticipantsStage.setScene(scene);
        editParticipantsStage.showAndWait();
    }

    private void closeStage(ActionEvent event) {
        stage.close();
    }

    public void addParticipant(User user) {
        participants.push(user);
    }

    public LinkedList<User> getParticipants() {
        return participants;
    }
}
