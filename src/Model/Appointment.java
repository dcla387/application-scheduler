package Model;

import java.time.LocalDateTime;

/**
 * Model Class representing an appointment in the scheduling system.
 *
 * <p>this class stores all the information about an appointment.
 * the class also has a complete set of setters and getters for transformation of appointment data.</p>
 *
 * @author Donna Clark
 * @version 1.0
 *
 */

public class Appointment {
    /**
     * the unique identifier for the appointment
     */

    private int appointmentId;

    /**the name of the customer associated with this appointment */

    private String customerName;

    /**the title of the appointment*/
    private String title;

    /**The description of the appointment*/

    private String description;

    /**the location of the appointment*/
    private String location;

    /**The contact ID associated with the appointment*/
    private String contactId;

    /**The ID of the contact person associated for this appointment*/
    private String contactName;

    /**The type of the appointment*/

    private String type;

    /**The start date and time of the appointment*/
    private LocalDateTime start;

    /**The end date and time of teh appointment*/

    private LocalDateTime end;

    /**The unique ID of the customer associated with this appointment*/
    private int customerId;

    /**The unique ID of teh user associated with this appointment*/
    private int userId;

    /**
     * Constructs a new Appointment with the specified details.
     *
     * @param appointmentId the unique ID for the appointment
     * @param customerName The name of the customer
     * @param title The title of the appointment
     * @param description The description of the appointment
     * @param location the location of the appointment
     * @param contactName The name of the contact person
     * @param type The type of the appointment
     * @param start the start date and time
     * @param end the end date and time
     * @param customerId The ID of the associated customer
     * @param userId The ID of the associated user
     */

    public Appointment(int appointmentId, String customerName, String title, String description, String location, String contactName, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId){

        this.appointmentId = appointmentId;
        this.customerName = customerName;
        this.title = title;
        this.description = description;
        this.location = location;
        /*this.contactId = contactId;*/
        this.contactName = contactName;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
    }

    /**
     * Gets the appointment ID
     * @return The ID of this appointment
     */

    public int getAppointmentId(){

        return appointmentId;
    }

    /**
     * Gets the appointment title
     * @return The title of this appointment
     */

    public String getTitle() {
        return title;

    }

    /**
     * Gets the appointment description
     * @return the description of this appointment
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the appointment location
     * @return the location of the appointment
     */

    public String getLocation(){

        return location;
    }

    /**
     * Gets the contact person for the appointment
     * @return the name of the contact person
     */
    public String getContactName(){

        return contactName;
    }

    /**
     * Gets the appointment type
     * @return The ty[e of this appointment
     */
    public String getType(){
        return type;
    }

    /**
     * gets the start date and time of the appointment
     * @return start date and time
     */
    public LocalDateTime getStart(){
        return start;
    }

    /**
     * Gets the end date and time of thsi appointment
     * @return end date and time
     */

    public LocalDateTime getEnd(){
        return end;
    }

    /**
     * Gets the customer ID associated with this appointment
     * @return The ID of teh associated customer
     */

    public int getCustomerId(){
        return customerId;
    }

    /**
     * Gets the User ID associated with this appointment
     * @return The unique ID of teh associated user
     */

    public int getUserId(){
        return userId;
    }

    /**
     * SETS the appointment ID
     * @param appointmentId The new appointment ID to set
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Sets the appointment title
     * @param title the new title to set
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * Sets the appointment description
     * @param description the new description to set
     */

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the appointment location
     * @param location the new location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets the name of the contact person for this appointment
     * @param contactName The new contact name to set
     */

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Sets the appointment type
     * @param type the new type to set
     */

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the start date and time of the appointment
     * @param start the new start date and time to set
     */

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * Sets the end date and time of the appointment
     * @param end the new end date and time to set
     */

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * Sets the customer ID associated with this appointment
     * @param customerId The new custmoer ID to set
     */

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Sets the user ID association with this appointment
     * @param userId The new User ID to set
     */

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }
}
