package com.example.iosnotificationpushy.service;

import com.example.iosnotificationpushy.model.dto.DeviceTokenDto;
import com.example.iosnotificationpushy.model.request.DeviceTokenRequest;
import com.example.iosnotificationpushy.model.request.IOSNotificationRequest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface IOSNotificationService {
    String pushIOSNotification(IOSNotificationRequest iosNotificationRequest, UUID userId) throws IOException, NoSuchAlgorithmException, InvalidKeyException, ExecutionException, InterruptedException;

    String saveDeviceToken(DeviceTokenRequest deviceToken);

    DeviceTokenDto getDeviceToken(UUID userId);
}
