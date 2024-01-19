package dev.micellok.gui;

import dev.micellok.model.Calendar;
import dev.micellok.model.Meeting;
import dev.micellok.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class MeetingsController implements Initializable {
    @FXML
    private ChoiceBox<String> calendarChoiceBox;
    @FXML
    private Button newMeetingBtn, newUserBtn, deleteUserBtn, findMeetingDateBtn, editMeetingBtn, deleteMeetingBtn, editUserBtn;
    @FXML
    private Label meetingInfoLabel, meetingNameLabel;
    @FXML
    private DatePicker meetingDatePicker;
    @FXML
    private ListView<String> meetingListView;


    private final Calendar calendar = new Calendar();
    private User selectedUser;
    private LocalDate selectedDate;
    private int selectedMeetingIdx = -1;
    private LinkedList<Meeting> meetings;
    private final Map<String, User> users = calendar.getUsers();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        meetings = new LinkedList<>();
        updateCalendarBox();
        calendarChoiceBox.setOnAction(this::getCalendar);
        meetingDatePicker.setOnAction(this::getDate);
        meetingListView.getItems().addAll(meetings.stream().map(Meeting::toString).toList());
        meetingListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedMeetingIdx = meetingListView.getSelectionModel().getSelectedIndex();
            if(selectedMeetingIdx >= 0) {
                meetingNameLabel.setText(meetings.get(selectedMeetingIdx).getName());
                meetingInfoLabel.setText(meetings.get(selectedMeetingIdx).getMeetingDesc());
            }
        });
        newMeetingBtn.setOnAction(e -> {
            try {
                addMeeting();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        editMeetingBtn.setOnAction(event -> {
            try {
                editMeeting();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        deleteMeetingBtn.setOnAction(this::deleteMeeting);
        deleteUserBtn.setOnAction(this::deleteUser);
        newUserBtn.setOnAction(event -> {
            try {
                newUser();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        editUserBtn.setOnAction(event -> {
            try {
                editUser(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        findMeetingDateBtn.setOnAction(event -> {
            try {
                findTerm();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void updateListView() {
        meetings = calendar.getMeetingsOnFor(selectedDate, selectedUser);
        resetMeetingInfo();
        meetingListView.getItems().clear();
        meetingListView.getItems().addAll(meetings.stream().map(Meeting::toString).toList());
    }

    private void resetMeetingInfo() {
        meetingNameLabel.setText("Nazwa spotkania");
        meetingInfoLabel.setText("Tutaj pojawi się opis spotkania.");
        selectedMeetingIdx = -1;
    }

    private void addMeeting() throws IOException {
        Stage newMeetingStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("new-meeting-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 420, 600);

        NewMeetingController newMeetingController = fxmlLoader.getController();
        newMeetingController.setCalendar(calendar, selectedUser);
        newMeetingController.setStage(newMeetingStage);

        newMeetingStage.initModality(Modality.APPLICATION_MODAL);
        newMeetingStage.setResizable(false);
        newMeetingStage.setTitle("Add new meeting");
        newMeetingStage.setScene(scene);
        newMeetingStage.showAndWait();
        updateListView();
    }

    private void deleteMeeting(ActionEvent event) {
        if(selectedMeetingIdx == -1) return;
        calendar.deleteMeeting(meetings.get(selectedMeetingIdx));
        updateListView();
    }

    private void editMeeting() throws IOException {
        if(selectedMeetingIdx == -1) return;

        Stage newMeetingStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("edit-meeting-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 420, 600);

        NewMeetingController newMeetingController = fxmlLoader.getController();
        newMeetingController.setCalendar(calendar, selectedUser);
        newMeetingController.setStage(newMeetingStage);
        newMeetingController.initStartConfig(meetings.get(selectedMeetingIdx));

        newMeetingStage.initModality(Modality.APPLICATION_MODAL);
        newMeetingStage.setResizable(false);
        newMeetingStage.setTitle("Edit meeting");
        newMeetingStage.setScene(scene);
        newMeetingStage.showAndWait();
        updateListView();
    }

    public void getCalendar(ActionEvent event) {
        int selectedCalendarIdx = calendarChoiceBox.getSelectionModel().getSelectedIndex();
        if(selectedCalendarIdx < 0) selectedCalendarIdx = 0;
        selectedUser = new ArrayList<>(users.values()).get(selectedCalendarIdx);
        updateListView();
    }

    public void getDate(ActionEvent event) {
        selectedDate = meetingDatePicker.getValue();
        updateListView();
    }

    public void deleteUser(ActionEvent event) {
        if(users.size() > 1) {
            calendar.deleteUser(selectedUser);
            updateCalendarBox();
            updateListView();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("error :c");
            alert.setHeaderText("BŁĄD!");
            alert.setContentText("Nie można usunąć wszystkich użytkowników.");
            alert.showAndWait();
        }

    }

    private void updateCalendarBox() {
        calendarChoiceBox.getItems().clear();
        calendarChoiceBox.getItems().addAll(new ArrayList<>(users.values().stream().map(User::toString).toList()));
        calendarChoiceBox.getSelectionModel().select(0);
        int selectedCalendarIdx = calendarChoiceBox.getSelectionModel().getSelectedIndex();
        selectedUser = new ArrayList<>(users.values()).get(selectedCalendarIdx);
    }

    private void newUser() throws IOException {
        Stage newUserStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("add-user-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 300);

        AddUserController controller = fxmlLoader.getController();
        controller.addCalendar(this, newUserStage);

        newUserStage.initModality(Modality.APPLICATION_MODAL);
        newUserStage.setResizable(false);
        newUserStage.setTitle("Add user");
        newUserStage.setScene(scene);
        newUserStage.showAndWait();
        updateListView();
    }

    private void editUser(ActionEvent event) throws IOException {
        Stage editUserStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("edit-user-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 300);

        AddUserController controller = fxmlLoader.getController();
        controller.addCalendar(this, editUserStage);
        controller.initUser(selectedUser);

        editUserStage.initModality(Modality.APPLICATION_MODAL);
        editUserStage.setResizable(false);
        editUserStage.setTitle("Edit user");
        editUserStage.setScene(scene);
        editUserStage.showAndWait();
        updateListView();
    }

    public void userUpdated(User user) {
        calendar.addUser(user);
        updateCalendarBox();
    }

    private void findTerm() throws IOException {
        Stage findTermStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("available-term-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 500);

        AvailableTermController controller = fxmlLoader.getController();
        controller.initController(this, findTermStage);

        findTermStage.initModality(Modality.APPLICATION_MODAL);
        findTermStage.setResizable(false);
        findTermStage.setTitle("find avaiable term");
        findTermStage.setScene(scene);
        findTermStage.showAndWait();
        updateListView();
    }

    public String getAvailableTerms(LocalDate date, LinkedList<User> participants, int meetingLength) {
        return calendar.findTerms(date, participants, meetingLength);
    }

    public Calendar getCalendarInstance() {
        return calendar;
    }
}
