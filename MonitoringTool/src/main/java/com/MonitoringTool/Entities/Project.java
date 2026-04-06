package com.MonitoringTool.Entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Getter
@Setter
public class Project {

    private Long id;

    private String projectName;

    private LocalDate startDate;
    private LocalDate targetEndDate;

    private ProjectStatus status;   // ✅ CORRECT

    private UserEntity createdBy;
}

