package com.example.iosnotificationpushy.model.request;

import com.example.iosnotificationpushy.model.DeviceToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceTokenRequest {
    private UUID userId;
    private String deviceToken;

    public DeviceToken toEntity(){
        return new DeviceToken(null, this.userId, this.deviceToken);
    }
}
