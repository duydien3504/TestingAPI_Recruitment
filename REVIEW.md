# Code Review Report

## 1. Tổng quan công nghệ
*   **Ngôn ngữ:** Java 21 (Hiện đại).
*   **Framework:** Playwright (cho thao tác API/UI), TestNG (Test runner).
*   **Thư viện hỗ trợ:** Lombok (Model), Jackson (JSON), AssertJ (Assert), Faker (Data).
*   **Kiến trúc:** Service Object Model.

## 2. Ưu điểm
*   **Cấu trúc dự án rõ ràng:** Phân chia tốt giữa các lớp `api` (core), `service` (business logic), `models` (POJO), và `tests`. Điều này giúp code dễ bảo trì và mở rộng.
*   **Sử dụng ThreadLocal:** Class `APIClientFactory` sử dụng `ThreadLocal` cho `Playwright` và `APIRequestContext`. Đây là thiết kế đúng đắn để hỗ trợ chạy test song song (parallel execution) trong tương lai.
*   **Mô hình POJO/Lombok:** Các request/response body được map vào các class Java (trong `models`) có sử dụng Lombok, giúp code gọn gàng, type-safe và dễ đọc hơn so với làm việc trực tiếp với JSON string.
*   **Configuration:** Class `ConfigLoad` hỗ trợ đọc từ file properties và cho phép ghi đè bằng System Property (rất tốt khi tích hợp CI/CD để thay đổi môi trường test linh hoạt).

## 3. Nhược điểm & Các vấn đề cần khắc phục (Quan trọng)

### A. Quản lý Playwright Context (Nghiêm trọng - Critical)
Đây là vấn đề lớn nhất của project hiện tại:
*   **Resource Leak (Rò rỉ tài nguyên):** Trong `LoginService.java`, hàm `login` tự gọi `APIClientFactory.createContext()`. Context mới này được tạo ra mỗi lần gọi hàm nhưng **không bao giờ được đóng (dispose)**. Điều này sẽ dẫn đến rò rỉ bộ nhớ và process của browser engine.
*   **Context không đồng bộ:**
    *   Trong `LoginTest` (và các test khác), bạn khởi tạo một context ở `@BeforeClass` (`requestContext = APIClientFactory.createContext()`).
    *   Tuy nhiên, context này **không được sử dụng** vì `LoginService` lại tự tạo context riêng của nó.
    *   Hậu quả: Test setup một đằng, chạy một nẻo. Các cấu hình global (như header, token) nếu set ở `@BeforeClass` sẽ không có tác dụng trong Service.

### B. Thiếu hụt tài nguyên & Cấu hình
*   **Thiếu file Config:** Project tham chiếu đến `config.properties` nhưng không thấy thư mục `src/main/resources` hay file này trong repo. Code sẽ ném `RuntimeException` ngay khi chạy.
*   **Dead Code:** Biến `private LoginService authen;` trong các class Test được khai báo nhưng để null, trong khi các phương thức của Service lại là `static`. Điều này gây hiểu nhầm về cách sử dụng (Instance vs Static).

### C. Chất lượng Code (Code Quality) & Clean Code
*   **Trùng lặp code (Duplication):** Logic khởi tạo `ObjectMapper` và parse JSON (`mapper.readTree(response.text())`) bị lặp lại trong hầu hết các test case. Nên chuyển logic này vào method chung trong `BaseTest` hoặc `JsonUtils`.
*   **Hardcoded Strings:** Các thông báo lỗi verify (ví dụ: "Email không đúng định dạng.") đang được viết cứng (hardcode) trong test. Nếu hệ thống thay đổi ngôn ngữ hoặc message, bạn phải sửa hàng loạt file test. Nên tách ra file Constant hoặc Properties.
*   **Sử dụng System.out.println:** Việc dùng `System.out.println` để in response là bad practice trong automation test. Nó làm rác console log và khó tích hợp với các hệ thống reporting.

### D. Reporting & Logging
*   **Thiếu Reporting:** Không thấy cấu hình Allure Report hay Extent Report. Khi chạy CI, sẽ rất khó biết test nào fail và tại sao (trừ khi đọc console logs raw).
*   **Thiếu Logging:** Chưa có Log4j hoặc SLF4J. Nên thêm log để trace request/response flow chuyên nghiệp hơn.

### E. Test Data
*   **Không nhất quán:** Class `TestGenerateAccount` sử dụng lẫn lộn giữa thư viện `Faker` và việc tự random bằng `Math.random`. Nên chuyển hết sang dùng `Faker` để dữ liệu phong phú và code sạch hơn.

## 4. Đề xuất cải thiện (Action Plan)
1.  **Refactor Context Management:**
    *   Chuyển `LoginService` (và các service khác) sang dạng Instance thay vì Static.
    *   Truyền `APIRequestContext` vào Constructor của Service.
    *   Khởi tạo Context duy nhất tại `@BeforeClass` (hoặc `@BeforeMethod`) trong Test và truyền nó cho Service.
2.  **Bổ sung Resources:** Tạo folder `src/main/resources` và file `config.properties`.
3.  **Clean Code Test:**
    *   Viết method helper `verifyErrorMessage(APIResponse response, String expectedMessage)` để tái sử dụng logic assert.
    *   Xóa các biến thừa.
4.  **Tích hợp Reporting:** Thêm Allure Listener vào `testng.xml` (hoặc cấu hình pom) để xuất báo cáo HTML đẹp.
5.  **Logging:** Thay `System.out` bằng Logger (SLF4J).
