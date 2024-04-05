package com.example.entity.custom;

import com.example.entity.SuperEntity;
import com.example.util.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle implements SuperEntity {
    private int id;
    private String brand;
    private String userId;
    private VehicleType vehicleType;
}
