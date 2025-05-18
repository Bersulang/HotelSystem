<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ page import="com.example.hotel.beans.RoomResultBean" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>选择房间</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }
        .container { background-color: #fff; padding: 20px; margin: auto; width: 50%; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        h1 { color: #333; text-align: center; }
        .room-item { border: 1px solid #ddd; padding: 15px; margin-bottom: 15px;text-align: center; border-radius: 5px; background-color: #f9f9f9;}
        .room-item h3 { margin-top: 0; color: #007bff; }
        .room-item p { margin: 5px 0; color: #555; }
        .room-item strong { color: #333; }
        .select-button {
            background-color: #28a745; color: white; padding: 8px 12px;
            border: none; border-radius: 4px; text-decoration: none; font-size: 14px; cursor: pointer;
        }
        .select-button:hover { background-color: #218838; }
        .no-results { color: #777; font-style: italic; }
        .back-link { display: inline-block; margin-top:20px; margin-bottom: 20px; color: #007bff; text-decoration: none; }
        .back-link:hover { text-decoration: underline; }
        .error { color: red; margin-top: 10px; }
        .price { font-weight: bold; }
        .original-price { text-decoration: line-through; color: #888; margin-left: 10px; }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/jspf/header.jsp" />

    <div class="container">
        <h1>选择您喜欢的房间</h1>
        <a href="query" class="back-link">&laquo; 重新查询</a>

        <c:if test="${not empty param.error}">
            <p class="error">
                <c:choose>
                    <c:when test="${param.error == 'bookingPrepFailed'}">抱歉，准备预订详情失败（房间可能已无库存或信息错误）。请尝试其他房间或返回修改查询。</c:when>
                    <c:otherwise>发生未知错误，请重试。</c:otherwise>
                </c:choose>
            </p>
        </c:if>

        <c:if test="${not empty requestScope.roomResults}">
            <p>根据您的查询条件（入住：${sessionScope.currentQuery.checkInDate}，离店：${sessionScope.currentQuery.checkOutDate}），找到以下房间：</p>
            <%--这里可以写更多但是在页面中看起来会很臃肿，不美观--%>
            <c:forEach var="room" items="${requestScope.roomResults}">
                <div class="room-item">
                    <h3>${room.hotelName} - ${room.roomTypeName}</h3>
                    <p><strong>酒店位置:</strong> ${room.hotelLocation}</p>
                    <p><strong>星级:</strong> ${room.hotelStarRating}星</p>
                    <p><strong>描述:</strong> ${room.roomDescription}</p>
                    <p><strong>床型:</strong> ${room.bedType} &nbsp;&nbsp;<strong>面积:</strong> ${room.area} m<sup>2</sup> &nbsp;&nbsp;<strong>最大入住:</strong> ${room.maxOccupancy}人</p>
                    <p><strong>设施:</strong>
                        <c:forEach var="facility" items="${room.roomFacilitiesList}" varStatus="loop">
                            ${facility}${!loop.last ? ', ' : ''}
                        </c:forEach>
                    </p>
                    <p><strong>价格:</strong>
                        <c:choose>
                            <c:when test="${room.promotionalPrice > 0 && room.promotionalPrice < room.pricePerNight}">
                                <span class="price"><fmt:formatNumber value="${room.promotionalPrice}" type="currency" currencySymbol="¥"/>/晚 (促销)</span>
                                <span class="original-price"><fmt:formatNumber value="${room.pricePerNight}" type="currency" currencySymbol="¥"/></span>
                            </c:when>
                            <c:otherwise>
                                <span class="price"><fmt:formatNumber value="${room.pricePerNight}" type="currency" currencySymbol="¥"/>/晚</span>
                            </c:otherwise>
                        </c:choose>
                    </p>
                    <p><strong>剩余:</strong> ${room.realTimeStock} 间</p>

                    <c:if test="${room.realTimeStock > 0}">
                        <form action="selectRoom" method="post" style="display: inline;">
                            <input type="hidden" name="roomId" value="${room.roomId}">
                            <button type="submit" class="select-button">选择此房间</button>
                        </form>
                    </c:if>
                    <c:if test="${room.realTimeStock <= 0}">
                        <p style="color: red;">此房型当前已无库存</p>
                    </c:if>
                </div>
            </c:forEach>
        </c:if>

        <c:if test="${empty requestScope.roomResults}">
            <p class="no-results">未找到符合条件的房间。请尝试修改查询条件。</p>
        </c:if>

    </div>
</body>
</html>