<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://webcalendar.com/functions" prefix="f" %>

<div
onclick="ajaxDetail('${event.id}', '${f:dateToString(event.startDate)}')"
style="background-color: ${event.color}; margin: 3px 7px 3px 7px;
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
<div>
    <c:if test="${event.title.length()>10}">
        ${event.title.substring(0,9)}...
    </c:if>
    <c:if test="${event.title.length()<=10}">
        ${event.title}
    </c:if>
</div>


</div>