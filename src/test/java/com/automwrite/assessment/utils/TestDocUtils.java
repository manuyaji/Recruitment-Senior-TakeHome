package com.automwrite.assessment.utils;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@SpringBootTest
public class TestDocUtils {

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void printAllFilesInResources() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:.");
        Path resourceDir = resource.getFile().toPath();
        System.out.println("YAJI: Listing all files...");

        // List and print all files in resources folder
        try (Stream<Path> paths = Files.walk(resourceDir)) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(System.out::println);
        }
    }

    @Test
    public void testDocStructure() throws FileNotFoundException, IOException {
        String docName = "src/test/resources/casual-tone.docx";
        XWPFDocument doc = new XWPFDocument(new FileInputStream(docName), true);
        for (XWPFParagraph paragraph: doc.getParagraphs()){
            System.out.println("=======");
            System.out.println(paragraph.getText());
            System.out.println("=======");
        }

    }

    @Test
    public void testDocStructure2() throws FileNotFoundException, IOException {
        String docName = "src/test/resources/casual-tone.docx";
        XWPFDocument doc = new XWPFDocument(new FileInputStream(docName), true);
        for (IBodyElement element: doc.getBodyElements()){
            System.out.println("====================");
            System.out.println("====================");
            System.out.println(element.getElementType());
            System.out.println("--------------");
            System.out.println(element.getBody());
        }

    }
}
