package com.example.controller;

import com.example.controller.qr.QrScannerFormController;
import com.example.dto.SlotVehicleDTO;
import com.example.dto.UserDTO;
import com.example.dto.VehicleDTO;
import com.example.entity.custom.SlotVehicle;
import com.example.entity.custom.Vehicle;
import com.example.service.SlotVehicleService;
import com.example.service.UserService;
import com.example.service.VehicleService;
import com.example.util.VehicleType;
import com.example.util.jwt.JwtUtil;
import com.example.util.qr.QrData;
import com.example.util.qr.QrPerformance;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ReservationAndParking implements QrPerformance {


    public TextField txtUserId;
    public TextField txtUserName;
    public TextField txtVehicleName;
    public TextField txtVehicleNumber;
    public TextField txtSlotNumberForReserve;
    public TextField txtPayment;
    public AnchorPane qrPane;
    public ComboBox<VehicleType> cmbVehicleType;
    private UserService userService;
    private VehicleService vehicleService;
    private SlotVehicleService slotVehicleService;


    public void initialize() throws IOException {
        userService = new UserService();
        vehicleService = new VehicleService();
        slotVehicleService = new SlotVehicleService();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/qr/QrScannerForm.fxml"));
        Parent load = fxmlLoader.load();
        QrScannerFormController controller = fxmlLoader.getController();
        controller.setController(this);
        qrPane.getChildren().clear();
        qrPane.getChildren().addAll(load);
    }

    @Override
    public void qrIdRequestAction(String id) {
        if (id.equals("remove"))return;
        QrData qrData = new Gson().fromJson(id, QrData.class);
        UserDTO userDTO = userService.searchUser(qrData.getId());
        VehicleDTO vehicleByUserId = vehicleService.getVehicleByUserId(qrData.getId());
        setVehicleAndUserData(userDTO,vehicleByUserId);

        boolean parked = slotVehicleService.isUserAlreadyParkedVehicle(qrData.getId());
        if (parked){

            SlotVehicleDTO existing = slotVehicleService.getExistingParkedDetails(qrData.getId());
            //Calculate fee
            double cost = slotVehicleService.calculateFee(existing.getSlotId(), userDTO.getType(), existing.getTime());
            Optional<ButtonType> buttonType = new Alert(Alert.AlertType.INFORMATION, "The User Already Parked . Confirm To Pay Out ? " + "Amout " +
                    "Rs." + cost + "",ButtonType.YES,ButtonType.NO).showAndWait();
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.YES)){
                try {
                    //add cost detail
                    boolean done = slotVehicleService.makePayment(existing, cost);
                    if (done){
                        new Alert(Alert.AlertType.INFORMATION,"Payment Done").show();
                    }else{
                        new Alert(Alert.AlertType.ERROR,"Something Went Wrong").show();
                    }
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR,"Something Went Wrong").show();
                    e.printStackTrace();
                }
            }


        }else{
            Optional<ButtonType> buttonType = new Alert(Alert.AlertType.INFORMATION, "The User Not Parked Yet. Confirm to find and Serve a slot",ButtonType.YES,ButtonType.NO).showAndWait();
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.YES)){
                try {
                    boolean isParked = slotVehicleService.parkVehicle(vehicleByUserId);
                    if (isParked){
                        new Alert(Alert.AlertType.INFORMATION,"Parking Compleate").show();
                    }else {
                        new Alert(Alert.AlertType.ERROR,"Something Went Wrong").show();
                    }
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR,"Something Went Wrong").show();
                    e.printStackTrace();
                }
            }


        }

        System.out.println(qrData);

    }

    @Override
    public String getDetails(String id) {
        return null;
    }


    public void btnReserveOnAction(ActionEvent actionEvent) {
    }

    public void btnPaymentOnAction(ActionEvent actionEvent) {


    }

    public void setVehicleAndUserData(UserDTO user,VehicleDTO vehicle){
        txtUserId.setText(user.getId());
        txtUserName.setText(user.getName());
        txtVehicleNumber.setText(vehicle.getId()+"");
        txtVehicleName.setText(vehicle.getBrand());
        cmbVehicleType.setValue(vehicle.getVehicleType());

    }


}
