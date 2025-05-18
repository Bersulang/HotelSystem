<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.hotel.beans.RoomBean" %>
<%@ page import="com.example.hotel.service.AdminService" %>

<!DOCTYPE html>
<html>
<head>    <title>管理房间</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/admin.css">
    <script>
        function confirmDelete(roomId) {
            if (confirm("您确定要删除这个房间吗？此操作不可撤销。")) {
                window.location.href = "manageRooms?action=delete&roomId=" + roomId;
            }
        }
    </script>
</head>
<body class="admin">
    <%@ include file="../WEB-INF/jspf/admin_header.jsp" %>
    <div class="container">
        <h1>房间管理</h1>

        <% String message = (String) request.getAttribute("message"); %>
        <% if (message != null && !message.isEmpty()) { %>
            <p class="message <%= message.startsWith("Error") ? "error-message" : "success-message" %>"><%= message %></p>
        <% } %>

        <a href="manageRooms?action=add" class="add-button">添加新房间</a>

        <table>
            <thead>
                <tr>
                    <th>房间ID</th>
                    <th>酒店名称</th>
                    <th>房型</th>
                    <th>价格/晚</th>
                    <th>促销价</th>
                    <th>剩余</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    AdminService adminService = new AdminService();
                    List<RoomBean> rooms = adminService.getAllRooms();
                    if (rooms != null && !rooms.isEmpty()) {
                        for (RoomBean room : rooms) {
                %>
                <tr>
                    <td><%= room.getRoomId() %></td>
                    <td><%= room.getHotelName() %></td>
                    <td><%= room.getRoomTypeName() %></td>
                    <td><%= String.format("%.2f", room.getPricePerNight()) %></td>
                    <td><%= String.format("%.2f", room.getPromotionalPrice()) %></td>
                    <td><%= room.getRealTimeStock() %></td>
                    <td>
                        <a href="manageRooms?action=edit&roomId=<%= room.getRoomId() %>" class="edit-button">编辑</a>
                        <button onclick="confirmDelete('<%= room.getRoomId() %>')" class="delete-button">删除</button>
                    </td>
                </tr>
                <% 
                        }
                    } else {
                %>
                <tr>
                    <td colspan="7">没有找到房间信息。</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
    <%@ include file="../WEB-INF/jspf/admin_footer.jsp" %>
</body>
</html>
