package service.JobService;

import api.ConfigLoad;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import models.Job.SearchRequest;

public class SearchService {
    private APIRequestContext context;

    public SearchService(APIRequestContext context) {
        this.context = context;
    }

    public APIResponse searchAllJob(SearchRequest data) {
        ConfigLoad config = ConfigLoad.getInstance();

        return context.get(config.getEPJob(), RequestOptions.create().setData(data));
    }
}
