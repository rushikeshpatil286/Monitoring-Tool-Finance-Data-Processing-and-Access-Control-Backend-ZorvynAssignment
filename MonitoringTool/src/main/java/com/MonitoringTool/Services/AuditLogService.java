package com.MonitoringTool.Services;

import org.springframework.stereotype.Service;
import com.MonitoringTool.Entities.FirestoreAuditLog;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final Firestore firestore;

    private static final String COLLECTION = "audit_logs";

    public void log(String entityType,
                    String entityId,
                    String action,
                    String description,
                    String performedBy) {

        try {

            FirestoreAuditLog log = new FirestoreAuditLog();
            log.setEntityType(entityType);
            log.setEntityId(entityId);
            log.setAction(action);
            log.setDescription(description);
            log.setPerformedBy(performedBy);

            firestore.collection(COLLECTION)
                    .document(log.getId())
                    .set(log);

        } catch (Exception e) {
            throw new RuntimeException("Audit logging failed", e);
        }
    }
}
