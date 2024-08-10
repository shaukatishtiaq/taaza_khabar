package dev.shaukat.Taaza_Khabar.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PDFServiceImpl implements PDFService {
    private static final Logger LOGGER = Logger.getLogger(PDFServiceImpl.class.getName());

    private static void checkIfPathsAreNull(Map<Integer, Object> imagePaths, String pdfGeneratePath) {
        Objects.requireNonNull(imagePaths, "Image paths cannot be null.");
        Objects.requireNonNull(pdfGeneratePath, "PDF save path can't be null");

        if (imagePaths.isEmpty()) {
            throw new IllegalArgumentException("Image paths map cannot be empty");
        }
        if (pdfGeneratePath.isEmpty()) {
            throw new IllegalArgumentException("PDF generate path cannot be empty");
        }
    }

    @Override
    public String createGKPdf(Map<Integer, Object> imagePaths, String pdfGeneratePath, float imageWidth, float imageHeight) {

        checkIfPathsAreNull(imagePaths, pdfGeneratePath);

        String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String pdfFilePath = pdfGeneratePath + date + ".pdf";

        createDirectoriesIfNotExist(pdfFilePath);

        generatePdfDocument(imagePaths, imageWidth, imageHeight, pdfFilePath);

        return pdfFilePath;
    }

    private void generatePdfDocument(Map<Integer, Object> imagePaths, float imageWidth, float imageHeight, String pdfFilePath) {

        Map<Integer, Object> sortedImages = new TreeMap<>(imagePaths);

        Rectangle pageSize = new Rectangle(imageWidth, imageHeight);

        pageSize.setBackgroundColor(new java.awt.Color(0xFF, 0xFF, 0xDE));

        try (FileOutputStream fos = new FileOutputStream(pdfFilePath);
             Document document = new Document(pageSize)) {

            PdfWriter writer = PdfWriter.getInstance(document, fos);

            document.open();

            addImagesToPdfDocument(sortedImages, document);

//            COMPRESS
//            PDFUtils.compress(pdfFilePath);

        } catch (DocumentException | IOException e) {
            LOGGER.log(Level.SEVERE, "Error while creating PDF", e);
            throw new RuntimeException("Error while creating PDF: " + e.getMessage(), e);
        }
    }


    private void addImagesToPdfDocument(Map<Integer, Object> imagePaths, Document document) {
        LOGGER.info("Adding images to PDF started.");
        imagePaths.keySet().forEach(key -> {
            try {
                Image img = Image.getInstance((imagePaths.get(key).toString()));
                document.add(img);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error while adding images to PDF.", e);
                throw new RuntimeException("Error while adding images to PDF.", e);
            }
        });

        LOGGER.info("Adding images to PDF DONE.");
    }

    private void createDirectoriesIfNotExist(String path) {
        try {
            Path direcotoryPath = Paths.get(path).getParent();
            if (direcotoryPath != null && !Files.exists(direcotoryPath)) {
                Files.createDirectories(direcotoryPath);
                LOGGER.info("Missing directories for " + path + " created.");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while creating directories.", e);
            throw new RuntimeException("Error while creating directories.", e);
        }
    }
}
