package com.MonitoringTool.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MonitoringTool.Services.FirestoreActivityService;
import com.MonitoringTool.Services.EmailService;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final FirestoreActivityService firestoreActivityService;
    private final EmailService emailService;

    @PostMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable String id,
            @RequestParam String status,
            @RequestParam String managerEmail,
            @RequestParam String activityName) {

        try {

            // 🔹 Update Firestore document
            firestoreActivityService.updateStatus(id, status);

            // 🔹 Send email
            emailService.sendEmail(
                    managerEmail,
                    "Task Status Updated",
                    activityName + " → " + status
            );

            return ResponseEntity.ok("Status Updated");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Update failed");
        }
    }
}
