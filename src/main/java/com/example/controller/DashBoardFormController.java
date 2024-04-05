package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashBoardFormController {
    public AnchorPane navPane;

    public void initialize() throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("/view/sub/UserManageForm.fxml"));
        navPane.getChildren().clear();
        navPane.getChildren().add(load);
    }

    public void btnManageUserOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("/view/sub/UserManageForm.fxml"));
        navPane.getChildren().clear();
        navPane.getChildren().add(load);
    }

    public void btnManageVehiclesOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("/view/sub/VehicleManageForm.fxml"));
        navPane.getChildren().clear();
        navPane.getChildren().add(load);
    }

    public void manageParkingSlotsOnAction(ActionEvent actionEvent) {
    }

    public void manageParkingOnAction(ActionEvent actionEvent) {

    }
}
