package test.java.com.qa.pages;

import com.qa.utils.TestUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

import java.util.List;

public class OnboardingPage extends BasePage {

    public OnboardingPage() {
    }

    //@AndroidFindBy(xpath = "//android.widget.TextView[@text='Announcements']")
    @AndroidFindBy(xpath = "//android.widget.LinearLayout/android.view.ViewGroup/android.widget.TextView")
    @iOSXCUITFindBy(iOSClassChain = "**/XCUIElementTypeStaticText[`label == \"Announcements\"`]")
    private MobileElement activate;

    @AndroidFindBy(id = TestUtils.APPLICATION_ID + TestUtils.ID_PART + "textView28")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeCell[@name='announcement_cell']")
    private List<MobileElement> nextBtn;

    @AndroidFindBy(id = TestUtils.APPLICATION_ID + TestUtils.ID_PART + "read_more_btn")
    @iOSXCUITFindBy(iOSClassChain = "**/XCUIElementTypeButton[`label == \"READ MORE\"`][1]")
    private MobileElement readMore;

    @AndroidFindBy(xpath = "//android.widget.LinearLayout/android.view.ViewGroup/androidx.appcompat.widget.LinearLayoutCompat")
    //no element on iOS App
    private MobileElement moreOptions;

    @AndroidFindBy(id = TestUtils.APPLICATION_ID + TestUtils.ID_PART + "title")
    //no element on iOS App
    private MobileElement refreshButton;

    public String getTitle() {
        return getText(announcementsTile, "Announcements page title is - ");
    }

    public void selectAnnouncement(int index) {
        click((MobileElement) announcementsList.get(index), "Display " + index + "Announcement");
    }

    public void clickReadMore() throws Exception {
        click(scrollToElement(readMore, "down"), "Click on Read More");
    }

    public void clickMoreOptions() throws Exception {
        click((MobileElement) moreOptions, "Click on ellipsis button ");
    }

    public void clickRefreshButton() throws Exception {
        click((MobileElement) refreshButton, "Click on Refresh button");
    }
}
