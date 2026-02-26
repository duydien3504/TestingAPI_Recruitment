package api;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;

import java.util.HashMap;
import java.util.Map;

public class APIClientFactory {
    private static final ThreadLocal<Playwright> playwright = ThreadLocal.withInitial(Playwright::create);
    private static final ThreadLocal<APIRequestContext> requestContext = new ThreadLocal<>();


    public static APIRequestContext createContext() {
        ConfigLoad config = ConfigLoad.getInstance();
        APIRequestContext context = playwright.get().request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(config.getBaseUrl())
                .setIgnoreHTTPSErrors(true));
        
        requestContext.set(context);
        return context;
    }

    public static void initContextwithToken(String token) {
        ConfigLoad config = ConfigLoad.getInstance();
        Map<String, String> headers = new HashMap<>();

        //headers.put("Content-Type", "application/json");
        //headers.put("Accept", "application/json");

        if(token != null && !token.isEmpty()) {
            headers.put("Authorization", "Bearer " + token);
        }

        APIRequestContext context = playwright.get().request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(config.getBaseUrl())
                .setExtraHTTPHeaders(headers)
                .setIgnoreHTTPSErrors(true));

        requestContext.set(context);
    }

    public static APIRequestContext getContext() {
        return requestContext.get();
    }

    public static void close() {
        if(requestContext.get() != null) {
            requestContext.get().dispose();
            requestContext.remove();
        }
    }
}
