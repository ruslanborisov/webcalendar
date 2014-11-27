<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://webcalendar.com/functions" prefix="f" %>

<c:forEach var="event" items="${events}" >
    <div id="remindTitleBlock" class="titleFont">
            ${event.title}
    </div>
    <div id="remindPeriodBlock">
        <c:if test="${event.allDay}">
            ${f:dateToString(event.startDate)}
            <c:if test="${event.startDate != event.endDate}">
                <br/> ${f:dateToString(event.endDate)}
            </c:if>
        </c:if>

        <c:if test="${!event.allDay}">
            <c:if test="${event.startDate == event.endDate}">
                ${f:dateToString(event.startDate)} <br/>
                ${event.startTime} - ${event.endTime}
            </c:if>
            <c:if test="${event.startDate != event.endDate}">
                ${f:dateToString(event.startDate)} ${event.startTime} <br/>
                ${f:dateToString(event.endDate)} ${event.endTime}
            </c:if>
        </c:if>
    </div>
    <c:if test="${event.description!=null}">
        <div id="remindDescriptionBlock">
            <span class="metaFont">Description:</span>
            <span> ${event.description} </span>
        </div>
    </c:if>
    <div id="remindAttenderBlock">
        <span class="metaFont">Guests:</span>
        <c:forEach var="attender" items="${event.eventAttenders}">
            <span> ${attender.toString()}</span>
        </c:forEach>
    </div>
</c:forEach>

