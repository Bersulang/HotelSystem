<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.hotel.beans.BookingDetailsBean" %>
<%@ page import="com.example.hotel.service.AdminService" %>
<%@ page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html>
<html>
<head>    <title>管理预订</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/admin.css">
    <script>
        function confirmDelete(bookingId) {
            if (confirm("您确定要删除这个订单吗？此操作不可撤销。")) {
                window.location.href = "manageBookings?action=delete&bookingId=" + bookingId;
            }
        }
        
        function updateStatus(bookingId) {
            var newStatus = document.getElementById('status_' + bookingId).value;
            if (confirm("确定要将订单状态更改为 " + newStatus + " 吗？")) {
                window.location.href = "manageBookings?action=updateStatus&bookingId=" + bookingId + "&status=" + newStatus;
            }
        }
    </script>
</head>
<body class="admin">
    <%@ include file="../WEB-INF/jspf/admin_header.jsp" %>
    <div class="container">
        <h1>预订管理</h1>

        <% String message = (String) request.getAttribute("message"); %>
        <% if (message != null && !message.isEmpty()) { %>
            <p class="message <%= message.startsWith("Error") ? "error-message" : "success-message" %>"><%= message %></p>
        <% } %>

        <table>
            <thead>
                <tr>
                    <th>预订ID</th>
                    <th>用户</th>
                    <th>酒店/房型</th>
                    <th>入住日期</th>
                    <th>退房日期</th>
                    <th>房间数量</th>
                    <th>总价</th>
                    <th>状态</th>
                    <th>预订日期</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    AdminService adminService = new AdminService();
                    List<BookingDetailsBean> bookings = adminService.getAllBookings();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    
                    if (bookings != null && !bookings.isEmpty()) {
                        for (BookingDetailsBean booking : bookings) {
                            // 将状态字符串处理成CSS类名友好的格式（小写且移除空格）
                            String statusClass = booking.getStatus().toLowerCase().replace(" ", "");
                %>
                <tr>
                    <td><%= booking.getBookingId() %></td>
                    <td>
                        <strong><%= booking.getCustomerName() %></strong><br>
                        <small><%= booking.getUserName() %></small>
                    </td>
                    <td>
                        <strong><%= booking.getHotelName() %></strong><br>
                        <small><%= booking.getRoomTypeName() %></small>
                    </td>
                    <td><%= dateFormat.format(java.sql.Date.valueOf(booking.getCheckInDate())) %></td>
                    <td><%= dateFormat.format(java.sql.Date.valueOf(booking.getCheckOutDate())) %></td>
                    <td><%= booking.getNumberOfRooms() %></td>
                    <td><%= String.format("￥%.2f", booking.getTotalAmount()) %></td>
                    <td>
                        <select id="status_<%= booking.getBookingId() %>" class="status-select <%= statusClass %>">
                            <option value="Pending" <%= "Pending".equals(booking.getStatus()) ? "selected" : "" %>>待处理</option>
                            <option value="Confirmed" <%= "Confirmed".equals(booking.getStatus()) ? "selected" : "" %>>已确认</option>
                            <option value="CheckedIn" <%= "CheckedIn".equals(booking.getStatus()) ? "selected" : "" %>>已入住</option>
                            <option value="CheckedOut" <%= "CheckedOut".equals(booking.getStatus()) ? "selected" : "" %>>已退房</option>
                            <option value="Cancelled" <%= "Cancelled".equals(booking.getStatus()) ? "selected" : "" %>>已取消</option>
                        </select>
                        <button onclick="updateStatus(<%= booking.getBookingId() %>)" class="small-button">更新</button>
                    </td>
                    <td><%= datetimeFormat.format(booking.getBookingDate()) %></td>
                    <td>
                        <a href="manageBookings?action=view&bookingId=<%= booking.getBookingId() %>" class="view-button">详情</a>
                        <button onclick="confirmDelete(<%= booking.getBookingId() %>)" class="delete-button">删除</button>
                    </td>
                </tr>
                <% 
                        }
                    } else {
                %>
                <tr>
                    <td colspan="10">没有找到预订信息。</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
    <%@ include file="../WEB-INF/jspf/admin_footer.jsp" %>
</body>
</html>
