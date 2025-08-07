package com.railse.workforcemgmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class Comment {
    private Instant timestamp;
    private String author;
    private String text;
}

