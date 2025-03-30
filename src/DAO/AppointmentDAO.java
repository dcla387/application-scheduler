package DAO;

import Model.Appointment;
import com.mysql.cj.exceptions.ConnectionIsClosedException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
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

                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        customerName,
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        contactName,
                        resultSet.getString("Type"),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
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

            String startStr = start.toString().replace('T', ' ');
            String endStr = end.toString().replace('T', ' ');

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
            String startStr = start.toString().replace('T', ' ');
            String endStr = end.toString().replace('T', ' ');

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

                contactNames.add(resultSet.getString("Contact Name"));

            }
            } catch (SQLException error) {
                    System.out.println("Error " + error.getMessage());

        }
        return contactNames;
    }

    public static int getContactIdFromName(String contactName) {
        int contactId = -1;

        try{
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

                Appointment appointment = new Appointment(
                       resultSet.getInt("Appointment_ID"),
                       customerName,
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        contactName,
                        resultSet.getString("Type"),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
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

}


