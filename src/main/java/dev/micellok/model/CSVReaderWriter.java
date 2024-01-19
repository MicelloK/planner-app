package dev.micellok.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class CSVReaderWriter {
    public static final String CSV_SPLIT_BY = ";";

    public static List<String[]> read(String file) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            reader.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<String[]> result = new LinkedList<>();

        String line;
        while(true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            result.add(line.split(CSV_SPLIT_BY));
        }
        return result;
    }

    public static void writeUsers(LinkedList<User> users) {
        String usersData ="email;firstName;lastName";
        for(User user : users) {
            String line = "\n" + user.getEmail() + ";" + user.getFirstName() + ";" + user.getLastName();
            usersData += line;
        }

        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/db/users.csv", false);
            fileWriter.write(usersData);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeMeetings(LinkedList<Meeting> meetings) {
        String meetingData ="name;startTime;endTime;desc;author;participants";
        for(Meeting meeting : meetings) {
            String participantsEmails = "";
            for (int i = 0; i < meeting.getParticipants().size(); i++) {
                participantsEmails += meeting.getParticipants().get(i).getEmail();

                if (i < meeting.getParticipants().size() - 1) {
                    participantsEmails += ",";
                }
            }

            LocalDateTime startTime = meeting.getStartTime();
            LocalDateTime endTime = meeting.getEndTime();
            String startTimeFormat = startTime.getYear() + "," + startTime.getMonthValue() + "," + startTime.getDayOfMonth() + "," + startTime.getHour() + "," + startTime.getMinute();
            String endTimeFormat = endTime.getYear() + "," + endTime.getMonthValue() + "," + endTime.getDayOfMonth() + "," + endTime.getHour() + "," + endTime.getMinute();


            String line = "\n" + meeting.getName() + ";" + startTimeFormat + ";" + endTimeFormat + ";" + meeting.getDesc() + ";" + meeting.getAuthor().getEmail() + ";" + participantsEmails;
            meetingData += line;
        }

        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/db/meetings.csv", false);
            fileWriter.write(meetingData);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
