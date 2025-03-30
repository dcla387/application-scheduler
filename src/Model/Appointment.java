package Model;

import java.time.LocalDateTime;

public class Appointment {

    private int appointmentId;

    private String customerName;
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

    public Appointment(int appointmentId, String customerName, String title, String description, String location, String contactName, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId){

        this.appointmentId = appointmentId;
        this.customerName = customerName;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contactId = contactId;
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

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }
}
