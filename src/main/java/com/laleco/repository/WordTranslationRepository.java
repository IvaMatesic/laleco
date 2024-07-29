package com.laleco.repository;

import com.laleco.model.WordTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordTranslationRepository extends JpaRepository<WordTranslation, Long> {
}
