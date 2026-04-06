package com.MonitoringTool.Services;

import org.springframework.stereotype.Service;

import com.MonitoringTool.Entities.FirestoreActivity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	
	 private final EmailService emailService;

	    public void notifyStatusChange(
	            FirestoreActivity activity,
	            String oldStatus,
	            String newStatus) {

	        try {

	            String subject = "Activity Status Updated";

	            String body = """
	                    Project: %s
	                    Activity: %s

	                    Previous Status: %s
	                    New Status: %s
	                    """.formatted(
	                            activity.getProjectName(),
	                            activity.getActivityName(),
	                            oldStatus,
	                            newStatus
	                    );

	            // Send mail to Responsible
	            if (activity.getResponsibleEmail() != null) {
	                emailService.sendEmail(
	                        activity.getResponsibleEmail(),
	                        subject,
	                        body
	                );
	            }

	            // Send mail to Manager
	            if (activity.getManagerEmail() != null) {
	                emailService.sendEmail(
	                        activity.getManagerEmail(),
	                        subject,
	                        body
	                );
	            }

	            // Send mail to Coordinator
	            if (activity.getCoordinatorEmail() != null) {
	                emailService.sendEmail(
	                        activity.getCoordinatorEmail(),
	                        subject,
	                        body
	                );
	            }

	        } catch (Exception e) {
	            throw new RuntimeException("Notification failed", e);
	        }
	    }
	    public void notifyActivityEnding(FirestoreActivity activity) {

	        emailService.sendEmail(
	                activity.getResponsibleEmail(),
	                "Activity Ending Today",
	                "Project: " + activity.getProjectName()
	                        + "\nActivity: " + activity.getActivityName()
	                        + "\nEnd Date: " + activity.getEndDate()
	        );
	    }
	    
	    public void notifyActivityStarting(FirestoreActivity activity) {

	        emailService.sendEmail(
	                activity.getResponsibleEmail(),
	                "Activity Starting Today",
	                "Project: " + activity.getProjectName()
	                        + "\nActivity: " + activity.getActivityName()
	                        + "\nStart Date: " + activity.getStartDate()
	        );
	    }
	    public void notifyOverdue(FirestoreActivity activity) {

	        emailService.sendEmail(
	                activity.getResponsibleEmail(),
	                "⚠ Activity Overdue",
	                "Project: " + activity.getProjectName()
	                        + "\nActivity: " + activity.getActivityName()
	                        + "\nEnd Date: " + activity.getEndDate()
	                        + "\n\nThis activity is overdue."
	        );
	    }

}

