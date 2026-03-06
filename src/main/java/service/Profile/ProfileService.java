package service.Profile;

import api.APIClientFactory;
import api.ConfigLoad;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.FilePayload;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import models.Profile.UpdateProfileRequest;
import utils.RandomFileUtils;

import java.nio.file.Files;
import java.nio.file.Path;

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

    public APIResponse updateAvatar(Path filePath) {
        try {
            byte[] fileBytes = Files.readAllBytes(filePath);

            FilePayload filePayload = new FilePayload(
                    filePath.getFileName().toString(),
                    Files.probeContentType(filePath),
                    fileBytes
            );

            return context.post(
                    config.getEPUploadAvt(),
                    RequestOptions.create()
                            .setMultipart(
                                    FormData.create()
                                            .set("file", filePayload)
                            )
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public APIResponse updateAvatarIvl(Path filePath) {
        return context.post(config.getEPUploadAvt(),
                RequestOptions.create()
                        .setMultipart(FormData.create()
                                .set("file", filePath)));
    }
}
