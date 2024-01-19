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
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class NewMeetingController implements Initializable {
    @FXML
    private TextField meetingNameTextField, startTimeMinutesTextField, endTimeMinutesTextField;
    @FXML
    private DatePicker startDatePicker, endDatePicker;
    @FXML
    private Spinner<Integer> startTimeHourSpinner, endTimeHourSpinner;
    @FXML
    private Label meetingParticipantsLabel;
    @FXML
    private Button editParticipantsBtn, addMeetingBtn, clearParticipantsBtn;
    @FXML
    private TextArea meetingDescTextArea;

    private Stage stage;
    private Calendar calendar;
    private User user;

    private LocalDate startDate = null;
    private LocalDate endDate = null;
    private Integer startHour = 12;
    private Integer endHour = 12;
    private Integer startMinute = 0;
    private Integer endMinute = 30;
    private String name = null;
    private String desc = "Brak opisu.";
    private User author;
    private LinkedList<User> participants = new LinkedList<>();
    private Meeting prevMeeting = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UnaryOperator<TextFormatter.Change> dateSpinnerFilter = c -> {
            if (c.isContentChange()) {
                ParsePosition parsePosition = new ParsePosition(0);
                NumberFormat format = NumberFormat.getIntegerInstance();
                format.parse(c.getControlNewText(), parsePosition);
                if (parsePosition.getIndex() == -1 ||
                        parsePosition.getIndex() < c.getControlNewText().length()) {
                    return null;
                }
            }
            return c;
        };

        UnaryOperator<TextFormatter.Change> textFieldFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change;
            }
            if (newText.matches("\\d*")) {
                try {
                    int value = Integer.parseInt(newText);
                    if (value >= 0 && value <= 59) {
                        return change;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
            return null;
        };


        startTimeMinutesTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), startMinute, textFieldFilter));
        endTimeMinutesTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), endMinute, textFieldFilter));

        startTimeHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        startTimeHourSpinner.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), startHour, dateSpinnerFilter));
        startTimeHourSpinner.commitValue();
        endTimeHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        endTimeHourSpinner.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), endHour, dateSpinnerFilter));
        endTimeHourSpinner.commitValue();

        startDatePicker.setOnAction(this::setStartDate);

        addMeetingBtn.setOnAction(this::addMeeting);
        editParticipantsBtn.setOnAction(event -> {
            try {
                editParticipants();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        clearParticipantsBtn.setOnAction(this::clearParticipants);
    }

    private boolean areCorrectSettings() {
        if (startDate == null || endDate == null || name == null) {
            return false;
        }
        if (participants.isEmpty()) {
            return false;
        }
        if(startDate.isAfter(endDate)) {
            return false;
        }
        int startTimeMinutes = startHour * 60 + startMinute;
        int endTimeMinutes = endHour * 60 + endMinute;
        return endTimeMinutes >= startTimeMinutes + 15;
    }

    private void addMeeting(ActionEvent event) {
        name = meetingNameTextField.getText();
        startDate = startDatePicker.getValue();
        endDate = endDatePicker.getValue();
        startHour = startTimeHourSpinner.getValue();
        startMinute = Integer.parseInt(startTimeMinutesTextField.getText());
        endHour = endTimeHourSpinner.getValue();
        endMinute = Integer.parseInt(endTimeMinutesTextField.getText());
        desc = meetingDescTextArea.getText();


        if (areCorrectSettings()) {
            LocalDateTime startTime = LocalDateTime.of(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth(), startHour, startMinute);
            LocalDateTime endTime = LocalDateTime.of(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth(), endHour, endMinute);
            Meeting meeting = new Meeting(name, startTime, endTime, desc, author,  participants);
            if(prevMeeting == null) {
                calendar.addNewMeeting(startDate, meeting);
            } else {
                calendar.editMeeting(prevMeeting, meeting);
            }

            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("error :c");
            alert.setHeaderText("Zła konfiguracja spotkania!");
            alert.setContentText("Sprawdź jeszcze raz, czy wszytko zostało poprawnie wypełnione.");
            alert.showAndWait();
        }
    }

    private void editParticipants() throws IOException {
        Stage editParticipantsStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("add-participants-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);

        AddParticipantsController addParticipantsController = fxmlLoader.getController();
        addParticipantsController.controllerInit(calendar, this);

        editParticipantsStage.initModality(Modality.APPLICATION_MODAL);
        editParticipantsStage.setResizable(false);
        editParticipantsStage.setTitle("Edit participants");
        editParticipantsStage.setScene(scene);
        editParticipantsStage.showAndWait();
    }

    private String convertMonth(int month) {
        if(month < 10) {
            return String.format("0%d", month);
        } else {
            return String.format("%d", month);
        }
    }

    private void clearParticipants(ActionEvent event) {
        participants.clear();
        updateParticipantsLabel();
    }

    private void setStartDate(ActionEvent event) {
        startDate = startDatePicker.getValue();
        endDate = startDate;
        endDatePicker.getEditor().setText(String.format("%d.%s.%d", startDate.getDayOfMonth(), convertMonth(startDate.getMonthValue()), startDate.getYear()));
        endDatePicker.commitValue();
    }

    public void setCalendar(Calendar calendar, User user) {
        this.calendar = calendar;
        this.user = user;
        author = user;
        participants.push(author);
        updateParticipantsLabel();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void addParticipant(User user) {
        participants.push(user);
        updateParticipantsLabel();
    }

    public void updateParticipantsLabel() {
        meetingParticipantsLabel.setText(String.format("Uczestnicy: %s", participants.toString()));
    }

    public LinkedList<User> getParticipants() {
        return participants;
    }

    public void initStartConfig(Meeting meeting) {
        prevMeeting = meeting;

        startDate = meeting.getStartTime().toLocalDate();
        endDate = meeting.getEndTime().toLocalDate();
        startHour = meeting.getStartTime().getHour();
        endHour = meeting.getEndTime().getHour();
        startMinute = meeting.getStartTime().getMinute();
        endMinute = meeting.getEndTime().getMinute();
        name = meeting.getName();
        desc = meeting.getDesc();
        participants = meeting.getParticipants();

        meetingNameTextField.setText(name);
        startDatePicker.getEditor().setText(String.format("%d.%s.%d", startDate.getDayOfMonth(), convertMonth(startDate.getMonthValue()), startDate.getYear()));
        startDatePicker.commitValue();
        endDatePicker.getEditor().setText(String.format("%d.%s.%d", endDate.getDayOfMonth(), convertMonth(endDate.getMonthValue()), endDate.getYear()));
        endDatePicker.commitValue();

        startTimeHourSpinner.getEditor().setText(startHour.toString());
        startTimeHourSpinner.commitValue();
        endTimeHourSpinner.getEditor().setText(endHour.toString());
        endTimeHourSpinner.commitValue();

        startTimeMinutesTextField.setText(startMinute.toString());
        startTimeMinutesTextField.commitValue();
        endTimeMinutesTextField.setText(endMinute.toString());
        endTimeMinutesTextField.commitValue();

        meetingDescTextArea.setText(desc);
        meetingDescTextArea.commitValue();

        updateParticipantsLabel();
    }

}
