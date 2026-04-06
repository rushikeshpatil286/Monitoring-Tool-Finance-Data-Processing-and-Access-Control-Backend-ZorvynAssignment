package com.MonitoringTool.Entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Activity {

	
    private Long id;

    private String activityName;
    
    private String theme;

    private String subtheme;

    private Double approvedBudget;
    private Double expenditure;

    private String startDate;
    private String endDate;

    private ActivityStatus status;

    private Project project;

    private UserEntity responsible;

    private UserEntity manager;

    private UserEntity coordinator;

    private boolean overdue;
}

