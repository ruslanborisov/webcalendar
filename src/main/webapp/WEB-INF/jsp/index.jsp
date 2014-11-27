<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<html>
<head>
<meta charset="utf-8">

<title>WebCalendar</title>

    <!--[if lt IE 9]>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <script src="<c:url value="/resources/js/jquery-1.11.1.min.js"/>"></script>
    <script	src="<c:url value="/resources/js/jquery-ui.min.js"/>"></script>
    <script	src="<c:url value="/resources/js/jquery-ui-timepicker-addon.min.js"/>"></script>
    <script	src="<c:url value="/resources/js/login-leanModal.js"/>"></script>
    <script	src="<c:url value="/resources/js/magnific.js"/>"></script>
    <script	src="<c:url value="/resources/js/moduleValidation.js"/>"></script>
    <script src="<c:url value="/resources/js/moduleNavigation.js"/>"></script>
    <script src="<c:url value="/resources/js/moduleSpeech.js"/>"></script>
    <script src="<c:url value="/resources/js/moduleReminder.js"/>"></script>
    <script	src="<c:url value="/resources/js/pageReg.js"/>"></script>
    <script src="<c:url value="/resources/js/pageCreate.js"/>"></script>
    <script src="<c:url value="/resources/js/pageSearch.js"/>"></script>
    <script src="<c:url value="/resources/js/pageSettings.js"/>"></script>
    <script src="<c:url value="/resources/js/main.js"/>"></script>

    <link href ="<c:url value="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css"/>" rel="stylesheet">
    <link href ="<c:url value="/resources/css/jquery-ui.css"/>" rel="stylesheet">
    <link href ="<c:url value="/resources/css/jquery-ui-timepicker-addon.min.css"/>" rel="stylesheet">
    <link href ="<c:url value="/resources/css/magnific.css"/>" rel="stylesheet">
    <link href ="<c:url value="/resources/css/magnific-dialog.css"/>" rel="stylesheet">
    <link href ="<c:url value="/resources/css/styleDatePicker.css"/>" rel="stylesheet">
    <link href ="<c:url value="/resources/css/styleLogin.css"/>" rel="stylesheet">
    <link href ="<c:url value="/resources/css/styleArrowDiv.css"/>" rel="stylesheet">
    <link href ="<c:url value="/resources/css/styleFrame.css"/>" rel="stylesheet">
    <link href ="<c:url value="/resources/css/styleGeneral.css"/>" rel="stylesheet">

    <script>
        var PATH = "${pageContext.request.contextPath}";
    </script>

</head>
<body>

<!-- hidden elements -->
<c:choose>
    <c:when test="${not empty login}">
        <script>
            $(document).ready(function() {
                Main.initLogin();
            });
        </script>
        <a id="modal_trigger" href="#modal" class="dispNone"></a>
        <jsp:include page="views/login.jsp" />
    </c:when>
    <c:when test="${empty login}">
        <script>
            $(document).ready(function() {
                Main.initGeneral();
            });
        </script>
    </c:when>
</c:choose>

<form id="formVoiceCommand" method="post" action="<c:url value="/speechControl"/>" class="dispNone">
    <input id="resultCommand" val="" name="command">
</form>
<div id="reminderContent" class="dispNone"></div>

<div id="small-dialog" class="zoom-anim-dialog mfp-hide">
    <jsp:include page="views/aboutapp.jsp" />
</div>
<!-- end of hidden elements -->

<div id="wrapper">
    <a id="headerButton" href="javascript:{}"><header id="calendarHeader"></header></a>
    <nav>
        <a id="buttonCreate" href="javascript:{}"> Create </a>
        <c:if test="${header['User-Agent'].contains('Chrome')}">
            <div id="SpeechBlock">
                <img id="micCommand" class="menuItem" src="<c:url value="/resources/images/micblack.png"/>" alt="">
                <a id="aboutSpeech" href="javascript:{}"> ? </a>
            </div>
            <span id="instructionsCommand"></span>
        </c:if>
        <a id="infoAboutApp" class="popup-with-zoom-anim" href="#small-dialog">For developers</a><br>
        <c:if test="${pageContext.request.userPrincipal.name != null}"><div id="user_name">${pageContext.request.userPrincipal.name}</div></c:if>
        <div id="menu">
             <a id="homeButton" href="javascript:{}" ><img id="homePic" class="menuItem" title="home" src="<c:url value="/resources/images/home.png"/>" alt=""></a>
             <a id="searchButton" href="javascript:{}" ><img id="searchPic" class="menuItem" title="search" src="<c:url value="/resources/images/search.png"/>" alt=""></a>
             <a id="settingsButton" href="javascript:{}"><img id="settingPic" class="menuItem" title="settings" src="<c:url value="/resources/images/settings.png"/>" alt=""></a>
             <a href="<c:url value="/j_spring_security_logout" />" ><img id="logoutPic" class="menuItem" title="logout" src="<c:url value="/resources/images/logout.png"/>" alt=""></a>
        </div>
    </nav>

    <section id="content">
        <aside id="asideBar">
            <div id="miniCalendar" class="ll-skin-latoja"></div>
            <div id="resultAside"></div>
        </aside>
        <div id="filterBlock" class="border-bottom">
            <input id="today" type="radio" name="todayFilter" value="today" checked>
            <label id="todayLabel" for="today" class="labelButton"> Today </label>
            <div id="prevNext">
                <a id="prev" title="previous period" class="labelButton" href="javascript:{}"> < </a>
                <a id="next" title="next period" class="labelButton" href="javascript:{}"> > </a>
            </div>
            <div id="periodPlace" class="displayedPeriod"> </div>
            <div id="dayWeekMonth">
                <input id="day" type="radio" name="periodFilter" value="day">
                <label for="day" class="labelButton"> Day </label>
                <input id="week" type="radio" name="periodFilter" value="week">
                <label for="week" class="labelButton"> Week </label>
                <input id="month" type="radio" name="periodFilter" value="month" checked>
                <label for="month" class="labelButton"> Month </label>
            </div>
        </div>
        <div id="result"></div>
    </section>

    <footer>
        <p id="owner">Borisov Ruslan, 2014</p>
    </footer>
</div>
</body>
</html>