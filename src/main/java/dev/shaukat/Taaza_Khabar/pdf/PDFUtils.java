package dev.shaukat.Taaza_Khabar.pdf;

import com.aspose.pdf.Document;
import com.aspose.pdf.optimization.OptimizationOptions;
import dev.shaukat.Taaza_Khabar.config.Constants;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PDFUtils {
    private static final Logger LOGGER = Logger.getLogger(PDFUtils.class.getName());

    public static String compress(String pdfPath) {
        try (Document document = new Document(pdfPath)) {

            Path pdfFile = Paths.get(pdfPath);

            System.out.println(pdfFile.getFileName());
            System.out.println(pdfFile.getParent());

            OptimizationOptions optimizationOptions = new OptimizationOptions();
            optimizationOptions.getImageCompressionOptions().setCompressImages(true);
            optimizationOptions.getImageCompressionOptions().setImageQuality(50);

            document.optimizeResources(optimizationOptions);
            document.optimize();

            String compressedPdfPath = pdfFile.getParent() + "/" + "compressed-" + pdfFile.getFileName();

            document.save(compressedPdfPath);

            return compressedPdfPath;
        } catch (InvalidPathException e) {
            LOGGER.log(Level.WARNING, "Error while compressing the PDF. The path was invalid " + "PDF path = " + pdfPath, e);
            throw new RuntimeException("Error while compressing the PDF. The path was invalid " + "PDF path = " + pdfPath, e);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error while compressing the PDF" + "PDF file = " + pdfPath, e);
            throw new RuntimeException("Error while compressing the PDF" + "PDF file = " + pdfPath, e);
        }
    }

    public static void deleteOldPdfs() {
        try {
            FileSystemUtils.deleteRecursively(Paths.get(Constants.DELETE_PDFS_PATH));

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while deleting old PDFs.", e);
            throw new RuntimeException("Error while deleting old PDFs.", e);
        }
    }

}
