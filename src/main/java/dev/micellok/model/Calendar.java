package dev.micellok.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Calendar {
    private final Map<LocalDate, LinkedList<Meeting>> meetings;
    private final Map<String, User> users;

    public Calendar() {
        meetings = new LinkedHashMap<>();
        users = new LinkedHashMap<>();
        loadUserData();
        loadCalendarData();
    }

    public void loadUserData() {
        List<String[]> usersToParse = CSVReaderWriter.read("src/main/resources/db/users.csv");
        for(String[] user : usersToParse) {
            String email = user[0];
            String firstName = user[1];
            String lastName = user[2];

            User newUser = new User(email, firstName, lastName);
            users.put(email, newUser);
        }
    }

    public void loadCalendarData() {
        List<String[]> meetingToParse = CSVReaderWriter.read("src/main/resources/db/meetings.csv");
        for(String[] meeting : meetingToParse) {
            String name = meeting[0];

            Integer[] startTimeData = Arrays.stream(meeting[1].split(",")).map(Integer::parseInt).toArray(Integer[]::new);
            LocalDateTime startTime = LocalDateTime.of(startTimeData[0], startTimeData[1], startTimeData[2], startTimeData[3], startTimeData[4]);
            Integer[] endTimeData = Arrays.stream(meeting[2].split(",")).map(Integer::parseInt).toArray(Integer[]::new);
            LocalDateTime endTime = LocalDateTime.of(endTimeData[0], endTimeData[1], endTimeData[2], endTimeData[3], endTimeData[4]);

            String desc = meeting[3];
            String authorData = meeting[4];
            User author;
            if(users.containsKey(authorData)) {
                author = users.get(authorData);
            }
            else {
                author = new User("unknown", "", "");
            }

            String[] participantsData = meeting[5].split(",");
            LinkedList<User> participants = new LinkedList<>();
            for(String participant : participantsData) {
                if(users.containsKey(participant)) {
                    participants.push(users.get(participant));
                }
            }
            LocalDate startTimeDate = LocalDate.of(startTimeData[0], startTimeData[1], startTimeData[2]);
            addNewMeeting(startTimeDate, new Meeting(name, startTime, endTime, desc, author, participants));
        }
    }

    public void addNewMeeting(LocalDate date, Meeting meeting) {
        if(meetings.containsKey(date)) {
            meetings.get(date).push(meeting);
        }
        else {
            meetings.put(date, new LinkedList<>());
            meetings.get(date).push(meeting);
        }
        updateMeetingsDb();
    }

    public void deleteMeeting(Meeting meeting) {
        meetings.get(meeting.getStartTime().toLocalDate()).remove(meeting);
        updateMeetingsDb();
    }

    public void editMeeting(Meeting currentMeeting, Meeting newMeeting) {
        LocalDateTime newMeetingTime = newMeeting.getStartTime();
        LocalDate newMeetingDate = LocalDate.of(newMeetingTime.getYear(), newMeetingTime.getMonth(), newMeetingTime.getDayOfMonth());
        deleteMeeting(currentMeeting);
        addNewMeeting(newMeetingDate, newMeeting);
        updateMeetingsDb();
    }

    public LinkedList<Meeting> getMeetings() {
        LinkedList<Meeting> allMeetings = new LinkedList<>();
        for(LinkedList<Meeting> meetingsOn : meetings.values()) {
            for(Meeting meeting : meetingsOn) {
                allMeetings.push(meeting);
            }
        }
        return allMeetings;
    }

    public LinkedList<Meeting> getMeetingsOn(LocalDate date) {
        if(meetings.containsKey(date)) {
            return meetings.get(date);
        }
        else {
            return new LinkedList<>();
        }
    }

    public LinkedList<Meeting> getMeetingsFor(User user) {
        LinkedList<Meeting> result = new LinkedList<>();
        for(LinkedList<Meeting> meetingsOn : meetings.values()) {
            for(Meeting meeting : meetingsOn) {
                if(meeting.hasParticipant(user)) {
                    result.push(meeting);
                }
            }
        }
        return result;
    }

    public LinkedList<Meeting> getMeetingsOnFor(LocalDate date, User user) {
        LinkedList<Meeting> result = new LinkedList<>();
        LinkedList<Meeting> meetingsOn;
        if(meetings.containsKey(date)) {
            meetingsOn = meetings.get(date);
            for (Meeting meeting : meetingsOn) {
                if (meeting.hasParticipant(user)) {
                    result.push(meeting);
                }
            }
        }
        return result;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void deleteUser(User user) {
        users.remove(user.getEmail());
        System.out.println(user.getEmail());

        for(LinkedList<Meeting> meetingsVal : meetings.values()) {
            for(Meeting meeting : meetingsVal) {
                meeting.deleteParticipant(user);
            }
        }
        updateUserDb();
    }

    public void addUser(User user) {
        users.put(user.getEmail(), user);
        updateUserDb();
    }

    private void updateUserDb() {
        CSVReaderWriter.writeUsers(new LinkedList<>(users.values()));
    }

    private void updateMeetingsDb() {
        CSVReaderWriter.writeMeetings(getMeetings());
    }

    public String findTerms(LocalDate date, LinkedList<User> participants, int meetingLength) {
        LinkedList<Meeting> meetingsOn = getMeetingsOn(date);
        LinkedList<Meeting> meetingsOnFor = new LinkedList<>();
        for(Meeting meeting : meetingsOn) {
            for (User user : participants) {
                if(!meetingsOnFor.contains(meeting) && meeting.hasParticipant(user)) {
                    meetingsOnFor.push(meeting);
                }
            }
        }

        String result = "";

        meetingsOnFor.sort(Comparator.comparing(Meeting::getStartTime));
        int startTime = 0; //in minutes
        for(Meeting meeting : meetingsOnFor) {
            int meetingStartTimeMIN = meeting.getStartTime().getHour() * 60 + meeting.getStartTime().getMinute();
            if(meetingStartTimeMIN - startTime >= meetingLength) {
                result += String.format("%s - %s\n", LocalDateTime.of(date, LocalTime.of(startTime/60, startTime%60)), meeting.getStartTime());
            }
            startTime = Integer.max(startTime, meeting.getEndTime().getHour() * 60 + meeting.getEndTime().getMinute());
        }
        if(24 * 60 - startTime >= meetingLength) {
            result += String.format("%s - %s\n",  LocalDateTime.of(date, LocalTime.of(startTime/60, startTime%60)), LocalDateTime.of(date, LocalTime.of(23, 59)));
        }
        return result;
    }
}
