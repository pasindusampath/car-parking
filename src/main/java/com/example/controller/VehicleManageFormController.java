package com.example.controller;

import com.example.dto.VehicleDTO;
import com.example.service.VehicleService;
import com.example.util.VehicleType;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.util.Optional;

public class VehicleManageFormController {
    public TextField txtUserId;
    public TextField txtVehicleId;
    public TextField txtVehicleBrand;
    public ComboBox<VehicleType> cmbVehicleType;
    public TableView tblParkingDetails;
    public TableColumn colParkingDate;
    public TableColumn colCost;
    private VehicleService service;

    public void initialize(){
        service = new VehicleService();

        cmbVehicleType.getItems().addAll(VehicleType.ELECTRIC,VehicleType.NORMAL);

    }

    public void txtUserIdOnAction(ActionEvent actionEvent) {
        VehicleDTO vehicleByUserId = service.getVehicleByUserId(txtUserId.getText());
        if (vehicleByUserId!=null){
            setData(vehicleByUserId);
        }else{
            new Alert(Alert.AlertType.ERROR,"User Not Found Or User Have No Vehicle").show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        VehicleDTO vehicleDTO = collectData();
        VehicleDTO vehicleByUserId = service.getVehicleByUserId(txtUserId.getText());
        if (vehicleByUserId!=null){
            new Alert(Alert.AlertType.ERROR,"User Already Registered A Vehicle").show();
            return;
        }
        boolean isAdded = service.addVehicle(vehicleDTO);
        if (isAdded){
            new Alert(Alert.AlertType.INFORMATION,"Vehicle Added Success").show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Vehicle Adding Failed").show();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        VehicleDTO vehicleDTO = collectData();
        boolean isUpdated = service.updateVehicle(vehicleDTO);
        if (isUpdated){
            new Alert(Alert.AlertType.INFORMATION,"Vehicle Update Success").show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Vehicle Update Failed").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        txtUserIdOnAction(actionEvent);
        if (txtVehicleId.getText().equals("")){
            new Alert(Alert.AlertType.WARNING,"No Vehicle Found").show();
            return;
        }
        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure To Delete " + txtVehicleId.getText() + " From The System", ButtonType.NO, ButtonType.YES).showAndWait();
        if (buttonType.isPresent()&&buttonType.get().equals(ButtonType.YES)){
            boolean b = service.deleteVehicle(Integer.parseInt(txtVehicleId.getText()));
            if (b){
                new Alert(Alert.AlertType.INFORMATION,"Vehicle Deleted Success").show();
                btnClearOnAction(actionEvent);
                setTableData();
            }else{
                new Alert(Alert.AlertType.ERROR,"Vehicle Not Found").show();
            }
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Not Deleted").show();
        }
    }

    private void setTableData() {

    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        txtVehicleId.clear();
        txtVehicleBrand.clear();
        txtUserId.clear();
        cmbVehicleType.setValue(null);
    }

    public VehicleDTO collectData(){
        int id = 0;
        try {
            id = Integer.parseInt(txtVehicleId.getText());
        }catch (NumberFormatException ex){

        }
        return new VehicleDTO(id,txtVehicleBrand.getText(),txtUserId.getText(),cmbVehicleType.getValue());
    }

    public void setData(VehicleDTO dto){
        txtVehicleId.setText(String.valueOf(dto.getId()));
        txtVehicleBrand.setText(dto.getBrand());
        txtUserId.setText(dto.getUserId());
        cmbVehicleType.setValue(dto.getVehicleType());
    }
}
