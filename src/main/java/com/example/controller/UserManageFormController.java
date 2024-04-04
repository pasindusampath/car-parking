package com.example.controller;

import com.example.dto.UserDTO;
import com.example.service.UserService;
import com.example.util.UserType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserManageFormController {
    public TableColumn<UserDTO,String> colUserName;
    public TableColumn<UserDTO,String> colEmail;
    public TableColumn<UserDTO, UserType> colType;
    public TextField txtUserName;
    public TextField txtEmail;
    public TextField txtPassword;
    public ComboBox<UserType> cmbUserType;
    public TextField txtUserId;
    public TextField txtNic;
    public TableView<UserDTO> tblUsers;
    private UserService service;

    public void initialize(){
        service = new UserService();
        String uid = service.generateNewUserId();
        txtUserId.setText(uid);
        setCellValueFactories();
        setTableData();

        cmbUserType.getItems().addAll(UserType.LECTURE,UserType.SHUTTLE_SERVICE,UserType.STUDENT,UserType.VISITOR);
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        UserDTO userDTO = collectData();
        boolean isAdded = service.addUser(userDTO);
        if (isAdded){
            new Alert(Alert.AlertType.INFORMATION,"User Added Success").show();
            btnClearOnAction(actionEvent);
            setTableData();
        }else {
            new Alert(Alert.AlertType.ERROR,"User Adding Failed").show();
        }

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        UserDTO userDTO = collectData();
        boolean isUpdated = service.updateUser(userDTO);
        if (isUpdated){
            new Alert(Alert.AlertType.INFORMATION,"User Updated Success").show();
            btnClearOnAction(actionEvent);
            setTableData();
        }else {
            new Alert(Alert.AlertType.ERROR,"User Update Failed").show();
        }

    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        txtUserIdOnAction(actionEvent);
        if (txtUserName.getText().equals("")){
            new Alert(Alert.AlertType.WARNING,"No User Found").show();
            return;
        }
        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure To Delete " + txtUserName.getText() + " From The System", ButtonType.NO, ButtonType.YES).showAndWait();
        if (buttonType.isPresent()&&buttonType.get().equals(ButtonType.YES)){
            boolean b = service.deleteUser(txtUserId.getText());
            if (b){
                new Alert(Alert.AlertType.INFORMATION,"User Deleted Success").show();
                btnClearOnAction(actionEvent);
                setTableData();
            }else{
                new Alert(Alert.AlertType.ERROR,"User Not Found").show();
            }
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Not Deleted").show();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        txtUserId.clear();
        txtUserName.clear();
        txtEmail.clear();
        txtNic.clear();
        txtPassword.clear();
        cmbUserType.setValue(null);
    }

    public void btnRefreshIdOnAction(ActionEvent actionEvent) {
        txtUserId.setText(service.generateNewUserId());
    }
    public UserDTO collectData(){
        return new UserDTO(txtUserId.getText(),txtUserName.getText(),txtEmail.getText(),cmbUserType.getValue(),txtPassword.getText(),txtNic.getText());
    }

    public void setData(UserDTO dto){
        txtUserId.setText(dto.getId());
        txtUserName.setText(dto.getName());
        txtEmail.setText(dto.getEmail());
        txtNic.setText(dto.getNic());
        txtPassword.setText(dto.getPassword());
        cmbUserType.setValue(dto.getType());
    }

    public boolean validateUserId(String userId) {
        String regexPattern = "U-\\d{3}";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(userId);
        return matcher.matches();
    }

    public void txtUserIdOnAction(ActionEvent actionEvent) {
        if (!validateUserId(txtUserId.getText())){
            new Alert(Alert.AlertType.ERROR,"Not Valid User Id").show();
            return;
        }
        UserDTO userDTO = service.searchUser(txtUserId.getText());
        if (userDTO!=null){
            setData(userDTO);
        }else {
            new Alert(Alert.AlertType.ERROR,"User Not Found").show();
        }
    }

    public void setTableData(){
        List<UserDTO> all = service.getAll();
        tblUsers.setItems(FXCollections.observableArrayList(all));
    }

    public void setCellValueFactories(){
        colUserName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
    }
}
