package com.MonitoringTool.Services;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.MonitoringTool.dto.ActivityDTO;

@Service
public class ExcelExportService {

    public byte[] exportToExcel(List<ActivityDTO> activities) throws Exception {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Dashboard Report");

            // Header Row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Project Name");
            header.createCell(1).setCellValue("Activity Name");
            header.createCell(2).setCellValue("Start Date");
            header.createCell(3).setCellValue("End Date");
            header.createCell(4).setCellValue("Status");
            header.createCell(5).setCellValue("Responsible");
            header.createCell(6).setCellValue("Manager");
            header.createCell(7).setCellValue("Coordinator");

            int rowIdx = 1;

            for (ActivityDTO a : activities) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(a.getProjectName());
                row.createCell(1).setCellValue(a.getActivityName());
                row.createCell(2).setCellValue(a.getStartDate());
                row.createCell(3).setCellValue(a.getEndDate());
                row.createCell(4).setCellValue(a.getStatus());
                row.createCell(5).setCellValue(a.getResponsibleEmail());
                row.createCell(6).setCellValue(a.getManagerEmail());
                row.createCell(7).setCellValue(a.getCoordinatorEmail());
            }

            workbook.write(out);

            return out.toByteArray();   // ✅ THIS IS IMPORTANT
        }
    }
}
