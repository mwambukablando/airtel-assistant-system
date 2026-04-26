package com.airtel.assistant.utils;

import java.io.FileWriter;
import java.io.IOException;

public class PDFGenerator {

    // Simple placeholder (later we upgrade to real PDF library)
    public static void generateTextReport(String fileName, String content) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
            System.out.println("Report generated: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}