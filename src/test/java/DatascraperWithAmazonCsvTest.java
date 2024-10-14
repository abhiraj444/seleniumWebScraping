

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.List;

public class DatascraperWithAmazonCsvTest {
        @Test
        void scrapeAmazon() {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=old");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        // driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        WebDriverWait waitExpl = new WebDriverWait(driver, Duration.ofSeconds(3));

        driver.get("https://www.amazon.in/");
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        String query = "Flower";
        searchBox.sendKeys(query);
        searchBox.sendKeys(Keys.ENTER);
        int currentpage = 1;
        int totalPage = 4;

        try (PrintWriter writer = new PrintWriter(new FileWriter(query + "_amazon.csv"))) {
            // Write CSV header
            writer.println("Product Name,Current Price,Original Price");

            int tillNow = 0;
            while (currentpage < totalPage) {
                List<WebElement> listOfItems = driver.findElements(By.cssSelector("div.s-result-item"));
                System.out.println("Currently at page no. " + currentpage);
                System.out.println("Till now " + tillNow + " product added");
                for (WebElement item : listOfItems) {
                    List<WebElement> productNameEle = item.findElements(By.cssSelector("h2 a"));

                    if (productNameEle.isEmpty()) {
                        continue;
                    } else {
                        String productName = productNameEle.get(0).getText();
                        writer.print('"' + productName + '"');
                        tillNow++;
                    }

                    List<WebElement> currPriceEle = item.findElements(By.cssSelector("span.a-price-whole"));
                    if (!currPriceEle.isEmpty()) {
                        String currentPrice = currPriceEle.get(0).getText();
                        writer.print("," + '"' + currentPrice + '"');
                    } else {
                        writer.print("," + '"' + "N/A" + '"');
                    }

                    List<WebElement> origPriceEle = item.findElements(By.cssSelector("span[data-a-color=\"secondary\"]>span.a-offscreen"));
                    if (!origPriceEle.isEmpty()) {
                        String originalPrice = origPriceEle.get(origPriceEle.size() - 1).getAttribute("innerText");
                        writer.println("," + '"' + originalPrice + '"');
                    } else {
                        writer.println("," + '"' + "N/A" + '"');
                    }
                }
             waitExpl.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()=\"Next\"]")));
             WebElement nextBtn = driver.findElement(By.xpath("//a[text()=\"Next\"]"));
             nextBtn.click();
             currentpage++;
             Thread.sleep(2000);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.quit();
    }
}