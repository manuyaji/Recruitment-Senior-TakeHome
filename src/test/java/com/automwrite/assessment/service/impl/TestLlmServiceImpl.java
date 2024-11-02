package com.automwrite.assessment.service.impl;

import com.automwrite.assessment.enums.Tone;
import com.automwrite.assessment.service.LlmService;
import com.automwrite.assessment.strategies.toneapplication.impl.ClaudeToneExtractionStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestLlmServiceImpl {

    @Autowired
    LlmService llmService;

    @Autowired
    ObjectMapper objectMapper;

    String getCasualText(){
        String inputText = "It was great seeing you and Sheryl last week. I’m sending you this letter to let you know what’s going on with your finances.\n" +
                "When we saw each other at your BBQ, you said you were most interested about:\n" +
                "•\tA summary on how your money is performing with us right now\n" +
                "•\tA bit more information about the risk you’re taking with your investments\n" +
                "•\tA review of your existing holdings and savings\n" +
                "•\tPotentially a recommendation if there appears to be anything outstanding or appropriate given your circumstances.\n" +
                "Before I proceed, let me explain how this letter works. It’s meant to act as a guide in the advice I give you. " +
                "It may seem lengthy but the idea is that it is thorough to make sure your assets have been in fact carefully reviewed. " +
                "If you feel like you need anything better explain, message me and we can chat. \n";
        return inputText;
    }

    String getFormalText(){
        String inputText = "Dear Bob,\n" +
                "I trust you are well. It is with pleasure that I write to provide you with a review of your financial assets. \n" +
                "When we first met you informed me that you were keen to consider the following:\n" +
                "•\tA summary of your circumstances and likely future requirements\n" +
                "•\tInformation about risk and how we ascertain the most appropriate recommendations for you\n" +
                "•\tA review of your existing holdings and savings\n" +
                "•\tA proposal based upon the information you have given us which includes a specific recommendation as to products\n" +
                "\n" +
                "Firstly, permit me to explain the nature of this letter, it is intended to act as a guide and reference to my advice to you. While this letter is certainly detailed, it has been written with care to ensure that no stone is left unturned in a thorough examination of your finances. If any element of this letter requires clarification, please contact me at your earliest convenience so that we may review together. \n" +
                "Before providing a more in-depth review of your assets, here is a summary of my financial advice to you contained within this letter\n";
        return inputText;
    }

    String getGrandiloquentText(){
        String inputText = "Dearest Bob,\n" +
                "I trust you are well. My team and I wish that you and Sheryl are having a lovely Autumn. It is with tremendous pleasure that I write to provide you with a review of your well-performing financial assets. \n" +
                "When we first met we discussed at great length your wishes for your financial objectives, the big picture if you will for your finances over the next 20 years. You were strongly minded to consider the following:\n" +
                "•\tA “big picture” overview of your circumstances and vista into your future requirements\n" +
                "•\tA deep dive into your risk profile, how we propagate risk within our portfolios and how we carefully ascertain the most suitable home for your investments. \n" +
                "•\tAn intricate review of your existing assets and savings\n" +
                "•\tA proposal based upon the information you have given us which includes a specific recommendation as to products\n" +
                "\n" +
                "Firstly, permit me to explain the nature of this letter, it is intended to act as a guide and reference to my advice to you. While this letter is certainly detailed, it has been written with care to ensure that no stone is left unturned in a thorough examination of your finances. Allow me to iterate how it is my team’s first and utmost priority to ensure that you receive the highest quality advice and we are deeply endeavored to this, as such should you require any clarification please do contact me or my team at your earliest convenience so that we may assist.  \n" +
                "Before providing a more in-depth review of your assets, here is a summary of my financial advice to you contained within this letter\n";
        return inputText;
    }

    void testExtraction(String inputText, Tone expectedTone){
        String extractionPrompt = String.format(ClaudeToneExtractionStrategy.TONE_EXTRACTION_PROMPT_TEMPLATE,
                StringUtils.joinWith(",", Tone.getTones()), inputText);
        String output = llmService.generateText(extractionPrompt);
        System.out.println(output);
        ClaudeToneExtractionStrategy.ToneExtractionLlmOutput out = null;
        try {
            out = objectMapper.readValue(output, ClaudeToneExtractionStrategy.ToneExtractionLlmOutput.class);
        } catch (JsonProcessingException j) {
            System.out.println(String.format("ERROR! JSONProcessingException while parsing %s", output));
            j.printStackTrace();
        }
        Assertions.assertEquals(Tone.fromTone(out.getTone()), expectedTone);
    }


    @Test
    void testExtractionPromptForCasual() {
        String inputText = getCasualText();
        testExtraction(inputText, Tone.CASUAL);
    }

    @Test
    void testExtractionPromptForFormal() {
        String inputText = getFormalText();
        testExtraction(inputText, Tone.FORMAL);
    }

    @Test
    void testExtractionPromptForGrandiloquent() {
        String inputText = getGrandiloquentText();
        testExtraction(inputText, Tone.GRANDILOQUENT);
    }


}
