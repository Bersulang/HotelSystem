<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.example.hotel.beans.RoomResultBean" %>
<%@ page import="com.example.hotel.beans.UserBean" %>

<html>
<head>
    <title>预订确认</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }
        .container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); max-width: 700px; margin: auto; }
        h1 { color: #333; text-align: center; }
        .booking-summary, .user-info-form, .payment-info {
            border: 1px solid #eee; padding: 15px; margin-bottom: 20px; border-radius: 5px;
        }
        .booking-summary h2, .user-info-form h2, .payment-info h2 { margin-top: 0; color: #007bff; border-bottom: 1px solid #eee; padding-bottom: 10px;text-align: center;}
        label { display: block; margin-top: 10px; color: #555; font-weight: bold; }
        input[type="text"], input[type="tel"], textarea, select {
            width: calc(100% - 22px);
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        textarea { resize: vertical; min-height: 60px; }
        input[type="submit"] {
            background-color: #28a745; color: white; padding: 12px 20px;
            border: none; border-radius: 4px; cursor: pointer; margin-top: 20px;
            font-size: 16px; width: 100%;
        }
        input[type="submit"]:hover { background-color: #218838; }
        .detail-item { margin-bottom: 8px; }
        .detail-item strong { color: #333; min-width:100px; display: inline-block;}
        .total-fee { font-size: 1.2em; font-weight: bold; color: #dc3545; margin-top:10px;}
        .error { color: red; margin-bottom: 15px; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; border-radius: 4px; }
        .back-link { display: inline-block; margin-bottom: 20px; color: #007bff; text-decoration: none; }
        .back-link:hover { text-decoration: underline; }
        .login-required-notice {
            background-color: #fff3cd;
            color: #856404;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            border: 1px solid #ffeeba;
            text-align: center;
        }
        .login-required-notice a {
            color: #0056b3;
            font-weight: bold;
            text-decoration: none;
        }
        .login-required-notice a:hover {
            text-decoration: underline;
        }

        /* 禁用表单的样式 */
        .form-disabled input,
        .form-disabled textarea,
        .form-disabled select {
            background-color: #f8f9fa;
            border-color: #ddd;
            color: #aaa;
            cursor: not-allowed;
        }
        
        .form-disabled label {
            color: #999;
        }
        
        /* 表单禁用时整体显示半透明效果 */
        .form-disabled .user-info-form,
        .form-disabled .payment-info {
            opacity: 0.7;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/jspf/header.jsp" />

    <div class="container">
        <h1>确认您的预订</h1>
        <a href="query" class="back-link">&laquo; 返回修改查询</a>
        <span style="margin: 0 10px;">|</span>
        <a href="query?action=showLastResults" class="back-link">返回房型选择 &raquo;</a>

        <%
            UserBean currentUser = (UserBean) session.getAttribute("currentUser");
            if (currentUser == null) {
        %>
            <div class="login-required-notice">
                <p><strong>请先登录</strong></p>
                <p>您需要登录才能完成预订和支付。请点击上方的"登录"按钮或 <a href="${pageContext.request.contextPath}/login">点此登录</a>。</p>
                <p>如果您还没有账户，可以 <a href="${pageContext.request.contextPath}/register">免费注册</a>。</p>
            </div>
        <% } %>

        <c:if test="${empty requestScope.bookingDetails}">
            <p class="error">无法加载预订详情。会话可能已过期或信息丢失。请 <a href="query">重新开始查询</a>。</p>
        </c:if>

        <c:if test="${not empty requestScope.bookingDetails}">
            <c:if test="${not empty requestScope.errorMessage}">
                <p class="error">${requestScope.errorMessage}</p>
            </c:if>

            <div class="booking-summary">
                <h2>预订概要</h2>
                <div class="detail-item"><strong>酒店名称:</strong> ${bookingDetails.selectedRoom.hotelName}</div>
                <div class="detail-item"><strong>房型:</strong> ${bookingDetails.selectedRoom.roomTypeName}</div>
                <div class="detail-item"><strong>入住日期:</strong> ${bookingDetails.checkInDate}</div>
                <div class="detail-item"><strong>离店日期:</strong> ${bookingDetails.checkOutDate}</div>
                <div class="detail-item"><strong>入住晚数:</strong> ${bookingDetails.numberOfNights} 晚</div>
                <div class="detail-item"><strong>每晚价格:</strong> 
                     <c:choose>
                        <c:when test="${bookingDetails.selectedRoom.promotionalPrice > 0 && bookingDetails.selectedRoom.promotionalPrice < bookingDetails.selectedRoom.pricePerNight}">
                            <fmt:formatNumber value="${bookingDetails.selectedRoom.promotionalPrice}" type="currency" currencySymbol="¥"/> (促销)
                            <span style="text-decoration: line-through; color: #888; margin-left: 5px;"><fmt:formatNumber value="${bookingDetails.selectedRoom.pricePerNight}" type="currency" currencySymbol="¥"/></span>
                        </c:when>
                        <c:otherwise>
                            <fmt:formatNumber value="${bookingDetails.selectedRoom.pricePerNight}" type="currency" currencySymbol="¥"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="detail-item total-fee"><strong>总费用:</strong> <fmt:formatNumber value="${bookingDetails.totalFee}" type="currency" currencySymbol="¥"/></div>
                <div class="detail-item total-fee"><strong>需付定金 (8%):</strong> <fmt:formatNumber value="${bookingDetails.depositAmount}" type="currency" currencySymbol="¥"/></div>
                <div class="detail-item"><strong>取消政策:</strong>${bookingDetails.cancellationPolicy}</div>
            </div>            <% 
                UserBean user = (UserBean) session.getAttribute("currentUser");
                boolean isLoggedIn = (user != null);
            %>
            <form action="confirmBooking" method="post" <% if (!isLoggedIn) { %>class="form-disabled"<% } %>>
                <div class="user-info-form">
                    <h2>入住人信息</h2>
                    <label for="guestName">入住人姓名:</label>
                    <input type="text" id="guestName" name="guestName" value="${bookingDetails.userName}" placeholder="姓名" required <% if (!isLoggedIn) { %>disabled<% } %>>

                    <label for="contactNumber">联系电话:</label>
                    <input type="tel" id="contactNumber" name="contactNumber" value="${bookingDetails.contactInfo}" placeholder="联系方式" required <% if (!isLoggedIn) { %>disabled<% } %>>

                    <label for="specialRequests">特殊需求:</label>
                    <textarea id="specialRequests" name="specialRequests" rows="3" <% if (!isLoggedIn) { %>disabled<% } %>>${bookingDetails.specialRequests}</textarea>
                </div>

                <div class="payment-info">
                    <h2>支付方式</h2>                    <label for="paymentMethod">选择支付方式:</label>
                    <select id="paymentMethod" name="paymentMethod" required <% if (!isLoggedIn) { %>disabled<% } %>>
                        <option value="支付宝">支付宝</option>
                        <option value="微信支付">微信支付</option>
                        <option value="信用卡">信用卡</option>
                        <option value="fail_test">模拟支付失败</option> <!-- 用于测试 -->
                    </select>                </div>                <% 
                    // 这里使用之前定义的 isLoggedIn 变量，而不是重新获取 user 对象
                    if (isLoggedIn) {
                %>
                    <input type="submit" value="确认预订并支付定金">
                <% } else { %>
                    <button type="button" style="background-color: #6c757d; color: white; padding: 12px 20px; border: none; border-radius: 4px; cursor: not-allowed; margin-top: 20px; font-size: 16px; width: 100%;" disabled>请先登录才能支付</button>
                <% } %>
            </form>
        </c:if>
    </div>
</body>
</html>