package com.laleco.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WordTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    private String translation;
    @Setter
    private String exampleSentence;

    private boolean hard;

    @Column(name = "review_interval")
    @Builder.Default
    private int interval = 4;

    private LocalDateTime lastReviewed;

    private LocalDateTime nextReview;

    @PrePersist
    public void prePersist() {
        this.nextReview = LocalDateTime.now();
    }
}

