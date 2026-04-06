package com.MonitoringTool.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    //private String id;

    private String name;
    
    private String email;

    private String password;

    private Role role; // ADMIN / USER

    private Status status;
    
    public void setDefaults() {
        if (this.role == null) {
            this.role = Role.USER;
        }
        if (this.status == null) {
            this.status = Status.ACTIVE;
        }
    }
}