package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;

public class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {}

    public static <T> T fromResponse(APIResponse response, Class<T> tClass) {
        try {
            return MAPPER.readValue(response.text(), tClass);
        } catch (Exception e) {
            throw new RuntimeException("Chuyen doi API response that bai!", e);
        }
    }
}
