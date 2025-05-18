<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <title>预订成功</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; display: flex; justify-content: center; align-items: center; min-height: 90vh; }
        .container { background-color: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 0 15px rgba(0,0,0,0.1); text-align: center; max-width: 600px;}
        .icon-success { font-size: 50px; color: #28a745; margin-bottom: 20px; }
        h1 { color: #28a745; margin-bottom: 15px;}
        p { color: #555; line-height: 1.6; }
        .order-details { text-align: left; margin-top: 25px; border-top: 1px solid #eee; padding-top: 20px;}
        .order-details p { margin-bottom: 8px; }
        .order-details strong { color: #333; min-width: 120px; display: inline-block;}
        .action-links a { 
            display: inline-block; margin-top: 30px; padding: 10px 20px; 
            background-color: #007bff; color: white; text-decoration: none; 
            border-radius: 5px; margin-right: 10px;
        }
        .action-links a:hover { background-color: #0056b3; }
        .important-note { font-size: 0.9em; color: #777; margin-top:20px; background-color: #f9f9f9; padding: 10px; border-left: 3px solid #007bff;}
    </style>
</head>
<body style="margin:0; background-color: #f4f4f4;">
    <!-- 顶部header始终在最上方，固定定位 -->
    <div style="width: 100%; background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.04); position: fixed; top: 0; left: 0; z-index: 100;">
        <jsp:include page="/WEB-INF/jspf/header.jsp" />
    </div>

    <!-- 主内容区，顶部留出header高度 -->
    <div class="success-container" style="display: flex; justify-content: center; align-items: center; min-height: 100vh; padding-top: 80px;">
        <div class="container" style="background-color: #fff; padding: 36px 32px; border-radius: 12px; box-shadow: 0 4px 24px rgba(0,0,0,0.10); text-align: center; max-width: 600px; width: 100%;">
            <div class="icon-success" style="font-size: 56px; color: #28a745; margin-bottom: 24px;">&#10004;</div>
            <h1 style="color: #28a745; margin-bottom: 18px; font-size: 2.1em;">恭喜您，预订成功！</h1>
            <c:if test="${not empty requestScope.finalOrder}">
                <p style="color: #444; font-size: 1.1em;">您的酒店房间已成功预订。感谢您的选择！</p>
                <p style="color: #666;">确认邮件/短信已发送至您的注册邮箱/手机。</p>
                <div class="order-details" style="text-align: left; margin-top: 28px; border-top: 1px solid #eee; padding-top: 22px;">
                    <p><strong style="color: #333; min-width: 120px; display: inline-block;">订单号:</strong> ${finalOrder.orderId}</p>
                    <p><strong style="color: #333; min-width: 120px; display: inline-block;">酒店名称:</strong> ${finalOrder.hotelName}</p>
                    <p><strong style="color: #333; min-width: 120px; display: inline-block;">房型:</strong> ${finalOrder.roomTypeName}</p>
                    <p><strong style="color: #333; min-width: 120px; display: inline-block;">入住人:</strong> ${finalOrder.guestName}</p>
                    <p><strong style="color: #333; min-width: 120px; display: inline-block;">联系电话:</strong> ${finalOrder.contactNumber}</p>
                    <p><strong style="color: #333; min-width: 120px; display: inline-block;">入住日期:</strong> ${finalOrder.checkInDate}</p>
                    <p><strong style="color: #333; min-width: 120px; display: inline-block;">离店日期:</strong> ${finalOrder.checkOutDate}</p>
                    <p><strong style="color: #333; min-width: 120px; display: inline-block;">总费用:</strong> <fmt:formatNumber value="${finalOrder.totalFee}" type="currency" currencySymbol="¥"/></p>
                    <p><strong style="color: #333; min-width: 120px; display: inline-block;">已付定金:</strong> <fmt:formatNumber value="${finalOrder.depositAmountPaid}" type="currency" currencySymbol="¥"/></p>
                    <p><strong style="color: #333; min-width: 120px; display: inline-block;">支付方式:</strong> ${finalOrder.paymentMethod}</p>
                    <p><strong style="color: #333; min-width: 120px; display: inline-block;">交易流水号:</strong> ${finalOrder.transactionId}</p>
                    <p><strong style="color: #333; min-width: 120px; display: inline-block;">订单状态:</strong> ${finalOrder.orderStatus}</p>
                    <p><strong style="color: #333; min-width: 120px; display: inline-block;">预订时间:</strong> ${finalOrder.bookingTimestamp}</p>
                    <p><strong style="color: #333; min-width: 120px; display: inline-block;">特殊需求:</strong> ${empty finalOrder.specialRequests ? '无' : finalOrder.specialRequests}</p>
                    <hr style="margin: 18px 0; border: none; border-top: 1px dashed #ddd;">
                    <p><strong style="color: #333; min-width: 120px; display: inline-block;">虚拟房号:</strong> ${finalOrder.virtualRoomNumber} <span style="color:#888; font-size:0.95em;">(请妥善保管)</span></p>
                    <p><strong style="color: #333; min-width: 120px; display: inline-block;">NFC密钥:</strong> ${finalOrder.nfcKey} <span style="color:#888; font-size:0.95em;">(此为模拟密钥)</span></p>
                </div>
                <div class="important-note" style="font-size: 1em; color: #555; margin-top:22px; background-color: #f9f9f9; padding: 12px 16px; border-left: 4px solid #007bff; border-radius: 4px;">
                    请注意：虚拟房号和NFC密钥是您入住的重要凭证。请您妥善保管！<br/>
                    酒店地址：${empty finalOrder.hotelLocation ? '[酒店地址未提供]' : finalOrder.hotelLocation}，联系电话：${empty finalOrder.hotelContact ? '[酒店联系方式未提供]' : finalOrder.hotelContact}。
                </div>
                <div class="action-links" style="margin-top: 34px;">
                    <a href="query" style="display: inline-block; padding: 12px 28px; background-color: #007bff; color: white; text-decoration: none; border-radius: 6px; margin-right: 12px; font-size: 1.08em; transition: background 0.2s;">继续预订其他房间</a>
                </div>
            </c:if>
            <c:if test="${empty requestScope.finalOrder}">
                <p style="color:red; font-size: 1.1em;">抱歉，未能加载您的订单信息。请联系客服确认您的预订状态。</p>
                <div class="action-links" style="margin-top: 34px;">
                    <a href="query" style="display: inline-block; padding: 12px 28px; background-color: #007bff; color: white; text-decoration: none; border-radius: 6px; margin-right: 12px; font-size: 1.08em; transition: background 0.2s;">返回首页</a>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html> 