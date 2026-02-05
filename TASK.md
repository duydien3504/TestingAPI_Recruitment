1. Nhóm Module Authentication & Authorization (Bắt buộc)
- Đây là cổng vào của mọi hệ thống, giúp bạn show kỹ năng xử lý Token/Session.
- Chức năng cần test:
- Đăng ký (Register)
- Đăng nhập (Login)
- Quên mật khẩu (Forgot password)
- Lấy thông tin User (Get Profile)
- Kỹ thuật thể hiện: Quản lý Authen Token, xử lý API Chaining (lấy token từ login dùng cho các request sau).

2. Nhóm Module Core Business - Jobs & Search (Dữ liệu & Tìm kiếm)
- Module này chứng minh bạn biết cách test các method GET phức tạp và CRUD cơ bản.
- Chức năng cần test:
  + Jobs (Công việc): Employer đăng bài (Create), cập nhật (Update), xóa (Delete).
  + Search/Filter: Test API tìm kiếm việc làm với nhiều tham số (keyword, location, salary).
  + Kỹ thuật thể hiện: Data Verification (so sánh dữ liệu trả về), Parameterization (test nhiều bộ dữ liệu tìm kiếm khác nhau).
  
3. Nhóm Module Transaction - Application & Resume (Upload & Workflow cơ bản)
- Đây là phần lõi của nghiệp vụ tuyển dụng.
- Chức năng cần test:.
  + Resume: Tính năng Upload CV (quan trọng để show kỹ năng handle file upload qua API).
  + Application (Ứng tuyển): Candidate nộp đơn vào một Job.
  + Kỹ thuật thể hiện: Handle Multipart/form-data (upload file), verified quan hệ dữ liệu (Candidate A đã apply Job B chưa).
  
4. Nhóm Module Process - Interview (Logic trạng thái phức tạp)
- Module này giúp project của bạn nổi bật hơn các project CRUD đơn giản user/post.
- Chức năng cần test:
  + Tạo lịch phỏng vấn (Schedule Interview).
  + Cập nhật trạng thái phỏng vấn (Pending -> Accepted -> Rejected).
  + Kỹ thuật thể hiện: State Transition Testing (kiểm tra chuyển đổi trạng thái đúng logic nghiệp vụ).
  
5. Nhóm Module Admin/Security (Phân quyền - RBAC)
- Module này dùng để test bảo mật và phân quyền.
- Chức năng cần test:
  + Dùng token của Candidate để thử gọi API xóa Job của Employer hoặc API của Admin -> Mong đợi lỗi 403 Forbidden.
  + Kỹ thuật thể hiện: Security/Permission Testing (Negative Testing).