package main.tal.root;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ScreenshotCapture {

    ChromeOptions chromeOptions;

    public ScreenshotCapture() {
        chromeOptions = new ChromeOptions()
                .addArguments("--log-level=3")
                .addArguments("--no-sandbox")
                .addArguments("--disable-dev-shm-usage")
                .addArguments("--silent");
    }

    public String capture(String url) {
            String name = "images/" + UUID.randomUUID().toString() + ".jpg";
            final File screenShot = new File(name).getAbsoluteFile();
            final WebDriver driver = new ChromeDriver(chromeOptions);
            try {
                driver.get(url);
                TimeUnit.SECONDS.sleep(5);
                final File outputFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(outputFile, screenShot);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                driver.close();
                return name;
            }
    }
}
