package com.example.iosnotificationpushy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceTokenDto {
    private UUID id;
    private UUID userId;
    private String deviceToken;
}
