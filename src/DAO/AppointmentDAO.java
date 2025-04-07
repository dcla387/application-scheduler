package DAO;

import Model.Appointment;
import Model.Country;
import Model.Customer;
import com.mysql.cj.exceptions.ConnectionIsClosedException;
import javafx.beans.binding.ObjectExpression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Access Object class for managing appointment data.
 *
 * <p>This class provides method for retrieving, creating, updating, and deleting appointments.
 * It also includes methods for handling time zone conversions, validations, overlap deteection and other appointment queries</p>
 *
 * @author Donna Clark
 * @version 1.0
 */


public class AppointmentDAO {

    /**
     * Retrieves all appointments from the database.
     *
     * <p>This method fetches all appointment records from the database and performs time conversions.</p>
     *
     * <p>It also retrieves associated contact and customer names using their respective IDs</p>
     *
     * @return returns a list containing all Appointments
     */

    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try {
            Connection connection = JDBC.getConnection();
            String find = "Select * From appointments";
            PreparedStatement preparedStatement = connection.prepareStatement(find);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                int contactId = resultSet.getInt("Contact_ID");
                String contactName = ContactDAO.getContactNameFromId(contactId);

                int customerId = resultSet.getInt("Customer_ID");
                String customerName = CustomerDAO.getCustomerNameFromId(customerId);



                LocalDateTime localStart = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime localEnd = resultSet.getTimestamp("End").toLocalDateTime();




                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        customerName,
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        contactName,
                        resultSet.getString("Type"),
                        localStart,
                        localEnd,
                        resultSet.getInt("Customer_Id"),
                        resultSet.getInt("User_Id")
                );



                appointmentList.add(appointment);
            }

        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
        }

        return appointmentList;
    }

    /**
     * Adds a new appointment to the database.
     *
     * <p>This method inserts a new appointment record into the databse.</p>
     *
     *
     * @param title  The title of the appointment
     * @param description Description of the appointment
     * @param location Location of the appointment
     * @param contactId contact ID associated with the appointment
     * @param type Type of appointment
     * @param start Start date and time of the appointment
     * @param end  End date and time of the appointment
     * @param customerId Customer ID associated with the appointment
     * @param userId User ID associated with the appointment
     */

    public static void addAppointment(String title, String description, String location, int contactId, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId) {
        try {
            Connection connection = JDBC.getConnection();


           String insert = "INSERT INTO appointments " +
                    "(Title, Description, Location, Contact_ID, Type, Start, End, Customer_ID, User_ID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            int paramindex = 1;
            preparedStatement.setString(paramindex++, title);
            preparedStatement.setString(paramindex++, description);
            preparedStatement.setString(paramindex++, location);
            preparedStatement.setInt(paramindex++, contactId);
            preparedStatement.setString(paramindex++, type);
            preparedStatement.setTimestamp(paramindex++, Timestamp.valueOf(start));
            preparedStatement.setTimestamp(paramindex++, Timestamp.valueOf(end));
            preparedStatement.setInt(paramindex++, customerId);
            preparedStatement.setInt(paramindex++, userId);

            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            System.out.println(("Error: " + error.getMessage()));

        }
    }

    /**
     * Updates an exsisting appointment in the database.
     *
     * <p>This method updatees and existing appointment record.</p>
     *
     *
     * @param appointmentId  The ID of the appointment to update
     * @param title Updated title of the appointment
     * @param description Updated description of the appointment
     * @param location Updated location of the appointment
     * @param contactId Updated contact ID associated with the appointment
     * @param type Updated type of appointment
     * @param start Updated start time and date of the appointment
     * @param end Updated end time and date of the appointment
     * @param customerId Customer ID associated with the appointment
     * @param userId User Id associated with the appointment
     */

    public static void updateAppointment(int appointmentId, String title, String description, String location, int contactId, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId) {
        try {
            Connection connection = JDBC.getConnection();


            String update = "Update appointments Set " +
                    "title = ?, " +
                    "Description = ?, " +
                    "Location = ?, " +
                    "Contact_ID = ?, " +
                    "Type = ?, " +
                    "Start = ?, " +
                    "End = ?, " +
                    "Customer_ID =  ?, " +
                    "User_ID = ? " +
                    "WHERE Appointment_ID = ? ";


            PreparedStatement preparedStatement = connection.prepareStatement(update);
            int paramindex = 1;
            preparedStatement.setString(paramindex++, title);
            preparedStatement.setString(paramindex++, description);
            preparedStatement.setString(paramindex++, location);
            preparedStatement.setInt(paramindex++, contactId);
            preparedStatement.setString(paramindex++, type);
            preparedStatement.setTimestamp(paramindex++, Timestamp.valueOf(start));
            preparedStatement.setTimestamp(paramindex++, Timestamp.valueOf(end));
            preparedStatement.setInt(paramindex++, customerId);
            preparedStatement.setInt(paramindex++, userId);
            preparedStatement.setInt(paramindex++, appointmentId);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
        }
    }

    /**
     * Deletes an appointment from the database
     *
     * @param appointmentId ID of the appointment to delete
     */

    public static void deleteAppointment(int appointmentId) {
        try {
            Connection connection = JDBC.getConnection();
            String delete = "Delete From appointments Where Appointment_ID = " + appointmentId;
            PreparedStatement preparedStatement = connection.prepareStatement(delete);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
        }
    }

    /**
     * Retrieve appointments for a specific customer name
     *
     * <p>Serves as a filter to filter by customer name</p>
     *
     * @param customerName The name of the customer to use as a filter for the appointments
     * @return A list containing the appointments belonging to the filtered customer
     */

    public static ObservableList<Appointment> getAppointmentsByCustomerName(String customerName) {

        ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

        ObservableList<Appointment> allAppointments = getAllAppointments();
        for (Appointment appointment : allAppointments) {
            if (appointment.getCustomerName().equals(customerName)) {
                filteredAppointments.add(appointment);
            }
        }
        return filteredAppointments;

    }

    /**
     * Generates a report of appointment counts by month and type.
     *
     * <p>Queries the database to count appointments grouped by Month and Type - used in a Map or dictionary structture.  Outer key in the month.</p>
     *
     * @return A Map containing appointment counts organized by month and type
     */

    public static Map<String, Map<String, Integer>> getAppointmentsByMonthAndType() {
        Map<String, Map<String, Integer>> reportData = new HashMap<>();

        try {
            Connection connection = JDBC.getConnection();
            String get = "Select MONTHNAME(Start) as month_name, type, COUNT(*) as count " +
                    "From Appointments " +
                    "Group by MONTHNAME(Start), Type " +
                    "Order by MONTH(Start), Type";

            PreparedStatement preparedStatement = connection.prepareStatement(get);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String monthName = resultSet.getString("month_name");
                String type = resultSet.getString("type");

                int count = resultSet.getInt("count");

                if (!reportData.containsKey(monthName)) {
                    reportData.put(monthName, new HashMap<>());
                }
                reportData.get(monthName).put(type, count);
            }
        } catch (SQLException error) {
            System.out.println("Report has error in retrieving Data: " + error.getMessage());
        }
        return reportData;
    }


    /**
     * Retrieves all contact names from the database.
     *
     * <p>This method queries the contact table to get all contact names, sorted alphabetically</p>
     * @return ObservableList containing all contact names
     *
     */

    public static ObservableList<String> getAllContactNames() {
        ObservableList<String> contactNames = FXCollections.observableArrayList();

        try {
            Connection connection = JDBC.getConnection();
            String search = "Select Contact_Name From Contacts order by Contact_Name";
            PreparedStatement preparedStatement = connection.prepareStatement(search);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                contactNames.add(resultSet.getString("Contact_Name"));

            }
        } catch (SQLException error) {
            System.out.println("Error " + error.getMessage());

        }
        return contactNames;
    }

    /**
     * Retrieve a contact ID based on the contact name.
     *
     * @param contactName The name of the contact to look up
     * @return The contact ID, or -1 if the contact is not found.
     */

    public static int getContactIdFromName(String contactName) {
        int contactId = -1;

        try {
            Connection connection = JDBC.getConnection();
            String searchName = "Select Contact_ID From contacts Where Contact_Name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(searchName);
            preparedStatement.setString(1, contactName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                contactId = resultSet.getInt("Contact_ID");
            }
        } catch (SQLException error) {
            System.out.println("Error " + error.getMessage());

        }
        return contactId;
    }

    /**
     * Retrieves all appointments for a specific contact.
     *
     * <p>does a query to find all appointment assoicated with the specified contact ID, sorted by Start Time</p>
     * @param contactId The ID of the contact to retrieve the appointements
     * @return ObservableList containt the appointments for the specified contact
     */

    public static ObservableList<Appointment> getAppointmentByContactId(int contactId) {

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            Connection connection = JDBC.getConnection();
            String search = "Select * From appointments Where Contact_ID = ? Order By Start";
            PreparedStatement preparedStatement = connection.prepareStatement(search);
            preparedStatement.setInt(1, contactId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int customerId = resultSet.getInt("Customer_ID");
                String customerName = CustomerDAO.getCustomerNameFromId(customerId);
                String contactName = ContactDAO.getContactNameFromId(contactId);


                LocalDateTime utcStart = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime utcEnd = resultSet.getTimestamp("End").toLocalDateTime();

                ZoneId utcZone = ZoneId.of("UTC");
                ZoneId localZone = ZoneId.systemDefault();

                ZonedDateTime utcZonedStart = utcStart.atZone(utcZone);
                ZonedDateTime localZonedStart = utcZonedStart.withZoneSameInstant(localZone);
                LocalDateTime localStart = localZonedStart.toLocalDateTime();

                ZonedDateTime utcZonedEnd = utcEnd.atZone(utcZone);
                ZonedDateTime localZonedEnd = utcZonedEnd.withZoneSameLocal(localZone);
                LocalDateTime localEnd = localZonedEnd.toLocalDateTime();

                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        customerName,
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        contactName,
                        resultSet.getString("Type"),
                        localStart,
                        localEnd,
                        customerId,
                        resultSet.getInt("User_Id")


                );
                appointments.add(appointment);

            }

        } catch (SQLException error) {
            System.out.println("Error " + error.getMessage());

        }
        return appointments;

    }

    /**
     * Generates a report of customer counts by country.
     *
     * <p>This method creates a report showing how many customers are associated with each country.</p>
     *
     * <p>Report does the following:</p>
     * <ul>
     *     <li>Retrieves all customers</li>
     *     <li>For each customer, determines their country based on Division ID</li>
     *     <li>Counts the number of customers</li>
     *     <li>formates the report into a User friendly dialog box</li>
     * </ul>
     *
     * @param report The Stringbuilder to append the report contenct
     */

    public static void getCustomerCountByCountry(StringBuilder report) {

        try {
            ArrayList<String> countries = new ArrayList<>();
            ArrayList<Integer> counts = new ArrayList<>();

            ObservableList<Customer> allCustomers = CustomerDAO.getAllCustomers();

            for (Customer customer : allCustomers) {

                int divisionId = customer.getDivisionId();
                Country countryObj = CountryDAO.getCountryByDivisionId(divisionId);

                String countryName = (countryObj !=null) ? countryObj.getCountryName() : "Unknown";

                int index = countries.indexOf(countryName);

                if (index >= 0) {
                    int currentCount = counts.get(index);
                    counts.set(index, currentCount + 1);
                } else {
                    countries.add(countryName);
                    counts.add(1);
                }
            }

            report.append("Customer Count By Country\n\n");

            for (int i = 0; i < countries.size(); i++) {
                String country = countries.get(i);
                int count = counts.get(i);

                report.append(country).append(" : ").append(count);
                report.append(count == 1 ? " customer\n" : " customers\n");
            }
        } catch (Exception error) {

            report.append("Error: ").append(error.getMessage());
        }

    }

    /**
     * Retrieves all appointments for a specific customer.
     *
     * @param customerId The ID of the customer to retrieve appointments
     * @return ObservabileList containing the appointments for the specified customer
     */

    public static ObservableList<Appointment> getAppointmentsByCustomerId(int customerId) {

        ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

        try {

            Connection connection = JDBC.getConnection();
            String search = "Select * From appointments Where Customer_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(search);
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int contactId = resultSet.getInt("Contact_ID");
                String contactName = ContactDAO.getContactNameFromId(contactId);
                String customerName = CustomerDAO.getCustomerNameFromId(customerId);

                LocalDateTime utcStart = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime utcEnd = resultSet.getTimestamp("End").toLocalDateTime();

                ZoneId utcZone = ZoneId.of("UTC");
                ZoneId localZone = ZoneId.systemDefault();

                ZonedDateTime utcZonedStart = utcStart.atZone(utcZone);
                ZonedDateTime localZonedStart = utcZonedStart.withZoneSameInstant(localZone);
                LocalDateTime localStart = localZonedStart.toLocalDateTime();

                ZonedDateTime utcZonedEnd = utcEnd.atZone(utcZone);
                ZonedDateTime localZonedEnd = utcZonedEnd.withZoneSameInstant(localZone);
                LocalDateTime localEnd = localZonedEnd.toLocalDateTime();


                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        customerName,
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        contactName,
                        resultSet.getString("Type"),
                        localStart,
                        localEnd,
                        customerId,
                        resultSet.getInt("User_Id")

                );
                filteredAppointments.add(appointment);
            }
        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
            return filteredAppointments;



    }

    /**
     * Checks if an appointment is made within business hours.
     *
     * <p>Validates that an appointments start and end times falls within the prescribed business hours</p>
     *
     * @param start The start date and time in the users local time
     * @param end The end date and time in the suers local time
     * @return True - if the appointment is within business hours, false otherwise
     */

//    public static boolean isInBizHours(LocalDateTime start, LocalDateTime end) {
//
//        ZoneId localZone = ZoneId.systemDefault();
//        ZoneId eastZone = ZoneId.of("America/New_York");
//
//        ZonedDateTime startZoned = start.atZone(localZone);
//        ZonedDateTime startEast = startZoned.withZoneSameInstant(eastZone);
//        LocalTime startTimeEast = startEast.toLocalTime();
//
//        ZonedDateTime endZoned = end.atZone(localZone);
//        ZonedDateTime endEast = endZoned.withZoneSameInstant(eastZone);
//        LocalTime endTimeEast = endEast.toLocalTime();
//
//        LocalTime bizStart = LocalTime.of(8, 0);
//        LocalTime bizEnd = LocalTime.of(22, 0);
//
//        return !startTimeEast.isBefore(bizStart) && !endTimeEast.isAfter(bizEnd);
//
//    }

    public static boolean isInBizHours(LocalDateTime start, LocalDateTime end) {
        ZoneId localZone = ZoneId.systemDefault();
        ZoneId estZone = ZoneId.of("America/New_York");

        ZonedDateTime startZoned = start.atZone(localZone).withZoneSameInstant(estZone);
        ZonedDateTime endZoned = end.atZone(localZone).withZoneSameInstant(estZone);


        ZonedDateTime bizStart = startZoned.toLocalDate().atTime(8, 0).atZone(estZone);
        ZonedDateTime bizEnd = startZoned.toLocalDate().atTime(22, 0).atZone(estZone);

//        System.out.println("---- BUSINESS HOURS VALIDATION ----");
//        System.out.println("Start ET:     " + startZoned);
//        System.out.println("End ET:       " + endZoned);
//        System.out.println("Biz Start ET: " + bizStart);
//        System.out.println("Biz End ET:   " + bizEnd);


        boolean isSameDay = endZoned.toLocalDate().equals(startZoned.toLocalDate());

        boolean result = !startZoned.isBefore(bizStart)
                && !endZoned.isAfter(bizEnd)
                && isSameDay;


        return result;
    }




    /**
     * Checks if an appointment overlaps with an existing appointment for the same customer.
     *
     * <p>this method verifies that a proposed appointment time for a specified customer does not overlap
     * with another appointment for that same customer.</p>
     *
     *
     * @param customerId The ID of the customer to check for overlaps
     * @param start The proposed start date and time of the appointment in user's local time
     * @param end  The proposed end date and time of the appointment in the user's local time
     * @param appointmentId The ID of the appointment being modified
     * @return True if there IS an overlapping appt, false if NOT
     */

    public static boolean appointIsOverlapping (int customerId, LocalDateTime start, LocalDateTime end, int appointmentId) {

        ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();

        try {

            Connection connection = JDBC.getConnection();
            String search = "Select * From appointments Where Customer_ID = " + customerId + " AND Appointment_ID != " + appointmentId;

            PreparedStatement preparedStatement = connection.prepareStatement(search);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                LocalDateTime utcStart = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime utcEnd = resultSet.getTimestamp("End").toLocalDateTime();

                ZoneId utcZone = ZoneId.of("UTC");
                ZoneId localZone = ZoneId.systemDefault();

                ZonedDateTime utcZonedStart = utcStart.atZone(utcZone);
                ZonedDateTime localZoneStart = utcZonedStart.withZoneSameInstant(localZone);
                LocalDateTime currentStart = localZoneStart.toLocalDateTime();

                ZonedDateTime utcZonedEnd = utcEnd.atZone(utcZone);
                ZonedDateTime localZonedEnd = utcZonedEnd.withZoneSameInstant(localZone);
                LocalDateTime currentEnd = localZonedEnd.toLocalDateTime();

                if (start.isBefore(currentEnd) && end.isAfter(currentStart)) {
                    return true;

                }
            }
        } catch (SQLException error) {
            System.out.println("Error in porcessing: " + error.getMessage());
        }
        return false;


    }


}


