package com.example.iosnotificationpushy.controller;

import com.example.iosnotificationpushy.model.request.DeviceTokenRequest;
import com.example.iosnotificationpushy.model.request.IOSNotificationRequest;
import com.example.iosnotificationpushy.service.IOSNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("iosnotifications")
@AllArgsConstructor
public class IOSNotificationController {

    private final IOSNotificationService iosNotificationService;

    @PostMapping("push/user/{userId}")
    public ResponseEntity<?> pushIOSNotification(@RequestBody IOSNotificationRequest iosNotificationRequest, @PathVariable UUID userId) throws IOException, NoSuchAlgorithmException, InvalidKeyException, ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.OK).body(iosNotificationService.pushIOSNotification(iosNotificationRequest, userId));
    }

    @PostMapping("subscribe")
    public ResponseEntity<?> saveDeviceToken(@RequestBody DeviceTokenRequest deviceToken){
        return ResponseEntity.status(HttpStatus.OK).body(iosNotificationService.saveDeviceToken(deviceToken));
    }

    @GetMapping("token/user/{userId}")
    public ResponseEntity<?> getDeviceToken(@PathVariable UUID userId){
        return ResponseEntity.status(HttpStatus.OK).body(iosNotificationService.getDeviceToken(userId));
    }
}
