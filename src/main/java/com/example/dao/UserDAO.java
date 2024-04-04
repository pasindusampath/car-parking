package com.example.dao;

import com.example.dto.UserDTO;
import com.example.entity.custom.User;
import com.example.util.DBConnection;
import com.example.util.UserType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final Connection connection;

    public UserDAO() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean createUser(User userDTO) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO user (id, name, email, type, password,nic) VALUES (?, ?, ?, ?, ?,?)"
            );
            preparedStatement.setString(1, userDTO.getId());
            preparedStatement.setString(2, userDTO.getName());
            preparedStatement.setString(3, userDTO.getEmail());
            preparedStatement.setString(4, userDTO.getType().name());
            preparedStatement.setString(5, userDTO.getPassword());
            preparedStatement.setString(6, userDTO.getNic());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                UserType type = UserType.valueOf(resultSet.getString("type"));
                String password = resultSet.getString("password");
                String nic = resultSet.getString("nic");
                users.add(new User(id, name, email, type, password,nic));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getUserById(String userId) {
        User user = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM user WHERE id = ?"
            );
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                UserType type = UserType.valueOf(resultSet.getString("type"));
                String password = resultSet.getString("password");
                String nic = resultSet.getString("nic");
                user = new User(id, name, email, type, password,nic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean updateUser(User userDTO) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE user SET name = ?, email = ?, type = ?, password = ? , nic = ? WHERE id = ?"
            );
            preparedStatement.setString(1, userDTO.getName());
            preparedStatement.setString(2, userDTO.getEmail());
            preparedStatement.setString(3, userDTO.getType().name());
            preparedStatement.setString(4, userDTO.getPassword());
            preparedStatement.setString(5, userDTO.getNic());
            preparedStatement.setString(6, userDTO.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String userId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM user WHERE id = ?"
            );
            preparedStatement.setString(1, userId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateNewUserId() {
        String prefix = "U-";
        String latestId = null;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM user ORDER BY id DESC LIMIT 1");

            if (resultSet.next()) {
                latestId = resultSet.getString("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (latestId == null) {
            return prefix + "001"; // If no user exists yet, return the first Id with prefix
        } else {
            // Extracting the numeric part of the latestId and incrementing it
            int numericPart = Integer.parseInt(latestId.substring(prefix.length())) + 1;
            // Padding the numeric part with leading zeros to ensure three digits
            String paddedNumericPart = String.format("%03d", numericPart);
            return prefix + paddedNumericPart;
        }
    }
}
