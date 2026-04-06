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
public class ActivityStartScheduler {

    private final FirestoreActivityService activityService;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 9 * * ?")
    public void checkStartingToday() {

        List<FirestoreActivity> activities =
                activityService.findAll();

        LocalDate today = LocalDate.now();

        activities.stream()
                .filter(a -> a.getStartDate() != null &&
                        LocalDate.parse(a.getStartDate()).isEqual(today))
                .forEach(notificationService::notifyActivityStarting);
    }
}

