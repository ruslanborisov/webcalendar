
####General
WebCalendar allows to schedule your events easy. <br/>
The project was developed as an analogue of the Google Calendar.<br/><br/>
Running application:
<a href="http://185.25.116.77:8080/webcalendar" target="_blank">http://185.25.116.77:8080/webcalendar</a>  
(login & passwd: admin : admin)<br/>

####Stack of technologies

Java ---> Spring (IoC, MVC, Security), Hibernate, Tomcat, JSP, JSTL.<br/>
Web ---> HTML, CSS, JavaScript, jQuery, Ajax.<br/>
Database ---> MySql.<br/>
Testing ---> jUnit, Mockito.<br/>
VCS ---> Git, GitHub.<br/>

####Architecture

After session created, all events of current user loaded from database into DataStore.<br/>
Publish and remove operations are executed using DataStore with database synchronization.<br/>
Search operations are executed using DataStore only.<br/>
Records are stored in the database with the use of Hibernate technology.<br/>

Notifier provides ability to send email notification about the event.<br/>
Verification of the need to notice is carried out every minute.<br/>

Server side based on Spring MVC framework and JSP technology.<br/>
User authentication is performed by using Spring Security.<br/>
Login with Facebook or Vkontakte account is performed using oAuth protocol.<br/>

Client side communicates with the server through Ajax requests.<br/>

![](https://github.com/ruslanborisov/webcalendar/blob/master/src/main/webapp/resources/images/architecture.png)
