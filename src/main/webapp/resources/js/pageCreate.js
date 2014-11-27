
function ajaxCreatePage(idEventForUpdate, startDate) {

    $("#filterBlock").addClass('dispNone');
    $("#miniCalendar").addClass('dispNone');

    $.ajax({
        url : PATH + "/createPage",
        type : "POST",

        success : function(data) {
            $("#result").html(data);
            $("#resultAside").html("<div id='miniCalendarForCreatePage' class='ll-skin-latoja'></div>" +
                "<a href='javascript:{} oncklick = ajaxSearchFreePage()' style='margin: 5px; text-decoration: underline'> Find a time </a>");
            Main.initCreatePage();
            if(idEventForUpdate) {
                ajaxGetEventForUpdate(idEventForUpdate, startDate);
            }
        },

        error : function(xhr, status, errorThrown) {
            alert('Create event failed with status: ' + status + '. ' + errorThrown);
        }
    });
}

function ajaxGetEventForUpdate(idEventForUpdate, startDate) {

    $.ajax({
        url : PATH + "/getEventForUpdate",
        data: {id: idEventForUpdate, startDate: startDate} ,
        type : "POST",

        success : function(data) {

            data = $.parseJSON(data);
            $("#formCreate").attr('eventId', idEventForUpdate);
            $("#startDate").val(data.startDate);
            $("#endDate").val(data.endDate);
            if(data.allDay) {
                $("#startTime").addClass("dispNone");
                $("#endTime").addClass("dispNone");
                $('#allDay').prop("checked", true);
            } else {
                $("#startTime").val(data.startTime);
                $("#endTime").val(data.endTime);
            }

            $("#title").val(data.title);
            $("#description").val(data.description);
            var color = data.color;
            $(color).prop("checked", true);

            if(data.numberOfOccurrence) {
                $("#after").prop("checked", true);
                $("#numberOfOccurrence").val(data.numberOfOccurrence);
            }
            if(data.reminderTime!=null) {
                $("#reminderTime").val(data.reminderTime);
            }

            var attendersString = "";
            for(i = 0; i <data.eventAttenders.length; i++) {
                attendersString = attendersString + data.eventAttenders[i].email + ";";
            }
            attendersString = attendersString.substring(0, attendersString.length - 1);
            $("#attenders").val(attendersString);

            for(i = 0; i <data.eventReminders.length; i++) {
                switch (data.eventReminders[i].reminder){
                    case "POPUP":
                        $('#popup').prop("checked", true);
                        break;
                    case "EMAIL":
                        $('#emailReminder').prop("checked", true);
                        break;
                    default:
                        break;
                }
            }

            for(i = 0; i <data.eventRepeaters.length; i++) {
                switch (data.eventRepeaters[i].repeater){
                    case "ONCE":
                        $('#once').prop("checked", true);
                        break;
                    case "DAILY":
                        $('#repeatPeriodic').prop("checked", true);
                        $('#repeatPeriodicArea').removeClass("dispNone");
                        $("#occurrenceArea").removeClass("dispNone");
                        $("#endDate").addClass("dispNone");
                        $('#daily').prop("checked", true);
                        break;
                    case "MONTHLY":
                        $('#repeatPeriodic').prop("checked", true);
                        $('#repeatPeriodicArea').removeClass("dispNone");
                        $("#occurrenceArea").removeClass("dispNone");
                        $('#monthly').prop("checked", true);
                        break;
                    case "YEARLY":
                        $('#repeatPeriodic').prop("checked", true);
                        $('#repeatPeriodicArea').removeClass("dispNone");
                        $("#occurrenceArea").removeClass("dispNone");
                        $('#yearly').prop("checked", true);
                        break;
                    case "SUNDAY":
                        $('#repeatDay').prop("checked", true);
                        $('#repeatDayArea').removeClass("dispNone");
                        $("#occurrenceArea").removeClass("dispNone");
                        $("#startDate").addClass("dispNone");
                        $("#endDate").addClass("dispNone");
                        $('#sunday').prop("checked", true);
                        break;
                    case "MONDAY":
                        $('#repeatDay').prop("checked", true);
                        $('#repeatDayArea').removeClass("dispNone");
                        $("#occurrenceArea").removeClass("dispNone");
                        $("#startDate").addClass("dispNone");
                        $("#endDate").addClass("dispNone");
                        $('#monday').prop("checked", true);
                        break;
                    case "TUESDAY":
                        $('#repeatDay').prop("checked", true);
                        $('#repeatDayArea').removeClass("dispNone");
                        $("#occurrenceArea").removeClass("dispNone");
                        $("#startDate").addClass("dispNone");
                        $("#endDate").addClass("dispNone");
                        $('#tuesday').prop("checked", true);
                        break;
                    case "WEDNESDAY":
                        $('#repeatDay').prop("checked", true);
                        $('#repeatDayArea').removeClass("dispNone");
                        $("#occurrenceArea").removeClass("dispNone");
                        $("#startDate").addClass("dispNone");
                        $("#endDate").addClass("dispNone");
                        $('#wednesday').prop("checked", true);
                        break;
                    case "THURSDAY":
                        $('#repeatDay').prop("checked", true);
                        $('#repeatDayArea').removeClass("dispNone");
                        $("#occurrenceArea").removeClass("dispNone");
                        $("#startDate").addClass("dispNone");
                        $("#endDate").addClass("dispNone");
                        $('#thursday').prop("checked", true);
                        break;
                    case "FRIDAY":
                        $('#repeatDay').prop("checked", true);
                        $('#repeatDayArea').removeClass("dispNone");
                        $("#occurrenceArea").removeClass("dispNone");
                        $("#startDate").addClass("dispNone");
                        $("#endDate").addClass("dispNone");
                        $('#friday').prop("checked", true);
                        break;
                    case "SATURDAY":
                        $('#repeatDay').prop("checked", true);
                        $('#repeatDayArea').removeClass("dispNone");
                        $("#occurrenceArea").removeClass("dispNone");
                        $("#startDate").addClass("dispNone");
                        $("#endDate").addClass("dispNone");
                        $('#saturday').prop("checked", true);
                        break;
                    default:
                        break;
                }
            }
        },

        error : function(xhr, status, errorThrown) {
            alert('Create event failed with status: ' + status + '. ' + errorThrown);
        }
    });
}

function ajaxCreateOrUpdate() {

    $('#created').addClass("dispNone");
    $('#exceptionCreate').addClass("dispNone");

    var data = {
        title: $("#title").val(),
        description: $("#description").val(),
        startDate: $("#startDate").val(),
        endDate: $("#endDate").val(),
        startTime: $("#startTime").val(),
        endTime: $("#endTime").val(),
        allDay: $('#allDay').is(":checked"),
    };

    if ($('#B8B9FF').is(":checked")) data.color = '#B8B9FF';
    if ($('#7AE7BF').is(":checked")) data.color = '#7AE7BF';
    if ($('#46D6DB').is(":checked")) data.color = '#46D6DB';
    if ($('#51B749').is(":checked")) data.color = '#51B749';
    if ($('#FBD75B').is(":checked")) data.color = '#FBD75B';
    if ($('#FFB878').is(":checked")) data.color = '#FFB878';
    if ($('#DC2127').is(":checked")) data.color = '#DC2127';
    if ($('#DBADFF').is(":checked")) data.color = '#DBADFF';
    if ($('#E1E1E1').is(":checked")) data.color = '#E1E1E1';


    var eventAttendersString = $("#attenders").val();
    if (eventAttendersString.length>0) {
        var arrayAttender = eventAttendersString.split(";");
        data.eventAttenders = arrayAttender;
    } else {
        var arrayAttender = [];
        data.eventAttenders = arrayAttender;
    }

    var arrayReminder = [];
    if ($('#popup').is(":checked")) {
        arrayReminder.push('popup');
        data.reminderTime = $("#reminderTime").val();
    }
    if ($('#emailReminder').is(":checked")) {
        arrayReminder.push('email');
        data.reminderTime = $("#reminderTime").val();
    }
    if (!$('#emailReminder').is(":checked") &&
        !$('#popup').is(":checked")) {
        data.reminderTime = "null";
    }
    data.eventReminders = arrayReminder;

    if ($('#after').is(":checked")) {
        data.numberOfOccurrence = $("#numberOfOccurrence").val();
    }
    if ($('#never').is(":checked")) {
        data.numberOfOccurrence = "null";
    }

    var arrayRepeater = [];
    switch ($('input[name="repeatCheck"]:checked').val()) {
        case 'once':
            arrayRepeater.push("once");
            break;
        case 'repeatPeriodic':
            var repeatPeriodic = $('input[name="repeatPeriodic"]:checked').val();
            arrayRepeater.push($('input[name="repeatPeriodic"]:checked').val());
            if (repeatPeriodic == 'daily')
                data.endDate = $("#startDate").val()
            break;
        case 'repeatDay':
            data.endDate = $("#startDate").val()
            if ($('#monday').is(":checked")) arrayRepeater.push('monday');
            if ($('#tuesday').is(":checked")) arrayRepeater.push('tuesday');
            if ($('#wednesday').is(":checked")) arrayRepeater.push('wednesday');
            if ($('#thursday').is(":checked")) arrayRepeater.push('thursday');
            if ($('#friday').is(":checked")) arrayRepeater.push('friday');
            if ($('#saturday').is(":checked")) arrayRepeater.push('saturday');
            if ($('#sunday').is(":checked")) arrayRepeater.push('sunday');
            break;
    }
    data.eventRepeaters = arrayRepeater;


    $.ajax({
        url : PATH + "/create",
        data : {data : JSON.stringify(data)},
        type : "POST",

        success : function(data) {
            data = $.parseJSON(data);
            if (data.result) {
                if ($("#formCreate").attr("eventId").length > 0) {
                    ajaxDeleteEvent($("#formCreate").attr("eventId"));
                }
                resetCheck();
                $('#created').show().removeClass("dispNone");
                $('#created').delay(5000).fadeOut(500);
            }

            if (data.exception) {
                $('#exceptionCreate').removeClass("dispNone").html(data.exception);
            }
        },
        error : function(xhr, status, errorThrown) {
            alert('Create event failed with status: ' + status + '. ' + errorThrown);
        }
    });
}

function resetCheck () {
    $(".input[type='text']").removeClass("greenBorder").val("");
    $("textarea").val("");
    $("#repeatPeriodicArea").addClass("dispNone");
    $("#repeatDayArea").addClass("dispNone");
    $('#exception').addClass("dispNone");

    $("#once").prop("checked", true);
    $("#daily").prop("checked", true);

    $("input:checkbox").prop("checked", false);

    $("#startDate").removeClass("dispNone");
    $("#startTime").removeClass("dispNone");
    $("#endDate").removeClass("dispNone");
    $("#endTime").removeClass("dispNone");
}

function validationOnCreatePage() {

// DESCRIPTION CHECK with counter
    $('#description').on('focus', function () {
        counter.countCreate($(this).attr('id'), '100');
    }).on('blur', function () {
        counter.countHide($(this).attr('id'));
    }).on('keyup', function () {
        var elemId = $(this).attr('id');
        counter.countPress(elemId, '100');
    });

// ATTENDERS LIST CHECK
    $('#attenders').on('focus', function () {
        checks.hide($(this).attr('id'));
    }).on('blur', function () {
        checks.checkTarget($(this).attr('id'), 'emailList');
    });

// INTEGER CHECK
    $('#numberOfOccurrence').on('focus', function () {
        checks.hide($(this).attr('id'));
    }).on('blur', function () {
        checks.checkTarget($(this).attr('id'), 'integer');
    });

}

function selectionOnCreatePage() {
    $('#allDay').on('change', function(){

        if ($('#allDay').is( ":checked" )) {
            $("#startTime").addClass("dispNone");
            $("#endTime").addClass("dispNone");
        } else {
            $("#startTime").removeClass("dispNone");
            $("#endTime").removeClass("dispNone");
        }
    });

    $('input[name="repeatCheck"]').on('change', function() {

        $('#created').addClass("dispNone");
        $('#failed').addClass("dispNone");
        $("#startTime").attr("placeholder", "Time");
        $("#endTime").attr("placeholder", "Time");

        switch ($(this).val()) {
            case 'once':
                $("#occurrenceArea").addClass("dispNone");
                $("#repeatPeriodicArea").addClass("dispNone");
                $("#repeatDayArea").addClass("dispNone")
                $("#endDate").removeClass("dispNone");
                $("#startDate").removeClass("dispNone");
                break;
            case 'repeatPeriodic':
                $("#repeatPeriodicArea").removeClass("dispNone");
                $("#occurrenceArea").removeClass("dispNone");
                $("#repeatDayArea").addClass("dispNone");

                if ($('#daily').is(":checked")) {
                    $("#startTime").attr("placeholder", "From");
                    $("#endTime").attr("placeholder", "To");
                    $("#endDate").addClass("dispNone");
                    $("#startDate").removeClass("dispNone");
                } else {
                    $("#startDate").removeClass("dispNone");
                    $("#endDate").removeClass("dispNone");
                }
                break;
            case 'repeatDay':
                $("#repeatPeriodicArea").addClass("dispNone");
                $("#repeatDayArea").removeClass("dispNone");
                $("#occurrenceArea").removeClass("dispNone");

                $("#startDate").addClass("dispNone");
                $("#endDate").addClass("dispNone");
                $("#startTime").attr("placeholder", "From");
                $("#endTime").attr("placeholder", "To");
                break;
        }
    });

    $('input[name="repeatPeriodic"]').on('change', function(){

        if ($('#daily').is( ":checked" )) {
            $("#endDate").addClass("dispNone");
            $("#startTime").attr("placeholder", "From");
            $("#endTime").attr("placeholder", "To");
        } else {
            $("#endDate").removeClass("dispNone");
            $("#startTime").attr("placeholder", "Time");
            $("#endTime").attr("placeholder", "Time");
        }
    });

    $('input[name="numberOfOccurrence"]').on('click', function(){
        $("#after").prop("checked", true);
        $("#numberOfOccurrence").removeAttr("readonly").val("1");
    });

    $('input[name="occurrence"]').on('change', function(){
        $("#numberOfOccurrence").css("border", "");
        if ($('#after').is( ":checked" )) {
            $("#numberOfOccurrence").removeAttr("readonly").val("1");
        } else {
            $("#numberOfOccurrence").attr("readonly", true).val("");
        }
    });
}