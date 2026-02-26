package tests.Profile;

import api.APIClientFactory;
import api.ConfigLoad;
import base.BaseTest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import data.TestGenerateAccount;
import models.Authen.LoginRequest;
import models.Profile.UpdateProfileRequest;
import models.Profile.UpdateProfileResponse;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;
import service.Profile.ProfileService;
import utils.RandomFileUtils;

public class Profile extends BaseTest{
    ConfigLoad config = ConfigLoad.getInstance();
    private APIRequestContext context;
    private String token;

    @BeforeClass
    public void setup() {
        LoginRequest data = new LoginRequest(config.getEmail(), config.getPassword());
        token = loginAndGetToken(data);

    }

    public ProfileService getPfsWithToken(String accessToken) {
        APIClientFactory.initContextwithToken(accessToken);
        context = APIClientFactory.getContext();
        return new ProfileService(context);
    }

    public ProfileService getPfsWithoutToken() {
        APIClientFactory.createContext();
        context = APIClientFactory.getContext();
        return new ProfileService(context);
    }

    @Test
    public void tc_GETProfile() {
        ProfileService pfs = getPfsWithToken(token);
        APIResponse response = pfs.getProfile();
        Assert.assertEquals(response.status(), 200);
        System.out.println(response.status() + "\n" + response.text());
        verifySuccessMessage(response, "Lấy thông tin thành công.");
    }

    @Test
    public void tc_UpdateProfileSuccess() {
        ProfileService pfs = getPfsWithToken(token);
        UpdateProfileRequest data = new UpdateProfileRequest(TestGenerateAccount.fullName()
                , TestGenerateAccount.phone()
                , TestGenerateAccount.address()
                , TestGenerateAccount.bio());

        System.out.println(data);

        APIResponse response = pfs.updateProfile(data);
        Assert.assertEquals(response.status(), 200);

        System.out.println(response.status() + "\n" + response.text());

        verifySuccessMessage(response, "Cập nhật thành công.");
    }

    @Test
    public void tc_UpdateProfileFailWithPhoneIvl() {
        ProfileService pfs = getPfsWithToken(token);
        UpdateProfileRequest data = new UpdateProfileRequest(TestGenerateAccount.fullName()
                , TestGenerateAccount.phoneIvl()
                , TestGenerateAccount.address()
                , TestGenerateAccount.bio());

        System.out.println(data);

        APIResponse response = pfs.updateProfile(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        verifyErrorMessage(response, "Số điện thoại không đúng định dạng Việt Nam.");
    }

    @Test
    public void tc_UpdateProfileFailWithEmptyFullname() {
        ProfileService pfs = getPfsWithToken(token);
        UpdateProfileRequest data = new UpdateProfileRequest(""
                , TestGenerateAccount.phone()
                , TestGenerateAccount.address()
                , TestGenerateAccount.bio());

        System.out.println(data);

        APIResponse response = pfs.updateProfile(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        verifyErrorMessage(response, "Họ tên là bắt buộc.");
    }

    @Test
    public void tc_UpdateProfileFailWithFullNameTooLong() {
        ProfileService pfs = getPfsWithToken(token);
        UpdateProfileRequest data = new UpdateProfileRequest(TestGenerateAccount.fullNameIvl()
                , TestGenerateAccount.phone()
                , TestGenerateAccount.address()
                , TestGenerateAccount.bio());


        APIResponse response = pfs.updateProfile(data);
        Assert.assertEquals(response.status(), 400);

        System.out.println(response.status() + "\n" + response.text());

        verifyErrorMessage(response, "Họ tên không được vượt quá 100 ký tự.");
    }

    @Test
    public void tc_UpdateProfileSuccessWithFullname() {
        ProfileService pfs = getPfsWithToken(token);
        UpdateProfileRequest data = UpdateProfileRequest.builder().fullName(TestGenerateAccount.fullName()).build();
        APIResponse response = pfs.updateProfile(data);

        Assert.assertEquals(response.status(), 200);
        verifySuccessMessage(response, "Cập nhật thành công.");

        verifyResponseMatchesModel(response.text(), UpdateProfileResponse.class);
    }

    @Test
    public void tc_VerifyGetProfileFailWithoutToken() {
        ProfileService pfs = getPfsWithoutToken();
        APIResponse response = pfs.getProfile();
        Assert.assertEquals(response.status(), 401);
        verifyErrorMessage(response, "Token xác thực là bắt buộc.");
    }

    @Test
    public void tc_UpdateProfileFailWithoutToken() {
        ProfileService pfs = getPfsWithoutToken();
        UpdateProfileRequest data = new UpdateProfileRequest(TestGenerateAccount.fullName()
                , TestGenerateAccount.phone()
                , TestGenerateAccount.address()
                , TestGenerateAccount.bio());

        System.out.println(data);

        APIResponse response = pfs.updateProfile(data);
        Assert.assertEquals(response.status(), 401);


        verifyErrorMessage(response, "Token xác thực là bắt buộc.");
    }

    @Test
    public void tc_VerifyUploadAvatarSuccess() {
        ProfileService pfs = getPfsWithToken(token);
        String folderPath = "E:\\BE_Recruit\\DataTest";

        java.nio.file.Path imagePath = RandomFileUtils.getFileAvatarValid(folderPath);

        APIResponse response = pfs.updateAvatar(imagePath);

        System.out.println("Response: " + response.text());
        Assert.assertEquals(response.status(), 200);
        verifySuccessMessage(response, "Upload avatar thành công.");
    }

    @Test
    public void tc_VerifyUploadAvatarFailWithoutToken() {
        ProfileService pfs = getPfsWithoutToken();
        String folderPath = "E:\\BE_Recruit\\DataTest";

        java.nio.file.Path imagePath = RandomFileUtils.getFileAvatarValid(folderPath);

        APIResponse response = pfs.updateAvatar(imagePath);

        System.out.println("Response: " + response.text());
        Assert.assertEquals(response.status(), 401);
        verifyErrorMessage(response, "Token xác thực là bắt buộc.");
    }

    @Test
    public void tc_VerifyUploadAvatarFailWithFileIvl() {
        ProfileService pfs = getPfsWithToken(token);
        String folderPath = "E:\\BE_Recruit\\DataTest";

        java.nio.file.Path imagePath = RandomFileUtils.getFileAvatarIvlPath(folderPath);

        APIResponse response = pfs.updateAvatar(imagePath);

        System.out.println("Response: " + response.text());
        Assert.assertEquals(response.status(), 400);
        verifyErrorMessage(response, "Chỉ chấp nhận file ảnh (jpg, jpeg, png).");
    }

    @AfterMethod
    public void tearDown() {
        APIClientFactory.close();
    }
}
