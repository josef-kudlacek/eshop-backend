package cz.jkdabing.backend.entity;

import cz.jkdabing.backend.enums.AudioFormatType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "audio_files")
public class AudioFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID audioFileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AudioFormatType audioFormatType;

    @Column(nullable = false)
    private long length;

    @Column(nullable = false)
    private Integer bitrate;

    private Integer trackNumber;

    @Column(nullable = false)
    private boolean isSample;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private Long size;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    @Column(nullable = false)
    private ZonedDateTime uploadDate;

    @PrePersist
    protected void onCreate() {
        this.uploadDate = ZonedDateTime.now();
    }
}
