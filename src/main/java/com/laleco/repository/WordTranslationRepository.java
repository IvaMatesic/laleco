package com.laleco.repository;

import com.laleco.model.WordTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface WordTranslationRepository extends JpaRepository<WordTranslation, Long> {
    @Query("SELECT w FROM WordTranslation w ORDER BY function('RAND')")
    List<WordTranslation> findAllRandom();

    @Query("SELECT w FROM WordTranslation w WHERE w.hard=true ORDER BY function('RAND')")
    List<WordTranslation> findAllHardWordsRandom();

    List<WordTranslation> findByNextReviewBefore(LocalDateTime nextReview);
}
