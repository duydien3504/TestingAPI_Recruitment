# Báo cáo Code Review Chi Tiết

## 1. Tổng quan Dự án
*   **Ngôn ngữ:** Java 21.
*   **Framework:** Playwright (API Testing), TestNG.
*   **Thư viện:** Lombok, Jackson, AssertJ, JavaFaker.
*   **Kiến trúc:** Service Object Model (SOM).

## 2. Điểm Mạnh (Pros)
*   **Cấu trúc thư mục:** Phân chia rõ ràng (`api`, `models`, `service`, `tests`), tuân thủ mô hình SOM giúp code dễ đọc.
*   **Thread Safety:** Sử dụng `ThreadLocal` trong `APIClientFactory` cho `Playwright` instance, là tiền đề tốt cho việc chạy test song song (parallel execution).
*   **Mô hình hóa dữ liệu (Data Modeling):** Sử dụng POJO kết hợp Lombok cho Request/Response body giúp code gọn gàng, tránh lỗi cú pháp JSON thủ công.
*   **Cấu hình linh hoạt:** `ConfigLoad` hỗ trợ đọc properties và ghi đè qua System Properties (tốt cho CI/CD).

## 3. Các Vấn đề Nghiêm trọng & Cần khắc phục (Critical Issues)

### A. Quản lý Resource & Context (Rất Quan Trọng)
*   **Rò rỉ tài nguyên (Resource Leak):**
    *   Trong `LoginService` và `RegisterService`, phương thức `login/register` gọi `APIClientFactory.createContext()`.
    *   Hàm `createContext()` tạo một **context mới** mỗi lần gọi (`newContext()`) nhưng **không lưu lại** và **không đóng** (dispose) sau khi request xong.
    *   **Hậu quả:** Mỗi request tạo ra một process browser context treo lơ lửng, gây tràn bộ nhớ nhanh chóng khi số lượng test tăng lên.
*   **Sự không nhất quán trong `APIClientFactory`:**
    *   Hàm `createContext()`: Trả về instance mới, KHÔNG lưu vào `ThreadLocal`.
    *   Hàm `initContextwithToken()`: Tạo instance mới VÀ lưu vào `ThreadLocal`.
    *   **Hậu quả:** Gây nhầm lẫn cho người sử dụng thư viện API này.

### B. Logic Test & "Ảo giác Setup" (Test Logic Flaws)
*   **Setup vô nghĩa:**
    *   Trong `LoginTest` (và các test khác), `@BeforeClass` gọi `requestContext = APIClientFactory.createContext()`.
    *   Tuy nhiên, `LoginService` lại tự tạo context riêng bên trong nó.
    *   **Hậu quả:** Context được tạo ở `@BeforeClass` hoàn toàn không được sử dụng, gây lãng phí tài nguyên và tạo "ảo giác" rằng test đã được cấu hình chung (ví dụ: base URL, header).
*   **Bad Practice - Static Access via Null Instance:**
    *   Khai báo `private LoginService authen;` (mặc định là null).
    *   Gọi `authen.login(...)`. Vì `login` là `static`, Java cho phép gọi qua biến null mà không lỗi `NullPointerException`.
    *   **Đánh giá:** Đây là bad practice, gây hiểu nhầm code là hướng đối tượng (Instance method) nhưng thực chất là thủ tục (Static method).

### C. Chất lượng Code & Code Smell
*   **Dead Code:** Các field `authen` trong test class là thừa thãi.
*   **Hardcoded Strings:** Message lỗi ("Email không đúng định dạng", v.v.) bị hardcode trong từng test case. Khi message hệ thống đổi, maintenance sẽ rất cực.
*   **Duplication:** Logic parse JSON (`mapper.readTree...`) lặp lại trong mọi test case failure. Nên tách ra hàm tiện ích chung.
*   **System.out.println:** Sử dụng `System.out` thay vì Logger. Không tốt cho việc trace log trên CI server.

### D. Thiếu sót về Cấu hình & Dữ liệu
*   **Missing Config:** Không tìm thấy file `src/main/resources/config.properties`. Project sẽ crash ngay lập tức nếu chạy.
*   **Data Generation:** Class `TestGenerateAccount` dùng lẫn lộn `Faker` và `Math.random`. Việc tự viết logic random thủ công (ví dụ: `(int)(Math.random() * 9000)`) dễ gây trùng lặp dữ liệu và khó debug hơn thư viện chuyên dụng.

## 4. Đề xuất Cải tiến (Action Plan)

### Bước 1: Refactor Core API & Service (Ưu tiên cao)
*   Sửa `APIClientFactory`: Đảm bảo `getContext()` luôn trả về context hiện tại từ ThreadLocal.
*   Sửa Service (`LoginService`):
    *   Bỏ `static`.
    *   Truyền `APIRequestContext` vào Constructor.
    *   Sử dụng context được truyền vào thay vì tự tạo mới.

**Ví dụ Refactor Service:**
```java
public class LoginService {
    private APIRequestContext context;

    public LoginService(APIRequestContext context) {
        this.context = context;
    }

    public APIResponse login(LoginRequest data) {
        // Reuse context, không tạo mới
        return context.post(ConfigLoad.getInstance().getEPLogin(),
               RequestOptions.create().setData(data));
    }
}
```

### Bước 2: Refactor Test Class
*   Khởi tạo Context và Service tại `@BeforeClass`.
*   Đóng Context tại `@AfterClass`.

**Ví dụ Refactor Test:**
```java
public class LoginTest {
    private APIRequestContext context;
    private LoginService loginService;

    @BeforeClass
    public void setup() {
        APIClientFactory.initContext(); // Lưu vào ThreadLocal
        context = APIClientFactory.getContext();
        loginService = new LoginService(context);
    }

    @AfterClass
    public void tearDown() {
        APIClientFactory.close();
    }
}
```

### Bước 3: Clean Code & Utilities
*   Tạo file `config.properties`.
*   Tạo class `AssertionUtils` hoặc `ResponseUtils` để đóng gói logic verify message lỗi.
*   Thay `System.out.println` bằng thư viện logging (SLF4J/Log4j).
