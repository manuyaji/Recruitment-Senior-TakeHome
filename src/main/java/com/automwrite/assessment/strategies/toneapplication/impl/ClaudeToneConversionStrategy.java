package com.automwrite.assessment.strategies.toneapplication.impl;

import com.automwrite.assessment.enums.Tone;
import com.automwrite.assessment.exceptions.AutomLLMException;
import com.automwrite.assessment.service.LlmService;
import com.automwrite.assessment.service.ToneExampleService;
import com.automwrite.assessment.strategies.toneapplication.ToneConversionStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClaudeToneConversionStrategy implements ToneConversionStrategy {

    public final String CONVERSION_PROMPT = "You have to convert a part of a letter which is in %s tone into %s tone. " +
            "Provide output in a JSON format, with key as 'output' and the value as the converted part of the letter." +
            "Do not make any assumptions on name, title, or the content, and only convert the sentence to the required tone " +
            "without any change in the meaning or fact.\n" +
            "Each paragraph may begin with <p> tag and end with </p> tag.\n" +
            "An example letter for %s tone is as follows\n ```\n%s\n```\n" +
            "An example letter for %s tone is as follows\n ```\n%s\n```\n" +
            "The part of the letter to be converted is \n```%s\n```"
            ;

    private final LlmService llmService;
    private final ToneExampleService toneExampleService;
    private final ObjectMapper objectMapper;
    public ClaudeToneConversionStrategy(
            LlmService llmService,
            ToneExampleService toneExampleService,
            ObjectMapper objectMapper
    ) {
        this.llmService = llmService;
        this.toneExampleService = toneExampleService;
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertTone(String text, Tone newTone) {
        throw new UnsupportedOperationException("This method is not implemented.");
    }

    @Override
    public String convertTone(String text, Tone currentTone, Tone newTone) {
        String toneExampleForCurrentTone = toneExampleService.getExampleForTone(currentTone, 200);
        String toneExampleForNewTone = toneExampleService.getExampleForTone(newTone, 200);
        String conversionPrompt = String.format(CONVERSION_PROMPT, currentTone.getTone(),
                newTone.getTone(), currentTone.getTone(), toneExampleForCurrentTone,
                newTone.getTone(), toneExampleForNewTone, text);
        log.info("Prompt: {}", conversionPrompt);
        String output = llmService.generateText(conversionPrompt);
        log.info("Output from LLM: {}", output);
        try{
            ClaudeToneConversionStrategy.LlmConversionOutput convertedOutput = objectMapper.readValue(output, ClaudeToneConversionStrategy.LlmConversionOutput.class);
            return convertedOutput.getOutput();
        } catch(JsonProcessingException j){
            log.error("JsonProcessingException while parsing output {}", output, j);
            throw new AutomLLMException("Something went wrong!", j);
        }
    }

    @Getter
    @Setter
    public static class LlmConversionOutput {
        private String output;
    }
}
