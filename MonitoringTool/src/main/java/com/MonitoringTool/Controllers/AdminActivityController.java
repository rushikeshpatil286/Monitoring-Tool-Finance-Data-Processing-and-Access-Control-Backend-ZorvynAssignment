package com.MonitoringTool.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MonitoringTool.Entities.FirestoreActivity;
import com.MonitoringTool.Services.EmailService;
import com.MonitoringTool.Services.FirestoreActivityService;

@RestController
@RequestMapping("/api/admin/activity")
@RequiredArgsConstructor
public class AdminActivityController {

    private final FirestoreActivityService activityService;
    private final EmailService emailService;

    // ===============================
    // ✅ CREATE ACTIVITY
    // ===============================
    @PostMapping
    public ResponseEntity<String> createActivity(
            @RequestBody FirestoreActivity activity) {

        try {

            activityService.save(activity);

            // 🔹 Send email to responsible person
            emailService.sendActivityCreatedMail(
                    activity.getResponsibleEmail(),
                    activity.getProjectName(),
                    activity.getActivityName()
            );

            return ResponseEntity.ok("Activity Created Successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("Activity Creation Failed");
        }
    }

    // ===============================
    // ✅ UPDATE ACTIVITY (SAFE UPDATE)
    // ===============================
    @PutMapping("/{id}")
    public ResponseEntity<String> updateActivity(
            @PathVariable String id,
            @RequestBody FirestoreActivity activity) {

        try {

            activityService.updateActivity(id, activity);

            return ResponseEntity.ok("Activity Updated Successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("Update Failed");
        }
    }

    // ===============================
    // ✅ UPDATE STATUS ONLY
    // ===============================
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable String id,
            @RequestParam String status) {

        try {

            activityService.updateStatus(id, status);

            return ResponseEntity.ok("Status Updated Successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("Status Update Failed");
        }
    }
}
