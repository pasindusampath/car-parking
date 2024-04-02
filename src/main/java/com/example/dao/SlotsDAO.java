package com.example.dao;

import com.example.dto.SlotsDTO;
import com.example.util.DBConnection;
import com.example.util.SlotStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SlotsDAO {

    private Connection connection;

    public SlotsDAO() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean createSlot(SlotsDTO slotsDTO) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO slots (type, status) VALUES (?, ?)"
            );
            preparedStatement.setString(1, slotsDTO.getType());
            preparedStatement.setString(2, slotsDTO.getStatus().name());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<SlotsDTO> getAllSlots() {
        List<SlotsDTO> slots = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM slots");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                SlotStatus status = SlotStatus.valueOf(resultSet.getString("status"));
                slots.add(new SlotsDTO(id, type, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    public SlotsDTO getSlotById(int slotId) {
        SlotsDTO slot = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM slots WHERE id = ?"
            );
            preparedStatement.setInt(1, slotId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String type = resultSet.getString("type");
                SlotStatus status = SlotStatus.valueOf(resultSet.getString("status"));
                slot = new SlotsDTO(slotId, type, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slot;
    }

    public boolean updateSlot(SlotsDTO slotsDTO) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE slots SET type = ?, status = ? WHERE id = ?"
            );
            preparedStatement.setString(1, slotsDTO.getType());
            preparedStatement.setString(2, slotsDTO.getStatus().name());
            preparedStatement.setInt(3, slotsDTO.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSlot(int slotId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM slots WHERE id = ?"
            );
            preparedStatement.setInt(1, slotId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
