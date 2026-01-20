package com.dankirent.api.model.photo;

import com.dankirent.api.model.photo.dto.PhotoUpdateDto;
import com.dankirent.api.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "photos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    public Photo(PhotoUpdateDto dto) {
        this.fileName = dto.fileName();
        this.contentType = dto.contentType();
        this.size = dto.size();
        this.createdAt = LocalDateTime.now();
    }
}