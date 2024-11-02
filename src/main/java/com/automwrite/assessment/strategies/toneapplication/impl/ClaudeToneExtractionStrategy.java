package com.automwrite.assessment.strategies.toneapplication.impl;

import com.automwrite.assessment.enums.Tone;
import com.automwrite.assessment.exceptions.AutomLLMException;
import com.automwrite.assessment.service.LlmService;
import com.automwrite.assessment.strategies.toneapplication.ToneExtractionStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class ClaudeToneExtractionStrategy implements ToneExtractionStrategy {

    public static final String TONE_EXTRACTION_PROMPT_TEMPLATE = "You are given initial few sentences of a letter. " +
            "You need to identify which tone the letter is written in, amongst the " +
            "given possible list of tones here - `%s`. \n" +
            "Respond as a JSON, with a single key called 'tone' and its value as the identified tone. \n" +
            "Each paragraph may begin with <p> tag and end with </p> tag.\n" +
            "The initial sentences of the letter are as follows - `%s`"
            ;
    private final LlmService llmService;
    private final ObjectMapper objectMapper;

    public ClaudeToneExtractionStrategy(LlmService llmService, ObjectMapper objectMapper){
        this.llmService = llmService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Tone extractTone(String text) {
        String promptForExtraction =
                String.format(TONE_EXTRACTION_PROMPT_TEMPLATE, StringUtils.joinWith(",",Tone.getTones()),
                        text);
        String llmOutput = llmService.generateText(promptForExtraction);
        log.info("LLM Output: {}", llmOutput);
        try {
            ClaudeToneExtractionStrategy.ToneExtractionLlmOutput output = objectMapper.readValue(llmOutput, ClaudeToneExtractionStrategy.ToneExtractionLlmOutput.class);
            return Tone.fromTone(output.getTone());
        } catch (JsonProcessingException e){
            log.error("JSON Processing exception encountered while parsing output [{}]", llmOutput, e);
            throw new AutomLLMException("Something went wrong!", e);
        }
    }

    @Override
    public CompletableFuture<Tone> asyncExtractTone(String text) {
        return CompletableFuture.supplyAsync(() -> extractTone(text));
    }


    @Getter
    @Setter
    public static class ToneExtractionLlmOutput {
        private String tone;
    }
}
