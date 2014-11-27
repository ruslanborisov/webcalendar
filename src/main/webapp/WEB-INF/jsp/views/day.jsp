<div id="info" class="dispNone" mini="${miniCalendar}" period="${currentPeriod}"></div>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://webcalendar.com/functions" prefix="f" %>

<c:if test="${not empty noEvents}">
    <p style="margin-left: 10px">${noEvents}</p>
</c:if>

<c:if test="${not empty events}">
    <c:forEach var="eventLocal" items="${events}" >
        <c:set var="event" value="${eventLocal}" scope="request"/>
        <jsp:include page="../includes/event.jsp" />
    </c:forEach>
</c:if>

