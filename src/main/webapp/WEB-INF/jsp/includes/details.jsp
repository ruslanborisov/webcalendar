<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://webcalendar.com/functions" prefix="f" %>

<div id="detailTitleBlock">
    <span class="titleFont">${event.title}</span>
</div>
<div id="detailPeriodBlock">
    ${period}
</div>
<div id="detailDescriptionBlock">
    <c:if test="${event.description!=null}">
        <span class="metaFont">Description:</span>
        ${event.description}
    </c:if>
</div>
<div id="detailAttenderBlock">
    <span class="metaFont">Guests:</span>
    <c:forEach var="attender" items="${event.eventAttenders}" >
        ${attender.toString()}
    </c:forEach>
</div>
<div id="detailRepeaterBlock">
    <span class="metaFont">Recurrence:</span>
    <c:forEach var="repeater" items="${event.eventRepeaters}" >
        ${repeater.toString()}
    </c:forEach>
    <c:if test="${event.numberOfOccurrence!=null}">
        , ${event.numberOfOccurrence} occurrences
    </c:if>

</div>
<div id="detailMenuBlock">
    <c:if test="${header['User-Agent'].contains('Chrome')}">
        <a href="javascript:{} oncklick = ajaxSpeechSynthesis('${event.id}', '${f:dateToString(event.startDate)}')"><img src="${pageContext.request.contextPath}/resources/images/speaker.png"></a>
    </c:if>
    <a style="margin-left: 160px" href="javascript:{} oncklick = ajaxDeleteEvent('${event.id}') "> <span class="metaFont" >Delete</span></a> |
    <a href="javascript:{} oncklick = ajaxCreatePage('${event.id}', '${f:dateToString(event.startDate)}') "> <span class="metaFont">Edit event</span></a>
</div>
