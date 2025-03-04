package com.laleco.service;

import com.laleco.dto.LessonDto;
import com.laleco.dto.LessonRequestDto;
import com.laleco.dto.UpdateReviewDto;
import com.laleco.dto.WordTranslationHardUpdateDto;
import com.laleco.model.Lesson;
import com.laleco.model.WordTranslation;
import com.laleco.repository.LessonRepository;
import com.laleco.repository.WordTranslationRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WordTranslationService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private WordTranslationRepository wordTranslationRepository;

    public List<WordTranslation> getWordTranslations(String filterBy, Integer numberOfLessons) {
        if ("latestLesson".equalsIgnoreCase(filterBy)) {
            int lessonsToFetch = (numberOfLessons != null && numberOfLessons > 0) ? numberOfLessons : 1;
            return getWordTranslationsForLatestLessons(lessonsToFetch);
        }
        if ("hardWords".equalsIgnoreCase(filterBy)) {
            return wordTranslationRepository.findAllHardWordsRandom();
        }
        return getWordsForReviewShuffled();
    }

    public List<WordTranslation> getWordsForReviewShuffled() {
        LocalDateTime now = LocalDateTime.now();
        List<WordTranslation> wordsForReview = wordTranslationRepository.findByNextReviewBefore(now);
        Collections.shuffle(wordsForReview);
        return wordsForReview;
    }

    private List<WordTranslation> getWordTranslationsForLatestLessons(int numberOfLessons) {
        List<WordTranslation> result = lessonRepository.findAll(
                        PageRequest.of(0, numberOfLessons, Sort.by(Sort.Order.desc("dateCreated")))
                ).stream()
                .flatMap(lesson -> lesson.getWordTranslations().stream())
                .filter(wordTranslation -> wordTranslation.getLastReviewed() == null)
                .collect(Collectors.toList());
        Collections.shuffle(result);
        return result;
    }

    public void deleteAllWordTranslations() {
        wordTranslationRepository.deleteAll();
    }

    public void createDefaultWordTranslations() {
        List<WordTranslation> words = new LinkedList<>();
        words.add(WordTranslation.builder().word("german").translation("english").build());
        words.add(WordTranslation.builder().word("german2").translation("english2").build());
        words.add(WordTranslation.builder().word("german3").translation("english3").build());
        wordTranslationRepository.saveAll(words);
    }

    @Transactional
    public void updateWordsSpacedRepetition(List<UpdateReviewDto> updateReviewDtos) {
        List<WordTranslation> updatedWords = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (UpdateReviewDto dto : updateReviewDtos) {
            WordTranslation word = wordTranslationRepository.findById(dto.getId())
                    .orElse(null);

            if (word == null) {
                System.err.println("Word not found with id: " + dto.getId());
                continue;
            }

            int currentInterval = word.getInterval();
            int newInterval = switch (dto.getDifficulty()) {
                case 4 -> Math.max(1, currentInterval / 4);
                case 3 -> Math.max(1, currentInterval / 2);
                case 2 -> currentInterval;
                case 1 -> currentInterval * 2;
                default -> currentInterval;
            };

            word.setInterval(newInterval);
            word.setLastReviewed(now);
            word.setNextReview(now.plusDays(newInterval));

            updatedWords.add(word);
        }

        wordTranslationRepository.saveAll(updatedWords);
    }


    public LessonDto createWordTranslations(LessonRequestDto lessonRequestDto) {
        List<WordTranslation> wordTranslations;
        String data = lessonRequestDto.getWordTranslationData();

        if (isExcelFormat(data)) {
            wordTranslations = getAllWordTranslationsFromExcelFormat(data);
        } else if (isSeedlangFormat(data)) {
            wordTranslations = getAllWordTranslationsFromSeedlangFormat(data);
        } else if (isEasyGermanCallVocabularyFormat(data)) {
            wordTranslations = getAllWordTranslationsFromEasyGermanCallVocabularyFormat(data);

        } else if (isSeedlangReviewCardFormat(data)) {
            wordTranslations = getAllWordTranslationsFromSeedlangReviewCardFormat(data);

        } else {
            throw new IllegalArgumentException("Unknown data format");
        }

        Lesson savedLesson = saveLesson(lessonRequestDto.getLessonTitle(), lessonRequestDto.getLessonUrl(), wordTranslations);

        return modelMapper.map(savedLesson, LessonDto.class);
    }

    private Lesson saveLesson(String title, String url, List<WordTranslation> wordTranslations) {
        return lessonRepository.save(Lesson.builder()
                .title(title)
                .url(url)
                .dateCreated(LocalDateTime.now())
                .wordTranslations(wordTranslations)
                .build());
    }

    private boolean isExcelFormat(String data) {
        return data.contains("\t");
    }

    private boolean isEasyGermanCallVocabularyFormat(String data) {
        String[] lines = data.split("\n");
        for (String line : lines) {
            if (!line.contains(" - ")) {
                return false;
            }
        }
        return true;
    }

    private boolean isSeedlangFormat(String data) {
        String[] levelIdentifiers = {"A1", "A2", "B1", "B2", "C1", "C2"};
        return Arrays.stream(levelIdentifiers).anyMatch(data::contains);
    }

    private boolean isSeedlangReviewCardFormat(String data) {
        return data.contains("FRONT OF CARD") && data.contains("BACK OF CARD");
    }

    private boolean isLevelIdentifier(String line) {
        return line.matches("^[A-C][1-2]$");
    }

    private boolean isLabelToIgnore(String line) {
        return line.equalsIgnoreCase("colloquial") || line.equalsIgnoreCase("vulgar");
    }

    private List<WordTranslation> getAllWordTranslationsFromExcelFormat(String data) {
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
        return wordTranslations;
    }

    private List<WordTranslation> getAllWordTranslationsFromSeedlangFormat(String data) {
        String[] lines = data.split("\n");
        List<WordTranslation> wordTranslations = new ArrayList<>();
        String currentWord = null;

        for (String line : lines) {
            line = line.trim();

            if (line.isEmpty() || isLevelIdentifier(line) || isLabelToIgnore(line)) {
                continue;
            }

            if (currentWord == null) {
                currentWord = line;
            } else {
                String translation = line;
                WordTranslation wordTranslation = WordTranslation.builder()
                        .word(currentWord)
                        .translation(translation)
                        .build();
                wordTranslations.add(wordTranslation);
                currentWord = null;
            }
        }

        return wordTranslations;
    }

    private List<WordTranslation> getAllWordTranslationsFromEasyGermanCallVocabularyFormat(String data) {
        List<WordTranslation> wordTranslations = new ArrayList<>();

        String[] lines = data.split("\n");
        for (String line : lines) {
            String[] parts = line.split(" - ");
            if (parts.length == 2) {
                String originalWord = parts[0].trim();
                String translation = parts[1].trim();
                WordTranslation wordTranslation = WordTranslation.builder().translation(translation).word(originalWord).build();
                wordTranslations.add(wordTranslation);
            }
        }

        return wordTranslations;
    }

    public List<WordTranslation> getAllWordTranslationsFromSeedlangReviewCardFormat(String input) {
        List<WordTranslation> wordTranslations = new ArrayList<>();

        String[] lines = input.split("\\n");

        WordTranslation currentCard = null;
        boolean isFront = false, isBack = false;

        for (String line : lines) {
            line = line.trim();

            if (line.equalsIgnoreCase("FRONT OF CARD")) {
                if (currentCard != null) {
                    wordTranslations.add(currentCard);
                }
                currentCard = new WordTranslation();
                isFront = true;
                isBack = false;
            } else if (line.equalsIgnoreCase("BACK OF CARD")) {
                isFront = false;
                isBack = true;
            } else if (line.equalsIgnoreCase("CARD TYPE")) {
                if (currentCard != null) {
                    wordTranslations.add(currentCard);
                    currentCard = null;
                }
                isFront = false;
                isBack = false;
            } else if (isFront && currentCard != null) {
                currentCard.setWord(line);
            } else if (isBack && currentCard != null) {
                currentCard.setTranslation(line);
            }
        }

        if (currentCard != null) {
            wordTranslations.add(currentCard);
        }

        return wordTranslations;
    }


    public List<WordTranslationHardUpdateDto> updateHardWords(List<WordTranslationHardUpdateDto> wordUpdates) {
        for (WordTranslationHardUpdateDto updateDTO : wordUpdates) {
            Optional<WordTranslation> wordTranslationOptional = wordTranslationRepository.findById(updateDTO.getId());
            if (wordTranslationOptional.isPresent()) {
                WordTranslation wordTranslation = wordTranslationOptional.get();
                wordTranslation.setHard(updateDTO.isHard());
                wordTranslationRepository.save(wordTranslation);
            }
        }
        return wordUpdates;
    }
}
