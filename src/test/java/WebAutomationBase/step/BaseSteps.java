package WebAutomationBase.step;

import WebAutomationBase.base.BasePageUtil;
import WebAutomationBase.helper.ElementHelper;
import WebAutomationBase.helper.StoreHelper;
import WebAutomationBase.model.ElementInfo;
import com.thoughtworks.gauge.Step;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;

import java.util.*;

public class BaseSteps extends BasePageUtil {

    public static int DEFAULT_MAX_ITERATION_COUNT = 150;
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;

    private static final Log4jLoggerAdapter logger = (Log4jLoggerAdapter) LoggerFactory
            .getLogger(BaseSteps.class);


    private final Actions actions = new Actions(driver);


    public BaseSteps() {

        PropertyConfigurator
                .configure(BaseSteps.class.getClassLoader().getResource("log4j.properties"));
    }

    public WebElement findElement(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    private void clickElement(WebElement element) {
        element.click();
    }

    public void hoverElement(WebElement element) {
        actions.moveToElement(element).build().perform();
    }

    public WebElement findElementWithKey(String key) {
        return findElement(key);
    }

    @Step({"<long> milisaniye bekle"})
    public void waitByMilliSeconds(long milliseconds) {
        try {
            logger.info(milliseconds + " milisaniye bekleniyor.");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step({"Elementine tıkla <key>"})
    public void clickElement(String key) {
        if (!key.equals("")) {
            WebElement element = findElement(key);
            hoverElement(element);
            waitByMilliSeconds(500);
            clickElement(element);
            logger.info(key + " elementine tıklandı.");
        }
    }

    @Step({"Element var mı kontrol et <key>",})
    public WebElement getElementWithKeyIfExists(String key) {
        WebElement webElement;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            try {
                webElement = findElementWithKey(key);
                logger.info(key + " elementi bulundu.");
                return webElement;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element: '" + key + "' doesn't exist.");
        return null;
    }


    @Step({"Şuanki URL <url> değerini içeriyor mu kontrol et"})
    public void checkURLContainsRepeat(String expectedURL) {
        int loopCount = 0;
        String actualURL = "";
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualURL = driver.getCurrentUrl();

            if (actualURL != null && actualURL.contains(expectedURL)) {
                logger.info("Şuanki URL" + expectedURL + " değerini içeriyor.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(
                "Actual URL doesn't match the expected." + "Expected: " + expectedURL + ", Actual: "
                        + actualURL);
    }


    @Step({"Son sekmeye odaklan"})
    public void chromeFocusLastTab() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
    }


    @Step({"<key> li elementi bul ve varsa tıkla"})
    public void existTapByKey(String key) {
        waitSecond(4);
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        WebElement element = null;
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        try {
            element = findElementWithKeyWithoutAssert(key);

        } catch (Exception e) {

        }
        if (element != null) {
            element.click();
            logger.info(key + " elementine tıklandı");
        } else {
            logger.info(key + " elementin bulunamadığından tıklanmadı.");
        }
    }

    @Step({"<key> li elementi bul"})
    public void justFindElement(String key) {
        waitSecond(4);
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        WebElement element = null;
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        try {
            element = findElement(key);
            logger.info("Element bulundu.");

        } catch (Exception e) {
        }
    }

    public WebElement findElementWithKeyWithoutAssert(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        WebElement element = null;
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        try {
            WebDriverWait wait = new WebDriverWait(driver, 0, 500);
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(infoParam));
        } catch (Exception e) {
        }
        return element;
    }

    @Step({"<times> kere aşağı kaydır"})
    public void scrollToBottomWithTimes(int times) {

        for (int i = 0; i < times; i++) {
            JavascriptExecutor js = ((JavascriptExecutor) driver);
            js.executeScript("window.scrollBy(0,200)");
            waitSecond(1);
            System.out.println("Aşağı kaydı.. " + i);
        }
    }

    @Step("<key> saniye bekle")
    public void waitSecond(int second) {
        waitByMilliSeconds(second * 1000L);
    }

}









