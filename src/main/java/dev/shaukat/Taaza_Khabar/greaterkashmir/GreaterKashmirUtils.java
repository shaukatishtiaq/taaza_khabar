package dev.shaukat.Taaza_Khabar.greaterkashmir;

import dev.shaukat.Taaza_Khabar.config.Constants;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Paths;

public class GreaterKashmirUtils {

    public static String cleanUrl(String url) {
        return url.replace("page/1/1", "page/<PAGE_NO>/2");
    }

    public static String formattedUrl(String url, int pageNo) {
        return url.replace("<PAGE_NO>", String.valueOf(pageNo));
    }

    public static String getScreenshotPath(String language) {

        return Constants.GK_BASE_SCREENSHOT_PATH + language + "/";
    }

    public static void deleteOldScreenshots() {
        try {
            FileSystemUtils.deleteRecursively(Paths.get(Constants.DELETE_SCREENSHOTS_PATH));
        } catch (IOException e) {
//            LOGGER.log(Level.SEVERE, "Error while deleting old Screenshots.", e);
            throw new RuntimeException("Error while deleting old Screenshots.", e);
        }
    }

}
