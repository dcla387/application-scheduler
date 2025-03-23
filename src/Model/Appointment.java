package Model;

import java.time.LocalDateTime;

public class Appointment {

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String contactId;

    private String contactName;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerId;
    private int userId;

    public Appointment(int appointmentId, String title, String description, String location, String contactName, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId){

        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contactName = contactName;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
    }

    public int getAppointmentId(){
        return appointmentId;
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


    public String getContactName(){
        return contactName;
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
