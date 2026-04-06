package com.MonitoringTool.dto;

import com.MonitoringTool.Entities.ActivityStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusUpdateRequest {

    private ActivityStatus status;
}
