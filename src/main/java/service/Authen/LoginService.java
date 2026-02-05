package service.Authen;

import api.APIClientFactory;
import api.ConfigLoad;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import models.Authen.LoginRequest;

public class LoginService {
    private APIRequestContext context;
    
    public LoginService(APIRequestContext context) {
        this.context = context;
    }

    public APIResponse login(LoginRequest data) {
        ConfigLoad config = ConfigLoad.getInstance();

        return context.post(config.getEPLogin(),
                RequestOptions.create().setData(data));
    }

    public APIResponse login(String email, String password) {
        return login(new LoginRequest(email, password));
    }
}
