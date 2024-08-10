package dev.shaukat.Taaza_Khabar.greaterkashmir.service;

import dev.shaukat.Taaza_Khabar.config.Constants;
import dev.shaukat.Taaza_Khabar.greaterkashmir.GreaterKashmirScraper;
import dev.shaukat.Taaza_Khabar.greaterkashmir.GreaterKashmirUtils;
import dev.shaukat.Taaza_Khabar.pdf.PDFService;
import dev.shaukat.Taaza_Khabar.pdf.PDFUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class GKServiceImpl implements GKService {
    private final GreaterKashmirScraper greaterKashmirScraper;
    private final PDFService pdfService;
    private final Logger LOGGER = Logger.getLogger(GKServiceImpl.class.getName());

    public GKServiceImpl(GreaterKashmirScraper greaterKashmirScraper, PDFService pdfService) {
        this.greaterKashmirScraper = greaterKashmirScraper;
        this.pdfService = pdfService;
    }

    public Map<String, Object> generatePDFsAsync() {
        Map<String, Object> pdfPaths = new LinkedHashMap<>();

        GreaterKashmirUtils.deleteOldScreenshots();
        PDFUtils.deleteOldPdfs();

        LOGGER.info("GK Async started.");
        CompletableFuture<String> gkAsync = CompletableFuture
                .supplyAsync(greaterKashmirScraper::generateEngScreenshots)
                .thenApply(screenshotPaths -> pdfService.createGKPdf(screenshotPaths, Constants.GK_PDF_PATH, 1400, 2469));

        LOGGER.info("GK Uzma Async started.");
        CompletableFuture<String> uzmaAsync = CompletableFuture
                .supplyAsync(greaterKashmirScraper::generateUrduScreenshots)
                .thenApply(screenshotPaths -> pdfService.createGKPdf(screenshotPaths, Constants.GK_UZMA_PDF_PATH, 1400, 2469));


        String gkPdfPath = gkAsync.join();
        LOGGER.info("Uncompressed GK PDF Generated");
        String gkUzmaPdfPath = uzmaAsync.join();
        LOGGER.info("Uncompressed Uzma PDF Generated");


        CompletableFuture<String> compressedGkPdfAsync = CompletableFuture.supplyAsync(() -> PDFUtils.compress(gkPdfPath))
                .exceptionallyAsync((e) -> {
                    LOGGER.log(Level.SEVERE, "Error caused while executing compressedGkPdfAsync", e);
                    throw new RuntimeException("Error caused while executing compressedGkPdfAsync");
                });

        CompletableFuture<String> compressedGkUzmaPdfAsync = CompletableFuture.supplyAsync(() -> PDFUtils.compress(gkUzmaPdfPath))
                .exceptionallyAsync(e -> {
                    LOGGER.log(Level.SEVERE, "Error caused while executing compressedUzmaPdfAsync", e);
                    throw new RuntimeException("Error caused while executing compressedUzmaPdfAsync");
                });


        pdfPaths.put("gk", compressedGkPdfAsync.join());
        LOGGER.info("Compressed GK PDF Generated");
        pdfPaths.put("uzma", compressedGkUzmaPdfAsync.join());
        LOGGER.info("Compressed Uzma PDF Generated");

        try {
            FileSystemUtils.deleteRecursively(Paths.get(gkPdfPath));
            FileSystemUtils.deleteRecursively(Paths.get(gkUzmaPdfPath));
            FileSystemUtils.deleteRecursively(Paths.get("./screenshots"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return pdfPaths;
    }
}
