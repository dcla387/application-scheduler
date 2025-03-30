package DAO;

import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

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

    /*public static void addAppointment(int appointmentId, String title, String description, String location, int contactId, String type, Timestamp start, Timestamp end, int customerId, int userId) {
        try {
            Connection connection = JDBC.getConnection();
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
    }*/

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
}


