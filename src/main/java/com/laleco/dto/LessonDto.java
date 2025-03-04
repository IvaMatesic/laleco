package com.laleco.dto;

import com.laleco.model.WordTranslation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private LocalDateTime dateCreated;
    @Builder.Default
    private List<WordTranslation> wordTranslations = new ArrayList<>();
}
