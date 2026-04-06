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
public class ActivityEndScheduler {

    private final FirestoreActivityService activityService;
    private final NotificationService notificationService;

    // Runs daily at 5 PM
    @Scheduled(cron = "0 0 17 * * ?")
    public void checkEndingToday() {

        try {

            List<FirestoreActivity> activities =
                    activityService.findAll();

            LocalDate today = LocalDate.now();

            activities.stream()
                    .filter(a -> {
                        if (a.getEndDate() == null) return false;
                        return LocalDate.parse(a.getEndDate()).isEqual(today);
                    })
                    .forEach(notificationService::notifyActivityEnding);

        } catch (Exception e) {
            System.out.println("Scheduler error: " + e.getMessage());
        }
    }
}
