package com.automwrite.assessment.strategies.toneapplication;

import com.automwrite.assessment.enums.Tone;

import java.util.concurrent.CompletableFuture;

public interface ToneExtractionStrategy {

    public Tone extractTone(String text);

    public CompletableFuture<Tone> asyncExtractTone(String text);
}
