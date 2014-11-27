<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://webcalendar.com/functions" prefix="f" %>

    <div class="searchPeriod">Free time</div>
    <c:if test="${not empty noFreeTime}">
        <p style="margin-left: 10px"> ${noFreeTime}</p>
    </c:if>

    <c:if test="${not empty freeTime}">
        <c:forEach var="interval" items="${freeTime}">
            <div class="divInterval">
                ${freeTime.indexOf(interval)+1} :
                ${f:periodWithTimeToStringDescription(interval.get(0), interval.get(1))}
            </div>
        </c:forEach>
    </c:if>