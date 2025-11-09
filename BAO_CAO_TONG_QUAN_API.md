# Tổng quan về dự án Siu Store và các API

## 1. Tổng quan dự án

Siu Store là một ứng dụng thương mại điện tử (e-commerce) được xây dựng trên nền tảng **Spring Boot 3.3.4** với **Java 17**, sử dụng **MySQL** làm hệ quản trị cơ sở dữ liệu. Dự án được thiết kế theo kiến trúc RESTful API, cung cấp các chức năng quản lý sản phẩm, giỏ hàng, đơn hàng, tài khoản người dùng và xác thực.

Hệ thống áp dụng **JWT (JSON Web Token)** cho việc xác thực và phân quyền người dùng, với các vai trò bao gồm ADMIN, EMPLOYEE và CUSTOMER. Bảo mật được thực hiện thông qua Spring Security với các annotation `@PreAuthorize` để kiểm soát quyền truy cập vào các endpoint. Ngoài ra, dự án còn tích hợp các công nghệ như Cloudinary cho quản lý hình ảnh, Google OAuth cho đăng nhập xã hội, và reCAPTCHA cho bảo vệ chống bot.

Tất cả các API được đặt dưới base path `/api/v1/` và trả về dữ liệu dưới định dạng JSON. Hệ thống xử lý exception tập trung thông qua `GlobalApiExceptionHandler` để đảm bảo phản hồi thống nhất và dễ xử lý.

## 2. Các bộ API theo Controller

### 2.1. AuthApiController (`/api/v1/auth`)

Controller này quản lý các chức năng xác thực và đăng nhập của hệ thống:

- **POST `/api/v1/auth/register`**: Đăng ký tài khoản mới. Nhận thông tin đăng ký qua `SignUpDto`, tạo tài khoản và gửi email xác thực.
- **POST `/api/v1/auth/login`**: Đăng nhập vào hệ thống. Nhận thông tin đăng nhập qua `SignInDto`, xác thực và trả về JWT token (access token và refresh token).
- **POST `/api/v1/auth/refresh`**: Làm mới access token khi token hiện tại hết hạn. Nhận refresh token từ header Authorization và trả về cặp token mới.

### 2.2. AccountApiController (`/api/v1/accounts`)

Controller này quản lý các thao tác liên quan đến tài khoản và profile người dùng:

- **GET `/api/v1/accounts/all`**: Lấy danh sách tất cả tài khoản trong hệ thống. Yêu cầu quyền ADMIN hoặc EMPLOYEE.
- **GET `/api/v1/accounts/{email}`**: Lấy thông tin chi tiết của một tài khoản theo email.
- **PUT `/api/v1/accounts/{email}/status`**: Cập nhật trạng thái tài khoản (kích hoạt/vô hiệu hóa). Yêu cầu quyền ADMIN.
- **GET `/api/v1/accounts/{email}/profile`**: Lấy thông tin profile của người dùng theo email.
- **PUT `/api/v1/accounts/{email}/profile`**: Cập nhật thông tin profile của người dùng (tên, địa chỉ, số điện thoại, v.v.).
- **PUT `/api/v1/accounts/{email}/password`**: Đổi mật khẩu của tài khoản. Người dùng có thể đổi mật khẩu của chính mình hoặc ADMIN có thể đổi mật khẩu cho bất kỳ tài khoản nào.
- **PUT `/api/v1/accounts/roles`**: Cập nhật vai trò (roles) của tài khoản. Yêu cầu quyền ADMIN.

### 2.3. ProductApiController (`/api/v1/products`)

Controller này quản lý các thao tác CRUD đối với sản phẩm:

- **GET `/api/v1/products`**: Lấy danh sách tất cả sản phẩm có trong hệ thống. Không yêu cầu xác thực.
- **GET `/api/v1/products/{slug}`**: Lấy thông tin chi tiết của một sản phẩm theo slug (định danh duy nhất dạng URL-friendly). Không yêu cầu xác thực.
- **POST `/api/v1/products`**: Tạo sản phẩm mới. Nhận thông tin sản phẩm qua `CreateProductRequest`. Yêu cầu quyền ADMIN.
- **PUT `/api/v1/products/{slug}`**: Cập nhật thông tin sản phẩm theo slug. Nhận thông tin cập nhật qua `UpdateProductRequest`. Yêu cầu quyền ADMIN.

### 2.4. CartApiController (`/api/v1/carts`)

Controller này quản lý giỏ hàng của người dùng:

- **GET `/api/v1/carts/{email}`**: Lấy thông tin giỏ hàng của người dùng theo email, bao gồm tổng tiền và danh sách sản phẩm.
- **GET `/api/v1/carts/{email}/details`**: Lấy danh sách chi tiết các mục trong giỏ hàng (cart details) của người dùng.
- **GET `/api/v1/carts/{email}/item/{cartDetailId}`**: Lấy thông tin chi tiết của một mục cụ thể trong giỏ hàng theo ID.
- **POST `/api/v1/carts/{email}/add`**: Thêm sản phẩm vào giỏ hàng. Nhận thông tin sản phẩm và số lượng qua `CreateItemCartRequest`.
- **PATCH `/api/v1/carts/{email}/quantity`**: Cập nhật số lượng của một mục trong giỏ hàng. Nhận `UpdateQuantityRequest` với cartDetailId và số lượng mới.
- **DELETE `/api/v1/carts/{email}/items/{cartDetailId}`**: Xóa một mục cụ thể khỏi giỏ hàng. Yêu cầu người dùng phải là chủ sở hữu giỏ hàng hoặc có quyền ADMIN.
- **DELETE `/api/v1/carts/{email}/items`**: Xóa nhiều mục khỏi giỏ hàng cùng lúc (bulk delete). Nhận danh sách cartDetailIds qua request body. Yêu cầu quyền tương tự như endpoint trên.
- **DELETE `/api/v1/carts/{email}/clear`**: Xóa toàn bộ giỏ hàng của người dùng. Yêu cầu người dùng phải là chủ sở hữu giỏ hàng hoặc có quyền ADMIN.

### 2.5. OrderApiController (`/api/v1/orders`)

Controller này quản lý đơn hàng và quy trình xử lý đơn hàng:

- **GET `/api/v1/orders/all`**: Lấy danh sách tất cả đơn hàng trong hệ thống. Dành cho quản trị viên.
- **GET `/api/v1/orders/{email}/me`**: Lấy danh sách đơn hàng của người dùng cụ thể. Người dùng chỉ có thể xem đơn hàng của chính mình hoặc ADMIN có thể xem tất cả.
- **GET `/api/v1/orders/{email}/history`**: Lấy lịch sử thay đổi trạng thái đơn hàng của người dùng (order history).
- **POST `/api/v1/orders/{email}/create`**: Tạo đơn hàng mới từ giỏ hàng. Nhận thông tin đơn hàng qua `CreateOrderRequest` (địa chỉ giao hàng, phương thức thanh toán, v.v.).
- **POST `/api/v1/orders/{id}/confirm`**: Xác nhận đơn hàng (chuyển từ trạng thái PENDING sang CONFIRMED). Dành cho quản trị viên.
- **POST `/api/v1/orders/{id}/shipping`**: Cập nhật đơn hàng sang trạng thái đang vận chuyển (SHIPPING). Dành cho quản trị viên.
- **POST `/api/v1/orders/{id}/complete`**: Hoàn thành đơn hàng (chuyển sang trạng thái COMPLETED). Dành cho quản trị viên.
- **POST `/api/v1/orders/{id}/cancel`**: Hủy đơn hàng. Người dùng có thể hủy đơn hàng của chính mình hoặc ADMIN có thể hủy bất kỳ đơn hàng nào.

### 2.6. GlobalApiExceptionHandler

Đây không phải là một controller API nhưng là một component quan trọng xử lý exception tập trung cho tất cả các API:

- Xử lý `NoSuchElementException` (404 Not Found): Khi không tìm thấy tài nguyên.
- Xử lý `IllegalArgumentException` (400 Bad Request): Khi dữ liệu đầu vào không hợp lệ.
- Xử lý `MethodArgumentNotValidException` (400 Bad Request): Khi validation của Spring không thỏa mãn.
- Xử lý `BadCredentialsException` (401 Unauthorized): Khi thông tin đăng nhập không đúng.
- Xử lý `AccessDeniedException` (403 Forbidden): Khi người dùng không có quyền truy cập.
- Xử lý `DuplicateEmailException` (409 Conflict): Khi email đã tồn tại trong hệ thống.
- Xử lý `TooManyRequestsException` (429 Too Many Requests): Khi vượt quá giới hạn số lượng request.
- Xử lý các exception khác (500 Internal Server Error): Xử lý các lỗi không mong đợi.

## 3. Đặc điểm bảo mật của API

Tất cả các API trong hệ thống được bảo vệ bởi các cơ chế sau:

1. **JWT Authentication**: Yêu cầu token hợp lệ trong header `Authorization: Bearer <token>` cho hầu hết các endpoint (trừ một số endpoint công khai như xem sản phẩm, đăng ký, đăng nhập).

2. **Role-Based Access Control (RBAC)**: Sử dụng `@PreAuthorize` để kiểm soát quyền truy cập dựa trên vai trò:
   - `hasAuthority('ADMIN')`: Chỉ ADMIN mới có quyền truy cập.
   - `hasAnyAuthority('ADMIN','EMPLOYEE')`: ADMIN hoặc EMPLOYEE có quyền truy cập.
   - `#email == authentication.name`: Người dùng chỉ có thể truy cập tài nguyên của chính mình.

3. **Input Validation**: Sử dụng `@Valid` và các annotation validation như `@NotNull`, `@NotBlank`, `@Min`, `@Max` để đảm bảo dữ liệu đầu vào hợp lệ.

4. **Exception Handling**: Xử lý exception tập trung đảm bảo không có thông tin nhạy cảm bị lộ ra ngoài và phản hồi thống nhất.

5. **HTTPS**: Khuyến nghị sử dụng HTTPS trong môi trường production để mã hóa dữ liệu truyền tải.

## 4. Định nghĩa các lỗ hổng bảo mật API theo OWASP API Security Top 10 2023

### API1:2023 - Broken Object Level Authorization (BOLA)
**Định nghĩa**: Lỗ hổng xảy ra khi API không kiểm tra quyền truy cập của người dùng đối với các đối tượng cụ thể (object). Kẻ tấn công có thể truy cập, sửa đổi hoặc xóa dữ liệu của người dùng khác bằng cách thay đổi ID trong request (ví dụ: `/api/v1/accounts/{email}` hoặc `/api/v1/orders/{id}`) mà không bị hệ thống chặn.

**Ví dụ**: Người dùng A có thể truy cập thông tin profile của người dùng B bằng cách thay đổi email trong URL từ `userA@example.com` sang `userB@example.com` nếu API không kiểm tra quyền sở hữu.

### API2:2023 - Broken Authentication
**Định nghĩa**: Lỗ hổng liên quan đến các vấn đề trong cơ chế xác thực của API, khiến kẻ tấn công có thể giả mạo danh tính người dùng hoặc chiếm quyền điều khiển tài khoản. Các vấn đề thường gặp bao gồm: token yếu, token không hết hạn, lưu trữ mật khẩu không an toàn, thiếu rate limiting cho các endpoint đăng nhập.

**Ví dụ**: API cho phép đăng nhập không giới hạn số lần thử, cho phép brute-force attack; hoặc JWT token không có thời gian hết hạn, có thể sử dụng vĩnh viễn.

### API4:2023 - Unrestricted Resource Consumption
**Định nghĩa**: Lỗ hổng xảy ra khi API không giới hạn việc tiêu thụ tài nguyên (CPU, bộ nhớ, băng thông, storage), cho phép kẻ tấn công thực hiện các cuộc tấn công từ chối dịch vụ (DoS) hoặc làm chậm hệ thống bằng cách gửi một lượng lớn request hoặc dữ liệu lớn.

**Ví dụ**: API cho phép upload file không giới hạn kích thước, hoặc không có rate limiting cho các endpoint quan trọng, cho phép kẻ tấn công gửi hàng nghìn request/giây làm quá tải server.

### API5:2023 - Broken Function Level Authorization (BFLA)
**Định nghĩa**: Lỗ hổng xảy ra khi API không kiểm tra quyền truy cập ở cấp độ chức năng (function level). Người dùng thường có thể truy cập các chức năng dành cho quản trị viên hoặc người dùng có quyền cao hơn bằng cách gọi trực tiếp endpoint mà không cần xác thực đúng vai trò.

**Ví dụ**: Người dùng thường có thể gọi endpoint `POST /api/v1/products` (tạo sản phẩm - chỉ dành cho ADMIN) hoặc `PUT /api/v1/accounts/roles` (cập nhật vai trò - chỉ dành cho ADMIN) nếu thiếu kiểm tra quyền `@PreAuthorize("hasAuthority('ADMIN')")`.

### API8:2023 - Security Misconfiguration
**Định nghĩa**: Lỗ hổng xảy ra do cấu hình bảo mật không đúng hoặc thiếu sót trong API, server, database, hoặc các thành phần khác của hệ thống. Các vấn đề thường gặp bao gồm: để mặc định credentials, bật debug mode trong production, thiếu security headers, CORS cấu hình quá rộng, lộ thông tin nhạy cảm trong error messages.

**Ví dụ**: API trả về stack trace chi tiết trong error message, tiết lộ cấu trúc code và thông tin nhạy cảm; hoặc CORS cho phép tất cả các origin (`*`), cho phép bất kỳ website nào gọi API.

