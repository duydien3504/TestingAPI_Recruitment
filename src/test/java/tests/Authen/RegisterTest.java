package tests.Authen;

import api.APIClientFactory;
import api.ConfigLoad;
import base.BaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import data.TestGenerateAccount;
import models.Authen.LoginResponse;
import models.Authen.RegisterRequest;
import models.Authen.RegisterResponse;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import service.Authen.RegisterService;
import utils.JsonUtils;

public class RegisterTest extends BaseTest {
    ConfigLoad config = ConfigLoad.getInstance();
    private APIRequestContext context;
    private RegisterService authen;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public void setup() {
        context = APIClientFactory.createContext();
        authen = new RegisterService(context);
    }

    @AfterClass
    public void tearDown() {
        if(context != null)
            context.dispose();
    }

    @Test
    public void tc_RegisterSuccessful() {
        RegisterRequest data = new RegisterRequest(TestGenerateAccount.email(), TestGenerateAccount.password(), TestGenerateAccount.fullName());

        APIResponse response = authen.register(data);
        Assert.assertEquals(response.status(), 201);

        System.out.println(response.status() + "\n" + response.text());

        verifySuccessMessage(response, "Đăng ký tài khoản thành công. Vui lòng kiểm tra email để xác thực.");
    }

    @Test
    public void tc_RegisterFailWithEmaiIvl() {
        RegisterRequest data = new RegisterRequest(TestGenerateAccount.emailInv(), TestGenerateAccount.password(), TestGenerateAccount.fullName());

        APIResponse response = authen.register(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        verifyErrorMessage(response, "Email không đúng định dạng.");
    }

    @Test
    public void tc_RegisterFailWithEmailIvlDomain() {
        RegisterRequest data = new RegisterRequest(TestGenerateAccount.emailInvDomain(), TestGenerateAccount.password(), TestGenerateAccount.fullName());

        System.out.println(data);

        APIResponse response = authen.register(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        verifyErrorMessage(response, "Email không đúng định dạng.");
    }

    @Test
    public void tc_RegisterFailWithPasswordIvl() {
        RegisterRequest data = new RegisterRequest(TestGenerateAccount.email(), TestGenerateAccount.passwordIvl(), TestGenerateAccount.fullName());

        APIResponse response = authen.register(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        verifyErrorMessage(response, "Mật khẩu phải có ít nhất 8 ký tự.");
    }

    @Test
    public void tc_RegisterFailWithPasswordwithoutUppercase() {
        RegisterRequest data = new RegisterRequest(TestGenerateAccount.email(), TestGenerateAccount.passwordWithoutUppercase(), TestGenerateAccount.fullName());

        APIResponse response = authen.register(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        verifyErrorMessage(response, "Mật khẩu phải chứa ít nhất 1 chữ hoa và 1 số.");
    }

    @Test
    public void tc_RegisterFailWithEmptyEmail() {
        RegisterRequest data = new RegisterRequest("", TestGenerateAccount.password(), TestGenerateAccount.fullName());

        APIResponse response = authen.register(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        verifyErrorMessage(response, "Email là bắt buộc.");
    }

    @Test
    public void tc_RegisterFailWithEmptyPassword() {
        RegisterRequest data = new RegisterRequest(TestGenerateAccount.email(),"", TestGenerateAccount.fullName());

        APIResponse response = authen.register(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        verifyErrorMessage(response, "Mật khẩu là bắt buộc.");
    }
}
