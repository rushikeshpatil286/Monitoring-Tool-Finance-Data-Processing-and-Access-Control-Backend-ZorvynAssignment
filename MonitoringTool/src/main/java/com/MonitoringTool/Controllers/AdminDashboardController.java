package com.MonitoringTool.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MonitoringTool.Services.FirestoreActivityService;
import com.MonitoringTool.dto.ActivityDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final FirestoreActivityService activityService;

    @GetMapping("/dashboard")
    public List<ActivityDTO> getAdminDashboard() {
        return activityService.findAllDTO();
    }
}

