package service.Authen;

import api.APIClientFactory;
import api.ConfigLoad;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import models.Authen.ForgotPWRequest;

public class ForgotPWService {
    public static APIResponse forgotPw(ForgotPWRequest data) {
        ConfigLoad config = ConfigLoad.getInstance();
        APIRequestContext context = APIClientFactory.createContext();

        return context.post(config.getEPForgotPW(), RequestOptions.create().setData(data));
    }

    public static APIResponse forgotPw(String email) {
        return forgotPw(new ForgotPWRequest(email));
    }
}
