package service.Authen;

import api.APIClientFactory;
import api.ConfigLoad;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import models.Authen.LoginRequest;

public class LoginService {
    public static APIResponse login(LoginRequest data) {
        ConfigLoad config = ConfigLoad.getInstance();
        APIRequestContext context = APIClientFactory.createContext();

        return context.post(config.getEPLogin(),
                RequestOptions.create().setData(data));
    }

    public static APIResponse login(String email, String password) {
        return login(new LoginRequest(email, password));
    }
}
