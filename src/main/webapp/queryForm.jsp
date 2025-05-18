<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>酒店房间查询</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }
        .container { background-color: #fff; margin: auto; padding: 20px; border-radius: 8px; width: 40%; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        /*45%的宽度看起来还是有点奇怪，input框有点长看起来有点丑，但是地址可能会很长所以就这样把*/
        h1 { color: #333; text-align: center; }
        label { display: block; margin-top: 10px; color: #555; }
        input[type="text"], input[type="date"], input[type="number"], select {
            width: calc(100% - 22px);
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        input[type="submit"] { 
            background-color: #007bff; color: white; padding: 10px 15px; 
            border: none; border-radius: 4px; cursor: pointer; margin-top: 20px;
            font-size: 16px;
            width: 40%;
        }
        input[type="submit"]:hover { background-color: #0056b3; }
        .error { color: red; margin-top: 10px; }
        .form-group { margin-bottom: 15px; }
        .facility-group label { display: inline-block; margin-right: 15px; }
        .form-group.center {
            text-align: center;
        }

    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/jspf/header.jsp" />

    <div class="container">
        <h1>查询酒店房间</h1>

        <c:if test="${not empty param.error}">
            <p class="error">
                <c:choose>
                    <c:when test="${param.error == 'missingRoomId'}">错误：选择房间时未提供房间ID。</c:when>
                    <c:when test="${param.error == 'sessionExpired'}">错误：会话已过期或查询信息丢失，请重新查询。</c:when>
                    <c:when test="${param.error == 'sessionExpiredOrInvalid'}">错误：会话已过期或预订信息无效，请重新开始预订流程。</c:when>
                    <c:otherwise>发生未知错误，请重试。</c:otherwise>
                </c:choose>
            </p>
        </c:if>

        <form action="query" method="post">
            <div class="form-group">
                <label for="hotelName">酒店名称(可选):</label>
                <input type="text" id="hotelName" name="hotelName" value="${queryForm.hotelName}">
            </div>

            <div class="form-group">
                <label for="location">位置(可选):</label>
                <input type="text" id="location" name="location" value="${queryForm.location}">
            </div>

            <div class="form-group">
                <label for="roomType">房型:</label>
                <select id="roomType" name="roomType">
                    <option value="" ${empty queryForm.roomType ? 'selected' : ''}>任何房型</option>
                    <option value="豪华大床房" ${queryForm.roomType == '豪华大床房' ? 'selected' : ''}>豪华大床房</option>
                    <option value="标准双床房" ${queryForm.roomType == '标准双床房' ? 'selected' : ''}>标准双床房</option>
                    <option value="行政套房" ${queryForm.roomType == '行政套房' ? 'selected' : ''}>行政套房</option>
                    <option value="经济房" ${queryForm.roomType == '经济房' ? 'selected' : ''}>经济房</option>
                    <option value="海景房" ${queryForm.roomType == '海景房' ? 'selected' : ''}>海景房</option>
                    <option value="情侣圆床房" ${queryForm.roomType == '情侣圆床房' ? 'selected' : ''}>情侣圆床房</option>
                </select>
            </div>

            <div class="form-group">
                <label for="checkInDate">*入住日期:</label>
                <input type="date" id="checkInDate" name="checkInDate" value="${queryForm.checkInDate}" required>
            </div>

            <div class="form-group">
                <label for="checkOutDate">*离店日期:</label>
                <input type="date" id="checkOutDate" name="checkOutDate" value="${queryForm.checkOutDate}" required>
            </div>

            <div class="form-group">
                <label for="priceRangeMin">价格范围 (最低):</label>
                <input type="number" id="priceRangeMin" name="priceRangeMin" step="0.01" value="${queryForm.priceRangeMin}">
            </div>

            <div class="form-group">
                <label for="priceRangeMax">价格范围 (最高):</label>
                <input type="number" id="priceRangeMax" name="priceRangeMax" step="0.01" value="${queryForm.priceRangeMax}">
            </div>
            
            <div class="form-group facility-group">
                <label>设施需求:</label><br>
                <input type="checkbox" id="facilitywifi" name="facilities" value="Wi-Fi" ${queryForm.facilities.contains('Wi-Fi') ? 'checked' : ''}>
                <label for="facilityPool">Wi-Fi</label>
                <input type="checkbox" id="facilitykt" name="facilities" value="空调" ${queryForm.facilities.contains('空调') ? 'checked' : ''}>
                <label for="facilityGym">空调</label>
                <input type="checkbox" id="facilityParking" name="facilitiestable" value="办公桌" ${queryForm.facilities.contains('办公桌') ? 'checked' : ''}>
                <label for="facilityParking">办公桌</label>
            </div>

            <div class="form-group center">
                <input type="submit" value="查询房间">
            </div>
        </form>
    </div>
</body>
</html>
