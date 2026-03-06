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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import service.JobService.JobService;

public class JobTest extends BaseTest {
    ConfigLoad config = ConfigLoad.getInstance();
    private JobService js;
    private String adminToken;
    private String candidateToken;

    @BeforeClass
    public void setup() {
        LoginRequest adminData = new LoginRequest(config.getEmail(), config.getPassword());
        adminToken = loginAndGetToken(adminData);

        LoginRequest candidateData = new LoginRequest(config.getCandidateEmail(), config.getCandidatePassword());
        candidateToken = loginAndGetToken(candidateData);
    }

    private JobService getJSWithtoken(String token) {
        APIClientFactory.initContextwithToken(token);
        APIRequestContext context = APIClientFactory.getContext();
        return new JobService(context);
    }

    private JobService getJSWithoutToken() {
        APIClientFactory.createContext();
        APIRequestContext context = APIClientFactory.getContext();
        return new JobService(context);
    }


    @Test
    public void tc_VerifyCreateJobPostSuccess() {
        JobService js = getJSWithtoken(adminToken);
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
        JobService js = getJSWithtoken(adminToken);
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
        JobService js = getJSWithtoken(adminToken);
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
        JobService js = getJSWithtoken(adminToken);
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
        JobService js = getJSWithtoken(adminToken);
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
        JobService js = getJSWithtoken(adminToken);
        String id = TestGenerateJob.idJob();
        APIResponse response = js.deleteJob(id);
        Assert.assertEquals(response.status(), 200);
        verifySuccessMessage(response, "Đã xóa tin tuyển dụng.");
    }

    @Test
    public void tc_DeleteJobNotExist() {
        JobService js = getJSWithtoken(adminToken);
        String id = TestGenerateJob.idJobIvl();
        APIResponse response = js.deleteJob(id);
        Assert.assertEquals(response.status(), 404);
        verifyErrorMessage(response, "Không tìm thấy việc làm.");
    }

    @Test
    public void tc_VerifyCreateJobPostFailWithCandidateAccount() {
        JobService js = getJSWithtoken(candidateToken);
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

        Assert.assertEquals(response.status(), 403);
        verifyErrorMessage(response, "Không có quyền sử dụng chức năng đăng bài tuyển dụng.");
    }

    @Test
    public void tc_VerifyCreateJobPostFailWithoutToken() {
        JobService js = getJSWithoutToken();
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

        Assert.assertEquals(response.status(), 401);
        verifyErrorMessage(response, "Token xác thực là bắt buộc.");
    }

    @Test
    public void tc_VerifyCreateJobFailWithSalaryIvl(){
        JobService js = getJSWithtoken(adminToken);
        JobPostRequest data = new JobPostRequest(TestGenerateJob.title()
                , TestGenerateJob.jd()
                , TestGenerateJob.requirements()
                , TestGenerateJob.category_id()
                , TestGenerateJob.location_id()
                , TestGenerateJob.level_id()
                , TestGenerateJob.salary_max()
                , TestGenerateJob.salary_min());

        System.out.println(data);

        APIResponse response = js.jobPost(data);

        Assert.assertEquals(response.status(), 400);
        verifyErrorMessage(response, "Khoảng lương không hợp lệ.");
    }

    @AfterMethod
    public void cleanupContext() {
        APIClientFactory.close();
    }
}
