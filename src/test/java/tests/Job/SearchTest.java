package tests.Job;

import api.APIClientFactory;
import api.ConfigLoad;
import base.BaseTest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import data.TestGenerateJob;
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
        APIClientFactory.createContext();
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
        Assert.assertEquals(response.status(), 200);
        verifySuccessMessage(response, "Lấy danh sách việc làm thành công.");
    }

    @Test
    public void tc_SearchJobSuccessWithKeyword() {
        SearchRequest data = SearchRequest.builder().keyword(config.getKwSearch()).build();
        APIResponse response = ss.searchAllJob(data);
        Assert.assertEquals(response.status(), 200);
        verifySuccessMessage(response, "Lấy danh sách việc làm thành công.");
    }

    @Test
    public void tc_SearchJobSuccess_WithPartialKeyword() {
        SearchRequest data = SearchRequest.builder().keyword(config.getPartialKw()).build();
        APIResponse response = ss.searchAllJob(data);
        Assert.assertEquals(response.status(), 200);
        verifySuccessMessage(response, "Lấy danh sách việc làm thành công.");
    }

    @Test
    public void tc_SearchJobSuccess_WithLocation() {
        SearchRequest data = SearchRequest.builder().location_id(TestGenerateJob.location_id()).build();
        APIResponse response = ss.searchAllJob(data);
        Assert.assertEquals(response.status(), 200);
        verifySuccessMessage(response, "Lấy danh sách việc làm thành công.");
    }

    @Test
    public void tc_SearchJobSuccess_WithCategory() {
        SearchRequest data = SearchRequest.builder().location_id(TestGenerateJob.category_id()).build();
        APIResponse response = ss.searchAllJob(data);
        Assert.assertEquals(response.status(), 200);
        verifySuccessMessage(response, "Lấy danh sách việc làm thành công.");
    }

    @Test
    public void tc_SearchJobFail_WithLocationIvl() {
        SearchRequest data = SearchRequest.builder().location_id(TestGenerateJob.locationId_Ivl()).build();
        APIResponse response = ss.searchAllJob(data);
        Assert.assertEquals(response.status(), 400);
        verifyErrorMessage(response, "Địa điểm không hợp lệ.");
    }

    @Test
    public void tc_SearchJobFail_WithLevelIvl() {
        SearchRequest data = SearchRequest.builder().category_id(TestGenerateJob.levelId_Ivl()).build();
        APIResponse response = ss.searchAllJob(data);
        Assert.assertEquals(response.status(), 400);
        verifyErrorMessage(response, "Ngành nghề không hợp lệ.");
    }

    @Test
    public void tc_SearchJobSuccessWithAllFilters() {
        SearchRequest data = new SearchRequest(10, 2, config.getKwSearch(), TestGenerateJob.category_id(), TestGenerateJob.location_id());
        APIResponse response = ss.searchAllJob(data);
        Assert.assertEquals(response.status(), 200);
        verifySuccessMessage(response, "Lấy danh sách việc làm thành công.");
    }

    @Test
    public void tc_SearchJobSuccessWithPagination() {
        SearchRequest data = SearchRequest.builder().page(10).limit(5).build();
        APIResponse response = ss.searchAllJob(data);
        Assert.assertEquals(response.status(), 200);
        verifySuccessMessage(response, "Lấy danh sách việc làm thành công.");
    }
}
