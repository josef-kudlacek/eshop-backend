package cz.jkdabing.backend.entity;

import cz.jkdabing.backend.enums.AudioFormatType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
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

    @OneToOne
    @JoinColumn(name = "file_id")
    private FileEntity fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AudioFormatType audioFormatType;

    @Column(nullable = false)
    private LocalTime length;

    private Integer bitrate;

    @Column(nullable = false)
    private int sequence;
}
