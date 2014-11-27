<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://webcalendar.com/functions" prefix="f" %>

<div
onclick="ajaxDetail('${event.id}', '${f:dateToString(event.startDate)}')"
style="background-color: ${event.color}; margin: 3px 10px 3px 10px;
<c:if test='${today.toLocalDate().isAfter(event.endDate) ||
    today.toLocalDate().isEqual(event.endDate) &&
    today.toLocalTime().isAfter(event.endTime)}'>
        opacity: 0.6;
</c:if>
"
class="${event.id}
<c:if test="${!event.startDate.isEqual(event.endDate) &&
      currentDate.isEqual(event.startDate)}">
    arrow_box_long_next
</c:if>
<c:if test="${!event.startDate.isEqual(event.endDate) &&
      currentDate.isEqual(event.endDate)}">
    arrow_box_long_prev
</c:if>
<c:if test="${!event.startDate.isEqual(event.endDate) &&
    currentDate.isAfter(event.startDate) &&
    currentDate.isBefore(event.endDate)}">
     arrow_box_long_between
</c:if>
<c:if test="${event.startDate.isEqual(event.endDate)}">
     divEvent
</c:if>
"
>

<div class="metaFont" style="margin-bottom: 5px"> ${event.title} </div>
<c:if test="${event.allDay}">
    <span style="font-size: 11px">
        ${f:dateToString(event.startDate)}
        <c:if test="${event.startDate != event.endDate}">
            - ${f:dateToString(event.endDate)}
        </c:if>
    </span>
</c:if>

<c:if test="${!event.allDay}">
    <span style="font-size: 11px">
        <c:if test="${event.startDate == event.endDate}">
            ${f:dateToString(event.startDate)}
            ${event.startTime} - ${event.endTime}
        </c:if>
        <c:if test="${event.startDate != event.endDate}">
            ${f:dateToString(event.startDate)} ${event.startTime}
            - ${f:dateToString(event.endDate)} ${event.endTime}
        </c:if>
    </span>
</c:if>
    <br/>
</div>