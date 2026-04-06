package com.MonitoringTool.Services;

import java.util.*;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.MonitoringTool.Entities.FirestoreActivity;
import com.MonitoringTool.dto.ExcelImportResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExcelImportService {

    private final FirestoreActivityService activityService;
    private final EmailService emailService;

    public ExcelImportResult importExcel(MultipartFile file) {

        int success = 0;
        int failed = 0;

        double totalBudget = 0;
        double totalExpenditure = 0;

        Map<String, Integer> themeCount = new HashMap<>();
        Map<String, Integer> subThemeCount = new HashMap<>();

        List<String> errors = new ArrayList<>();
        
        System.out.println("Imported Activities: " + success);
        System.out.println("Failed Activities: " + failed);

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            int sheetCount = workbook.getNumberOfSheets();
            System.out.println("Workbook Sheets: " + sheetCount);

            for (int s = 0; s < sheetCount; s++) {

                Sheet sheet = workbook.getSheetAt(s);
                System.out.println("Processing Sheet: " + sheet.getSheetName());
                if (sheet == null) continue;

                System.out.println("Processing Sheet: " + sheet.getSheetName());

                // Project Name (Row 2 Column B)
                String projectName = getString(sheet.getRow(1).getCell(1));

                if (projectName.isBlank()) {
                    System.out.println("Project name missing in sheet: " + sheet.getSheetName());
                    continue;
                }

                for (int i = 5; i <= sheet.getLastRowNum(); i++) {

                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    try {

                        String activityName = getString(row.getCell(1));
                        if (activityName.isBlank()) continue;

                        String theme = getString(row.getCell(2));
                        String subTheme = getString(row.getCell(3));

                        double approvedBudget = getDouble(row.getCell(4));
                        double expenditure = getDouble(row.getCell(5));

                        int target = (int) getDouble(row.getCell(6));
                        int achievement = (int) getDouble(row.getCell(7));

                        String startDate = getString(row.getCell(8));
                        String endDate = getString(row.getCell(9));

                        String status = getString(row.getCell(10));
                        String responsibleEmail = getString(row.getCell(11));

                        FirestoreActivity activity = new FirestoreActivity();

                        activity.setProjectName(projectName);
                        activity.setActivityName(activityName);
                        activity.setTheme(theme);
                        activity.setSubTheme(subTheme);
                        activity.setApprovedBudget(approvedBudget);
                        activity.setExpenditure(expenditure);
                        activity.setTarget(target);
                        activity.setAchievement(achievement);
                        activity.setStartDate(startDate);
                        activity.setEndDate(endDate);
                        activity.setStatus(status);
                        activity.setResponsibleEmail(responsibleEmail);

                        activityService.save(activity);

                        // Analytics Counters
                        totalBudget += approvedBudget;
                        totalExpenditure += expenditure;

                        if (!theme.isBlank()) {
                            themeCount.merge(theme, 1, Integer::sum);
                        }

                        if (!subTheme.isBlank()) {
                            subThemeCount.merge(subTheme, 1, Integer::sum);
                        }

                        // Email notification
                        if (!responsibleEmail.isBlank()) {
                            emailService.sendEmail(
                                    responsibleEmail,
                                    "New Task Assigned",
                                    "Project: " + projectName +
                                    "\nActivity: " + activityName +
                                    "\nStart Date: " + startDate +
                                    "\nEnd Date: " + endDate
                            );
                        }

                        success++;

                    } catch (Exception rowError) {

                        failed++;
                        errors.add("Sheet: " + sheet.getSheetName() +
                                   " Row: " + (i + 1) +
                                   " Error: " + rowError.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Excel Import Failed", e);
        }

        return new ExcelImportResult(
                success,
                failed,
                totalBudget,
                totalExpenditure,
                themeCount,
                subThemeCount,
                errors
        );
    }

    // ------------------------------
    // Helper Methods
    // ------------------------------

    private String getString(Cell cell) {

        if (cell == null) return "";

        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }

    private double getDouble(Cell cell) {

        try {
            String value = getString(cell);
            if (value.isBlank()) return 0;
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0;
        }
    }
}