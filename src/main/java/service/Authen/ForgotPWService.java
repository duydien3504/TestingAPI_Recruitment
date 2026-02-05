package service.Authen;

import api.APIClientFactory;
import api.ConfigLoad;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import models.Authen.ForgotPWRequest;

public class ForgotPWService {
    private APIRequestContext context;

    public ForgotPWService(APIRequestContext context) {
        this.context = context;
    }

    public APIResponse forgotPw(ForgotPWRequest data) {
        ConfigLoad config = ConfigLoad.getInstance();

        return context.post(config.getEPForgotPW(), RequestOptions.create().setData(data));
    }

    public APIResponse forgotPw(String email) {
        return forgotPw(new ForgotPWRequest(email));
    }
}
