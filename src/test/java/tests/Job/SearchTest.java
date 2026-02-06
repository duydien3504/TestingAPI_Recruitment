package tests.Job;

import api.APIClientFactory;
import api.ConfigLoad;
import base.BaseTest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import models.Authen.LoginRequest;
import models.Job.SearchRequest;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import service.JobService.SearchService;

public class SearchTest extends BaseTest {
    private APIRequestContext context;
    private SearchService ss;
    ConfigLoad config = ConfigLoad.getInstance();

    @BeforeClass
    public void setup() {
        LoginRequest data = new LoginRequest(config.getEmail(), config.getPassword());
        String token = loginAndGetToken(data);
        APIClientFactory.initContextwithToken(token);
        context = APIClientFactory.getContext();
        ss = new SearchService(context);
    }

    @AfterClass
    public void tearDown() {
        if(context != null) {
            context.dispose();
        }
    }

    @Test
    public void tc_SearchJobSuccess() {
        SearchRequest data = new SearchRequest();
        APIResponse response = ss.searchAllJob(data);
        System.out.println(response.text());
        Assert.assertEquals(response.status(), 200);
        verifySuccessMessage(response, "Lấy danh sách việc làm thành công.");
    }
}
