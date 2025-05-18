<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>    <title>用户登录 - 酒店预订系统</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/auth.css">
</head>
<body class="auth-page">
    <div class="auth-container login-container">
        <h1 class="app-title">酒店预订<br>系统</h1>
        
        <% 
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
        %>
            <div class="message error-message"><%= errorMessage %></div>
        <% 
            }
            String successMessage = (String) request.getAttribute("successMessage");
            if (successMessage != null) {
        %>
            <div class="message success-message"><%= successMessage %></div>
        <% 
            }
        %>
        
        <form action="${pageContext.request.contextPath}/login" method="post">            <div class="input-group">
                <input type="text" class="input-field" id="username" name="username" placeholder="用户名/邮箱" required autocomplete="off">
            </div>
            
            <div class="input-group">
                <input type="password" class="input-field" id="password" name="password" placeholder="密码" required>
                <span class="password-toggle" id="togglePassword">👀</span>
            </div>
            
            <button type="submit" class="submit-btn">登 录</button>
              <div class="footer">
                <label class="remember-me">
                    <input type="checkbox" name="remember"> 记住我
                </label>
                <a href="#" class="forgot-password">忘记密码?</a>
            </div>
              <div class="auth-link-container">
                <p>没有账号? <a href="${pageContext.request.contextPath}/register" class="auth-link">立即注册</a></p>
            </div>
        </form>
    </div>
      <script src="${pageContext.request.contextPath}/js/login.js"></script>
    </div>
</body>
</html>
