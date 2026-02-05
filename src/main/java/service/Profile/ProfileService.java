package service.Profile;

import api.APIClientFactory;
import api.ConfigLoad;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import models.Profile.UpdateProfileRequest;

public class ProfileService {
    private APIRequestContext context;
    ConfigLoad config = ConfigLoad.getInstance();

    public ProfileService(APIRequestContext context) {
        this.context = context;
    }

    public APIResponse getProfile() {
        return context.get(config.getEPProfile());
    }

    public APIResponse updateProfile(UpdateProfileRequest data) {
        return  context.put(config.getEPProfile(), RequestOptions
                .create()
                .setData(data));
    }

}
