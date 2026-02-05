package tests.Authen;

import api.APIClientFactory;
import api.ConfigLoad;
import base.BaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import data.TestGenerateAccount;
import models.Authen.ForgotPWRequest;
import models.Authen.ForgotPWResponse;
import models.Authen.LoginResponse;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import service.Authen.ForgotPWService;
import utils.JsonUtils;

public class ForgotPWTest extends BaseTest {
    private APIRequestContext requestContext;
    ConfigLoad config = ConfigLoad.getInstance();
    private ForgotPWService authen;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public void setup() {
        requestContext = APIClientFactory.createContext();
        authen = new ForgotPWService(requestContext);
    }

    @AfterClass
    public void tearDown() {
        if(requestContext != null) {
            requestContext.dispose();
        }
    }

    @Test
    public void tc_ForgotPWSuccess() {
        ForgotPWRequest data = new ForgotPWRequest(config.getEmail());

        APIResponse response = authen.forgotPw(data);
        Assert.assertEquals(response.status(), 200);

        System.out.println(response.status() + "\n" + response.text());

        ForgotPWResponse forgotPWResponse = JsonUtils.fromResponse(response, ForgotPWResponse.class);
        verifySuccessMessage(response, "Mã OTP đã được gửi đến email của bạn.");
    }

    @Test
    public void tc_ForgotPWFailWithEmptyEmail() {
        ForgotPWRequest data = new ForgotPWRequest("");

        APIResponse response = authen.forgotPw(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        verifyErrorMessage(response, "Email là bắt buộc.");
    }

    @Test
    public void tc_ForgotPWFailWithEmailIvlDomain() {
        ForgotPWRequest data = new ForgotPWRequest(TestGenerateAccount.emailInvDomain());

        APIResponse response = authen.forgotPw(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        verifyErrorMessage(response, "Email không đúng định dạng.");
    }
}
