package com.MonitoringTool.Controllers;

import java.util.List;
import java.util.concurrent.Executors;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.MonitoringTool.Services.FirestoreActivityService;
import com.MonitoringTool.dto.ActivityDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserDashboardController {

    private final FirestoreActivityService firestoreActivityService;

    @GetMapping("/dashboard")
    public List<ActivityDTO> getUserDashboard(Authentication auth) {

        String email = auth.getName();
        System.out.println("LOGGED USER EMAIL → " + email);

        return firestoreActivityService.findByUserEmail(email);
    }
    
    @PostMapping("/dashboard/update-status")
    public ResponseEntity<String> updateStatus(
            @RequestParam String id,
            @RequestParam String status) throws Exception {

        firestoreActivityService.updateStatus(id, status);
        return ResponseEntity.ok("Updated");
    }
    
    @GetMapping(value = "/dashboard/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamDashboard(Authentication auth) {

        SseEmitter emitter = new SseEmitter();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String email = auth.getName();

                while (true) {
                    List<ActivityDTO> data =
                            firestoreActivityService.findByUserEmail(email);

                    emitter.send(data);
                    Thread.sleep(5000);
                }
            } catch (Exception e) {
                emitter.complete();
            }
        });

        return emitter;
    }

}


