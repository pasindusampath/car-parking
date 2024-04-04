package com.example.entity.custom;

import com.example.entity.SuperEntity;
import com.example.util.SlotVehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotVehicle implements SuperEntity {
    private int id;
    private int vehicleId;
    private int slotId;
    private Date day;
    private Time time;
    private SlotVehicleStatus status;
}
