package com.MonitoringTool.Services;

import org.springframework.stereotype.Service;
import com.MonitoringTool.Entities.FirestoreActivity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final FirestoreActivityService firestoreActivityService;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    public void updateStatus(String activityId,
                             String newStatus,
                             String performedBy) {

        try {

            // 1️⃣ Fetch activity from Firestore
            FirestoreActivity activity =
                    firestoreActivityService.findById(activityId);

            if (activity == null) {
                throw new RuntimeException("Activity not found");
            }

            String oldStatus = activity.getStatus();

            // 2️⃣ Update status
            activity.setStatus(newStatus);
            firestoreActivityService.save(activity);

            // 3️⃣ Audit log
            auditLogService.log(
                    "ACTIVITY",
                    activityId,
                    "STATUS_UPDATED",
                    "Activity '" + activity.getActivityName()
                            + "' changed from " + oldStatus + " to " + newStatus,
                    performedBy
            );

            // 4️⃣ Email Notification
            notificationService.notifyStatusChange(
                    activity, oldStatus, newStatus
            );

        } catch (Exception e) {
            throw new RuntimeException("Status update failed", e);
        }
    }
}
