package com.example.iosnotificationpushy.service.impl;

import com.example.iosnotificationpushy.exception.NotFoundException;
import com.example.iosnotificationpushy.model.DeviceToken;
import com.example.iosnotificationpushy.model.dto.DeviceTokenDto;
import com.example.iosnotificationpushy.model.request.DeviceTokenRequest;
import com.example.iosnotificationpushy.model.request.IOSNotificationRequest;
import com.example.iosnotificationpushy.repository.DeviceTokenRepository;
import com.example.iosnotificationpushy.service.IOSNotificationService;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.auth.ApnsSigningKey;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.concurrent.PushNotificationFuture;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
public class IOSNotificationServiceImpl implements IOSNotificationService {

    private final DeviceTokenRepository deviceTokenRepository;

    @Override
    public String pushIOSNotification(IOSNotificationRequest iosNotificationRequest, UUID userId) throws IOException, NoSuchAlgorithmException, InvalidKeyException, ExecutionException, InterruptedException {
        String host = "api.sandbox.push.apple.com";
        String keyID = "SU6U32YB7V";
        String teamID = "62EMNW6G6D";
        String bundleID = "com.kshrd.PushNotificationUsingAPNs";
        String deviceToken = getDeviceToken(userId).getDeviceToken();
        ApnsClient APNS_CLIENT = new ApnsClientBuilder().setApnsServer(host).setSigningKey(
                        ApnsSigningKey.loadFromInputStream(new FileInputStream("/Users/macbookpro/Documents/Advance_Course/FinTrack/API/ios-notification-pushy/src/main/resources/key/AuthKey_SU6U32YB7V.p8"), teamID, keyID))
                .build();
        ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
        payloadBuilder.setAlertBody(iosNotificationRequest.getTitle());
        payloadBuilder.setAlertTitle(iosNotificationRequest.getBody());
        payloadBuilder.setSound("bingbong.aiff");

        String payload = payloadBuilder.buildWithDefaultMaximumLength();

        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(deviceToken, bundleID, payload);

        PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = APNS_CLIENT.sendNotification(pushNotification);
        PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get();
        if (!pushNotificationResponse.isAccepted()) {
            System.err.println("Notification rejected by the APNs gateway: " + pushNotificationResponse.getRejectionReason());
        }
        return "Successful";
    }

    @Override
    public String saveDeviceToken(DeviceTokenRequest deviceToken) {
        deviceTokenRepository.save(deviceToken.toEntity());
        return "Successful";
    }

    @Override
    public DeviceTokenDto getDeviceToken(UUID userId) {
        return deviceTokenRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Device not found")).toDto();
    }
}
