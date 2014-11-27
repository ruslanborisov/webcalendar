<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class='searchPeriod'> ${currentPeriod}</div>

<c:if test="${not empty noEvents}">
    <p style="margin-left: 10px">${noEvents}</p>
</c:if>

<c:if test="${not empty events}">
    <c:forEach var="eventLocal" items="${events}" >
        <c:set var="event" value="${eventLocal}" scope="request"/>
        <jsp:include page="eventSearch.jsp"/>
    </c:forEach>
</c:if>