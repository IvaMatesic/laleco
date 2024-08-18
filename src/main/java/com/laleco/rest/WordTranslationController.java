package com.laleco.rest;

import com.laleco.dto.LessonRequestDto;
import com.laleco.model.WordTranslation;
import com.laleco.service.WordTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/translation")
public class WordTranslationController {

    private final WordTranslationService wordTranslationService;

    @GetMapping()
    public List<WordTranslation> findAllTranslations() {
        return wordTranslationService.getWordTranslations();
    }

    @PostMapping("/create/default-translations")
    public void createDefaultTranslations() {
        wordTranslationService.createDefaultWordTranslations();
    }

    @PostMapping("/create/bulk")
    public ResponseEntity<String> createWordTranslations(@RequestBody LessonRequestDto lessonRequestDto) {
        wordTranslationService.createWordTranslations(lessonRequestDto);
        return ResponseEntity.ok("Word translations created successfully");
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAllWordTranslations() {
        wordTranslationService.deleteAllWordTranslations();
        return ResponseEntity.ok("Word translations deleted successfully");
    }
}
