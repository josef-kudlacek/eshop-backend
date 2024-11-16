package cz.jkdabing.backend.entity;

import cz.jkdabing.backend.enums.AudioFormatType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "files")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(nullable = false)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AudioFormatType audioFormatType;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private LocalTime length;

    @Column(nullable = false)
    private Long size;

    private Integer bitrate;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    @Column(nullable = false)
    private ZonedDateTime uploadDate;

    @Column(nullable = false)
    private int sequence;

    @OneToMany(mappedBy = "file")
    private List<UserAccessEntity> accessEntityList = new ArrayList<>();
}
