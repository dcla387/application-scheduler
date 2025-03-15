package Model;

import java.time.LocalDateTime;

public class Appointment {

    private int apptId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerId;
    private int userId;

    public Appointment(int apptId, String title, String description, String location, String contact, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId){

        this.apptId = apptId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
    }

    public int getApptId(){
        return apptId;
    }

    public String getTitle() {
        return title;

    }

    public String getDescription() {
        return description;
    }

    public String getLocation(){
        return location;
    }

    public String getContact(){
        return contact;
    }

    public String getType(){
        return type;
    }

    public LocalDateTime getStart(){
        return start;
    }

    public LocalDateTime getEnd(){
        return end;
    }

    public int getCustomerId(){
        return customerId;
    }

    public int getUserId(){
        return userId;
    }

}
