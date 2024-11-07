package com.springboot.productclient.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "METADATAEVENT")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetaDataEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String productName;
    @Column(nullable = false)
    private int retryCount;
    @Column(nullable = false)
    private LocalDateTime updatedTime;
}
