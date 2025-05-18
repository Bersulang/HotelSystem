<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.hotel.beans.UserBean" %>
<%@ page import="com.example.hotel.service.AdminService" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.hotel.beans.RoomBean" %>
<%@ page import="com.example.hotel.beans.BookingDetailsBean" %>

<!DOCTYPE html>
<html>
<head>    <title>管理员仪表板</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/admin.css">
    <link rel="stylesheet" href="../css/dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body class="admin">
    <%@ include file="../WEB-INF/jspf/admin_header.jsp" %>
    
    <div class="container">
        <h1>控制面板</h1>
        
        <%
            AdminService adminService = new AdminService();
            List<RoomBean> rooms = adminService.getAllRooms();
            List<BookingDetailsBean> bookings = adminService.getAllBookings();
            List<UserBean> users = adminService.getAllUsers();
            
            int totalRooms = rooms != null ? rooms.size() : 0;
            int totalRoomsAvailable = 0;
            if (rooms != null) {
                for (RoomBean room : rooms) {
                    totalRoomsAvailable += room.getRealTimeStock();
                }
            }
            
            int totalBookings = bookings != null ? bookings.size() : 0;
            int activeBookings = 0;
            if (bookings != null) {
                for (BookingDetailsBean booking : bookings) {
                    if (!"Cancelled".equals(booking.getStatus()) && !"CheckedOut".equals(booking.getStatus())) {
                        activeBookings++;
                    }
                }
            }
            
            int totalUsers = users != null ? users.size() : 0;
            double totalRevenue = 0;
            if (bookings != null) {
                for (BookingDetailsBean booking : bookings) {
                    if (!"Cancelled".equals(booking.getStatus())) {
                        totalRevenue += booking.getTotalAmount();
                    }
                }
            }
        %>        <!-- 统计信息 -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-bed"></i></div>
                <div class="stat-number"><%= totalRooms %></div>
                <div class="stat-label">总房型</div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-door-open"></i></div>
                <div class="stat-number"><%= totalRoomsAvailable %></div>
                <div class="stat-label">可用房间</div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-calendar-check"></i></div>
                <div class="stat-number"><%= activeBookings %></div>
                <div class="stat-label">活跃预订</div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-user"></i></div>
                <div class="stat-number"><%= totalUsers %></div>
                <div class="stat-label">注册用户</div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-money-bill-wave"></i></div>
                <div class="stat-number">￥<%= String.format("%.2f", totalRevenue) %></div>
                <div class="stat-label">总收入</div>
            </div>
        </div>
        
        <!-- 快速链接 -->
        <div class="quick-links">
            <h2>快速操作</h2>
            <div class="link-grid">
                <a href="manage_rooms.jsp" class="quick-link link-rooms">
                    <i class="fas fa-bed"></i>
                    <div>管理房间</div>
                </a>
                
                <a href="manage_bookings.jsp" class="quick-link link-bookings">
                    <i class="fas fa-calendar-check"></i>
                    <div>管理预订</div>
                </a>
                
                <a href="manage_users.jsp" class="quick-link link-users">
                    <i class="fas fa-users"></i>
                    <div>管理用户</div>
                </a>
            </div>
        </div>
        
        <div class="dashboard-grid">
            <!-- 最近预订 -->
            <div class="recent-section">
                <h2>最近预订</h2>
                <%
                    int recentBookingsCount = 0;
                    if (bookings != null && !bookings.isEmpty()) {
                        java.util.Collections.sort(bookings, (b1, b2) -> b2.getBookingDate().compareTo(b1.getBookingDate()));
                        
                        for (BookingDetailsBean booking : bookings) {
                            if (recentBookingsCount < 5) {
                %>
                <div class="recent-item">
                    <strong><%= booking.getCustomerName() %></strong> 预订了 
                    <strong><%= booking.getHotelName() %></strong> 的 
                    <strong><%= booking.getRoomTypeName() %></strong> 
                    ￥<%= String.format("%.2f", booking.getTotalAmount()) %>
                    <div class="date">状态: <%= booking.getStatus() %> | <%= new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(booking.getBookingDate()) %></div>
                </div>
                <%
                                recentBookingsCount++;
                            } else {
                                break;
                            }
                        }
                    } else {
                %>
                <div class="recent-item">暂无预订记录</div>
                <%
                    }
                %>
                <a href="manage_bookings.jsp" class="view-all">查看所有预订</a>
            </div>
            
            <!-- 房间可用性 -->
            <div class="recent-section">
                <h2>房型状态</h2>
                <% 
                    int availableTypesCount = 0;
                    if (rooms != null && !rooms.isEmpty()) {
                        for (RoomBean room : rooms) {
                            if (availableTypesCount < 8) {
                %>
                <div class="recent-item">
                    <strong><%= room.getRoomTypeName() %></strong> 
                    (<%= room.getHotelName() %>) 
                    <div>剩余: <strong><%= room.getRealTimeStock() %></strong> | 价格: ￥<%= String.format("%.2f", room.getPricePerNight()) %></div>
                </div>
                <%          
                                availableTypesCount++;
                            } else {
                                break;
                            }
                        }
                    } else {
                %>
                <div class="recent-item">暂无房型数据</div>
                <%
                    }
                %>
                <a href="manage_rooms.jsp" class="view-all">查看所有房型</a>
            </div>
        </div>
    </div>
    
    <%@ include file="../WEB-INF/jspf/admin_footer.jsp" %>
</body>
</html>