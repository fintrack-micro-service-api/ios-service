package com.example.iosnotificationpushy.model;

import com.example.iosnotificationpushy.model.dto.DeviceTokenDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID userId;
    private String deviceToken;

    public DeviceTokenDto toDto(){
        return new DeviceTokenDto(this.id, this.userId, this.deviceToken);
    }
}
