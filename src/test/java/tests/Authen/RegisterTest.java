package tests.Authen;

import api.APIClientFactory;
import api.ConfigLoad;
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

public class RegisterTest {
    ConfigLoad config = ConfigLoad.getInstance();
    private APIRequestContext context;
    private RegisterService authen;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public void setup() {
        context = APIClientFactory.createContext();
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

        RegisterResponse registerResponse = JsonUtils.fromResponse(response, RegisterResponse.class);
        Assert.assertEquals(registerResponse.getMessage(), "Đăng ký tài khoản thành công. Vui lòng kiểm tra email để xác thực.");
    }

    @Test
    public void tc_RegisterFailWithEmaiIvl() {
        RegisterRequest data = new RegisterRequest(TestGenerateAccount.emailInv(), TestGenerateAccount.password(), TestGenerateAccount.fullName());

        APIResponse response = authen.register(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.text());

            Assert.assertEquals(json.get("error").get("message").asText(), "Email không đúng định dạng.");
        } catch (Exception e) {
            Assert.fail("Không parse được JSON response", e);
        }
    }

    @Test
    public void tc_RegisterFailWithEmailIvlDomain() {
        RegisterRequest data = new RegisterRequest(TestGenerateAccount.emailInvDomain(), TestGenerateAccount.password(), TestGenerateAccount.fullName());

        APIResponse response = authen.register(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.text());

            Assert.assertEquals(json.get("error").get("message").asText(), "Email không đúng định dạng.");
        } catch (Exception e) {
            Assert.fail("Không parse được JSON response", e);
        }
    }

    @Test
    public void tc_RegisterFailWithPasswordIvl() {
        RegisterRequest data = new RegisterRequest(TestGenerateAccount.email(), TestGenerateAccount.passwordIvl(), TestGenerateAccount.fullName());

        APIResponse response = authen.register(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.text());

            Assert.assertEquals(json.get("error").get("message").asText(), "Mật khẩu phải có ít nhất 8 ký tự.");
        } catch (Exception e) {
            Assert.fail("Không parse được JSON response", e);
        }
    }

    @Test
    public void tc_RegisterFailWithPasswordwithoutUppercase() {
        RegisterRequest data = new RegisterRequest(TestGenerateAccount.email(), TestGenerateAccount.passwordWithoutUppercase(), TestGenerateAccount.fullName());

        APIResponse response = authen.register(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.text());

            Assert.assertEquals(json.get("error").get("message").asText(), "Mật khẩu phải chứa ít nhất 1 chữ hoa và 1 số.");
        } catch (Exception e) {
            Assert.fail("Không parse được JSON response", e);
        }
    }

    @Test
    public void tc_RegisterFailWithEmptyEmail() {
        RegisterRequest data = new RegisterRequest("", TestGenerateAccount.password(), TestGenerateAccount.fullName());

        APIResponse response = authen.register(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.text());

            Assert.assertEquals(json.get("error").get("message").asText(), "Email là bắt buộc.");
        } catch (Exception e) {
            Assert.fail("Không parse được JSON response", e);
        }
    }

    @Test
    public void tc_RegisterFailWithEmptyPassword() {
        RegisterRequest data = new RegisterRequest(TestGenerateAccount.email(),"", TestGenerateAccount.fullName());

        APIResponse response = authen.register(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.text());

            Assert.assertEquals(json.get("error").get("message").asText(), "Mật khẩu là bắt buộc.");
        } catch (Exception e) {
            Assert.fail("Không parse được JSON response", e);
        }
    }
}
