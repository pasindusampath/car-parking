package com.example.dao;

import com.example.dto.UserDTO;
import com.example.util.DBConnection;
import com.example.util.UserType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private Connection connection;

    public UserDAO() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean createUser(UserDTO userDTO) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO user (id, name, email, type, password) VALUES (?, ?, ?, ?, ?)"
            );
            preparedStatement.setString(1, userDTO.getId());
            preparedStatement.setString(2, userDTO.getName());
            preparedStatement.setString(3, userDTO.getEmail());
            preparedStatement.setString(4, userDTO.getType().name());
            preparedStatement.setString(5, userDTO.getPassword());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                UserType type = UserType.valueOf(resultSet.getString("type"));
                String password = resultSet.getString("password");
                users.add(new UserDTO(id, name, email, type, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public UserDTO getUserById(String userId) {
        UserDTO user = null;
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
                user = new UserDTO(id, name, email, type, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean updateUser(UserDTO userDTO) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE user SET name = ?, email = ?, type = ?, password = ? WHERE id = ?"
            );
            preparedStatement.setString(1, userDTO.getName());
            preparedStatement.setString(2, userDTO.getEmail());
            preparedStatement.setString(3, userDTO.getType().name());
            preparedStatement.setString(4, userDTO.getPassword());
            preparedStatement.setString(5, userDTO.getId());

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
}
