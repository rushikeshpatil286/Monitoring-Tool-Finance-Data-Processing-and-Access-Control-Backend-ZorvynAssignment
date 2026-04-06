package com.MonitoringTool.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirestoreUser {
    private String id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String status;
}

