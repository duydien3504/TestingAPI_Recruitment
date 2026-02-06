package service.JobService;

import api.ConfigLoad;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import models.Job.JobPostRequest;
import models.Job.JobPostResponse;

public class JobService {
    private APIRequestContext context;

    public JobService(APIRequestContext context) {
        this.context = context;
    }

    public APIResponse jobPost(JobPostRequest data) {
        ConfigLoad config = ConfigLoad.getInstance();

        return context.post(config.getEPJob(),
                RequestOptions.create()
                        .setData(data));
    }

    public APIResponse updateJob(String id, JobPostRequest data) {
        ConfigLoad config = ConfigLoad.getInstance();

        return context.put(config.getEPJob() + "/" + id
                , RequestOptions.create()
                        .setData(data));
    }

    public APIResponse deleteJob(String id) {
        ConfigLoad config = ConfigLoad.getInstance();
        return context.delete(config.getEPJob() + "/" + id);
    }
}
