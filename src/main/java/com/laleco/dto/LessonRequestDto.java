package com.laleco.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class LessonRequestDto {
    private String lessonTitle;
    private String lessonUrl;
    private String wordTranslationData;
}
