package base;

import api.APIClientFactory;
import api.ConfigLoad;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import models.Authen.LoginRequest;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import service.Authen.LoginService;

import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    ConfigLoad config = ConfigLoad.getInstance();
    APIRequestContext context = APIClientFactory.createContext();
    private LoginService authen;
    ObjectMapper mapper = new ObjectMapper();


    public String loginAndGetToken() {
        try {
            authen = new LoginService(context);

            LoginRequest data = new LoginRequest(config.getEmail(), config.getPassword());
            APIResponse response = authen.login(data);

            if(response.status() != 200) {
                throw new RuntimeException("Đăng nhập thất bại! Status: " + response.status());
            }

            JsonNode json = mapper.readTree(response.body());
            String token = json.get("data").get("accessToken").asText();

            context.dispose();
            return token;
        } catch (Exception e) {
            throw new RuntimeException("Lấy token thất bại",e);
        }
    }

    public void verifySuccessMessage(APIResponse response, String expectedMessage) {
        try {
            JsonNode json = mapper.readTree(response.text());
            Assert.assertEquals(json.get("message").asText(), expectedMessage);
        } catch (Exception e) {
            Assert.fail("Không parse được JSON response: " + e.getMessage());
        }
    }

    public void verifyErrorMessage(APIResponse response, String expectedMessage) {
        try {
            JsonNode json = mapper.readTree(response.text());
            Assert.assertEquals(json.get("error").get("message").asText(), expectedMessage);
        } catch (Exception e) {
            Assert.fail("Không parse được JSON response: " + e.getMessage());
        }
    }
}
