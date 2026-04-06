package com.MonitoringTool.Controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.MonitoringTool.Services.ExcelImportService;
import com.MonitoringTool.dto.ExcelImportResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminExcelController {

    private final ExcelImportService excelImportService;

    @PostMapping("/upload-excel")
    public ExcelImportResult uploadExcel(@RequestParam("file") MultipartFile file) {

        return excelImportService.importExcel(file);
    }
}


