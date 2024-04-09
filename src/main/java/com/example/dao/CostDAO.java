package com.example.dao;

import com.example.dto.CostDTO;
import com.example.entity.custom.Cost;
import com.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CostDAO {

    private Connection connection;

    public CostDAO() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean createCost(Cost costDTO) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO cost (id, amount) VALUES (?, ?)"
            );
            preparedStatement.setInt(1, costDTO.getId());
            preparedStatement.setDouble(2, costDTO.getAmount());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Cost> getAllCosts() {
        List<Cost> costs = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM cost");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                double amount = resultSet.getDouble("amount");
                costs.add(new Cost(id, amount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return costs;
    }

    public CostDTO getCostById(int costId) {
        CostDTO cost = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM cost WHERE id = ?"
            );
            preparedStatement.setInt(1, costId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double amount = resultSet.getDouble("amount");
                cost = new CostDTO(costId, amount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cost;
    }

    public boolean updateCost(Cost costDTO) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE cost SET amount = ? WHERE id = ?"
            );
            preparedStatement.setDouble(1, costDTO.getAmount());
            preparedStatement.setInt(2, costDTO.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCost(int costId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM cost WHERE id = ?"
            );
            preparedStatement.setInt(1, costId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
