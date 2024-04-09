package com.example.dao;

import com.example.dto.SlotsDTO;
import com.example.entity.custom.SlotVehicle;
import com.example.entity.custom.Slots;
import com.example.util.DBConnection;
import com.example.util.SlotStatus;
import com.example.util.SlotVehicleStatus;
import com.example.util.VehicleType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SlotsDAO {

    private Connection connection;

    public SlotsDAO() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean createSlot(Slots slotsDTO) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO slots (type, status) VALUES (?, ?)"
            );
            preparedStatement.setString(1, slotsDTO.getType().name());
            preparedStatement.setString(2, slotsDTO.getStatus().name());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Slots> getAllSlots() {
        List<Slots> slots = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM slots");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                VehicleType type = VehicleType.valueOf(resultSet.getString("type"));
                SlotStatus status = SlotStatus.valueOf(resultSet.getString("status"));
                slots.add(new Slots(id, type, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    public List<Slots> getAllAvailableSlotsByType(VehicleType type) {
        List<Slots> slots = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM slots s where s.type = '"+type.name()+"' and status = 'Y'");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                SlotStatus status = SlotStatus.valueOf(resultSet.getString("status"));
                slots.add(new Slots(id, type, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    public List<Slots> getAllSlotsByAvailability(SlotStatus status) {
        List<Slots> slots = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM slots where status ='"+status.name()+"'");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                VehicleType type = VehicleType.valueOf(resultSet.getString("type"));
                slots.add(new Slots(id, type, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    public Slots getSlotById(int slotId) {
        Slots slot = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM slots WHERE id = ?"
            );
            preparedStatement.setInt(1, slotId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                VehicleType type = VehicleType.valueOf(resultSet.getString("type"));
                SlotStatus status = SlotStatus.valueOf(resultSet.getString("status"));
                slot = new Slots(slotId, type, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slot;
    }

    public boolean updateSlot(Slots slotsDTO) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE slots SET type = ?, status = ? WHERE id = ?"
            );
            preparedStatement.setString(1, slotsDTO.getType().name());
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


    public boolean updateAvailability(int slotId, SlotStatus flag) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE slots SET  status = ? WHERE id = ?"
            );
            preparedStatement.setString(1, flag.name());
            preparedStatement.setInt(2, slotId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
