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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
public class IOSNotificationServiceImpl implements IOSNotificationService {

    private final DeviceTokenRepository deviceTokenRepository;

    @Override
    public String pushIOSNotification(IOSNotificationRequest iosNotificationRequest, UUID userId) {
        try {
            String host = "api.sandbox.push.apple.com";
            String keyID = "SU6U32YB7V";
            String teamID = "62EMNW6G6D";
            String bundleID = "com.kshrd.PushNotificationUsingAPNs";
            String deviceToken = getDeviceToken(userId).getDeviceToken();
            String filePath = "AuthKey_SU6U32YB7V.p8";
            System.out.println("");
            try (InputStream inputStream = new ClassPathResource(filePath).getInputStream()) {
                ApnsClient APNS_CLIENT = new ApnsClientBuilder()
                        .setApnsServer(host)
                        .setSigningKey(ApnsSigningKey.loadFromInputStream(inputStream, teamID, keyID))
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
                    // Log the rejection reason instead of printing to System.err
//                    log.error("Notification rejected by the APNs gateway: {}", pushNotificationResponse.getRejectionReason());
                }

                return "Successful";
            }
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | ExecutionException | InterruptedException e) {
            // Handle or log the exception appropriately
//            log.error("Error sending iOS notification", e);
            return "Failed";
        }
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
