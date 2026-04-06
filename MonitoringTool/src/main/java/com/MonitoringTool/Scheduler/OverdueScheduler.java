package com.MonitoringTool.Scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.MonitoringTool.Entities.FirestoreActivity;
import com.MonitoringTool.Services.FirestoreActivityService;
import com.MonitoringTool.Services.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OverdueScheduler {

    private final FirestoreActivityService activityService;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 10 * * ?")
    public void checkOverdue() {

        List<FirestoreActivity> activities = activityService.findAll();

        LocalDate today = LocalDate.now();

        activities.stream()
                .filter(a -> a.getEndDate() != null)
                .filter(a -> !"COMPLETE".equalsIgnoreCase(a.getStatus()))
                .filter(a -> LocalDate.parse(a.getEndDate()).isBefore(today))
                .forEach(activity -> {

                    try {
                        // ✅ Mark overdue in Firestore
                        activity.setOverdue(true);
                        activityService.save(activity);

                        // ✅ Send notification
                        notificationService.notifyOverdue(activity);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}

