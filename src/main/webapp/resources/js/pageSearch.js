
function ajaxSearchPage() {

    $("#filterBlock").addClass('dispNone');
    $("#miniCalendar").addClass('dispNone');

    $.ajax({
        url : PATH + "/searchPage",
        type : "POST",

        success : function(data) {
            $("#resultAside").html(data);
            $("#result").html("<div class='searchPeriod'>Result of search will be here..</div>");
            Main.initSearchPage();
        },

        error : function(xhr, status, errorThrown) {
            alert('Create event failed with status: ' + status + '. ' + errorThrown);
        }
    });
}

function ajaxSearchFreePage() {

    $("#filterBlock").addClass('dispNone');
    $("#miniCalendar").addClass('dispNone');

    $.ajax({
        url : PATH + "/searchFreePage",
        type : "POST",

        success : function(data) {
            $("#resultAside").html(data);
            $("#result").html("<div class='searchPeriod'>Result of search will be here..</div>");
            Main.initSearchPage();
        },

        error : function(xhr, status, errorThrown) {
            alert('Create event failed with status: ' + status + '. ' + errorThrown);
        }
    });
}

function ajaxSearch() {

    $("#result").html("");
    $("#exceptionSearch").addClass("dispNone").html("");

    var url;
    var data = {};

    switch ($("#searchSelect").val()) {
        case 'searchByTitle':
            url = '/searchByTitle';
            data.title = $("#title").val();
            break;
        case 'searchByTitleStartWith':
            url = '/searchByTitleStartWith';
            data.titleStart = $("#title").val();
            break;
        case 'searchByDate':
            url = '/searchByDate';
            data.date = $("#date").val() ;
            break;
        case 'searchByAttender':
            url = '/searchByAttender';
            data.attender = $("#attender").val();
            break;
        case 'searchIntoPeriod':
            url = '/searchIntoPeriod';
            data.startDate = $("#startDate").val();
            data.endDate = $("#endDate").val();
            break;
        case 'searchByAttenderIntoPeriod':
            url = '/searchByAttenderIntoPeriod';
            data.startDate = $("#startDate").val();
            data.endDate = $("#endDate").val();
            data.attender = $("#attender").val();
            break;
        case 'searchFreeTime':
            url = '/searchFreeTime';
            data.startDate = $("#startDate").val();
            data.startTime = $("#startTime").val();
            data.endDate = $("#endDate").val();
            data.endTime = $("#endTime").val();
            break;
        case 'isAttenderFree':
            url = '/searchIsAttenderFree';
            data.startDate = $("#startDate").val();
            data.startTime = $("#startTime").val();
            data.endDate = $("#endDate").val();
            data.endTime = $("#endTime").val();
            data.attender = $("#attender").val();
            break;
    }

    $.ajax({
        url : PATH + url,
        data : data,
        type: 'POST',

        success : function(data) {
            if (data=="WrongDateTime")
                $("#exceptionSearch").removeClass("dispNone").html("Wrong date/time");
            else
                $("#result").html(data);
        },
        error : function(xhr, status, errorThrown) {
            alert('Search event failed with status: ' + status + '. ' + errorThrown);
        }
    });
}

function validationOnSearchPage() {

// EMAIL CHECK
    $('#attender').on('focus', function () {
        $("#exceptionSearch").addClass("dispNone");
        checks.hide($(this).attr('id'));
    }).on('blur', function () {
        checks.checkTarget($(this).attr('id'), 'email');
    });
}

function selectionOnSearchPage() {

    $('#searchSelect').on('change', function(){

        $("#title").val("").addClass("dispNone").siblings().remove();
        $("#date").val("").addClass("dispNone").siblings().remove();
        $("#startDate").val("").addClass("dispNone")
        $("#startTime").val("").addClass("dispNone")
        $("#endDate").val("").addClass("dispNone")
        $("#endTime").val("").addClass("dispNone")
        $("#attender").val("").addClass("dispNone").siblings().remove();

        switch ($("#searchSelect").val()) {
            case 'select':
                break;
            case 'searchByTitle':
                $("#title").removeClass("dispNone");
                $("#title").attr('placeholder', 'Enter title..');
                break;
            case 'searchByTitleStartWith':
                $("#title").removeClass("dispNone");
                $("#title").attr('placeholder', 'Enter start title..');
                break;
            case 'searchByDate':
                $("#date").removeClass("dispNone");
                break;
            case 'searchByAttender':
                $("#attender").removeClass("dispNone");
                break;
            case 'searchIntoPeriod':
                $("#startDate").removeClass("dispNone");
                $("#endDate").removeClass("dispNone");
                break;
            case 'searchByAttenderIntoPeriod':
                $("#startDate").removeClass("dispNone");
                $("#startTime").removeClass("dispNone");
                $("#endDate").removeClass("dispNone");
                $("#endTime").removeClass("dispNone");
                $("#attender").removeClass("dispNone");
                break;
            case 'searchFreeTime':
                $("#startDate").removeClass("dispNone");
                $("#startTime").removeClass("dispNone");
                $("#endDate").removeClass("dispNone");
                $("#endTime").removeClass("dispNone");
                break;
            case 'isAttenderFree':
                $("#startDate").removeClass("dispNone");
                $("#startTime").removeClass("dispNone");
                $("#endDate").removeClass("dispNone");
                $("#endTime").removeClass("dispNone");
                $("#attender").removeClass("dispNone");
                break;
        }
    });
}

