package com.laleco.rest;

import com.laleco.model.WordTranslation;
import com.laleco.service.WordTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/translation")
public class WordTranslationController {

    private final WordTranslationService wordTranslationService;

    @GetMapping()
    public List<WordTranslation> findAllTranslations() {
        return wordTranslationService.getWordTranslations();
    }

    @PostMapping("/create/default-translations")
    public void createDefaultTranslations() {
        wordTranslationService.createWordTranslations();
    }
}
