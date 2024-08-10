package dev.shaukat.Taaza_Khabar.greaterkashmir;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import dev.shaukat.Taaza_Khabar.config.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class GreaterKashmirScraper {
    private static final Logger LOGGER = Logger.getLogger(GreaterKashmirScraper.class.getName());

    public Map<Integer, Object> generateEngScreenshots() {
        Map<Integer, Object> imagePaths = new HashMap<>();

        String todayUrl = getTodayEngUrl();

        LOGGER.info("Generating screenshots for GK.");

        for (int pageNo = 1; pageNo <= 12; pageNo++) {
            WebDriver driver = getScreenshotChromeDriver();

            driver.get(GreaterKashmirUtils.formattedUrl(todayUrl, pageNo));

            try {
                Thread.sleep(Duration.ofSeconds(8));
                Shutterbug.shootElement(driver, driver.findElement(By.cssSelector("#de-chunks-container")), true)
                        .withName(String.valueOf(pageNo))
                        .save(GreaterKashmirUtils.getScreenshotPath("eng"));

                imagePaths.put(pageNo, GreaterKashmirUtils.getScreenshotPath("eng") + pageNo + ".png");

                driver.quit();
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Error while getting screenthots of GK eng.", e);
                throw new RuntimeException("Error while getting screenthots of GK eng.", e);
            } finally {
                driver.quit();
            }
        }
        LOGGER.info("Generating screenshots for GK DONE.");
        return imagePaths;
    }

    public Map<Integer, Object> generateUrduScreenshots() {
        Map<Integer, Object> imagePaths = new HashMap<>();

        String todayUrl = getTodayUrduUrl();

        LOGGER.info("Generating screenshots for GK Uzma.");
        for (int pageNo = 1; pageNo <= 12; pageNo++) {
            WebDriver driver = getScreenshotChromeDriver();

            driver.get(GreaterKashmirUtils.formattedUrl(todayUrl, pageNo));

            try {
                Thread.sleep(Duration.ofSeconds(8));

                Shutterbug.shootElement(driver, driver.findElement(By.cssSelector("div#content-row > div#page-div")),
                                true)
                        .withName(String.valueOf(pageNo))
                        .save(GreaterKashmirUtils.getScreenshotPath("urdu"));

                imagePaths.put(pageNo, GreaterKashmirUtils.getScreenshotPath("urdu") + pageNo + ".png");

                driver.quit();
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Error while getting screenthots of GK Uzma.", e);
                throw new RuntimeException("Error while getting screenthots of GK Uzma.", e);
            } finally {
                driver.quit();
            }
        }
        LOGGER.info("Generating screenshots for GK Uzma DONE.");

        return imagePaths;
    }

    private WebDriver getScreenshotChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments("--window-size=1560,2789");
        WebDriver driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        return driver;
    }

    private String getTodayEngUrl() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));

        driver.get(Constants.GK_URL);
        WebElement divContainingSpan = driver.findElement(By.cssSelector(".cards-title"));
        WebElement spanButton = divContainingSpan.findElement(By.cssSelector("h2 > span"));
        spanButton.click();

        WebElement readNowButton = driver.findElement(By.cssSelector("div.pck-btn > a"));
        readNowButton.click();

        String todayUrl = GreaterKashmirUtils.cleanUrl(driver.getCurrentUrl());
        driver.quit();

        return todayUrl;
    }

    private String getTodayUrduUrl() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5L));

        driver.get(Constants.GK_UZMA_URL);
        driver.findElement(By.cssSelector("div.papr-card > div.papr-img")).click();

        String todayUrl = GreaterKashmirUtils.cleanUrl(driver.getCurrentUrl());

        driver.quit();

        return todayUrl;
    }

}