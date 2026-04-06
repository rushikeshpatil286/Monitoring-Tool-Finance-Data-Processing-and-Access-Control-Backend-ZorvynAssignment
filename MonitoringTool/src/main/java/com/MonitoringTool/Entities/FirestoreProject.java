package com.MonitoringTool.Entities;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirestoreProject {
    private String id;
    private String projectName;
    private String status;
    private LocalDate startDate;
    private LocalDate targetEndDate;
}

