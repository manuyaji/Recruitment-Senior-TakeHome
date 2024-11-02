package com.automwrite.assessment.service.impl;

import com.automwrite.assessment.enums.Tone;
import com.automwrite.assessment.exceptions.AutomRuntimeException;
import com.automwrite.assessment.service.ToneApplicationService;
import com.automwrite.assessment.strategies.toneapplication.TextToneConversionValidityStrategy;
import com.automwrite.assessment.strategies.toneapplication.ToneConversionStrategy;
import com.automwrite.assessment.strategies.toneapplication.ToneExtractionStrategy;
import com.automwrite.assessment.utils.DocUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class XWPFToneApplicationService implements ToneApplicationService<XWPFDocument> {

    private final int maxWords;
    private final ToneExtractionStrategy toneExtractionStrategy;
    private final ToneConversionStrategy toneConversionStrategy;
    private final TextToneConversionValidityStrategy textToneConversionValidityStrategy;

    public XWPFToneApplicationService(
            @Value("${application.extract.tone.max.words:400}") int maxWords,
            ToneExtractionStrategy toneExtractionStrategy,
            ToneConversionStrategy toneConversionStrategy,
            TextToneConversionValidityStrategy textToneConversionValidityStrategy
    ) {
        this.maxWords = maxWords;
        this.toneExtractionStrategy = toneExtractionStrategy;
        this.toneConversionStrategy = toneConversionStrategy;
        this.textToneConversionValidityStrategy = textToneConversionValidityStrategy;
    }

    private File createOutputFile(String filename) throws IOException {
        File file = new File(filename);
        if(file.exists()){
            log.error("Could not create output file {} as it already exists.", filename);
            throw new FileAlreadyExistsException(filename);
        }
        if(file.createNewFile()){
            return file;
        } else{
            log.error("Could not create new Output File {}", filename);
            throw new AutomRuntimeException("Something went wrong!", null);
        }
    }

    @Override
    public void applyTone(String newDocName, XWPFDocument doc, Tone newTone) {
        File outFile = new File(newDocName);
        XWPFDocument outDoc = new XWPFDocument();
        String promptForExtraction = extractSentencesForToneExtraction(doc);
        Tone currentTone = toneExtractionStrategy.extractTone(promptForExtraction);
        for (int bodyElementPos = 0; bodyElementPos < doc.getBodyElements().size(); bodyElementPos++){
            IBodyElement bodyElement = doc.getBodyElements().get(bodyElementPos);
            if(bodyElement.getElementType() == BodyElementType.PARAGRAPH){
                int paragraphPos = doc.getParagraphPos(bodyElementPos);
                XWPFParagraph paragraph = doc.getParagraphArray(paragraphPos);
                String text = paragraph.getText();
                String newText = text;
                if(textToneConversionValidityStrategy.shouldToneOfTextBeConverted(text)) {
                    newText = toneConversionStrategy.convertTone(
                            text, currentTone, newTone);
                }
                XWPFParagraph newParagraph = outDoc.createParagraph();
                XWPFRun newRun = newParagraph.createRun();
                newRun.setText(newText);
                DocUtils.copyParagraphProperties(paragraph, newParagraph, doc, outDoc);
            } else if (bodyElement.getElementType() == BodyElementType.TABLE) {
                int tablePos = doc.getTablePos(bodyElementPos);
                XWPFTable currentTable = doc.getTableArray(tablePos);
                XWPFTable newTable = outDoc.createTable();
                DocUtils.copyTable(currentTable, newTable);
            } else if (bodyElement.getElementType() == BodyElementType.CONTENTCONTROL) {
                // I couldn't find a way to copy SDT to the new document.
                // So, ignoring this.
                log.warn("Couldn't copy SDT element to the new document {}", newDocName);
            }
        }
        try {
            outDoc.write(new FileOutputStream(new File(newDocName)));
        } catch (IOException f){
            log.error("Encountered error while creating output document.", f);
            throw new AutomRuntimeException("Something went wrong!", f);
        }
    }

    private String extractSentencesForToneExtraction(XWPFDocument doc){
        return DocUtils.extractSentencesWithMinWords(doc, maxWords);
    }

    @Override
    public CompletableFuture<Void> asyncApplyTone(String newDocName, XWPFDocument doc, Tone newTone) {
        return CompletableFuture.supplyAsync(() -> {
            applyTone(newDocName, doc, newTone);
            return null;
        });
    }
}
