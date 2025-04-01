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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class AppointmentDAO {

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

                //System.out.println("Raw database time: " + resultSet.getTimestamp("Start").toLocalDateTime());

                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();


                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        customerName,
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        contactName,
                        resultSet.getString("Type"),
                        start,
                        end,
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

    public static void addAppointment(String title, String description, String location, int contactId, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId) {
        try {
            Connection connection = JDBC.getConnection();

            /*ZoneId localZone = ZoneId.systemDefault();
            ZoneId utcZone = ZoneOffset.UTC;

            LocalDateTime startUTC = start.atZone(localZone).withZoneSameInstant(utcZone).toLocalDateTime();
            LocalDateTime endUTC = end.atZone(localZone).withZoneSameInstant(utcZone).toLocalDateTime();

            String startStr = start.toString().replace('T', ' ');
            String endStr = end.toString().replace('T', ' ');*/

            String get = "Insert Into appointments (Title, Description, Location, Contact_ID, Type, Start, End, Customer_ID, User_ID )" +
                    "Values ('" + title + "', '" + description + "', '" + location + "', " + contactId + ", '" + type + "', '" + start + "', '" + end + "', " + customerId + ", " + userId + ")";

            PreparedStatement preparedStatement = connection.prepareStatement(get);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            System.out.println(("Error: " + error.getMessage()));

        }
    }

    public static void updateAppointment(int appointmentId, String title, String description, String location, int contactId, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId) {
        try {
            Connection connection = JDBC.getConnection();

            /*ZoneId localZone = ZoneId.systemDefault();
            ZoneId utcZone = ZoneOffset.UTC;

            LocalDateTime startUTC = start.atZone(localZone).withZoneSameInstant(utcZone).toLocalDateTime();
            LocalDateTime endUTC = end.atZone(localZone).withZoneSameInstant(utcZone).toLocalDateTime();*/


            String update = "Update appointments Set " +
                    "Title = '" + title + "', " +
                    "Description = '" + description + "', " +
                    "Location = '" + location + "', " +
                    "Contact_ID = " + contactId + ", " +
                    "Type = '" + type + "', " +
                    "Start = '" + start + "', " +
                    "End = '" + end + "', " +
                    "Customer_ID = " + customerId + ", " +
                    "User_ID = " + userId + " " +
                    "WHERE Appointment_ID = " + appointmentId;

            /*System.out.println("Converting to UTC:");
            System.out.println("Local start: " + start);
            System.out.println("UTC start: " + startUTC);
            System.out.println("Local end: " + end);
            System.out.println("UTC end: " + endUTC);*/

            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
        }
    }

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


                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();

                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        customerName,
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        contactName,
                        resultSet.getString("Type"),
                        start,
                        end,
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

                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();


                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        customerName,
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        contactName,
                        resultSet.getString("Type"),
                        start,
                        end,
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

    public static boolean isInBizHours(LocalDateTime start, LocalDateTime end) {

        ZoneId localZone = ZoneId.systemDefault();
        ZoneId eastZone = ZoneId.of("America/New_York");

        ZonedDateTime startZoned = start.atZone(localZone);
        ZonedDateTime startEast = startZoned.withZoneSameInstant(eastZone);
        LocalTime startTimeEast = startEast.toLocalTime();

        ZonedDateTime endZoned = end.atZone(localZone);
        ZonedDateTime endEast = endZoned.withZoneSameInstant(eastZone);
        LocalTime endTimeEast = endEast.toLocalTime();

        LocalTime bizStart = LocalTime.of(8, 0);
        LocalTime bizEnd = LocalTime.of(22, 0);

        return !startTimeEast.isBefore(bizStart) && !endTimeEast.isAfter(bizEnd);

    }

    public static boolean appointIsOverlapping (int customerId, LocalDateTime start, LocalDateTime end, int appointmentId) {

        ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();

        try {

            Connection connection = JDBC.getConnection();
            String search = "Select * From appointments Where Customer_ID = " + customerId + " AND Appointment_ID != " + appointmentId;

            PreparedStatement preparedStatement = connection.prepareStatement(search);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                LocalDateTime currentStart = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime currentEnd = resultSet.getTimestamp("End").toLocalDateTime();

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


