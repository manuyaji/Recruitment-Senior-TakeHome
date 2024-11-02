package com.automwrite.assessment.service.impl;

import com.automwrite.assessment.enums.Tone;
import com.automwrite.assessment.service.ToneExampleService;
import com.automwrite.assessment.utils.DocUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DefaultToneExampleService implements ToneExampleService {

    public static final String CASUAL_DOC_PATH = "src/main/resources/casual-tone.docx";
    public static final String FORMAL_DOC_PATH = "src/main/resources/formal-tone.docx";
    public static final String GRANDILOQUENT_DOC_PATH = "src/main/resources/grandiloquent-tone.docx";


    private Map<Tone, String> exampleToneTextMap;

    @PostConstruct
    public void initialize() throws Exception {
        exampleToneTextMap = new HashMap<>();
        XWPFDocument casualDoc = new XWPFDocument(new FileInputStream(CASUAL_DOC_PATH), true);
        String casualText = DocUtils.getAllTextFromDoc(casualDoc);
        log.info("CASUAL TEXT: {}", casualText);
        exampleToneTextMap.put(Tone.CASUAL, casualText);

        XWPFDocument formalDoc = new XWPFDocument(new FileInputStream(FORMAL_DOC_PATH), true);
        String formalText = DocUtils.getAllTextFromDoc(formalDoc);
        log.info("FORMAL TEXT: {}", formalText);
        exampleToneTextMap.put(Tone.FORMAL, formalText);

        XWPFDocument grandiloquentDoc = new XWPFDocument(new FileInputStream(GRANDILOQUENT_DOC_PATH), true);
        String grandiloquentText = DocUtils.getAllTextFromDoc(grandiloquentDoc);
        log.info("GRANDILOQUENT TEXT: {}", grandiloquentText);
        exampleToneTextMap.put(Tone.GRANDILOQUENT, grandiloquentText);
    }

    @Override
    public String getExampleForTone(Tone tone, int maxWords) {
        String example = DocUtils.getFirstNWordsFromText(exampleToneTextMap.getOrDefault(tone, StringUtils.EMPTY), maxWords);
        return example;
    }
}
