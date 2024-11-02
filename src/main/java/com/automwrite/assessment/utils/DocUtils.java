package com.automwrite.assessment.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;

import java.math.BigInteger;

@Slf4j
public class DocUtils {

    private static final String PARAGRAPH_BEGIN = "<p>";
    private static final String PARAGRAPH_END = "</p>";

    public static String extractSentencesWithMinWords(XWPFDocument document, int n) {
        StringBuilder sb = new StringBuilder();
        if(ObjectUtils.anyNull(document, document.getParagraphs())){
            log.error("Empty Document: Cannot extract sentences.");
            throw new RuntimeException("Empty Document: Cannot extract sentences.");
        }
        int wordCount = 0;
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            sb = sb.append(PARAGRAPH_BEGIN);
            String[] sentences = paragraph.getText().split("(?<=\\.|\\?|!)"); // Split by sentence-ending punctuation

            for (String sentence : sentences) {
                String[] words = sentence.trim().split("\\s+"); // Split sentence into words
                wordCount += words.length;
                sb = sb.append(sentence);

                if (wordCount >= n) {
                    sb.append(PARAGRAPH_END);
                    return sb.toString();
                }
            }
        }
        sb.append(PARAGRAPH_END);
        return sb.toString();
    }

    public static String getAllTextFromDoc(XWPFDocument document){
        StringBuilder sb = new StringBuilder();
        if(ObjectUtils.anyNull(document, document.getParagraphs())){
            return StringUtils.EMPTY;
        }
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            sb = sb.append(PARAGRAPH_BEGIN);
            sb = sb.append(paragraph.getText());
            sb = sb.append(PARAGRAPH_END);
        }
        return sb.toString();
    }

    public static String getFirstNWordsFromText(String text, int n){
        StringBuilder sb = new StringBuilder();
        int wordCount = 0;
        String[] sentences = text.split("(?<=\\.|\\?|!)"); // Split by sentence-ending punctuation

        for (String sentence : sentences) {
            String[] words = sentence.trim().split("\\s+"); // Split sentence into words
            wordCount += words.length;
            sb = sb.append(sentence);

            if (wordCount >= n) {
                break;
            }
        }
        return sb.toString();
    }

    public static void copyTable(XWPFTable sourceTable, XWPFTable destTable){
        // Copy the table structure
        destTable.getCTTbl().setTblPr(sourceTable.getCTTbl().getTblPr());
        destTable.getCTTbl().setTblGrid(sourceTable.getCTTbl().getTblGrid());

        // Copy each row and cell
        for (int i = 0; i < sourceTable.getNumberOfRows(); i++) {
            XWPFTableRow currentRow = sourceTable.getRow(i);
            XWPFTableRow newRow = destTable.createRow();

            for (int j = 0; j < currentRow.getTableCells().size(); j++) {
                XWPFTableCell currentCell = currentRow.getCell(j);
                XWPFTableCell newCell = newRow.createCell();

                // Copy cell properties and paragraphs
                newCell.getCTTc().setTcPr(currentCell.getCTTc().getTcPr());
                newCell.setText(currentCell.getText());
            }
        }

    }

    private static boolean isPreviousParagraphANumberedBulletPoint(XWPFParagraph paragraph, XWPFDocument document){
        int posOfParagraph = document.getPosOfParagraph(paragraph);
        if(posOfParagraph == 0){
            return false;
        } else if(document.getParagraphArray(posOfParagraph-1).getNumID() != null){
            return true;
        }
        return false;
    }

    /**
     *
     * @param sourceParagraph Source Paragraph
     * @param destParagraph Destination Paragraph
     * @param destDoc Destination Document (if you need to extract more info from it)
     */
    public static void copyParagraphProperties(XWPFParagraph sourceParagraph, XWPFParagraph destParagraph, XWPFDocument sourceDoc, XWPFDocument destDoc){
        CTPPr pPr = sourceParagraph.getCTP().getPPr();
        if (pPr != null) {
            destParagraph.getCTP().setPPr(pPr);
        }

        copyParagraphMetadata(sourceParagraph, destParagraph);

        // Bullet points
        if (sourceParagraph.getNumID() != null) {
            BigInteger numID = sourceParagraph.getNumID();
            if (destDoc.getNumbering() == null ||
                    !isPreviousParagraphANumberedBulletPoint(sourceParagraph, sourceDoc)){
                destDoc.createNumbering();
            }
            BigInteger targetNumID = destDoc.getNumbering().addNum(numID);
            destParagraph.setNumID(targetNumID);
        }
    }

    public static void copyParagraphMetadata(XWPFParagraph sourceParagraph, XWPFParagraph destParagraph){
        destParagraph.setAlignment(sourceParagraph.getAlignment());
        destParagraph.setStyle(sourceParagraph.getStyleID());
        destParagraph.setBorderBetween(sourceParagraph.getBorderBetween());
        destParagraph.setBorderBottom(sourceParagraph.getBorderBottom());
        destParagraph.setBorderLeft(sourceParagraph.getBorderLeft());
        destParagraph.setBorderRight(sourceParagraph.getBorderRight());
        destParagraph.setBorderTop(sourceParagraph.getBorderTop());
        destParagraph.setFirstLineIndent(sourceParagraph.getFirstLineIndent());
        destParagraph.setFontAlignment(sourceParagraph.getFontAlignment());
        destParagraph.setIndentationFirstLine(sourceParagraph.getIndentationFirstLine());
        destParagraph.setIndentationHanging(sourceParagraph.getIndentationHanging());
        destParagraph.setIndentationRight(sourceParagraph.getIndentationRight());
        destParagraph.setIndentationLeftChars(sourceParagraph.getIndentationLeftChars());
        destParagraph.setIndentationRightChars(sourceParagraph.getIndentationRightChars());
        destParagraph.setIndentationLeft(sourceParagraph.getIndentationLeft());
        destParagraph.setIndentFromLeft(sourceParagraph.getIndentFromLeft());
        destParagraph.setIndentFromRight(sourceParagraph.getIndentFromRight());
    }

}
