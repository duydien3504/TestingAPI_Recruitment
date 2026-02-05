package service.Authen;

import api.APIClientFactory;
import api.ConfigLoad;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import models.Authen.RegisterRequest;

public class RegisterService {
    private APIRequestContext context;

    public RegisterService(APIRequestContext context) {
        this.context = context;
    }

    public APIResponse register(RegisterRequest data) {
        ConfigLoad config = ConfigLoad.getInstance();

        return context.post(config.getEPRegister(), RequestOptions.create().setData(data));
    }

    public APIResponse register(String email, String password, String full_name) {
        return register(new RegisterRequest(email, password, full_name));
    }
}
