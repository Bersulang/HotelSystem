<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.hotel.beans.UserBean" %>
<%@ page import="com.example.hotel.service.AdminService" %>

<!DOCTYPE html>
<html>
<head>
    <title><%= request.getParameter("userName") != null ? "编辑用户" : "添加用户" %></title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/admin.css">
    <script>
        function validateForm() {
            var password = document.getElementById("password").value;
            var confirmPassword = document.getElementById("confirmPassword").value;
            
            if (password !== confirmPassword) {
                alert("两次输入的密码不一致，请重新输入");
                return false;
            }
            return true;
        }
    </script>
</head>
<body class="admin">
    <%@ include file="../WEB-INF/jspf/admin_header.jsp" %>
    <div class="container">
        <h1><%= request.getParameter("userName") != null ? "编辑用户" : "添加新用户" %></h1>

        <% 
            String userName = request.getParameter("userName");
            UserBean user = null;
            AdminService adminService = new AdminService();
            boolean isEditMode = userName != null && !userName.isEmpty();
            boolean isAdminAccount = false;
            if (isEditMode) {
                user = adminService.getUserByUsername(userName);
                isAdminAccount = "admin".equals(userName);
            }
            if (user == null) {
                user = new UserBean(); // For add mode or if user not found for edit
            }

            String action = isEditMode ? "updateUser" : "createUser";
        %>

        <% String message = (String) request.getAttribute("message"); %>
        <% if (message != null && !message.isEmpty()) { %>
            <p class="message <%= message.startsWith("Error") ? "error-message" : "success-message" %>"><%= message %></p>
        <% } %>

        <form action="manageUsers" method="post" onsubmit="return validateForm()">
            <input type="hidden" name="action" value="<%= action %>">

            <div class="form-group">
                <label for="userName">用户名:</label>
                <input type="text" id="userName" name="userName" value="<%= user.getUserName() != null ? user.getUserName() : "" %>" <%= isEditMode ? "readonly" : "required" %>>
                <% if (!isEditMode) { %><small>添加新用户时，用户名必须唯一。编辑时不可更改。</small><% } %>
            </div>

            <% if (!isEditMode || !isAdminAccount) { %>
            <div class="form-group">
                <label for="password">密码:</label>
                <input type="password" id="password" name="password" <%= !isEditMode ? "required" : "" %>>
                <small><%= isEditMode ? "留空表示不更改密码" : "创建用户时必填" %></small>
            </div>

            <div class="form-group">
                <label for="confirmPassword">确认密码:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" <%= !isEditMode ? "required" : "" %>>
            </div>
            <% } %>

            <div class="form-group">
                <label for="fullName">姓名:</label>
                <input type="text" id="fullName" name="fullName" value="<%= user.getFullName() != null ? user.getFullName() : "" %>" required>
            </div>

            <div class="form-group">
                <label for="email">电子邮件:</label>
                <input type="email" id="email" name="email" value="<%= user.getEmail() != null ? user.getEmail() : "" %>">
            </div>

            <div class="form-group">
                <label for="phone">电话:</label>
                <input type="text" id="phone" name="phone" value="<%= user.getPhone() != null ? user.getPhone() : "" %>">
            </div>

            <div class="form-group">
                <label for="userRole">用户角色:</label>
                <select id="userRole" name="userRole" <%= isAdminAccount ? "disabled" : "" %>>
                    <option value="user" <%= "user".equals(user.getUserRole()) ? "selected" : "" %>>普通用户</option>
                    <option value="admin" <%= "admin".equals(user.getUserRole()) ? "selected" : "" %>>管理员</option>
                </select>
                <% if (isAdminAccount) { %><input type="hidden" name="userRole" value="admin"><% } %>
                <small><% if (isAdminAccount) { %>主管理员角色不可更改<% } %></small>
            </div>

            <button type="submit" class="submit-button"><%= isEditMode ? "更新用户" : "添加用户" %></button>
            <a href="manage_users.jsp" class="cancel-button">取消</a>
        </form>
    </div>
    <%@ include file="../WEB-INF/jspf/admin_footer.jsp" %>
</body>
</html>
