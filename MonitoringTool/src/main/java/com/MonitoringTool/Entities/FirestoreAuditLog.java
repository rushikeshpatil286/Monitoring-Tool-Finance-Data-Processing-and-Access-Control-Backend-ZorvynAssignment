package com.MonitoringTool.Entities;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FirestoreAuditLog {

    private String id;
    private String entityType;
    private String entityId;
    private String action;
    private String description;
    private String performedBy;
    private String timestamp;

    public FirestoreAuditLog() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now().toString();
    }
}
