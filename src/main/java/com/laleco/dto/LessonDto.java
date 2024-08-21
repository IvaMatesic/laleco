package com.laleco.dto;

import com.laleco.model.WordTranslation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class LessonDto {
    private Long id;
    private String title;
    private String url;
    private LocalDate dateCreated;
    private List<WordTranslation> wordTranslations = new ArrayList<>();
}
