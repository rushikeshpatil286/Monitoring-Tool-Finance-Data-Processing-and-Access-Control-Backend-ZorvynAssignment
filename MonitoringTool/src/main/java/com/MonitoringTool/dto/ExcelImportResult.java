package com.MonitoringTool.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExcelImportResult {

    private int successCount;
    private int failedCount;

    private double totalBudget;
    private double totalExpenditure;

    private Map<String,Integer> themeDistribution;
    private Map<String,Integer> subThemeDistribution;

    private List<String> errors;
}