package com.example.service;

import com.example.dao.VehicleDAO;
import com.example.dto.VehicleDTO;
import com.example.entity.custom.Vehicle;
import org.modelmapper.ModelMapper;


import java.util.List;
import java.util.stream.Collectors;

public class VehicleService {
    private VehicleDAO dao;
    private ModelMapper mapper;

    public VehicleService() {
        dao = new VehicleDAO();
        mapper = new ModelMapper();
    }

    public boolean addVehicle(VehicleDTO vehicleDTO) {
        return dao.createVehicle(mapper.map(vehicleDTO,Vehicle.class));
    }

    public boolean updateVehicle(VehicleDTO vehicleDTO) {
        return dao.updateVehicle(mapper.map(vehicleDTO,Vehicle.class));
    }

    public VehicleDTO getVehicleById(int vehicleId) {
        Vehicle vehicle = dao.getVehicleById(vehicleId);
        return vehicle != null ? mapToDTO(vehicle) : null;
    }

    public VehicleDTO getVehicleByUserId(String uid) {
        Vehicle vehicle = dao.getVehicleByUserId(uid);
        return vehicle != null ? mapToDTO(vehicle) : null;
    }

    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> allVehicles = dao.getAllVehicles();
        return allVehicles.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public boolean deleteVehicle(int vehicleId) {
        return dao.deleteVehicle(vehicleId);
    }

    private VehicleDTO mapToDTO(Vehicle vehicle) {
        return new VehicleDTO(vehicle.getId(), vehicle.getBrand(), vehicle.getUserId() ,vehicle.getVehicleType());
    }
}
