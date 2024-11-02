package com.automwrite.assessment.strategies.toneapplication;

import com.automwrite.assessment.enums.Tone;

public interface ToneConversionStrategy {

    public String convertTone(String text, Tone newTone);
    public String convertTone(String text, Tone currentTone, Tone newTone);
}
