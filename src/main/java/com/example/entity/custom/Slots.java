package com.example.entity.custom;

import com.example.entity.SuperEntity;
import com.example.util.SlotStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slots implements SuperEntity {
    private int id;
    private String type;
    private SlotStatus status;
}
