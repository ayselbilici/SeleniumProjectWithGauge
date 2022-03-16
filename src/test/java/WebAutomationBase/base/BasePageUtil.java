package WebAutomationBase.base;

import WebAutomationBase.helper.ElementHelper;
import WebAutomationBase.helper.StoreHelper;
import WebAutomationBase.model.ElementInfo;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BasePageUtil extends BaseTest {

    public WebDriverWait wait = new WebDriverWait(driver, 20);
    final static Logger logger = Logger.getLogger(BasePageUtil.class);

    public WebElement findElement(String key) {

        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By by = ElementHelper.getElementInfoToBy(elementInfo);

        try {
            logger.info("findElement method called:  finding " + key);
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Exception ex) {
            logger.error(key + " element can not find! " + by.toString());
            throw ex;
        }
    }

    public void hoverElement(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).build().perform();
    }

    public void waitByMilliSeconds(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitSecond(int second) {
        waitByMilliSeconds(second * 1000L);
    }


}

