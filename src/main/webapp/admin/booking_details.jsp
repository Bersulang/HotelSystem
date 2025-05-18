<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.hotel.beans.BookingDetailsBean" %>
<%@ page import="com.example.hotel.service.AdminService" %>
<%@ page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html>
<html>
<head>
    <title>预订详情</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/admin.css">
</head>
<body class="admin">
    <%@ include file="../WEB-INF/jspf/admin_header.jsp" %>
    <div class="container">
        <h1>预订详情</h1>

        <% 
            String bookingIdStr = request.getParameter("bookingId");
            int bookingId = 0;
            try {
                bookingId = Integer.parseInt(bookingIdStr);
            } catch (NumberFormatException e) {
                // 处理错误
            }
            
            AdminService adminService = new AdminService();
            BookingDetailsBean booking = adminService.getBookingById(bookingId);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            if (booking != null) {
        %>
        
        <div class="info-section">
            <h2>预订信息</h2>
            <div class="info-row">
                <div class="info-label">预订ID:</div>
                <div class="info-value"><%= booking.getBookingId() %></div>
            </div>
            <div class="info-row">
                <div class="info-label">状态:</div>
                <div class="info-value status-tag <%= booking.getStatus().toLowerCase() %>">
                    <%= booking.getStatus() %>
                </div>
            </div>
            <div class="info-row">
                <div class="info-label">预订日期:</div>
                <div class="info-value"><%= datetimeFormat.format(booking.getBookingDate()) %></div>
            </div>
        </div>

        <div class="info-section">
            <h2>用户信息</h2>
            <div class="info-row">
                <div class="info-label">用户名:</div>
                <div class="info-value"><%= booking.getUserName() %></div>
            </div>
            <div class="info-row">
                <div class="info-label">姓名:</div>
                <div class="info-value"><%= booking.getCustomerName() %></div>
            </div>
            <div class="info-row">
                <div class="info-label">联系人姓名:</div>
                <div class="info-value"><%= booking.getContactName() %></div>
            </div>
            <div class="info-row">
                <div class="info-label">联系电话:</div>
                <div class="info-value"><%= booking.getContactPhone() %></div>
            </div>
            <div class="info-row">
                <div class="info-label">联系邮箱:</div>
                <div class="info-value"><%= booking.getContactEmail() %></div>
            </div>
        </div>

        <div class="info-section">
            <h2>酒店和房间信息</h2>
            <div class="info-row">
                <div class="info-label">酒店名称:</div>
                <div class="info-value"><%= booking.getHotelName() %></div>
            </div>
            <div class="info-row">
                <div class="info-label">房间类型:</div>
                <div class="info-value"><%= booking.getRoomTypeName() %></div>
            </div>
            <div class="info-row">
                <div class="info-label">房间ID:</div>
                <div class="info-value"><%= booking.getRoomId() %></div>
            </div>
        </div>

        <div class="info-section">
            <h2>入住信息</h2>
            <div class="info-row">
                <div class="info-label">入住日期:</div>
                <div class="info-value"><%= dateFormat.format(java.sql.Date.valueOf(booking.getCheckInDate())) %></div>
            </div>
            <div class="info-row">
                <div class="info-label">退房日期:</div>
                <div class="info-value"><%= dateFormat.format(java.sql.Date.valueOf(booking.getCheckOutDate())) %></div>
            </div>
            <div class="info-row">
                <div class="info-label">房间数量:</div>
                <div class="info-value"><%= booking.getNumberOfRooms() %></div>
            </div>
            <div class="info-row">
                <div class="info-label">客人数量:</div>
                <div class="info-value"><%= booking.getNumberOfGuests() %></div>
            </div>
        </div>

        <div class="info-section">
            <h2>付款信息</h2>
            <div class="info-row">
                <div class="info-label">房间单价:</div>
                <div class="info-value">￥<%= String.format("%.2f", booking.getPricePerNight()) %>/晚</div>
            </div>
            <div class="info-row">
                <div class="info-label">总金额:</div>
                <div class="info-value">￥<%= String.format("%.2f", booking.getTotalAmount()) %></div>
            </div>
            <div class="info-row">
                <div class="info-label">支付方式:</div>
                <div class="info-value"><%= booking.getPaymentMethod() %></div>
            </div>
        </div>

        <% if (booking.getSpecialRequests() != null && !booking.getSpecialRequests().isEmpty()) { %>
        <div class="info-section">
            <h2>特殊要求</h2>
            <div class="special-requests">
                <%= booking.getSpecialRequests() %>
            </div>
        </div>
        <% } %>

        <div class="action-buttons">
            <a href="manageBookings?action=updateStatus&bookingId=<%= booking.getBookingId() %>&status=Confirmed" class="action-button confirm-button">确认预订</a>
            <a href="manageBookings?action=updateStatus&bookingId=<%= booking.getBookingId() %>&status=CheckedIn" class="action-button checkin-button">标记为已入住</a>
            <a href="manageBookings?action=updateStatus&bookingId=<%= booking.getBookingId() %>&status=CheckedOut" class="action-button checkout-button">标记为已退房</a>
            <a href="manageBookings?action=updateStatus&bookingId=<%= booking.getBookingId() %>&status=Cancelled" class="action-button cancel-button">取消预订</a>
            <a href="manage_bookings.jsp" class="action-button back-button">返回列表</a>
        </div>

        <% } else { %>
            <p>未找到预订信息。</p>
            <a href="manage_bookings.jsp" class="back-button">返回列表</a>
        <% } %>
    </div>
    <%@ include file="../WEB-INF/jspf/admin_footer.jsp" %>
</body>
</html>
