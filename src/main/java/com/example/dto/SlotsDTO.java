package com.example.dto;

import com.example.util.SlotStatus;
import com.example.util.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotsDTO {
    private int id;
    private VehicleType type;
    private SlotStatus status;
}
