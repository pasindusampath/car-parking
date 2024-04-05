package com.example.dto;

import com.example.util.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {
    private int id;
    private String brand;
    private String userId;
    private VehicleType vehicleType;
}
