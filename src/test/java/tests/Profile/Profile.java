package tests.Profile;

import api.APIClientFactory;
import api.ConfigLoad;
import base.BaseTest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;
import org.testng.annotations.*;
import service.Profile.ProfileService;

public class Profile extends BaseTest{
    ConfigLoad config = ConfigLoad.getInstance();
    private APIRequestContext context;
    private ProfileService prf;

    @BeforeClass
    public void setup() {
        String token = loginAndGetToken();
        APIClientFactory.initContextwithToken(token);
    }

    @AfterClass
    public void tearDown() {
        if(context != null) {
            context.dispose();
        }
    }

    @Test
    public void tc_GETProfile() {
        APIResponse response = prf.getProfile();
        Assert.assertEquals(response.status(), 200);
        System.out.println(response.status() + "\n" + response.text());
    }
}
