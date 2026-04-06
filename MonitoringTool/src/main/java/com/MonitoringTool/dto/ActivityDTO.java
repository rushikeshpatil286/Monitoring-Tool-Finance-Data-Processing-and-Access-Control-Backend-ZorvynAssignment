package com.MonitoringTool.dto;

import lombok.Data;

@Data
public class ActivityDTO {

    private String id;

    private String activityName;

    private String projectName;

    private String startDate;   // STRING, not LocalDate
    private String endDate;     // STRING

    private Double approvedBudget;
    private Double expenditure;

    private String status;

    private String responsibleEmail;
    private String managerEmail;
    private String coordinatorEmail;
}

