<div id="info" class="dispNone" mini="${miniCalendar}" period="${currentPeriod}"></div>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://webcalendar.com/functions" prefix="f" %>

<table>
    <tr style="height: 20px">
        <th> <a class="infoFont" href="javascript:{} oncklick = ajaxQuickSelection('${f:dateToString(week.get(0))}')">
            Mon / ${week.get(0).getDayOfMonth()}.${week.get(0).getMonthValue()} </a></th>
        <th> <a class="infoFont" href="javascript:{} oncklick = ajaxQuickSelection('${f:dateToString(week.get(1))}')">
            Tue / ${week.get(1).getDayOfMonth()}.${week.get(1).getMonthValue()} </a></th>
        <th> <a class="infoFont" href="javascript:{} oncklick = ajaxQuickSelection('${f:dateToString(week.get(2))}')">
            Wed / ${week.get(2).getDayOfMonth()}.${week.get(2).getMonthValue()} </a></th>
        <th> <a class="infoFont" href="javascript:{} oncklick = ajaxQuickSelection('${f:dateToString(week.get(3))}')">
            Thu / ${week.get(3).getDayOfMonth()}.${week.get(3).getMonthValue()} </a></th>
        <th> <a class="infoFont" href="javascript:{} oncklick = ajaxQuickSelection('${f:dateToString(week.get(4))}')">
            Fri / ${week.get(4).getDayOfMonth()}.${week.get(4).getMonthValue()} </a></th>
        <th> <a class="infoFont" href="javascript:{} oncklick = ajaxQuickSelection('${f:dateToString(week.get(5))}')">
            Sat / ${week.get(5).getDayOfMonth()}.${week.get(5).getMonthValue()} </a></th>
        <th> <a class="infoFont" href="javascript:{} oncklick = ajaxQuickSelection('${f:dateToString(week.get(6))}')">
            Sun / ${week.get(6).getDayOfMonth()}.${week.get(6).getMonthValue()} </a></th>

    </tr>
    <tr style="height: 590px;">
        <c:forEach var="weekDay" items="${week}" >
            <td <c:if test="${weekDay.isEqual(today.toLocalDate())}"> style="background-color: #FAEBD7;" </c:if> >
                <div class="weekDayCell">
                    <c:if test="${not empty events}">
                        <c:forEach var="eventLocal" items="${events}" >
                            <c:if test="${weekDay.isEqual(eventLocal.startDate) || eventLocal.startDate.isBefore(weekDay) &&
                                              eventLocal.endDate.isAfter(weekDay) || eventLocal.endDate.isEqual(weekDay)}">
                                <c:set var="event" value="${eventLocal}" scope="request"/>
                                <c:set var="currentDate" value="${weekDay}" scope="request"/>
                                <c:set var="today" value="${today}" scope="request"/>
                                <jsp:include page="../includes/eventShort.jsp" />
                            </c:if>
                        </c:forEach>
                    </c:if>
                </div>
            </td>
        </c:forEach>
    </tr>
 </table>