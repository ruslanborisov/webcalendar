
function ajaxUserLogin() {

    $.ajax({
        url: PATH + "/userLogin",
        data: {username: $("#usernameLogin").val(), password: $("#passLogin").val()},
        type: "POST",

        success: function (data) {

            if (data == 'success') {
                $("#loginForm").submit();
            }
            if (data == 'exception') {
                $("#exceptionLogin").html("Wrong username/password");
            }
        },

        error: function (xhr, status, errorThrown) {
            alert('Create event failed with status: ' + status + '. ' + errorThrown);
        }
    });
}

function ajaxReg() {

    $(".loading").removeClass("dispNone");

    $.ajax({
        url: PATH + "/registration",
        data: {username: $("#username").val(), password: $("#pass").val(), email: $("#email").val()},
        type: "POST",

        success: function (data) {
            $(".loading").addClass("dispNone");
            if (data == 'success') {
                $("#resultReg").html("Register successful. Please, " +
                    "confirm your account by clicking on the link that is sent to your email");
                $("#username").val("");
                $("#pass").val("");
                $("#repeatPass").val("");
                $("#email").val("");
            }
            if (data == 'error') {
                $("#exceptionReg").html("Error add user");
            }
            if (data == 'exception') {
                $("#exceptionReg").html("User with enter username/email is already exists");
            }
        },

        error: function (xhr, status, errorThrown) {
            alert('Create event failed with status: ' + status + '. ' + errorThrown);
        }
    });
}

function ajaxRemingPass() {

    $(".loading").removeClass("dispNone");

    $.ajax({
        url: PATH + "/remindPass",
        data: "email=" + $("#emailRemindPass").val(),
        type: "POST",

        success: function (data) {
            $(".loading").addClass("dispNone");
            if (data == 'success') {
                $("#resultRemindPass").html("Username and new password has been sent to the entered email.");
                $("#emailRemindPass").val("");
            }
            if (data == 'exception') {
                $("#exceptionRemindPass").html("User with the entered email was not found.");
            }
        },

        error: function (xhr, status, errorThrown) {
            alert('Create event failed with status: ' + status + '. ' + errorThrown);
        }
    });
}

function validationOnRegPage() {

// LATINandNUMBER CHECK
    $('#username, #pass').on('focus', function () {
        $("#exceptionReg").html("");
        $("#resultReg").html("");
        checks.hide($(this).attr('id'));
    }).on('blur', function () {
        checks.checkTarget($(this).attr('id'), 'latinAndNumber');
    });

// EMAIL CHECK
    $('#email').on('focus', function () {
        $("#exceptionReg").html("");
        $("#resultReg").html("");
        checks.hide($(this).attr('id'));
    }).on('blur', function () {
        checks.checkTarget($(this).attr('id'), 'email');
    });

// CORRECT REPEAT CHECK
    $('#repeatPass').on('focus', function () {
        $("#exceptionReg").html("");
        $("#resultReg").html("");
        repeater.hide('repeatPass');
    }).on('blur', function () {
        repeater.correctRepeat('repeatPass', 'pass');
    })
}

function validationOnForgotPassPage() {
    // EMAIL CHECK
    $('#emailRemindPass').on('focus', function () {
        $("#exceptionRemindPass").html("");
        checks.hide($(this).attr('id'));
    }).on('blur', function () {
        checks.checkTarget($(this).attr('id'), 'email');
    });
}
