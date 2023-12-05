package com.example.iosnotificationpushy.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IOSNotificationRequest {
    private String title;
    private String body;
}
