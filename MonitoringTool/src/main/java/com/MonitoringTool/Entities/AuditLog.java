package com.MonitoringTool.Entities;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditLog {

    private Long id;

    private String entityType;
    private Long entityId;
    private String action;

    private String description;

    private String performedBy;
    private LocalDateTime performedAt;
}



