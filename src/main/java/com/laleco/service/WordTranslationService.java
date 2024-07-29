package com.laleco.service;

import com.laleco.model.WordTranslation;
import com.laleco.repository.WordTranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class WordTranslationService {

    @Autowired
    private WordTranslationRepository wordTranslationRepository;

    public List<WordTranslation> getWordTranslations() {
        return wordTranslationRepository.findAll();
    }

    public void createWordTranslations() {
        List<WordTranslation> words = new LinkedList<>();
        words.add(WordTranslation.builder().word("german").translation("english").build());
        words.add(WordTranslation.builder().word("german2").translation("english2").build());
        words.add(WordTranslation.builder().word("german3").translation("english3").build());
        wordTranslationRepository.saveAll(words);
    }
}
