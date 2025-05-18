<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.hotel.beans.UserBean" %>


<style>
  .app-header {
    background-color: #ffffff;
    color: #333;
    padding: 15px 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #e0e0e0;
    box-shadow: 0 2px 10px rgba(0,0,0,0.03);
    position: sticky;
    top: 0;
    z-index: 100;
    transition: all 0.3s ease;
  }
  .app-header .logo a {
    color: #2c3e50;
    text-decoration: none;
    font-size: 1.5em;
    font-weight: bold;
    transition: color 0.3s ease;
  }
  .app-header .logo a:hover {
    color: #3498db;
  }
  .app-header .user-status {
    display: flex;
    align-items: center;
  }
  .app-header .user-status a {
    color: #333;
    text-decoration: none;
    margin-left: 15px;
    padding: 8px 15px;
    border-radius: 4px;
    font-weight: 500;
    transition: all 0.3s ease;
  }
  .app-header .user-status a:hover {
    transform: translateY(-2px);
    box-shadow: 0 2px 5px rgba(0,0,0,0.1);
  }
  .app-header .user-status .username {
    margin-right: 10px;
    color: #495057;
    font-weight: 500;
    padding: 8px 10px;
    background-color: #f8f9fa;
    border-radius: 4px;
    border: 1px solid #e9ecef;
  }
  .app-header .user-status .logout-btn {
    background-color: #fff;
    color: #dc3545;
    border: 1px solid #dc3545;
  }
  .app-header .user-status .logout-btn:hover {
    background-color: #dc3545;
    color: white;
  }
  .app-header .user-status .login-btn {
    background-color: #fff;
    color: #28a745;
    border: 1px solid #28a745;
  }
  .app-header .user-status .login-btn:hover {
    background-color: #28a745;
    color: white;
  }
  .app-header .user-status .register-btn {
    background-color: #fff;
    color: #007bff;
    border: 1px solid #007bff;
  }
  .app-header .user-status .register-btn:hover {
    background-color: #007bff;
    color: white;
  }
  .logout-message {
    background-color: #d4edda;
    color: #155724;
    padding: 10px 15px;
    margin-bottom: 15px;
    border: 1px solid #c3e6cb;
    border-radius: 4px;
    text-align: center;
    animation: fadeOut 5s forwards;
    position: relative;
    z-index: 101;
  }
  @keyframes fadeOut {
    0% { opacity: 1; }
    70% { opacity: 1; }
    100% { opacity: 0; visibility: hidden; }
  }
</style>

<% 
  // 显示登出成功消息
  String logoutMessage = (String) session.getAttribute("logoutMessage");
  String loginMessage = (String) session.getAttribute("loginMessage");
  
  if (logoutMessage != null) {
%>
  <div class="logout-message">
    <%= logoutMessage %>
  </div>
  <% 
    // 显示一次后删除，避免刷新后再次显示
    session.removeAttribute("logoutMessage");
  %>
<% } 
  
  if (loginMessage != null) {
%>
  <div class="logout-message" style="background-color: #cce5ff; border-color: #b8daff; color: #004085;">
    <%= loginMessage %>
  </div>
  <% 
    // 显示一次后删除，避免刷新后再次显示
    session.removeAttribute("loginMessage");
  %>
<% } %>

<div class="app-header">
    <div class="logo">
        <a href="${pageContext.request.contextPath}/queryForm.jsp">酒店预订系统</a>
    </div>
    <div class="user-status">
        <% 
            UserBean currentUser = (UserBean) session.getAttribute("currentUser");
            if (currentUser != null) {
        %>
            <span class="username">欢迎, <%= currentUser.getUsername() %>!</span>
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">退出登录</a>
        <% 
            } else {
        %>
            <a href="${pageContext.request.contextPath}/login" class="login-btn">登录</a>
            <a href="${pageContext.request.contextPath}/register" class="register-btn">注册</a>
        <% 
            }
        %>
    </div>
</div>