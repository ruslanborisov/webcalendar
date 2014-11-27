<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<p class="aboutAppTitle">General</p>
WebCalendar allows to schedule your events easy.
The project was developed as an analogue of the Google Calendar.

<p>
    <span style="font-weight: bold">source code: </span>
    <a href="https://github.com/ruslanborisov/webcalendar.git" target="_blank">https://github.com/ruslanborisov/webcalendar.git</a>
</p>


<p class="aboutAppTitle">Stack of technologies</p>
<div class="stackInfo">
    <span style="font-weight: bold">java: </span>
    Spring (IoC, MVC, Security), Hibernate, Tomcat, JSP, JSTL.
</div>
<div class="stackInfo">
    <span style="font-weight: bold">web: </span>
    HTML, CSS, JavaScript, jQuery, Ajax.
</div>
<div class="stackInfo">
    <span style="font-weight: bold">database: </span>
    MySql.
</div>
<div class="stackInfo">
    <span style="font-weight: bold">testing: </span>
    jUnit, Mockito.
</div>
<div class="stackInfo">
    <span style="font-weight: bold">vcs: </span>
    Git, GitHub.
</div>

<p class="aboutAppTitle">Architecture</p>
<p>
    After session created, all events of current user loaded from database into DataStore.
    Publish and remove operations are executed using DataStore with database synchronization.
    Search operations are executed using DataStore only.
    Records are stored in the database with the use of Hibernate technology.
</p>
<p>
    Notifier provides ability to send email notification about the event.
    Verification of the need to notice is carried out every minute.
</p>
<p>
    Server side based on Spring MVC framework and JSP technology.
    User authentication is performed by using Spring Security.
    Login with Facebook or Vkontakte account is performed using oAuth protocol.
</p>
<p>
    Client side communicates with the server through Ajax requests.
</p>
<img style="width: 450px" src="<c:url value="/resources/images/architecture.png"/>">





