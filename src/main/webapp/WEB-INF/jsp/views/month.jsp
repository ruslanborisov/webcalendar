<div id="info" class="dispNone" mini="${miniCalendar}" period="${currentPeriod}"></div>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://webcalendar.com/functions" prefix="f" %>

<table>
    <tr style="height: 20px">
        <th> Mon </th>
        <th> Tue </th>
        <th> Wed </th>
        <th> Thu </th>
        <th> Fri </th>
        <th> Sat </th>
        <th> Sun </th>
    </tr>
    <c:forEach begin="0" end="5" var="i">
        <tr style="height: 75px">
            <c:forEach var="monthDay" items="${month.get(i)}" >
                <td <c:if test="${monthDay.isEqual(today.toLocalDate())}"> style="background-color: #FAEBD7;" </c:if> >
                    <c:choose>
                        <c:when test="${monthDay.getDayOfMonth()==1}">
                            ${f:replaceMonth(monthDay.getMonth()).substring(0,3)} ,
                            <a class="infoFont" href="javascript:{} oncklick = ajaxQuickSelection('${f:dateToString(monthDay)}')"> ${monthDay.getDayOfMonth()} </a>
                            <br/>
                        </c:when>
                        <c:otherwise>
                            <a class="infoFont" href="javascript:{} oncklick = ajaxQuickSelection('${f:dateToString(monthDay)}')"> ${monthDay.getDayOfMonth()} </a>
                            <br/>
                        </c:otherwise>
                    </c:choose>
                    <div class="monthDayCell">
                        <c:if test="${not empty events}">
                            <c:forEach var="eventLocal" items="${events.get(i)}" >
                                <c:if test="${monthDay.isEqual(eventLocal.startDate) || eventLocal.startDate.isBefore(monthDay) &&
                                              eventLocal.endDate.isAfter(monthDay) || eventLocal.endDate.isEqual(monthDay)}">
                                    <c:set var="event" value="${eventLocal}" scope="request"/>
                                    <c:set var="currentDate" value="${monthDay}" scope="request"/>
                                    <c:set var="today" value="${today}" scope="request"/>
                                    <jsp:include page="../includes/eventShort.jsp" />
                                </c:if>
                            </c:forEach>
                        </c:if>
                    </div>
                </td>
            </c:forEach>
        </tr>
    </c:forEach>
</table>