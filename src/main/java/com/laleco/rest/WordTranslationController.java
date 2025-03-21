package com.laleco.rest;

import com.laleco.dto.LessonDto;
import com.laleco.dto.LessonRequestDto;
import com.laleco.dto.UpdateReviewDto;
import com.laleco.dto.WordTranslationHardUpdateDto;
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
    public List<WordTranslation> findAllTranslations(@RequestParam(required = false) String filterBy, @RequestParam(required = false)Integer numberOfLessons) {
        return wordTranslationService.getWordTranslations(filterBy, numberOfLessons);
    }

    @PostMapping("/create/default-translations")
    public void createDefaultTranslations() {
        wordTranslationService.createDefaultWordTranslations();
    }

    @PostMapping("/create/bulk")
    public ResponseEntity<LessonDto> createWordTranslations(@RequestBody LessonRequestDto lessonRequestDto) {
        return ResponseEntity.ok(wordTranslationService.createWordTranslations(lessonRequestDto));
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAllWordTranslations() {
        wordTranslationService.deleteAllWordTranslations();
        return ResponseEntity.ok("Word translations deleted successfully");
    }

    @PutMapping("/set-hard")
    public ResponseEntity<List<WordTranslationHardUpdateDto>> setWordsAsHard(@RequestBody List<WordTranslationHardUpdateDto> wordUpdates) {
        return ResponseEntity.ok(this.wordTranslationService.updateHardWords(wordUpdates));
    }

    @PostMapping("/update-review")
    public ResponseEntity<String> updateWordSpacedRepetition(@RequestBody List<UpdateReviewDto> updateReviews) {
        wordTranslationService.updateWordsSpacedRepetition(updateReviews);
        return ResponseEntity.ok("Successfully saved reviews");
    }
}
