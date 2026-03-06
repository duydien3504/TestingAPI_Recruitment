# Dự án Kiểm thử Tự động API - Hệ thống Tuyển dụng

## 1. Tổng quan dự án
Đây là dự án tự xây dựng framework kiểm thử tự động cho hệ thống API tuyển dụng. Mục tiêu của dự án là đảm bảo tính chính xác, ổn định và bảo mật của các nghiệp vụ cốt lõi trong quy trình tuyển dụng, từ khâu xác thực, quản lý bài đăng, đến nộp hồ sơ và phỏng vấn.

- Ngày bắt đầu: 03/02/2026
- Ngày kết thúc: 06/03/2026

## 2. Công nghệ và Kiến trúc
Dự án được xây dựng dựa trên các công nghệ và thư viện hiện đại để đảm bảo khả năng mở rộng và dễ bảo trì:
- Ngôn ngữ lập trình: Java 21
- Framework kiểm thử: TestNG
- Công cụ tương tác API: Playwright Java
- Kiến trúc thiết kế: Service Object Model (SOM)
- Thư viện hỗ trợ:
  - Lombok: Tối ưu hóa mã nguồn cho các lớp dữ liệu (POJO).
  - Jackson: Chuyển đổi và xử lý dữ liệu định dạng JSON.
  - AssertJ: Viết các câu lệnh xác minh kết quả trực quan (Fluent Assertions).
  - Java Faker: Sinh dữ liệu ngẫu nhiên phục vụ kiểm thử.
  - OpenCSV: Đọc dữ liệu từ file để thực hiện Data-Driven Testing.
  - ThreadLocal: Quản lý bối cảnh chạy test để hỗ trợ thực thi song song (Parallel execution).

## 3. Phạm vi kiểm thử

### 3.1. Nhóm cấu phần Xác thực và Điều quyền (Authentication & Authorization)
- Nội dung kiểm thử: Đăng ký, Đăng nhập, Quên mật khẩu, Quản lý thông tin tài khoản.
- Kỹ thuật áp dụng: Quản lý vòng đời của Bearer Token, thực hiện API Chaining (lấy token từ bước đăng nhập để sử dụng cho các yêu cầu xác thực phía sau).

### 3.2. Nhóm nghiệp vụ lõi - Công việc và Tìm kiếm (Jobs & Search)
- Nội dung kiểm thử: Nhà tuyển dụng đăng tải, cập nhật và xóa công việc (CRUD). Nền tảng tìm kiếm việc làm dựa trên các tiêu chí lọc đa dạng.
- Kỹ thuật áp dụng: Kiểm chứng dữ liệu trả về với cơ sở dữ liệu kỳ vọng (Data Verification), tham số hóa dữ liệu đầu vào cho các trường hợp tìm kiếm phức tạp (từ khóa, địa điểm, mức lương).

### 3.3. Nhóm nghiệp vụ giao dịch - Hồ sơ và Ứng tuyển (Application & Resume)
- Nội dung kiểm thử: Tải lên hồ sơ lý lịch (CV), ứng viên nộp hồ sơ ứng tuyển vào vị trí công việc.
- Kỹ thuật áp dụng: Xử lý các yêu cầu tải file qua Multipart/form-data. Kiểm tra ràng buộc dữ liệu quan hệ (ví dụ: một ứng viên không thể ứng tuyển nhiều lần vào cùng một vị trí).

### 3.4. Nhóm nghiệp vụ quy trình - Phỏng vấn (Interview)
- Nội dung kiểm thử: Tạo lịch phỏng vấn, cập nhật trạng thái phỏng vấn (Chờ duyệt, Chấp nhận, Từ chối).
- Kỹ thuật áp dụng: Kiểm thử sự chuyển đổi trạng thái (State Transition Testing) để đảm bảo luồng nghiệp vụ vận hành đúng logic.

### 3.5. Nhóm cấu phần Bảo mật và Cấp quyền (Security & RBAC)
- Nội dung kiểm thử: Ràng buộc quyền truy cập giữa các vai trò (Ứng viên, Nhà tuyển dụng, Quản trị viên).
- Kỹ thuật áp dụng: Kiểm thử tiêu cực (Negative Testing). Ví dụ: Dùng token của ứng viên để gọi API xóa công việc của nhà tuyển dụng và xác minh hệ thống trả về lỗi 403 Forbidden.

## 4. Cấu trúc thư mục dự án
- src/main/java/api: Chứa các lớp quản lý kết nối và tạo ngữ cảnh tương tác API.
- src/main/java/models: Định nghĩa các lớp đối tượng mô phỏng cấu trúc dữ liệu của Request và Response.
- src/main/java/service: Chứa mã nguồn thực thi logic gọi API phân chia theo từng nhóm nghiệp vụ.
- src/test/java/base: Chứa lớp cấu hình chung cho các kịch bản kiểm thử (setup/teardown).
- src/test/java/tests: Nơi tổ chức các kịch bản kiểm thử (Test cases).
