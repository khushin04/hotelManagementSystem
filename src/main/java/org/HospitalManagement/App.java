package org.HospitalManagement;



import java.sql.*;
import java.util.Scanner;

class App {
    private static final String url = "jdbc:mysql://localhost:3306/myhospitalmanagement";

    private static final String username = "root";
    private static final String password = "root";

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        Scanner sc = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            patients patient = new patients(connection, sc);
            Doctor doctor = new Doctor(connection);
            while (true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1.Add Patients");
                System.out.println("2.View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4.Book Appointment");
                System.out.println("5.Exit");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1: patient.addPatients();  // Add patients
                        System.out.println();
                    case 2: patient.viewPatients(); // View Patients
                        System.out.println();
                    case 3: doctor.viewDoctors();  // view Doctors
                        System.out.println();
                    case 4: bookAppintment(doctor,patient,connection,sc); // Book Appointments
                    case 5:  return; // exit

                    default:
                        System.out.println("invalid Selection ");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void bookAppintment(Doctor doctor, patients patients, Connection connection, Scanner sc) {


        System.out.println("Enter Patients Id :");
        int patientId = sc.nextInt();
        System.out.println("Enter Doctor ID:");
        int doctorID = sc.nextInt();
        System.out.println("Enter Date (YYYY-MM-DD");
        String appointmentDate = sc.next();
        if (patients.getPatientByID(patientId) && doctor.getDoctorByID(doctorID)) {
            if (checkAvailibilty(doctorID, appointmentDate, connection)) {
                String AppointmentQuery = "Insert Into appointments(patient_id, appointment_date) values(?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(AppointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorID);
                    preparedStatement.setString(3, appointmentDate);
                    int rowAffected = preparedStatement.executeUpdate();
                    if (rowAffected > 0) {
                        System.out.println("Appointment Booked");

                    } else {
                        System.out.println("Appointment Booked Failed !!");
                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
            else{
                System.out.println("Doctor Not Available On this Date");

            }
        }
        else {
            System.out.println("Doctor And Patients Does Not Exist!!");
        }
    }

    private static boolean checkAvailibilty(int doctorID, String appointmentDate, Connection connection) {

        String checkQuery = "Select count(*) From appointments Where doctor_id= ? AND appointment_date = ? ";
        try{

            PreparedStatement preparedStatement = connection.prepareStatement(checkQuery);
            preparedStatement.setInt(1, doctorID);
            preparedStatement.setString(2,appointmentDate );
            ResultSet resultSet = preparedStatement.executeQuery();
if(resultSet.next()){
    int count= resultSet.getInt(1);
    if(count == 0){
  return  true;
    }
    else{
        return false;
    }

}

        }
        catch (SQLException e){
            e.printStackTrace();
        }



    return false;}



}