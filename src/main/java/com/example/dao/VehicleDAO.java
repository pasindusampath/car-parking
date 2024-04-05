package com.example.dao;

import com.example.dto.VehicleDTO;
import com.example.entity.custom.Vehicle;
import com.example.util.DBConnection;
import com.example.util.VehicleType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    private Connection connection;

    public VehicleDAO() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean createVehicle(Vehicle vehicleDTO) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO vehicle (brand, u_id,type ) VALUES (?, ?,?)"
            );
            System.out.println(vehicleDTO.getVehicleType().name());
            preparedStatement.setString(1, vehicleDTO.getBrand());
            preparedStatement.setString(2, vehicleDTO.getUserId());
            preparedStatement.setString(3, vehicleDTO.getVehicleType().name());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM vehicle");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String brand = resultSet.getString("brand");
                String userId = resultSet.getString("u_id");
                VehicleType vehicleType = VehicleType.valueOf(resultSet.getString("type"));
                vehicles.add(new Vehicle(id, brand, userId,vehicleType));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public Vehicle getVehicleById(int vehicleId) {
        Vehicle vehicle = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM vehicle WHERE id = ?"
            );
            preparedStatement.setInt(1, vehicleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String brand = resultSet.getString("brand");
                String userId = resultSet.getString("u_id");
                VehicleType vehicleType = VehicleType.valueOf(resultSet.getString("type"));
                vehicle = new Vehicle(vehicleId, brand, userId,vehicleType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicle;
    }

    public Vehicle getVehicleByUserId(String uid) {
        Vehicle vehicle = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM vehicle WHERE u_id = ?"
            );
            preparedStatement.setString(1, uid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int vehicleId = resultSet.getInt("id");
                String brand = resultSet.getString("brand");
                String userId = resultSet.getString("u_id");
                VehicleType vehicleType = VehicleType.valueOf(resultSet.getString("type"));
                vehicle = new Vehicle(vehicleId, brand, userId,vehicleType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicle;
    }

    public boolean updateVehicle(Vehicle vehicleDTO) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE vehicle SET brand = ?, u_id = ? , type = ? WHERE id = ?"
            );
            preparedStatement.setString(1, vehicleDTO.getBrand());
            preparedStatement.setString(2, vehicleDTO.getUserId());
            preparedStatement.setString(3, vehicleDTO.getVehicleType().name());
            preparedStatement.setInt(4, vehicleDTO.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteVehicle(int vehicleId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM vehicle WHERE id = ?"
            );
            preparedStatement.setInt(1, vehicleId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
