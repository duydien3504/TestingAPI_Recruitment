package service.Profile;

import api.APIClientFactory;
import api.ConfigLoad;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import models.Profile.UpdateProfileRequest;

public class ProfileService {
    public static APIResponse getProfile() {
        ConfigLoad config = ConfigLoad.getInstance();
        APIRequestContext context = APIClientFactory.getContext();

        return context.get(config.getEPProfile());
    }

    public static APIResponse updateProfile(UpdateProfileRequest data) {
        ConfigLoad config = ConfigLoad.getInstance();
        APIRequestContext context = APIClientFactory.getContext();

        return  context.put(config.getEPProfile(), RequestOptions
                .create()
                .setData(data));
    }

}
