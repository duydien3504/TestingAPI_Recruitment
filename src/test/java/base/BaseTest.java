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
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    ConfigLoad config = ConfigLoad.getInstance();
    private LoginService authen;
    ObjectMapper mapper = new ObjectMapper();

    public String loginAndGetToken(LoginRequest data) {
        APIRequestContext tempContext = APIClientFactory.createContext();
        try {
            authen = new LoginService(tempContext);

            APIResponse response = authen.login(data);

            if (response.status() != 200) {
                System.out.println("Login fail body: " + response.text());
                throw new RuntimeException("Đăng nhập thất bại! Status: " + response.status());
            }

            JsonNode json = mapper.readTree(response.body());
            String token = json.get("data").get("accessToken").asText();

            return token;
        } catch (Exception e) {
            throw new RuntimeException("Lấy token thất bại", e);
        } finally {
            if (tempContext != null) {
                tempContext.dispose();
            }
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


    protected <T> void verifyResponseMatchesModel(String responseBody, Class<T> clazz) {
        try {
            T obj = mapper.readValue(responseBody, clazz);
            validateObject(obj);
        } catch (Exception e) {
            throw new RuntimeException("Xác thực phản hồi thất bại", e);
        }
    }

    private void validateObject(Object obj) throws IllegalAccessException {

        if (obj == null) {
            throw new AssertionError("Đối tượng rỗng");
        }

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(obj);

            if (value == null) {
                throw new AssertionError("Trường rỗng: " + field.getName());
            }

            if (!field.getType().isPrimitive() && !field.getType().getName().startsWith("java.lang")) {

                validateObject(value);
            }
        }
    }
}
