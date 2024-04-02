package com.example.dto;

import com.example.util.SlotStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotsDTO {
    private int id;
    private String type;
    private SlotStatus status;
}
