package com.apirest.JwtAuthService.persistence.entity;

import com.apirest.JwtAuthService.persistence.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder
public class Maintenance{

    @CreatedDate
    @Column(nullable = false, columnDefinition = "timestamp with time zone")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(columnDefinition = "timestamp with time zone")
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String usuReg;

    private String usuMod;

    @Enumerated(value = EnumType.STRING)
    private Status status;
}
