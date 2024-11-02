package com.automwrite.assessment.service.impl;

import com.automwrite.assessment.enums.Tone;
import com.automwrite.assessment.service.ToneApplicationService;
import com.automwrite.assessment.strategies.toneapplication.TextToneConversionValidityStrategy;
import com.automwrite.assessment.strategies.toneapplication.ToneConversionStrategy;
import com.automwrite.assessment.strategies.toneapplication.ToneExtractionStrategy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@SpringBootTest
public class TestXWPFToneApplicationService {

    @Autowired
    ToneApplicationService<XWPFDocument> toneApplicationService;

    @MockBean
    ToneExtractionStrategy toneExtractionStrategy;

    @MockBean
    ToneConversionStrategy toneConversionStrategy;

    public static void deleteAllGeneratedFiles() throws IOException{
        Files.deleteIfExists(Path.of("grandiloquent-to-casual.docx"));
        Files.deleteIfExists(Path.of("grandiloquent-to-formal.docx"));
        Files.deleteIfExists(Path.of("casual-to-formal.docx"));
        Files.deleteIfExists(Path.of("casual-to-grandiloquent.docx"));
        Files.deleteIfExists(Path.of("formal-to-grandiloquent.docx"));
        Files.deleteIfExists(Path.of("formal-to-casual.docx"));
    }
    @BeforeAll
    public static void beforeAll() throws IOException{
        deleteAllGeneratedFiles();
    }

    @AfterAll
    public static void afterAll() throws IOException{
        deleteAllGeneratedFiles();
    }

    private static XWPFDocument getGrandiloquentDoc() throws FileNotFoundException, IOException {
        return new XWPFDocument(new FileInputStream(new File("src/test/resources/grandiloquent-tone.docx")), true);
    }

    private static XWPFDocument getFormalDoc() throws FileNotFoundException, IOException {
        return new XWPFDocument(new FileInputStream(new File("src/test/resources/formal-tone.docx")), true);
    }

    private static XWPFDocument getCasualDoc() throws FileNotFoundException, IOException {
        return new XWPFDocument(new FileInputStream(new File("src/test/resources/casual-tone.docx")), true);
    }

    public static Stream<Arguments> testCases() throws IOException{
        return Stream.of(
                Arguments.of("grandiloquent-to-casual.docx", getGrandiloquentDoc(), Tone.CASUAL, Tone.GRANDILOQUENT),
                Arguments.of("formal-to-casual.docx", getFormalDoc(), Tone.CASUAL, Tone.FORMAL),
                Arguments.of("grandiloquent-to-formal.docx", getGrandiloquentDoc(), Tone.FORMAL, Tone.GRANDILOQUENT),
                Arguments.of("casual-to-formal.docx", getCasualDoc(), Tone.FORMAL, Tone.CASUAL),
                Arguments.of("casual-to-grandiloquent.docx", getCasualDoc(), Tone.GRANDILOQUENT, Tone.CASUAL),
                Arguments.of("formal-to-grandiloquent.docx", getFormalDoc(), Tone.GRANDILOQUENT, Tone.FORMAL)
        );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    public void testApplyTone(String newDocName, XWPFDocument doc, Tone newTone, Tone currentTone){
        //Assertions.assertTrue(new File(newDocName).exists());
        when(toneExtractionStrategy.extractTone(anyString())).thenReturn(currentTone);
        when(toneConversionStrategy.convertTone(anyString(), eq(currentTone), eq(newTone))).thenReturn("Dummy Test Value");
        toneApplicationService.applyTone(newDocName, doc, newTone);
        verify(toneExtractionStrategy, atLeastOnce()).extractTone(anyString());
        verify(toneConversionStrategy, atLeastOnce()).convertTone(anyString(), eq(currentTone), eq(newTone));
    }
}
