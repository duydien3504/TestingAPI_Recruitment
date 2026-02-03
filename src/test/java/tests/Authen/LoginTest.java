package tests.Authen;

import api.APIClientFactory;
import api.ConfigLoad;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import data.TestGenerateAccount;
import models.Authen.LoginRequest;
import models.Authen.LoginResponse;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import service.Authen.LoginService;
import utils.JsonUtils;

public class LoginTest {
    ConfigLoad config = ConfigLoad.getInstance();
    private APIRequestContext requestContext;
    private LoginService authen;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public void setup() {
        requestContext = APIClientFactory.createContext();
    }

    @AfterClass
    public void tearDown() {
        if(requestContext != null) {
            requestContext.dispose();
        }
    }

    @Test
    public void tc_LoginSuccess() {
        LoginRequest data = new LoginRequest(config.getEmail(), config.getPassword());

        APIResponse response = authen.login(data);
        Assert.assertEquals(response.status(), 200);

        System.out.println(response.status() + "\n" + response.text());

        LoginResponse loginResponse = JsonUtils.fromResponse(response, LoginResponse.class);
        Assert.assertEquals(loginResponse.getMessage(), "Đăng nhập thành công.");
    }

    @Test
    public void tc_LoginFailWithEmailIvl() {
        LoginRequest data = new LoginRequest(TestGenerateAccount.emailInv(), TestGenerateAccount.password());

        APIResponse response = authen.login(data);
        System.out.println(response.status() + "\n" + response.text());

        Assert.assertEquals(response.status(), 400);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.text());

            Assert.assertEquals(json.get("error").get("message").asText(), "Email không đúng định dạng.");
        } catch (Exception e) {
            Assert.fail("Không parse được JSON response", e);
        }
    }

    @Test
    public void tc_LoginFailWithEmailIvlDomain() {
        LoginRequest data = new LoginRequest(TestGenerateAccount.emailInvDomain(), TestGenerateAccount.password());

        APIResponse response = authen.login(data);
        System.out.println(response.status() + "\n" + response.text());

        Assert.assertEquals(response.status(), 401);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.text());

            Assert.assertEquals(json.get("error").get("message").asText(), "Email hoặc mật khẩu không chính xác.");
        } catch (Exception e) {
            Assert.fail("Không parse được JSON response", e);
        }
    }

    @Test
    public void tc_LoginFailWithPasswordIvl() {
        LoginRequest data = new LoginRequest(config.getEmail(), TestGenerateAccount.passwordIvl());

        APIResponse response = authen.login(data);
        System.out.println(response.status() + "\n" + response.text());

        Assert.assertEquals(response.status(), 401);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.text());

            Assert.assertEquals(json.get("error").get("message").asText(), "Email hoặc mật khẩu không chính xác.");
        } catch (Exception e) {
            Assert.fail("Không parse được JSON response", e);
        }
    }

    @Test
    public void tc_LoginFailWithoutEmail() {
        LoginRequest data = new LoginRequest("", TestGenerateAccount.password());

        APIResponse response = authen.login(data);
        System.out.println(response.status() + "\n" + response.text());

        Assert.assertEquals(response.status(), 400);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.text());

            Assert.assertEquals(json.get("error").get("message").asText(), "Email là bắt buộc.");
        } catch (Exception e) {
            Assert.fail("Không parse được JSON response", e);
        }
    }

    @Test
    public void tc_LoginFailWithoutPassword() {
        LoginRequest data = new LoginRequest(config.getEmail(), "");

        APIResponse response = authen.login(data);
        System.out.println(response.status() + "\n" + response.text());

        Assert.assertEquals(response.status(), 400);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.text());

            Assert.assertEquals(json.get("error").get("message").asText(), "Mật khẩu là bắt buộc.");
        } catch (Exception e) {
            Assert.fail("Không parse được JSON response", e);
        }
    }
}
