<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- hidden elements -->

<form id="formDeleteAccHidden" method="post" action="<c:url value="/deleteAccReload"/>" class="dispNone">
    <input id="passwAcc" val="" name="passwAcc">
</form>
<!-- end of hidden elements -->

<div class="border-bottom">
    <form id="formChangeEmail" style="margin: 10px">
        <p class="metaFont" style="margin-left: 10px">Change email </p>
        <input id="newEmail" class="input" type="text" placeholder="Enter new email..">
        <img id="loading" class="dispNone" style="margin-left: 10px" src="<c:url value="/resources/images/loading.gif"/>">
        <div id="resultChangeEmail" style="margin: 5px" class="succesful dispNone"></div>
        <div id="exceptionChangeEmail" style="margin: 5px" class="exception dispNone"></div>
        <p><input class="button" type="submit" value="SUBMIT"></p>
    </form>
</div>
<div class="border-bottom">
    <form id="formDeleteAcc" style="margin: 10px">
        <p class="exception" style="font-weight: bold">Delete your account</p>
        <input id="checkPass" class="input" type="password" placeholder="Enter password.." name="checkPass">
        <div id="exceptionDeleteAcc" style="margin: 5px" class="exception dispNone"></div>
        <p><input class="button" type="submit" value="SUBMIT"></p>
    </form>
</div>
