package com.laleco.service;

import com.laleco.model.WordTranslation;
import com.laleco.repository.WordTranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class WordTranslationService {

    @Autowired
    private WordTranslationRepository wordTranslationRepository;

    public List<WordTranslation> getWordTranslations() {
        return wordTranslationRepository.findAll();
    }

    public void createDefaultWordTranslations() {
        List<WordTranslation> words = new LinkedList<>();
        words.add(WordTranslation.builder().word("german").translation("english").build());
        words.add(WordTranslation.builder().word("german2").translation("english2").build());
        words.add(WordTranslation.builder().word("german3").translation("english3").build());
        wordTranslationRepository.saveAll(words);
    }

    public void createWordTranslations(String data) {
        String[] lines = data.split("\n");
        List<WordTranslation> wordTranslations = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\t");
            if (parts.length == 2) {
                String word = parts[0].trim();
                String translation = parts[1].trim();
                WordTranslation wordTranslation = WordTranslation.builder()
                        .word(word)
                        .translation(translation)
                        .build();
                wordTranslations.add(wordTranslation);
            }
        }
        wordTranslationRepository.saveAll(wordTranslations);
    }

    public void deleteAllWordTranslations() {
        wordTranslationRepository.deleteAll();
    }

}
