package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class LoginFormController {
    public TextField txtPassword;
    public TextField txtUserName;

    public void btnLogInOnAction(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage)txtPassword.getScene().getWindow();
        window.close();
        Parent load = FXMLLoader.load(getClass().getResource("/view/DashBoardForm.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.show();
    }
}
