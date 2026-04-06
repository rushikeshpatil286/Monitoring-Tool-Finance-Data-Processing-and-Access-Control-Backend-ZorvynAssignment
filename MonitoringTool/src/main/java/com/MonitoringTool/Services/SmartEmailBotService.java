package com.MonitoringTool.Services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.MonitoringTool.Entities.FirestoreActivity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmartEmailBotService {

    private final Firestore firestore;
    private final EmailService emailService;

    // Runs every 1 hour
    @Scheduled(cron = "0 0 * * * *")
    public void runSmartBot() throws Exception {

        System.out.println("🤖 Smart Email Bot Running...");

        ApiFuture<QuerySnapshot> future =
                firestore.collection("activities").get();

        List<QueryDocumentSnapshot> docs = future.get().getDocuments();

        LocalDate today = LocalDate.now();

        for (QueryDocumentSnapshot doc : docs) {

            FirestoreActivity a = doc.toObject(FirestoreActivity.class);

            if (a.getEndDate() == null || a.getEndDate().isBlank()) return;

            LocalDate end = LocalDate.parse(a.getEndDate());
            long daysLeft = ChronoUnit.DAYS.between(today, end);


            // 🔔 2 Days Reminder
            if (daysLeft == 2) {
                sendReminder(a, "⏰ Task Due in 2 Days");
            }

            // 🔔 Tomorrow Reminder + Manager CC
            if (daysLeft == 1) {
                sendUrgentReminder(a);
            }

            // 🔥 Overdue Alert
            if (daysLeft < 0) {
                sendOverdueAlert(a);
            }
        }
    }

    private void sendReminder(FirestoreActivity a, String subject) {
        emailService.sendEmail(
                a.getResponsibleEmail(),
                subject,
                buildMailBody(a)
        );
    }

    private void sendUrgentReminder(FirestoreActivity a) {
        emailService.sendEmail(
                a.getResponsibleEmail(),
                "🚨 Task Due Tomorrow",
                buildMailBody(a) + "\n\n⚠ Manager: " + a.getManagerEmail()
        );
    }

    private void sendOverdueAlert(FirestoreActivity a) {
        emailService.sendEmail(
                a.getManagerEmail(),
                "🔥 OVERDUE TASK ALERT",
                buildMailBody(a) + "\n\n⚠ Immediate Action Required"
        );
    }

    private String buildMailBody(FirestoreActivity a) {
        return """
                Project: %s
                Activity: %s
                Start: %s
                End: %s
                Status: %s

                Please take immediate action.
                """
                .formatted(
                        a.getProjectName(),
                        a.getActivityName(),
                        a.getStartDate(),
                        a.getEndDate(),
                        a.getStatus()
                );
    }
}

