package com.laleco.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private boolean hard;

    @Column(name = "review_interval")
    private int interval = 2;

    private LocalDateTime lastReviewed;

    private LocalDateTime nextReview;

}

