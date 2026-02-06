package tests.Job;

import api.APIClientFactory;
import api.ConfigLoad;
import base.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import data.TestGenerateAccount;
import data.TestGenerateJob;
import models.Authen.LoginRequest;
import models.Job.JobPostRequest;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import service.JobService.JobService;

public class JobTest extends BaseTest {
    ConfigLoad config = ConfigLoad.getInstance();
    private APIRequestContext context;
    private JobService js;

    @BeforeClass
    public void setup() {
        LoginRequest data = new LoginRequest(config.getEmail(), config.getPassword());
        String token = loginAndGetToken(data);
        APIClientFactory.initContextwithToken(token);
        context = APIClientFactory.getContext();
        js = new JobService(context);
    }

    @AfterClass
    public void tearDown() {
        if(context != null) {
            context.dispose();
        }
    }

    @Test
    public void tc_VerifyCreateJobPostSuccess() {
        JobPostRequest data = new JobPostRequest(TestGenerateJob.title()
                , TestGenerateJob.jd()
                , TestGenerateJob.requirements()
                , TestGenerateJob.category_id()
                , TestGenerateJob.location_id()
                , TestGenerateJob.level_id()
                , TestGenerateJob.salary_min()
                , TestGenerateJob.salary_max());

        System.out.println(data);

        APIResponse response = js.jobPost(data);

        Assert.assertEquals(response.status(), 201);
        verifySuccessMessage(response, "Tạo tin tuyển dụng thành công. Vui lòng thanh toán để đăng bài.");
    }

    @Test
    public void tc_VerifyCreateJobPostFailWithEmptyTitle() {
        JobPostRequest data = new JobPostRequest(""
                , TestGenerateJob.jd()
                , TestGenerateJob.requirements()
                , TestGenerateJob.category_id()
                , TestGenerateJob.location_id()
                , TestGenerateJob.level_id()
                , TestGenerateJob.salary_min()
                , TestGenerateJob.salary_max());

        System.out.println(data);

        APIResponse response = js.jobPost(data);

        Assert.assertEquals(response.status(), 400);
        verifyErrorMessage(response, "Vui lòng nhập đầy đủ thông tin bài tuyển dụng.");
    }

    @Test
    public void tc_VerifyCreateJobPostFailWithEmptyJD() {
        JobPostRequest data = new JobPostRequest(TestGenerateJob.title()
                , ""
                , TestGenerateJob.requirements()
                , TestGenerateJob.category_id()
                , TestGenerateJob.location_id()
                , TestGenerateJob.level_id()
                , TestGenerateJob.salary_min()
                , TestGenerateJob.salary_max());

        System.out.println(data);

        APIResponse response = js.jobPost(data);

        Assert.assertEquals(response.status(), 400);
        verifyErrorMessage(response, "Vui lòng nhập đầy đủ thông tin bài tuyển dụng.");
    }

    @Test
    public void tc_VerifyCreateJobPostFailWithEmptyRequirement() {
        JobPostRequest data = new JobPostRequest(TestGenerateJob.title()
                , TestGenerateJob.jd()
                , ""
                , TestGenerateJob.category_id()
                , TestGenerateJob.location_id()
                , TestGenerateJob.level_id()
                , TestGenerateJob.salary_min()
                , TestGenerateJob.salary_max());

        System.out.println(data);

        APIResponse response = js.jobPost(data);

        Assert.assertEquals(response.status(), 400);
        verifyErrorMessage(response, "Vui lòng nhập đầy đủ thông tin bài tuyển dụng.");
    }

    @Test
    public void tc_UpdateJobPostSuccess() {
        String id = TestGenerateJob.idJob();

        JobPostRequest data = new JobPostRequest(TestGenerateJob.title()
                , TestGenerateJob.jd()
                , TestGenerateJob.requirements()
                , TestGenerateJob.category_id()
                , TestGenerateJob.location_id()
                , TestGenerateJob.level_id()
                , TestGenerateJob.salary_min()
                , TestGenerateJob.salary_max());

        APIResponse response = js.updateJob(id, data);

        Assert.assertEquals(response.status(), 200);

        verifySuccessMessage(response, "Cập nhật tin tuyển dụng thành công.");
    }

    @Test
    public void tc_DeleteJobSuccess() {
        String id = TestGenerateJob.idJob();
        APIResponse response = js.deleteJob(id);

        verifySuccessMessage(response, "Đã xóa tin tuyển dụng.");
    }
}
