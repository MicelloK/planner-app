package dev.micellok.gui;

import dev.micellok.model.Calendar;
import dev.micellok.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class FindTermParticipantsController implements Initializable {
    @FXML
    private ListView<String> participantsListView;
    @FXML
    private Button addParticipantBtn;

    private AvailableTermController AvailableTermController;
    private LinkedList<User> users;

    public void controllerInit(Calendar calendar, AvailableTermController controller) {
        this.AvailableTermController = controller;
        users = new LinkedList<>(calendar.getUsers().values());
        updateListView();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addParticipantBtn.setOnAction(this::addParticipant);
    }

    private void addParticipant(ActionEvent event) {
        int selectedUserIdx = participantsListView.getSelectionModel().getSelectedIndex();
        User selectedUser = users.get(selectedUserIdx);
        AvailableTermController.addParticipant(selectedUser);
        users.remove(selectedUser);
        updateListView();
    }

    private void updateListView() {
        participantsListView.getItems().clear();
        LinkedList<User> selectedUsers = AvailableTermController.getParticipants();
        users.removeIf(selectedUsers::contains);

        participantsListView.getItems().addAll(users.stream().map(User::toString).toList());
    }
}
