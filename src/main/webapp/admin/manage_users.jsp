<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.hotel.beans.UserBean" %>
<%@ page import="com.example.hotel.service.AdminService" %>

<!DOCTYPE html>
<html>
<head>    <title>管理用户</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/admin.css">
    <script>
        function confirmDelete(userName) {
            if (confirm("您确定要删除这个用户吗？此操作不可撤销。")) {
                window.location.href = "manageUsers?action=delete&userName=" + encodeURIComponent(userName);
            }
        }
    </script>
</head>
<body class="admin">
    <%@ include file="../WEB-INF/jspf/admin_header.jsp" %>
    <div class="container">
        <h1>用户管理</h1>

        <% String message = (String) request.getAttribute("message"); %>
        <% if (message != null && !message.isEmpty()) { %>
            <p class="message <%= message.startsWith("Error") ? "error-message" : "success-message" %>"><%= message %></p>
        <% } %>

        <a href="manageUsers?action=add" class="add-button">添加新用户</a>

        <table>
            <thead>
                <tr>
                    <th>用户名</th>
                    <th>姓名</th>
                    <th>电子邮件</th>
                    <th>电话</th>
                    <th>用户角色</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    AdminService adminService = new AdminService();
                    List<UserBean> users = adminService.getAllUsers();
                    if (users != null && !users.isEmpty()) {
                        for (UserBean user : users) {
                %>
                <tr>
                    <td><%= user.getUserName() %></td>
                    <td><%= user.getFullName() %></td>
                    <td><%= user.getEmail() != null ? user.getEmail() : "" %></td>
                    <td><%= user.getPhone() != null ? user.getPhone() : "" %></td>
                    <td><%= user.getUserRole() %></td>
                    <td>
                        <a href="manageUsers?action=edit&userName=<%= user.getUserName() %>" class="edit-button">编辑</a>
                        <button onclick="confirmDelete('<%= user.getUserName() %>')" class="delete-button"
                            <%= "admin".equals(user.getUserName()) ? "disabled title='管理员账户不能删除'" : "" %>>
                            删除
                        </button>
                    </td>
                </tr>
                <% 
                        }
                    } else {
                %>
                <tr>
                    <td colspan="6">没有找到用户信息。</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
    <%@ include file="../WEB-INF/jspf/admin_footer.jsp" %>
</body>
</html>
