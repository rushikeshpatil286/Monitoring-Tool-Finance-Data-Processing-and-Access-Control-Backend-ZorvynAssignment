package com.MonitoringTool.Controllers;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MonitoringTool.Services.ExcelExportService;
import com.MonitoringTool.Services.FirestoreActivityService;
import com.MonitoringTool.dto.ActivityDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ExcelExportController {

    private final FirestoreActivityService firestoreActivityService;
    private final ExcelExportService excelExportService;

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportDashboardExcel() {

        try {

            List<ActivityDTO> activities = firestoreActivityService.findAllDTO();

            byte[] excel = excelExportService.exportToExcel(activities);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=dashboard-report.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excel);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
