package com.automwrite.assessment.service;


import com.automwrite.assessment.enums.Tone;

import java.util.concurrent.CompletableFuture;

public interface ToneExtractionService<DOCTYPE> {

    public Tone extractTone(DOCTYPE doc);

    public CompletableFuture<Tone> asyncExtractTone(DOCTYPE doc);
}
