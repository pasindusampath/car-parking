package com.example.entity.custom;

import com.example.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cost implements SuperEntity {
    private int id;
    private double amount;
}
