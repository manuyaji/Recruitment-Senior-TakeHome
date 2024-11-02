package com.automwrite.assessment.strategies.toneapplication.impl;

import com.automwrite.assessment.strategies.toneapplication.TextToneConversionValidityStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/*
A simple strategy which prevents conversion of text if sentence/paragraph length is lesser than 4
 */
@Component
public class DefaultTextToneToneConversionValidityStrategy implements TextToneConversionValidityStrategy {

    @Override
    public boolean shouldToneOfTextBeConverted(String text) {
        if(StringUtils.isBlank(text)){
            return false;
        }
        String[] sentences = text.split("(?<=\\.|\\?|!)"); // Split by sentence-ending punctuation
        String[] words = text.split("\\s+");
        return sentences.length > 2 || words.length > 4;
    }
}
