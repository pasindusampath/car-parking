package com.example.util.qr;


import com.example.util.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor
public class QrData {
    private String id;
    private String name;
    private UserType type;
}
