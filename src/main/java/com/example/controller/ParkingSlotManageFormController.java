package com.example.controller;

import com.example.dto.SlotsDTO;
import com.example.service.SlotService;
import com.example.util.SlotStatus;
import com.example.util.VehicleType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;

public class ParkingSlotManageFormController {
    public TextField txtSlotId;
    public ComboBox<VehicleType> cmbSlotType;
    public ComboBox<SlotStatus> cmbSLotAvailability;
    public TableView<SlotsDTO> tblSlots;
    public TableColumn<SlotsDTO,Integer> colSlotId;
    public TableColumn<SlotsDTO,VehicleType> colSlotType;
    public TableColumn<SlotsDTO,SlotStatus> colSlotAvailability;
    private SlotService service;
    public void initialize(){
        service = new SlotService();
        visualizeTableData();
        setTableData();
        setComboBoxData();
    }
    public void setComboBoxData(){
        cmbSlotType.getItems().addAll(VehicleType.values());
        cmbSLotAvailability.getItems().addAll(SlotStatus.values());
    }

    public void setTableData(){
        List<SlotsDTO> allSlots = service.getAllSlots();
        tblSlots.setItems(FXCollections.observableArrayList(allSlots));
    }

    public void visualizeTableData(){
        colSlotId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSlotType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colSlotAvailability.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void txtSlotIdOnAction(ActionEvent actionEvent) {
        SlotsDTO slotsDTO = service.searchSlotById(Integer.parseInt(txtSlotId.getText()));
        if(slotsDTO!=null)setData(slotsDTO);
        else new Alert(Alert.AlertType.ERROR,"No SLots Found").show();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        SlotsDTO slotsDTO = collectData();
        boolean b = service.saveSlot(slotsDTO);
        if (b){
            new Alert(Alert.AlertType.INFORMATION,"Saved Success").show();
            setTableData();
            btnClearOnAction(actionEvent);
        }else{
            new Alert(Alert.AlertType.ERROR,"Save Fail").show();
        }
    }

    
    public void btnUpdateOnAction(ActionEvent actionEvent) {
        SlotsDTO slotsDTO = collectData();
        boolean b = service.updateSlot(slotsDTO);
        if (b){
            new Alert(Alert.AlertType.INFORMATION,"Update Success").show();
            setTableData();
            btnClearOnAction(actionEvent);
        }else{
            new Alert(Alert.AlertType.ERROR,"Update Fail").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        txtSlotId.clear();
        txtSlotIdOnAction(actionEvent);
        if (!txtSlotId.getText().equals("")){
            Optional<ButtonType> buttonType = new Alert(Alert.AlertType.WARNING, "Do You Want To Delete " + txtSlotId.getText() +
                    " From The System", ButtonType.YES, ButtonType.NO).showAndWait();
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.YES)){
                boolean delete = service.delete(Integer.parseInt(txtSlotId.getText()));
                if(delete){
                    new Alert(Alert.AlertType.INFORMATION,"Delete Success").show();
                    setTableData();
                    btnClearOnAction(actionEvent);
                }else{
                    new Alert(Alert.AlertType.ERROR,"Delete Fail").show();
                }
            }else {
                new Alert(Alert.AlertType.ERROR,"Operation Cancelled by User").show();
            }
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        txtSlotId.clear();
        cmbSlotType.setValue(null);
        cmbSLotAvailability.setValue(null);
    }

    public void setData(SlotsDTO dto){
        txtSlotId.setText(String.valueOf(dto.getId()));
        cmbSlotType.setValue(dto.getType());
        cmbSLotAvailability.setValue(dto.getStatus());
    }

    public SlotsDTO collectData(){
        int id = 0 ;
        try{id = Integer.parseInt(txtSlotId.getText());}catch (NumberFormatException e){}
        return new SlotsDTO(id,cmbSlotType.getValue(),cmbSLotAvailability.getValue());
    }
}
