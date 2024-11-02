package com.automwrite.assessment.controller;

import com.automwrite.assessment.enums.Tone;
import com.automwrite.assessment.service.LlmService;
import com.automwrite.assessment.service.ToneApplicationService;
import com.automwrite.assessment.service.ToneExtractionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class Controller {

    private final LlmService llmService;
    private final ToneApplicationService<XWPFDocument> toneApplicationService;
    private final ToneExtractionService<XWPFDocument> toneExtractionService;

    /**
     * You should extract the tone from the `toneFile` and update the `targetContent` to convey the same content
     * but using the extracted tone.
     *
     * @param sourceStyle File to extract the tone from
     * @param targetContent File to apply the tone to
     * @return A response indicating that the processing has completed
     */
    @PostMapping("/transfer-style")
    public ResponseEntity<String> test(@RequestParam MultipartFile sourceStyle, @RequestParam MultipartFile targetContent) throws
            IOException {
        // Load the documents
        XWPFDocument toneDocument = new XWPFDocument(sourceStyle.getInputStream());
        XWPFDocument contentDocument = new XWPFDocument(targetContent.getInputStream());
        String newDocName = generateNewDocName(targetContent.getName());
        Tone newTone = toneExtractionService.extractTone(toneDocument);
        toneApplicationService.applyTone(newDocName, contentDocument, newTone);

        // Simple response to indicate that everything completed
        return ResponseEntity.ok("File successfully uploaded, processing completed. " +
                "The new filename of the processed doc is "+newDocName);
    }

    private static String generateNewDocName(String currentDocName){
        String uuid = UUID.randomUUID().toString();
        if(StringUtils.isBlank(currentDocName)) {
            return uuid+".docx";
        } else if(currentDocName.endsWith(".docx")){
            return currentDocName.substring(0, currentDocName.length()-5)+"_"+uuid+".docx";
        } else {
            return currentDocName.split("\\.")[0]+"_"+uuid+".docx";
        }
    }
}
