package com.qa.pages;

import com.qa.pages.SearchAthlete.SearchCountryPage;
import com.qa.utils.DriverManager;
import com.qa.utils.GlobalParams;
import com.qa.utils.PlatformHelper;
import com.qa.utils.TestUtils;
import io.appium.java_client.*;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;

public class BasePage {
    protected AppiumDriver<MobileElement> driver;

    public BasePage() {
        this(new DriverManager().getDriver(), false);
    }

    public BasePage(AppiumDriver<MobileElement> driver) {
        this(driver, false);
    }

    public BasePage(AppiumDriver<MobileElement> driver, boolean lazyInit) {
        this.driver = driver;
        if (!lazyInit) {
            bindElements(driver);
        }
    }

    public final void bindElements(AppiumDriver<MobileElement> driver) {
        try {
            PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(5)), this);
        } catch (Exception exception) {
            TestUtils.log().error("Error occurred on binding elements to page!", exception);
        }
    }

    public final void rebind() {
        bindElements(driver);
    }

    public void waitForVisibility(MobileElement e) {
        WebDriverWait wait = new WebDriverWait(driver, TestUtils.WAIT);
        wait.until(ExpectedConditions.visibilityOf(e));
    }

    public void waitForVisibility(By e) {
        WebDriverWait wait = new WebDriverWait(driver, TestUtils.WAIT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(e));
    }

    public void clear(MobileElement e) {
        waitForVisibility(e);
        e.clear();
    }

    public void click(MobileElement e) {
        waitForVisibility(e);
        e.click();
    }

    public void click(MobileElement e, String msg) {
        waitForVisibility(e);
        TestUtils.log().info(msg);
        e.click();
    }

    public void backButton() throws InterruptedException {
        Thread.sleep(20);
        driver.navigate().back();
        TestUtils.log().info("Click device Back button");
    }

    public void click(By e, String msg) {
        waitForVisibility(e);
        driver.findElement(e).click();
    }

    public void sendKeys(MobileElement e, String txt) {
        waitForVisibility(e);
        e.sendKeys(txt);
    }

    public void sendKeys(MobileElement e, String txt, String msg) {
        waitForVisibility(e);
        TestUtils.log().info(msg);
        e.sendKeys(txt);
        switch (PlatformHelper.get(driver)) {
            case ANDROID:
                driver.hideKeyboard();
                break;
            case iOS:
                new SearchCountryPage().clickOnKeyboardSearch();
                break;
        }
    }

    public String getAttribute(MobileElement e, String attribute) {
        waitForVisibility(e);
        return e.getAttribute(attribute);
    }

    public String getAttribute(By e, String attribute) {
        waitForVisibility(e);
        return driver.findElement(e).getAttribute(attribute);
    }

    public String getText(MobileElement e, String msg) {
        String txt;
        switch (PlatformHelper.get(driver)) {
            case ANDROID:
                txt = getAttribute(e, "text");
                break;
            case iOS:
                txt = getAttribute(e, "label");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + new GlobalParams().getPlatform());
        }
        TestUtils.log().info(msg + txt);
        return txt;
    }

    public String getText(By e, String msg) {
        String txt;
        switch (PlatformHelper.get(driver)) {
            case ANDROID:
                txt = getAttribute(e, "text");
                break;
            case iOS:
                txt = getAttribute(e, "label");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + new GlobalParams().getPlatform());
        }
        TestUtils.log().info(msg + txt);
        return txt;
    }

    public void closeApp() {
        ((InteractsWithApps) driver).closeApp();
    }

    public void launchApp() {
        ((InteractsWithApps) driver).launchApp();
    }

    public MobileElement andScrollToElementUsingUiScrollable(String childLocAttr, String childLocValue) {
        return (MobileElement) ((FindsByAndroidUIAutomator) driver).findElementByAndroidUIAutomator(
                "new UiScrollable(new UiSelector()" + ".scrollable(true)).scrollIntoView("
                        + "new UiSelector()." + childLocAttr + "(\"" + childLocValue + "\"));");
    }

    public MobileElement scrollToElementWithText(String text) {
        return driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable("
                + "new UiSelector().scrollable(true)).scrollIntoView("
                + "new UiSelector().textContains(\"" + text + "\"));"));
    }

    public MobileElement scrollToElementWithId(String id) {
        MobileElement foundElement;
        int forwardScrollAmount = 5;
        // Start with a forward scroll when the element is not found
        for (int i = 0; i < forwardScrollAmount; i++) {
            try {
                driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
                foundElement = driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"" + id + "\")"));
                if (foundElement.isDisplayed()) {
                    return foundElement;
                }
            } catch (NoSuchElementException ex) {
                driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true)).scrollForward()"));
            }
        }

        return driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable("
                + "new UiSelector().scrollable(true)).scrollIntoView("
                + "new UiSelector().resourceId(\"" + id + "\"));"));
    }

    public MobileElement scrollToElementWithXpath(String xpath) {
        return driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable("
                + "new UiSelector().scrollable(true)).scrollIntoView("
                + "new UiSelector().resourceIdMatches(\"" + xpath + "\"));"));
    }

    public MobileElement iOSScrollToElementUsingMobileScroll(MobileElement e) {
        RemoteWebElement element = ((RemoteWebElement) e);
        String elementID = element.getId();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("element", elementID);
//	  scrollObject.put("direction", "down");
//	  scrollObject.put("predicateString", "label == 'ADD TO CART'");
//	  scrollObject.put("name", "test-ADD TO CART");
        scrollObject.put("toVisible", "sdfnjksdnfkld");
        driver.executeScript("mobile:scroll", scrollObject);
        return e;
    }

    public By iOSScrollToElementUsingMobileScrollParent(MobileElement parentE, String predicateString) {
        RemoteWebElement parent = (RemoteWebElement) parentE;
        String parentID = parent.getId();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("element", parentID);
//	  scrollObject.put("direction", "down");
        scrollObject.put("predicateString", predicateString);
//	  scrollObject.put("name", "test-ADD TO CART");
//        scrollObject.put("toVisible", "sdfnjksdnfkld");
        driver.executeScript("mobile:scroll", scrollObject);
        By m = MobileBy.iOSNsPredicateString(predicateString);
        System.out.println("Mobilelement is " + m);
        return m;
    }

    public MobileElement scrollToElement(MobileElement element, String direction) throws Exception {
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.5);
        int endX = (int) (size.width * 0.5);
        int startY = 0;
        int endY = 0;
        boolean isFound = false;

        switch (direction) {
            case "left":
                startY = (int) (size.height * 0.5);
                endX = (int) (size.width * 0.8);
                startX = (int) (size.width * 0.05);
                break;

            case "right":
                startY = (int) (size.height * 0.5);
                startX = (int) (size.width * 0.80);
                endX = (int) (size.width * 0.05);
                break;

            case "down":
                endY = (int) (size.height * 0.4);
                startY = (int) (size.height * 0.8);
                break;

            case "up":
                endY = (int) (size.height * 0.6);
                startY = (int) (size.height * 0.4);
                break;
        }

        for (int i = 0; i < 30; i++) {
            if (find(element, 1)) {
                isFound = true;
                break;
            } else {
                swipe(startX, startY, endX, endY, 1000);
            }
        }
        if (!isFound) {
            throw new Exception("Element not found");
        }
        return element;
    }

    public By scrollToElement(By element, String direction) throws Exception {
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.5);
        int endX = (int) (size.width * 0.5);
        int startY = 0;
        int endY = 0;
        boolean isFound = false;

        switch (direction) {
            case "up":
                endY = (int) (size.height * 0.4);
                startY = (int) (size.height * 0.6);
                break;

            case "down":
                endY = (int) (size.height * 0.6);
                startY = (int) (size.height * 0.4);
                break;
        }

        for (int i = 0; i < 3; i++) {
            if (find(element, 1)) {
                isFound = true;
                break;
            } else {
                swipe(startX, startY, endX, endY, 1000);
            }
        }
        if (!isFound) {
            throw new Exception("Element not found");
        }
        return element;
    }

    public boolean find(final MobileElement element, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            return wait.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    if (element.isDisplayed()) {
                        return true;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            return false;
        }
    }

    public boolean find(final By element, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            return wait.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    if (driver.findElement(element).isDisplayed()) {
                        return true;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            return false;
        }
    }

    public void swipeLeft() throws InterruptedException {
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.05);
        int endX = (int) (size.width * 0.8);
        int startY = (int) (size.height * 0.5);
        int endY = 0;

        swipe(startX, startY, endX, endY, 1000);
        Thread.sleep(3000);
    }

    public void pullToRefresh(String msg) throws InterruptedException {
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.5);
        int endX = (int) (size.width * 0.5);
        int startY = (int) (size.height * 0.3);
        int endY = (int) (size.height * 0.8);

        swipe(startX, startY, endX, endY, 1000);
        Thread.sleep(3000);
    }

    public void swipe(int startX, int startY, int endX, int endY, int millis)
            throws InterruptedException {
        TouchAction t = new TouchAction(driver);
        t.press(point(startX, startY)).waitAction(waitOptions(ofMillis(millis))).moveTo(point(endX, endY)).release()
                .perform();
    }

    public void clickByCoordinates(int startX, int startY) {
        TouchAction touchAction = new TouchAction(driver);
        touchAction.tap(point(startX, startY)).perform();

    }

    @Nullable
    public MobileElement findElementOrNull(final By selector, int timeout) {
        if (driver == null) {
            return null;
        }
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            return wait.until((ExpectedCondition<MobileElement>) driver -> {
                List<MobileElement> elementList;
                try {
                    elementList = driver.findElements(selector);
                } catch (NoSuchElementException exception) {
                    elementList = new ArrayList<>();
                    TestUtils.log().error("Element not found!", exception);
                }
                return elementList.size() > 0 ? elementList.get(0) : null;
            });
        } catch (Exception e) {
            TestUtils.log().warn("Mobile element not found " + selector.toString());
            return null;
        }
    }

    @Nullable
    public MobileElement findElementOrNull(final By selector) {
        return findElementOrNull(selector, 2);
    }

    public List<MobileElement> findElements(final By selector, int timeout) {
        if (driver == null) {
            return new ArrayList<MobileElement>();
        }
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            return wait.until(new ExpectedCondition<List<MobileElement>>() {
                @Override
                public List<MobileElement> apply(WebDriver driver) {
                    return driver.findElements(selector);
                }
            });
        } catch (Exception e) {
            TestUtils.log().warn("Mobile element not found", e);
            return new ArrayList<MobileElement>();
        }
    }

    public By chooseCurrentPlatformSelector(By androidSelector, By iOSSelector) {
        switch (PlatformHelper.get(driver)) {
            case ANDROID:
                return androidSelector;
            case iOS:
                return iOSSelector;
            default:
                return null;
        }
    }

    public boolean isSideMenuOpened() {
        By selector = chooseCurrentPlatformSelector(
                By.id(TestUtils.getFullAndroidId("imageView7")),
                MobileBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == \"Dashboard\"`][2]")
        );
        MobileElement mobileElement = findElementOrNull(selector, 2);
        return mobileElement != null && mobileElement.isDisplayed();
    }
}