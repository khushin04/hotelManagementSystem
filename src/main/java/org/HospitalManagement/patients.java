package org.HospitalManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class patients {
    private Connection connection;
    private Scanner sc;

    public patients(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    public void addPatients() {
        System.out.println("Enter Patient Name :");
        String name = sc.nextLine();
        System.out.println("Enter Patient Age :");
        int age = sc.nextInt();
        System.out.println("Enter Patient Gender :");
        String gender = sc.next();

        try {
            String Query = "INSERT INTO PATIENTS( name, age, gender) values(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(Query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int affectedrow = preparedStatement.executeUpdate();

            if (affectedrow > 0) {
                System.out.println("Patients Added Sussccefully");

            } else {
                System.out.println("Failed to Add patients");

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void viewPatients() {
        String Query = "Select * from  patients";
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(Query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("patients");
            System.out.println("+--------------+-----------------+-----------+-----------+");
            System.out.println("|patient ID    | Name            |  Age      | Gender    |");
            System.out.println("+--------------+-----------------+-----------+-----------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-14s|%-16s|%11-s|%-11s\n",id, name,age,gender);
                System.out.println("+--------------+-----------------+-----------+-----------+");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean getPatientByID(int Id) {
        String Query = "Select * from patients WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Query);
            preparedStatement.setInt(1, Id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return true;

            }
            else{
                return false;
            }


        } catch (SQLException e) {
            e.printStackTrace();

        }

        return  false;
    }


}






