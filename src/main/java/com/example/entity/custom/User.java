package com.example.entity.custom;

import com.example.entity.SuperEntity;
import com.example.util.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements SuperEntity {
    private String id;
    private String name;
    private String email;
    private UserType type;
    private String password;
    private String nic;
}
