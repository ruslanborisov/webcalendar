<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container">
    <div id="modal" class="popupContainer" style="display:none;">
        <header class="popupHeader">
            <span class="header_title">Login</span>
        </header>

        <section class="popupBody">
            <!-- Social Login -->
            <div class="social_login_form">
                <div class="">
                    <a href="<c:url value="/fbAuth"/>" class="social_box fb">
                        <span class="icon"><i class="fa fa-facebook"></i></span>
                        <span class="icon_title">Login with Facebook</span>
                    </a>
                    <a href="<c:url value="/vkAuth"/>" class="social_box vk">
                        <span class="icon"><i class="fa fa-vk"></i></span>
                        <span class="icon_title">Login with Vkontakte</span>
                    </a>
                </div>

                <div class="centeredText">
                    <span>Or use your internal account</span>
                </div>

                <div class="action_btns">
                    <div class="one_half"><a href="#" id="login_form_btn" class="btn">Login</a></div>
                    <div class="one_half last"><a href="#" id="register_form_btn" class="btn">Sign up</a></div>
                </div>
            </div>

            <!-- Username & Password Login form -->
            <div class="user_login_form">
                <form id="loginForm" action="<c:url value="/init"/>" method="POST">
                    <label>Username</label>
                    <input id="usernameLogin" type="text"/>
                    <br />

                    <label>Password</label>
                    <input id="passLogin" type="password"/>
                    <br />

                    <p id="exceptionLogin" class="exception"></p>
                    <div class="action_btns">
                        <div class="one_half"><a href="#" class="btn back_btn"><i class="fa fa-angle-double-left"></i> Back </a></div>
                        <div class="one_half last"><a id="loginButton" href="javascript:{}" class="btn btn_red"> Login </a></div>
                    </div>
                </form>

                <a href="#" class="forgot_password_btn">Forgot password?</a>
                <p>
                    * Use "admin:admin" as test account
                </p>
            </div>

            <!-- Forgot Password form -->
            <div class="remind_pass_form">
                <form id="remindPassForm">
                    <label>Email Address</label>
                    <input id="emailRemindPass" type="email"/>
                    <br />

                    <img style="margin-left: 130px" class="dispNone loading" src="<c:url value="/resources/images/loading.gif"/>">
                    <p id="resultRemindPass" class="succesful"></p>
                    <p id="exceptionRemindPass" class="exception"></p>
                    <div class="action_btns">
                        <div class="one_half"><a href="#" class="btn back_remind_btn"><i class="fa fa-angle-double-left"></i> Back </a></div>
                        <div class="one_half last"><a id="remindButton" href="javascript:{}" class="btn btn_red"> Remind </a></div>
                    </div>
                </form>
            </div>

            <!-- Register Form -->

            <div class="user_register_form">
                <form>
                    <label>Username</label>
                    <input id="username" type="text" name="username" />
                    <br />

                    <label>Password</label>
                    <input id="pass" type="password" name="password" />
                    <br />

                    <label>Repeat password</label>
                    <input id="repeatPass" type="password" name="passwordRepeat" />
                    <br />

                    <label>Email Address</label>
                    <input id="email" type="email" name="email" />
                    <br />

                    <img style="margin-left: 130px" class="dispNone loading" src="<c:url value="/resources/images/loading.gif"/>">
                    <p id="resultReg" class="succesful"></p>
                    <p id="exceptionReg" class="exception"></p>

                    <div class="action_btns">
                        <div class="one_half"><a href="#" class="btn back_btn"><i class="fa fa-angle-double-left"></i> Back</a></div>
                        <div class="one_half last"><a id="registerButton" href="javascript:{}" class="btn btn_red">Register</a></div>
                    </div>
                </form>
            </div>
        </section>
    </div>
</div>