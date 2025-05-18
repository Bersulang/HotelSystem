<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>    <title>用户注册 - 酒店预订系统</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/auth.css">
</head>
<body class="auth-page">
    <div class="auth-container register-container">
        <h1 class="app-title">酒店预订<br>系统</h1>
        
        <% 
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
        %>
            <div class="message error-message"><%= errorMessage %></div>
        <% 
            }
        %>
        
        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="input-group">
                <input type="text" class="input-field" id="username" name="username" placeholder="用户名" required autocomplete="off">
            </div>
            
            <div class="input-group">
                <input type="password" class="input-field" id="password" name="password" placeholder="密码" required>
                <span class="password-toggle" id="togglePassword">👀</span>
            </div>
            
            <div class="input-group">
                <input type="password" class="input-field" id="confirmPassword" name="confirmPassword" placeholder="确认密码" required>
                <span class="password-toggle" id="toggleConfirmPassword">👀</span>
            </div>
            
            <button type="submit" class="submit-btn">注 册</button>
              <div class="auth-link-container">
                <p>已有账户? <a href="${pageContext.request.contextPath}/login" class="auth-link">立即登录</a></p>
            </div>
        </form>
    </div>
    
    <script>
        // 密码可见性切换
        document.getElementById('togglePassword').addEventListener('click', function() {
            const passwordInput = document.getElementById('password');
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            this.textContent = type === 'password' ? '👁️' : '👁️‍🗨️';
        });
        
        document.getElementById('toggleConfirmPassword').addEventListener('click', function() {
            const confirmPasswordInput = document.getElementById('confirmPassword');
            const type = confirmPasswordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            confirmPasswordInput.setAttribute('type', type);
            this.textContent = type === 'password' ? '👁️' : '👁️‍🗨️';
        });
        
        // 输入框动画效果
        const inputs = document.querySelectorAll('.input-field');
        inputs.forEach(input => {
            input.addEventListener('focus', function() {
                this.parentElement.classList.add('focused');
                this.style.transform = 'translateY(-2px)';
                this.style.boxShadow = '0 4px 8px rgba(40, 167, 69, 0.2)';
            });
            
            input.addEventListener('blur', function() {
                if (!this.value) {
                    this.parentElement.classList.remove('focused');
                }
                this.style.transform = 'translateY(0)';
                this.style.boxShadow = 'none';
            });
        });
        
        // 自动隐藏提示消息
        const messages = document.querySelectorAll('.message');
        if (messages.length > 0) {
            setTimeout(() => {
                messages.forEach(msg => {
                    msg.style.opacity = '0';
                    msg.style.transition = 'opacity 0.5s ease';
                    setTimeout(() => {
                        msg.style.display = 'none';
                    }, 500);
                });
            }, 5000);
        }
    </script>
</body>
</html> 