package org.abhinav;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.List;

public class DatascraperWithFlipkartCsv {
    public static void main(String[] args) throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=old");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        // driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.get("https://www.flipkart.com/");

        // Close the login popup if it appears
        // try {
        //     WebElement closePopupBtn = driver.findElement(By.cssSelector("button._2KpZ6l._2doB4z"));
        //     closePopupBtn.click();
        // } catch (NoSuchElementException e) {
        //     // Do nothing, popup not present
        // }

        WebElement searchBox = driver.findElement(By.cssSelector("input[name=\"q\"]"));
        String query = "Mobile";
        searchBox.sendKeys(query);
        searchBox.sendKeys(Keys.ENTER);

        int currentpage = 1;
        // int totalPage = 5;

        try (PrintWriter writer = new PrintWriter(new FileWriter(query + "_flipkart.csv"))) {
            // Write CSV header
            writer.println("Product Name,Current Price,Original Price");

            int tillNow = 0;
            // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            while (true) {
                List<WebElement> listOfItems = driver.findElements(By.cssSelector("div.cPHDOP"));
                System.out.println("Currently at page no. " + currentpage);
                System.out.println("Till now " + tillNow + " product added");

                for (WebElement item : listOfItems) {
                    List<WebElement> productNameEle = item.findElements(By.cssSelector("div.KzDlHZ"));

                    if (productNameEle.isEmpty()) {
                        continue;
                    } else {
                        String productName = productNameEle.get(0).getText();
                        writer.print('"' + productName + '"');
                        tillNow++;
                    }

                    List<WebElement> currPriceEle = item.findElements(By.cssSelector("div[class=\"Nx9bqj _4b5DiR\"]"));
                    if (!currPriceEle.isEmpty()) {
                        String currentPrice = currPriceEle.get(0).getText();  // Use get(0) instead of getFirst()
                        writer.print("," + '"' + currentPrice + '"');
                    } else {
                        writer.print("," + '"' + "N/A" + '"');
                    }

                    List<WebElement> origPriceEle = item.findElements(By.cssSelector("div[class=\"yRaY8j ZYYwLA\"]"));
                    if (!origPriceEle.isEmpty()) {
                        String originalPrice = origPriceEle.get(origPriceEle.size() - 1).getAttribute("innerText");  // Use get(size() - 1) instead of getLast()
                        writer.println("," + '"' + originalPrice + '"');
                    } else {
                        writer.println("," + '"' + "N/A" + '"');
                    }
                }
                currentpage++;
                try{
                    WebElement nextBtn = driver.findElement(By.xpath("//a[@class='_9QVEpD']//span[text()='Next']"));
                    nextBtn.click();
                    Thread.sleep(2000);
                    nextBtn = driver.findElement(By.xpath("//a[@class='_9QVEpD']//span[text()='Next']"));
                }catch(NoSuchElementException e){
                    System.out.println("Total item captured");
                    break;
                }

                }      
                    
                
               

                
        }   
        catch (Exception e) {
            e.printStackTrace();
        }
        driver.quit();
    }       
}
 // try{
                //     nextBtn.click();
                //     nextBtn = driver.findElement(By.xpath("//a[@class='_9QVEpD']//span[text()='Next']"));

                // }catch(StaleElementReferenceException st){
                //     nextBtn = driver.findElement(By.xpath("//a[@class='_9QVEpD']//span[text()='Next']"));
                //     nextBtn.click();
                // }
                // Locate the next button after the current page is processed
//                JavascriptExecutor js = (JavascriptExecutor) driver;
                // List<WebElement> nextBtn = driver.findElements(By.cssSelector("a._9QVEpD"));
                // WebDriverWait expwait = new WebDriverWait(driver, Duration.ofSeconds(5));
                // WebElement nextBtn = expwait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='_9QVEpD']//span[text()='Next']")));
                // nextBtn.click();
//                js.executeScript("arguments[0].click();", nextBtn);
            // int attempts = 0;
            //     while (attempts < 3) {
            //         try {
            //             nextBtn.getLast().click();
            //             break;
            //         } catch (StaleElementReferenceException e) {
            //             attempts++;
            //             nextBtn = driver.findElements(By.cssSelector("a._9QVEpD"));
            //         }
                
                // try{
                //     nextBtn.click();
                // }catch(StaleElementReferenceException st){
                //     nextBtn = driver.findElement(By.cssSelector("a._9QVEpD"));
                //     nextBtn.getLast().click();
                // }catch (Exception we){
                //     we.printStackTrace();
                // }
    
    

