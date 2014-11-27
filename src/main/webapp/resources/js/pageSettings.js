function ajaxSettingsPage() {

    $("#filterBlock").addClass('dispNone');
    $("#miniCalendar").addClass('dispNone');

    $.ajax({
        url : PATH + "/settingsPage",
        type : "POST",

        success : function(data) {
            $("#result").html(data);
            Main.initSettingsPage();
        },

        error : function(xhr, status, errorThrown) {
            alert('Create event failed with status: ' + status + '. ' + errorThrown);
        }
    });
}

function ajaxSettingsAside() {

    $.ajax({
        url : PATH + "/settingsAside",
        type : "POST",

        success : function(data) {
            $("#resultAside").html(data);
        },

        error : function(xhr, status, errorThrown) {
            alert('Create event failed with status: ' + status + '. ' + errorThrown);
        }
    });
}

function ajaxDeleteAcc() {

    $.ajax({
        url : PATH + "/deleteAcc",
        data: "checkPass=" + $("#checkPass").val(),
        type : "POST",

        success : function(data) {
           if(data=='exception') {
               $("#exceptionDeleteAcc").removeClass("dispNone").html("Wrong password");
           }
           if(data=='success') {
               $('#formDeleteAccHidden').submit();
           }
        },

        error : function(xhr, status, errorThrown) {
            alert('Create event failed with status: ' + status + '. ' + errorThrown);
        }
    });
}

function ajaxChangeEmail() {
    $('#loading').removeClass('dispNone');

    $.ajax({
        url : PATH + "/changeEmail",
        data: "newEmail=" + $("#newEmail").val(),
        type : "POST",

        success : function(data) {
            $('#loading').addClass('dispNone');
            if(data=='success') {
                $("#resultChangeEmail").removeClass("dispNone").html("Change email successful. <br/> Please, " +
                    "confirm by clicking on the link that is sent to your new email");
            }
            if(data=='exception') {
                $("#exceptionChangeEmail").removeClass("dispNone").html("Change email failed. <br/>" +
                    "Entered email already exists");
            }
        },

        error : function(xhr, status, errorThrown) {
            alert('Create event failed with status: ' + status + '. ' + errorThrown);
        }
    });
}

function validationOnSettingsPage() {

// EMAIL CHECK
    $('#newEmail').on('focus', function () {
        $('#exceptionChangeEmail').html("");
        checks.hide($(this).attr('id'));
    }).on('blur', function () {
        checks.checkTarget($(this).attr('id'), 'email');
    });

    $('#checkPass').on('focus', function () {
        $('#exceptionDeleteAcc').html("");
    });

}
