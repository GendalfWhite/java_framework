
package autotest;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseTestSettings {
  static  WebDriver driver;
  static void prepareDriver()
  {
    System.setProperty("webdriver.chrome.driver", 
        "C:\\Users\\MSI-GL72\\Downloads\\chromedriver_win32\\chromedriver.exe");
      ChromeOptions options = new ChromeOptions();
      LoggingPreferences logPrefs = new LoggingPreferences();
      logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
      options.setCapability("goog:loggingPrefs", logPrefs);
      driver = new ChromeDriver(options); 
  }
  static void login(String email, String password)
  {
    prepareDriver();
    driver.get("http://premierdev.newizze.com");
      driver.findElement(By.id("email")).sendKeys(email);
      driver.findElement(By.id("password")).sendKeys(password);
      driver.findElement(By.xpath("//button[@type='submit']")).click();
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      String url = driver.getCurrentUrl();
      System.out.println(url);
    System.out.println("status code: " + getStatus());
    JavascriptExecutor js = (JavascriptExecutor) driver;
        // Получаем время Load Event End (окончание загрузки страницы)
        long loadEventEnd = (Long) js.executeScript("return window.performance.timing.loadEventEnd;");
        // Получаем Navigation Event Start (начало перехода)
        long navigationStart = (Long) js.executeScript("return window.performance.timing.navigationStart;");
        // Разница между Load Event End и Navigation Event Start - это время загрузки страницы
        System.out.println("Page Load Time is " + (loadEventEnd - navigationStart)/1000 + " seconds.");
  }
  static int getStatus()
  {
    try {
          LogEntries logs = driver.manage().logs().get("performance");
          int status = -1;
        String url = driver.getCurrentUrl();
      for (LogEntry entry : logs) {
        try {
          JSONObject json = new JSONObject(entry.getMessage());
          JSONObject message = json.getJSONObject("message");
          String method = message.getString("method");
          if (method != null && "Network.responseReceived".equals(method)) {
            JSONObject params = message.getJSONObject("params");
            JSONObject response = params.getJSONObject("response");
            String messageUrl = response.getString("url");
            if (url.equals(messageUrl)) {
              status = response.getInt("status");
            }
          }
        } 
        catch (JSONException e) {
          e.printStackTrace();
        }
      }
        return status;
    }
        finally {}
  }
  static void sendChar(String locator, String value)
  {
       WebElement element = driver.findElement(By.xpath(locator));  
       element.clear();
        for (int i = 0; i < value.length(); i++) {
          char c = value.charAt(i);
          String s = new StringBuilder().append(c).toString();
          element.sendKeys(s);
        }       
  }
  static String RandomStr(int n)
  {
      Random r = new Random();
      String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      String fullalphabet = alphabet + alphabet.toLowerCase() + "1234567890";
      StringBuilder result = new StringBuilder(n);
      for (int i = 0; i < n; i++) {
          char code = fullalphabet.charAt(r.nextInt(fullalphabet.length()));
          result.append(code);
      }

  return result.toString();
  }
  static String RandomDigitsStr(int n)
  {
      Random r = new Random();
      String digits = "1234567890";
      StringBuilder result = new StringBuilder(n);
      for (int i = 0; i < n; i++) {
          char code = digits.charAt(r.nextInt(digits.length()));
          result.append(code);
      } 
      return result.toString();
  }
  static String RandomFullAlphabetStr(int n)
  {
      Random r = new Random();
      String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      String fullalphabet = alphabet + alphabet.toLowerCase();
      StringBuilder result = new StringBuilder(n);
      for (int i = 0; i < n; i++) {
          char code = fullalphabet.charAt(r.nextInt(fullalphabet.length()));
          result.append(code);
      } 
      return result.toString();
  }
  static void WaitForElementBynameClickable(String item)
  {
    WebDriverWait wait = new WebDriverWait(driver,30);
    wait.until(ExpectedConditions.elementToBeClickable(By.name(item)));
  }
  static void WaitForElementByxpathClickable(String item)
  {
    WebDriverWait wait = new WebDriverWait(driver,20);
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(item)));
  }
  static void WaitForElementByxpathInvisibility(String item, String item2){
      WebDriverWait wait = new WebDriverWait(driver,30);
      if (wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(item)))) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(item2)));
      }    
  }
  public static void WaitForElementByxpathInvisibility2(String item, String item2) 
  {
      WebDriverWait wait = new WebDriverWait(driver,30);
      if (wait.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(By.xpath(item))))) {
        wait.until(ExpectedConditions.elementToBeClickable(By.id(item2)));
      }
  }
  public static void WaitForElementByxpathVisibility(String item) 
  {
    WebDriverWait wait = new WebDriverWait(driver,30);
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(item)));
  }
  public static void WaitForElementByxpathPresence(String item) 
  {
    (new WebDriverWait(driver, 30))  
    .until(ExpectedConditions.presenceOfElementLocated(By.xpath(item)));
  }
  static void WaitForElementByClassNamePresence(String item)
  {
    (new WebDriverWait(driver, 30))  
    .until(ExpectedConditions.presenceOfElementLocated(By.className(item)));
  }
  static void WaitForElementByIdPresence(String item)
  {
     (new WebDriverWait(driver, 30))  
      .until(ExpectedConditions.presenceOfElementLocated(By.id(item)));
  }
  private static void WaitForElementText(String item, String text)
  {
      (new WebDriverWait(driver, 30))
      .until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(item)), text));
  }
  /*public static String WaitForTablexpath(String item) 
  {
      WebDriverWait wait = new WebDriverWait(driver, 10);
      WebElement table = wait.until(ExpectedConditions.presence_of_element_located(By.className(item));
      return item;
  }*/
  static void SelectAndClickLine(String table, String tag, String etalon)
  {
    WebElement baseTable = driver.findElement(By.className(table));
    List<WebElement> rows_table = baseTable.findElements(By.tagName("tr"));
    int rows_count = rows_table.size();
    for (int row=1; row<rows_count; row++) {
      WebElement tableRow = rows_table.get(row);
      List<WebElement> cell = tableRow.findElements(By.xpath(tag));
      WebElement tablecell = cell.get(1);
      String valueIneed = tablecell.getText();
      WebElement tablecell3 = cell.get(0);
      String valueIneed3 = tablecell3.getText();
      System.out.println(valueIneed);
      if (valueIneed.equals(etalon)) {  
        System.out.println("OK");
        String xpathValue = "//a[contains(text(),'"+valueIneed3+"')]";
        WaitForElementByxpathClickable(xpathValue);
        driver.findElement(By.xpath(xpathValue)).click();
        break;
      }
    }
  }
  static void SelectAndCheck(String table, int position, String tag, String etalon)
  {


WebElement baseTable = driver.findElement(By.id(table));
    List<WebElement> rows_table = baseTable.findElements(By.tagName("tr"));
    int rows_count = rows_table.size();
    for (int row=1; row<rows_count; row++) {
      WebElement tableRow = rows_table.get(row);
      List<WebElement> cell = tableRow.findElements(By.xpath(tag));
      WebElement tablecell = cell.get(position);
      String valueIneed = tablecell.getText();
      System.out.println(valueIneed);
      if (valueIneed.equals(etalon)) {  
        System.out.println("OK");
      }
      else {
         System.out.println("Mismatch");
         System.out.println(valueIneed);
       }  
    }
  }
  static void SelectAndCheckInject(String table, int position, String tag, String table2, int pos2, String tag2, String etalon2)
  {
    WebElement baseTable = driver.findElement(By.id(table));
    List<WebElement> rows_table = baseTable.findElements(By.tagName("tr"));
    int rows_count = rows_table.size();
    for (int row=1; row<rows_count; row++) {
      WebElement tableRow = rows_table.get(row);
      List<WebElement> cell = tableRow.findElements(By.xpath(tag));
      WebElement tablecell = cell.get(position);
      String temp = tablecell.getTagName();
      System.out.println(temp);
      String temp4 = tablecell.getAttribute("innerHTML");
      System.out.println(temp4);
      int Result = temp4.indexOf("id=\"livicon");
      System.out.println (Result);
      String Result2 = temp4.substring(Result+12, Result+14);
      System.out.println (Result2);
      WaitForElementByxpathClickable("//*[@id=\"canvas-for-livicon-"+Result2+"\"]");
      tablecell.findElement(By.xpath("//*[@id=\"canvas-for-livicon-"+Result2+"\"]")).click();
      System.out.println("Klatc");
      SelectAndCheck(table2, pos2, tag2, etalon2);
      driver.findElement(By.xpath("//div[11]/div/div/div/button/span")).click();
    }
  }
  static List<WebElement> InitiateTable(String tab2) // получение массива строк таблицы вэб элементов
  {
    WebElement baseTable5 = driver.findElement(By.id(tab2));
    return baseTable5.findElements(By.tagName("tr"));
  }
  static int GetRowsOfTable(String tab2)
  {
    List<WebElement> rows_table6 = InitiateTable(tab2);
    return rows_table6.size();
  }
  static void CheckLoadOfTable(String tab3)
  {
     for (int row=0; row < GetRowsOfTable(tab3); row++) {
      WebElement tableRow4 = InitiateTable(tab3).get(row);
      List<WebElement> cell4 = tableRow4.findElements(By.xpath("./td"));
      WebElement tablecell4 = cell4.get(1);
      String valueIneed4 = tablecell4.getText();
      System.out.println(valueIneed4 + " loads");
      String xpathValue4 = "//a[contains(text(),'"+valueIneed4+"')]";
      WaitForElementByxpathClickable(xpathValue4);
      //break;
     }
  }  
  static void CheckLoadOfTable2(String tab4, String tag2)
  {
     for (int row=1; row < GetRowsOfTable(tab4); row++) {
      WebElement tableRow7 = InitiateTable(tab4).get(row);
      List<WebElement> cell7 = tableRow7.findElements(By.xpath(tag2));
      WebElement tablecell7 = cell7.get(1);
      String valueIneed7 = tablecell7.getText();
      System.out.println(valueIneed7 + " loads");
      String xpathValue7 = "//a[contains(text(),'"+valueIneed7+"')]";
      WaitForElementText(xpathValue7, valueIneed7);
      //break;
     }
  }  
}
