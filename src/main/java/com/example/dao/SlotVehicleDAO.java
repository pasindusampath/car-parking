package com.example.dao;

import com.example.dto.SlotVehicleDTO;
import com.example.entity.custom.SlotVehicle;
import com.example.entity.custom.Slots;
import com.example.util.DBConnection;
import com.example.util.SlotVehicleStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SlotVehicleDAO {

    private Connection connection;

    public SlotVehicleDAO() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean createSlotVehicle(SlotVehicleDTO slotVehicleDTO) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO slot_vehicle (v_id, s_id, day, r_time, status) VALUES (?, ?, ?, ?, ?)"
            );
            preparedStatement.setInt(1, slotVehicleDTO.getVehicleId());
            preparedStatement.setInt(2, slotVehicleDTO.getSlotId());
            preparedStatement.setDate(3, slotVehicleDTO.getDay());
            preparedStatement.setTime(4, slotVehicleDTO.getTime());
            preparedStatement.setString(5, slotVehicleDTO.getStatus().name());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<SlotVehicleDTO> getAllSlotVehicles() {
        List<SlotVehicleDTO> slotVehicles = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM slot_vehicle");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int vehicleId = resultSet.getInt("v_id");
                int slotId = resultSet.getInt("s_id");
                Date day = resultSet.getDate("day");
                Time rTime = resultSet.getTime("r_time");
                SlotVehicleStatus status = SlotVehicleStatus.valueOf(resultSet.getString("status"));
                slotVehicles.add(new SlotVehicleDTO(id, vehicleId, slotId, day, rTime, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slotVehicles;
    }

    public SlotVehicleDTO getSlotVehicleById(int slotVehicleId) {
        SlotVehicleDTO slotVehicle = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM slot_vehicle WHERE id = ?"
            );
            preparedStatement.setInt(1, slotVehicleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int vehicleId = resultSet.getInt("v_id");
                int slotId = resultSet.getInt("s_id");
                Date day = resultSet.getDate("day");
                Time rTime = resultSet.getTime("r_time");
                SlotVehicleStatus status = SlotVehicleStatus.valueOf(resultSet.getString("status"));
                slotVehicle = new SlotVehicleDTO(slotVehicleId, vehicleId, slotId, day, rTime, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slotVehicle;
    }

    public boolean updateSlotVehicle(SlotVehicleDTO slotVehicleDTO) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE slot_vehicle SET v_id = ?, s_id = ?, day = ?, r_time = ?, status = ? WHERE id = ?"
            );
            preparedStatement.setInt(1, slotVehicleDTO.getVehicleId());
            preparedStatement.setInt(2, slotVehicleDTO.getSlotId());
            preparedStatement.setDate(3, slotVehicleDTO.getDay());
            preparedStatement.setTime(4, slotVehicleDTO.getTime());
            preparedStatement.setString(5, slotVehicleDTO.getStatus().name());
            preparedStatement.setInt(6, slotVehicleDTO.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSlotVehicle(int slotVehicleId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM slot_vehicle WHERE id = ?"
            );
            preparedStatement.setInt(1, slotVehicleId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public SlotVehicle isUserAlreadyParkedVehicle(String userId){
        Slots slot = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT sv.id , sv.v_id , sv.s_id , sv.day , sv.r_time , sv.status FROM user u join vehicle v on u.id = v.u_id join slot_vehicle sv on v.id = sv.v_id where sv.status = 'Y' and u.id = ?"
            );
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                int v_id = resultSet.getInt(2);
                int s_id = resultSet.getInt(3);
                Date day = resultSet.getDate(4);
                Time time = resultSet.getTime(5);
                SlotVehicleStatus status = SlotVehicleStatus.valueOf(resultSet.getString(6));
                return new SlotVehicle(id,v_id,s_id,day,time,status);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean updateStatus(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE slot_vehicle SET status = ? WHERE id = ?"
            );
           preparedStatement.setString(1,SlotVehicleStatus.N.name());
           preparedStatement.setInt(2,id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
