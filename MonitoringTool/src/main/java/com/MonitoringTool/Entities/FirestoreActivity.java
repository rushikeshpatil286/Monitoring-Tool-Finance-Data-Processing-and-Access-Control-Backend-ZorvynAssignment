package com.MonitoringTool.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirestoreActivity {

	private String id;

    private String projectName;
    private String activityName;

    private String theme;
    private String subTheme;

    private Double approvedBudget;
    private Double expenditure;

    private Integer target;
    private Integer achievement;

    private String startDate;
    private String endDate;

    private String status;

    private String responsibleEmail;
    private String managerEmail;
    private String coordinatorEmail;

    
    private Boolean overdue;

}
