package com.example.dao;

import com.example.dto.VehicleDTO;
import com.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    private Connection connection;

    public VehicleDAO() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean createVehicle(VehicleDTO vehicleDTO) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO vehicle (brand, u_id) VALUES (?, ?)"
            );
            preparedStatement.setString(1, vehicleDTO.getBrand());
            preparedStatement.setString(2, vehicleDTO.getUserId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<VehicleDTO> getAllVehicles() {
        List<VehicleDTO> vehicles = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM vehicle");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String brand = resultSet.getString("brand");
                String userId = resultSet.getString("u_id");
                vehicles.add(new VehicleDTO(id, brand, userId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public VehicleDTO getVehicleById(int vehicleId) {
        VehicleDTO vehicle = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM vehicle WHERE id = ?"
            );
            preparedStatement.setInt(1, vehicleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String brand = resultSet.getString("brand");
                String userId = resultSet.getString("u_id");
                vehicle = new VehicleDTO(vehicleId, brand, userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicle;
    }

    public boolean updateVehicle(VehicleDTO vehicleDTO) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE vehicle SET brand = ?, u_id = ? WHERE id = ?"
            );
            preparedStatement.setString(1, vehicleDTO.getBrand());
            preparedStatement.setString(2, vehicleDTO.getUserId());
            preparedStatement.setInt(3, vehicleDTO.getId());

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
