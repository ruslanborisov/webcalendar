<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://webcalendar.com/functions" prefix="f" %>

<c:if test="${not empty photo}">
    <div id="userPhoto" style=" background-image: url(${photo})">
    </div>
</c:if>

<div id="userInfo">
    <p>
        <span class="titleFont">${username}</span> <br/>
    </p>
    <p>
        <span class="metaFont"> since: </span>
        <span style="font-size: 11px">
            ${since.getDayOfMonth()},
            ${f:replaceMonth(since.getMonth())}
            ${since.getYear()}
        </span>
        <br/>
    </p>
    <p>
        <span class="metaFont"> email: </span> ${email} <br/>
    </p>
    <p>
        <span class="metaFont"> events: </span> ${amountEvent}
    </p>
</div>