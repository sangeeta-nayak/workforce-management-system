package com.railse.workforcemgmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ActivityEvent {
    private Instant timestamp;
    private String description;
}

