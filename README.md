\# Báo cáo triển khai dự án TechShop



Mô hình: Triển khai ứng dụng Web Fullstack (Spring Boot + Nginx Reverse Proxy + Cloudflare Tunnel) trên hệ điều hành Ubuntu Server (VirtualBox).



\---



\## 1. Tổng quan kiến trúc hệ thống



```text

\[ Internet Client ] 

&#x20;      │ (HTTPS)

&#x20;      ▼

\[ Cloudflare Tunnel ] 

&#x20;      │ (HTTP - Port 80)

&#x20;      ▼

\[ Nginx Web Server (Port 80) ]

&#x20;      ├── Phục vụ Static Files (index.html, JS, CSS) -> /var/www/html

&#x20;      └── Reverse Proxy /api/ ──> \[ Spring Boot App (Port 8080) ]

&#x20;                                           │

&#x20;                                           └── \[ H2 In-Memory Database ]

Frontend: HTML5, CSS3, JavaScript (Fetch API).   Backend: Java 21, Spring Boot, Spring Data JPA.   Database: H2 Database (In-Memory).   Web Server \& Reverse Proxy: Nginx (lắng nghe cổng 80).   Public Network Access: Cloudflare Tunnel (cloudflared) tạo kết nối HTTPS an toàn ra Internet.   



2\. Nhật ký quá trình triển khai và xử lý lỗi



Bước 1: Chuẩn bị môi trường và biên dịch BackendMở rộng dung lượng đĩa LVM: Giải phóng dung lượng phân vùng gốc /dev/mapper/ubuntu--vg-ubuntu--lv bằng lvextend và resize2fs để đủ bộ nhớ cài đặt OpenJDK 21.Cài đặt Java 21: Đáp ứng đúng yêu cầu Toolchain của Gradle.   Biên dịch mã nguồn:Bashcd \~/demo/demo-backend-frontend-devops

./gradlew clean build -x test

Khởi chạy ứng dụng Spring Boot:Bashjava -jar build/libs/\*.jar

Kết quả: Spring Boot khởi tạo HikariPool, tự động tạo cấu trúc bảng products trong H2 Database và chạy trên cổng 8080.   



Bước 2: Cấu hình Nginx và giải phóng xung đột cổngCấu hình Reverse Proxy: Chỉnh sửa file /etc/nginx/sites-available/default:   Lắng nghe cổng 80.   Điều hướng tất cả request /api/ về http://localhost:8080/api/.   Khắc phục xung đột cấu hình (Conflicting Server Name):   Phát hiện các file cấu hình cũ (multi-port.conf, opt-app.conf, spring-proxy.conf) còn tồn tại trong /etc/nginx/sites-enabled/ gây đụng độ cổng.   



Thực hiện dọn dẹp và kích hoạt duy nhất file default:   Bashsudo rm /etc/nginx/sites-enabled/multi-port.conf /etc/nginx/sites-enabled/opt-app.conf /etc/nginx/sites-enabled/spring-proxy.conf

sudo ln -s /etc/nginx/sites-available/default /etc/nginx/sites-enabled/

sudo pkill -9 nginx

sudo nginx -t

sudo systemctl restart nginx



Kết quả: Nginx kiểm tra cú pháp thành công (syntax is ok) và khởi chạy ổn định trên cổng 80.   Bước 3: Khắc phục lỗi kết nối Endpoint ở FrontendHiện tượng lỗi: Trang web truy cập qua Cloudflare báo lỗi "Chưa kết nối được API http://localhost:8080/api/v1" do trình duyệt chặn Mixed Content (HTTPS gọi HTTP) và không hiểu localhost.   Khắc phục: Cập nhật biến BASE\_URL trong mã nguồn JavaScript (/var/www/html/index.html) từ đường dẫn tuyệt đối thành đường dẫn tương đối:   JavaScript// Trước khi sửa:

// const BASE\_URL = 'http://localhost:8080/api/v1';



// Sau khi sửa:

const BASE\_URL = '/api/v1';

Kết quả: Các request API tự động đi qua Reverse Proxy Nginx ở cổng 80 để chuyển tiếp vào Spring Boot ở cổng 8080.   Bước 4: Đưa ứng dụng ra Internet bằng Cloudflare TunnelKhởi chạy Quick Tunnel kết nối cổng 80 ra ngoài Internet:   Bashcloudflared tunnel --url http://localhost:80



Cloudflare cấp phát URL HTTPS ngẫu nhiên dạng https://...trycloudflare.com.   3. Kết quả thành phẩmGiao diện Trưng bày Sản phẩm (Showcase View):Cho phép xem danh sách sản phẩm công nghệ theo danh mục (Tất cả, Laptop, Điện thoại).Hiển thị đầy đủ hình ảnh, tên, giá bán và chức năng xem chi tiết sản phẩm.Giao diện Quản lý kho (Admin Inventory Management):Biểu mẫu thêm mới sản phẩm gồm: Tên sản phẩm, Danh mục, Giá bán, Link ảnh, Thông số kỹ thuật.   Bảng danh sách sản phẩm hỗ trợ xem nhanh, chỉnh sửa và xóa dữ liệu trực tiếp trong CSDL.   4. Đánh giá kết quảTriển khai thành công mô hình Multi-tier Web Architecture trên hệ điều hành Linux Ubuntu.Cấu hình Nginx Reverse Proxy giúp ẩn cổng Backend (8080) và khắc phục lỗi CORS/Mixed Content.   Tích hợp Cloudflare Tunnel cung cấp kết nối HTTPS công khai an toàn.   Hệ thống hoạt động ổn định, truyền tải dữ liệu API chính xác giữa Frontend và Backend.   

