package com.automwrite.assessment.service.impl;

import com.automwrite.assessment.enums.Tone;
import com.automwrite.assessment.service.ToneExtractionService;
import com.automwrite.assessment.strategies.toneapplication.ToneExtractionStrategy;
import com.automwrite.assessment.utils.DocUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class XWPFToneExtractionService implements ToneExtractionService<XWPFDocument> {

    private final int maxWords;
    private final ToneExtractionStrategy toneExtractionStrategy;

    public XWPFToneExtractionService(
            @Value("${application.extract.tone.max.words:400}") int maxWords,
            ToneExtractionStrategy toneExtractionStrategy
    ) {
        this.maxWords = maxWords;
        this.toneExtractionStrategy = toneExtractionStrategy;
    }

    @Override
    public Tone extractTone(XWPFDocument doc) {
        String promptForExtraction = extractSentencesForToneExtraction(doc);
        return toneExtractionStrategy.extractTone(promptForExtraction);
    }

    @Override
    public CompletableFuture<Tone> asyncExtractTone(XWPFDocument doc) {
        return CompletableFuture.supplyAsync(() -> extractTone(doc));
    }

    private String extractSentencesForToneExtraction(XWPFDocument doc){
        return DocUtils.extractSentencesWithMinWords(doc, maxWords);
    }

}
