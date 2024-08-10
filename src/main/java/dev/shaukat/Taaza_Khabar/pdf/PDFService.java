package dev.shaukat.Taaza_Khabar.pdf;

import java.util.Map;

public interface PDFService {
    String createGKPdf(Map<Integer, Object> imagePaths, String pdfGeneratePath, float imageWidth, float imageHeight);
}
