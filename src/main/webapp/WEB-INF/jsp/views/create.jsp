<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!-- CREATE FORM -->

	<form id="formCreate" eventId="${event.id}">

            <div id="allDayBlock">
                <input id="allDay" type="checkbox" name="allDay" value="true">
                <label for="allDay" class="labelButton"> All Day </label>
                <span id="created" class="dispNone succesful">created</span>
                <span id="exceptionCreate" class="dispNone exception"></span>
            </div>

            <div id="titleBlock">
                <input id="title" class="input" type="text" placeholder="Enter Title.." name="title" style="width :340px" maxlength="30">
                <c:if test="${header['User-Agent'].contains('Chrome')}">
                    <img id="micTitle" class="menuItem" src="<c:url value="/resources/images/micblack.png"/>" alt="">
                    <span id="instructionsTitle" class="InfoFont"></span>
                </c:if>
            </div>

            <div id="periodBlock">
                <input id="startDate" class="input" type="text" placeholder="Start date.." name="startDate" style="width :90px" readonly>
                <input id="startTime" class="input" type="text" placeholder="Time.." name="startTime" style="width :50px" readonly>
                <input id="endDate" class="input" type="text" placeholder="End date.." name="endDate" style="width :90px" readonly>
                <input id="endTime" class="input" type="text" placeholder="Time.." name="endTime" style="width :50px" readonly>
            </div>

            <div id="descriptionBlock">
                <textarea id="description" class="input" placeholder="Enter Description.." name="desc" maxlength="100"></textarea>
                <c:if test="${header['User-Agent'].contains('Chrome')}">
                    <img id="micDescription" class="menuItem" src="<c:url value="/resources/images/micblack.png"/>" alt="">
                    <span id="instructionsDescription" class="InfoFont"></span>
                </c:if>
            </div>

            <div id="attenderBlock">
                <input id="attenders" class="input" type="text" placeholder="Enter Attenders.." name="attender" maxlength="100">
                <a id="aboutAttenderInput" href="javascript:{}"> ? </a>
            </div>

            <div id="colorBlock">
                <span class="metaFont"> Event color: </span>
                <input id="B8B9FF" type="radio" name="color" value="#B8B9FF" checked>
                <label class="colorBox" for="B8B9FF" style="background-color: #B8B9FF"></label>

                <input id="7AE7BF" type="radio" name="color" value="#7AE7BF">
                <label class="colorBox" for="7AE7BF" style="background-color: #7AE7BF" ></label>

                <input id="46D6DB" type="radio" name="color" value="#46D6DB">
                <label class="colorBox" for="46D6DB" style="background-color: #46D6DB" ></label>

                <input id="51B749" type="radio" name="color" value="#51B749">
                <label class="colorBox" for="51B749" style="background-color: #51B749" ></label>

                <input id="FBD75B" type="radio" name="color" value="#FBD75B">
                <label class="colorBox" for="FBD75B" style="background-color: #FBD75B" ></label>

                <input id="FFB878" type="radio" name="color" value="#FFB878">
                <label class="colorBox" for="FFB878" style="background-color: #FFB878" ></label>

                <input id="DC2127" type="radio" name="color" value="#DC2127">
                <label class="colorBox" for="DC2127" style="background-color: #DC2127" ></label>

                <input id="DBADFF" type="radio" name="color" value="#DBADFF">
                <label class="colorBox" for="DBADFF" style="background-color: #DBADFF" ></label>

                <input id="E1E1E1" type="radio" name="color" value="#E1E1E1">
                <label class="colorBox" for="E1E1E1" style="background-color: #E1E1E1" ></label>
            </div>

            <div id="repeatBlock">
                <p class="metaFont"> Repeats: </p>
                    <input id="once" type="radio" name="repeatCheck" value="once" checked>
                    <label for="once" class="labelButton"> Once </label>
                    <input id="repeatPeriodic" type="radio" name="repeatCheck" value="repeatPeriodic">
                    <label for="repeatPeriodic" class="labelButton"> Period </label>
                    <input id="repeatDay" type="radio" name="repeatCheck" value="repeatDay">
                    <label for="repeatDay" class="labelButton"> Day </label>
                    <div id="repeatPeriodicArea" class="dispNone">
                        <label><input id="daily" type="radio" name="repeatPeriodic" value="daily" checked> Daily </label>
                        <label><input id="monthly" type="radio" name="repeatPeriodic" value="monthly">Monthly </label>
                        <label><input id="yearly" type="radio" name="repeatPeriodic" value="yearly">Yearly </label>
                    </div>
                    <div id="repeatDayArea" class="dispNone">
                        <label><input id="monday" type="checkbox" name="repeatDay" value="monday"> Mo </label>
                        <label><input id="tuesday" type="checkbox" name="repeatDay" value="tuesday"> Tu </label>
                        <label><input id="wednesday" type="checkbox" name="repeatDay" value="wednesday"> We </label>
                        <label><input id="thursday" type="checkbox" name="repeatDay" value="thursday"> Th </label>
                        <label><input id="friday" type="checkbox" name="repeatDay" value="friday"> Fr </label>
                        <label><input id="saturday" type="checkbox" name="repeatDay" value="saturday"> Sa </label>
                        <label><input id="sunday" type="checkbox" name="repeatDay" value="sunday"> Su </label>
                    </div>
                    <div id="occurrenceArea" class="dispNone">
                        Ends: <br/>
                        <label><input id="never" type="radio" name="occurrence" value="never" checked> Never </label> <br/>
                        <label><input id="after" type="radio" name="occurrence" value="after"> After </label>
                        <input id="numberOfOccurrence" type="text" class="input" name="numberOfOccurrence" style="width :20px; height: 20px"> occurrences
                    </div>
                </div>

            <div id="reminderBlock">
                <p class="metaFont"> Remind: </p>
                <div id="reminderMinutesDiv">
                    <select class="input" id="reminderTime" name="reminderTime">
                        <option value="0" checked>At start</option>
                        <option value="10">10 minutes</option>
                        <option value="30">30 minutes</option>
                    </select>
                    <label><input id="popup" type="checkbox" name="popup" value="popup"> Pop-up </label>
                    <label><input id="emailReminder" type="checkbox" name="emailReminder" value="emailReminder"> Email </label> <br/>
                </div>
            </div>
            <p><input id="submitCreate" class="button" type="submit" value="SUBMIT"></p>
	</form>
