<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page import="com.example.hotel.beans.UserBean" %>

<%
    UserBean currentUser = (UserBean) session.getAttribute("currentUser");
    if (currentUser == null || !"admin".equals(currentUser.getUserRole())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=unauthorized");
        return;
    }
%>

<header class="admin-header">
    <div class="header-logo">
        <h1><a href="dashboard.jsp">酒店管理系统</a></h1>
    </div>
    <nav class="admin-nav">
        <ul>
            <li><a href="dashboard.jsp">控制面板</a></li>
            <li><a href="manage_rooms.jsp">房间管理</a></li>
            <li><a href="manage_bookings.jsp">预订管理</a></li>
            <li><a href="manage_users.jsp">用户管理</a></li>
        </ul>
    </nav>
    <div class="admin-user-info">
        <span>当前登录: <%= currentUser.getFullName() != null ? currentUser.getFullName() : currentUser.getUserName() %>,欢迎！</span>
        <a href="<%= request.getContextPath() %>/logout" class="logout-btn">退出登录</a>
    </div>
</header>
