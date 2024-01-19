package dev.micellok.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class Meeting {
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String desc;
    private User author;
    private final LinkedList<User> participants;


    public Meeting(String name, LocalDateTime startTime, LocalDateTime endTime, String desc, User author, LinkedList<User> participants) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.desc = desc;
        this.participants = participants;
        this.author = author;

        if (author != null && !hasParticipant(author)) {
            participants.push(author);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(LocalDateTime newTime) {
        startTime = newTime;
    }

    public void setEndTime(LocalDateTime newTime) {
        endTime = newTime;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean hasParticipant(User participant) {
        return participants.contains(participant);
    }

    public void deleteParticipant(User user) {
        participants.remove(user);
    }

    public LinkedList<User> getParticipants() {
        return participants;
    }

    public User getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return String.format("%s, (%s - %s)", name, startTime, endTime);
    }

    public String getMeetingDesc() {
        return String.format(
                "Data rozpoczęcia: %s\nData zakończenia: %s\n\nOpis:\n%s\n\nUczestnicy:\n%s",
                startTime, endTime, desc, participants
        );
    }

    public String getDesc() {
        return desc;
    }
}
