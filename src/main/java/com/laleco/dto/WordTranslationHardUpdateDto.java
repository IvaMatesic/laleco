package com.laleco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordTranslationHardUpdateDto {
    private Long id;
    private boolean hard;
}
