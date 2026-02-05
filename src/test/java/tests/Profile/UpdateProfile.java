package tests.Profile;

import api.APIClientFactory;
import api.ConfigLoad;
import base.BaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import data.TestGenerateAccount;
import models.Authen.LoginResponse;
import models.Profile.UpdateProfileRequest;
import models.Profile.UpdateProfileResponse;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import service.Profile.ProfileService;
import utils.JsonUtils;

public class UpdateProfile extends BaseTest {
    ConfigLoad config = ConfigLoad.getInstance();
    APIRequestContext context;
    ProfileService pfs;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public void setup() {
        String token = loginAndGetToken();
        APIClientFactory.initContextwithToken(token);
    }

    @AfterClass
    public void tearDown() {
        if(context != null) {
            context.dispose();
        }
    }

    @Test
    public void tc_UpdateProfileSuccess() {
        UpdateProfileRequest data = new UpdateProfileRequest(TestGenerateAccount.fullName()
                , TestGenerateAccount.phone()
                , TestGenerateAccount.address()
                , TestGenerateAccount.bio());

        System.out.println(data);

        APIResponse response = pfs.updateProfile(data);
        Assert.assertEquals(response.status(), 200);

        System.out.println(response.status() + "\n" + response.text());

        UpdateProfileResponse updateProfileResponse = JsonUtils.fromResponse(response, UpdateProfileResponse.class);
        Assert.assertEquals(updateProfileResponse.getMessage(), "Cập nhật thành công.");
    }

    @Test
    public void tc_UpdateProfileFailWithPhoneIvl() {
        UpdateProfileRequest data = new UpdateProfileRequest(TestGenerateAccount.fullName()
                , TestGenerateAccount.phoneIvl()
                , TestGenerateAccount.address()
                , TestGenerateAccount.bio());

        System.out.println(data);

        APIResponse response = pfs.updateProfile(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.text());

            Assert.assertEquals(json.get("error").get("message").asText(), "Số điện thoại không đúng định dạng Việt Nam.");
        } catch (Exception e) {
            Assert.fail("Không parse được JSON response", e);
        }
    }

    @Test
    public void tc_UpdateProfileFailWithEmptyFullname() {
        UpdateProfileRequest data = new UpdateProfileRequest(""
                , TestGenerateAccount.phone()
                , TestGenerateAccount.address()
                , TestGenerateAccount.bio());

        System.out.println(data);

        APIResponse response = pfs.updateProfile(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.text());

            Assert.assertEquals(json.get("error").get("message").asText(), "Họ tên là bắt buộc.");
        } catch (Exception e) {
            Assert.fail("Không parse được JSON response", e);
        }
    }

    @Test
    public void tc_UpdateProfileFailWithFullNameTooLong() {
        UpdateProfileRequest data = new UpdateProfileRequest(TestGenerateAccount.fullNameIvl()
                , TestGenerateAccount.phone()
                , TestGenerateAccount.address()
                , TestGenerateAccount.bio());


        APIResponse response = pfs.updateProfile(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.text());

            Assert.assertEquals(json.get("error").get("message").asText(), "Họ tên không được vượt quá 100 ký tự.");
        } catch (Exception e) {
            Assert.fail("Không parse được JSON response", e);
        }
    }
}
