package com.automwrite.assessment.service.impl;

import com.automwrite.assessment.enums.Tone;
import com.automwrite.assessment.service.ToneExampleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

@SpringBootTest
public class TestDefaultToneExampleService {

    @Autowired
    private ToneExampleService toneExampleService;

    private static Stream<Arguments> testCases(){
        return Stream.of(
                Arguments.of(Tone.CASUAL, "<p></p><p>Mr. B. Builder</p><p>21 Paved driveway</p><p>Hedge end</p><p>Hampshire</p><p>PO5 2SF</p><p></p><p>Date</p><p></p><p>RE: Your recommendation letter</p><p>Hi Bob,</p><p>It was great seeing you and Sheryl last week. I’m sending you this letter to let you know what’s going on with your finances.</p><p>When we saw each other at your BBQ, you said you were most interested about:</p><p>A summary on how your money is performing with us right now</p><p>A bit more information about the risk you’re taking with your investments</p><p>A review of your existing holdings and savings</p><p>Potentially a recommendation if there appears to be anything outstanding or appropriate given your circumstances.</p><p>Before I proceed, let me explain how this letter works. It’s meant to act as a guide in the advice I give you. It may seem lengthy but the idea is that it is thorough to make sure your assets have been in fact carefully reviewed. If you feel like you need anything better explain, message me and we can chat. </p><p>Here is a summary of my advice to you contained in this letter:</p><p></p><p>It is important to understand the context in which my advice is given, the “big picture” and your long-term view on your investments is paramount, as such here is an overview of your overall finances:</p><p>Protection:</p><p>Investments:</p><p>Cash:</p><p>Retirement </p><p></p><p>I suggest that once we have completed the work outlined in this letter that we meet again to discuss the outstanding items listed above."),
                Arguments.of(Tone.FORMAL, "<p></p><p>Mr. B. Builder</p><p>21 Paved driveway</p><p>Hedge end</p><p>Hampshire</p><p>PO5 2SF</p><p></p><p>Date</p><p></p><p>RE: Your recommendation letter</p><p>Dear Bob,</p><p>I trust you are well. It is with pleasure that I write to provide you with a review of your financial assets. </p><p>When we first met you informed me that you were keen to consider the following:</p><p>A summary of your circumstances and likely future requirements</p><p>Information about risk and how we ascertain the most appropriate recommendations for you</p><p>A review of your existing holdings and savings</p><p>A proposal based upon the information you have given us which includes a specific recommendation as to products</p><p></p><p>Firstly, permit me to explain the nature of this letter, it is intended to act as a guide and reference to my advice to you. While this letter is certainly detailed, it has been written with care to ensure that no stone is left unturned in a thorough examination of your finances. If any element of this letter requires clarification, please contact me at your earliest convenience so that we may review together. </p><p>Before providing a more in-depth review of your assets, here is a summary of my financial advice to you contained within this letter:</p><p></p><p></p><p></p><p>It is important to understand the context in which my advice is given, the “big picture” and your long-term view on your investments is paramount, as such here is an overview of your overall finances:</p><p>Protection:</p><p>Investments:</p><p>Cash:</p><p>Retirement </p><p></p><p>I suggest that once we have completed the work outlined in this letter that we meet again to discuss the outstanding items listed above."),
                Arguments.of(Tone.GRANDILOQUENT, "<p></p><p>Mr. B. Builder</p><p>21 Paved driveway</p><p>Hedge end</p><p>Hampshire</p><p>PO5 2SF</p><p></p><p>Date</p><p></p><p>RE: Your recommendation letter</p><p>Dearest Bob,</p><p>I trust you are well. My team and I wish that you and Sheryl are having a lovely Autumn. It is with tremendous pleasure that I write to provide you with a review of your well-performing financial assets. </p><p>When we first met we discussed at great length your wishes for your financial objectives, the big picture if you will for your finances over the next 20 years. You were strongly minded to consider the following:</p><p>A “big picture” overview of your circumstances and vista into your future requirements</p><p>A deep dive into your risk profile, how we propagate risk within our portfolios and how we carefully ascertain the most suitable home for your investments. </p><p>An intricate review of your existing assets and savings</p><p>A proposal based upon the information you have given us which includes a specific recommendation as to products</p><p></p><p>Firstly, permit me to explain the nature of this letter, it is intended to act as a guide and reference to my advice to you. While this letter is certainly detailed, it has been written with care to ensure that no stone is left unturned in a thorough examination of your finances. Allow me to iterate how it is my team’s first and utmost priority to ensure that you receive the highest quality advice and we are deeply endeavored to this, as such should you require any clarification please do contact me or my team at your earliest convenience so that we may assist.")
        );
    }


    @MethodSource("testCases")
    @ParameterizedTest
    public void testGetExampleForTone(Tone tone, String exampleExpected){
        String printed = toneExampleService.getExampleForTone(tone, 200);
        System.out.println("=====");
        System.out.println(tone.getTone());
        System.out.println(printed);
        System.out.println("=====");
        Assertions.assertTrue(toneExampleService.getExampleForTone(tone, 200).equals(exampleExpected));
    }
}
