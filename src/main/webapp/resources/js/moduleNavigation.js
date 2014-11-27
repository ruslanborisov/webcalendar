var mouse = {x: 0, y: 0};

document.addEventListener('mousemove', function(evt){
    mouse.x = (evt.clientX - $('#content').offset().left) + $(window).scrollLeft();
    mouse.y = (evt.clientY - $('#content').offset().top) + $(window).scrollTop();
}, false);


function ajaxFilterPeriod(filter) {

    $.ajax({
        url : PATH + "/" + filter,
        type: 'POST',

        success : function(data) {
            $("#result").removeClass("dispNone").html(data);
        },

        error : function(xhr, status, errorThrown) {
            alert('Search event failed with status: ' + status + '. ' + errorThrown);
        },

        complete: function() {
            $("#miniCalendar").datepicker('setDate', $("#info").attr("mini"));
            $("#periodPlace").html($("#info").attr("period"));
        }
    });
}

function ajaxToday() {

    $("#filterBlock").removeClass('dispNone');
    $("#miniCalendar").removeClass('dispNone');
    $("#resultAside").html("");

    var filter = $('input[name="periodFilter"]:checked').val();

    $.ajax({
        url : PATH + "/today",
        data : "filter=" + filter,
        type: 'POST',

        success : function(data) {
            $("#result").removeClass("dispNone").html(data);
        },

        error : function(xhr, status, errorThrown) {
            alert('Search event failed with status: ' + status + '. ' + errorThrown);
        },

        complete: function() {
            $("#miniCalendar").datepicker('setDate', $("#info").attr("mini"));
            $("#periodPlace").html($("#info").attr("period"));
        }
    });
}

function ajaxPrev() {

    $('input[name="todayFilter"]').removeAttr('checked');
    var filter = $('input[name="periodFilter"]:checked').val();

    $.ajax({
        url : PATH + "/prev",
        data: "filter=" + filter,
        type: 'POST',

        success : function(data) {
            $("#result").removeClass("dispNone").html(data);

        },
        error : function(xhr, status, errorThrown) {
            alert('Search event failed with status: ' + status + '. ' + errorThrown);
        },

        complete: function() {
            $("#miniCalendar").datepicker('setDate', $("#info").attr("mini"));
            $("#periodPlace").html($("#info").attr("period"));
        }
    });
}

function ajaxNext() {

    $('input[name="todayFilter"]').removeAttr('checked');
    var filter = $('input[name="periodFilter"]:checked').val();

    $.ajax({
        url : PATH + "/next",
        data: "filter=" + filter,
        type: 'POST',

        success : function(data) {
            $("#result").removeClass("dispNone").html(data);
        },

        error : function(xhr, status, errorThrown) {
            alert('Search event failed with status: ' + status + '. ' + errorThrown);
        },

        complete: function() {
            $("#miniCalendar").datepicker('setDate', $("#info").attr("mini"));
            $("#periodPlace").html($("#info").attr("period"));
        }
    });
}

function ajaxQuickSelection(param) {

    $.ajax({
        url : PATH + "/quickSelection",
        data: "displayedPeriod=" + param,
        type: 'POST',

        success : function(data) {
            $("#day").prop("checked", true);
            $("#result").removeClass("dispNone").html(data);
        },

        error : function(xhr, status, errorThrown) {
            alert('Search event failed with status: ' + status + '. ' + errorThrown);
        },

        complete: function() {
            $("#periodPlace").html($("#info").attr("period"));
        }
    });
}

function ajaxDetail(id, startDate) {

    $('.divDetail').remove();

    $.ajax({
        url : PATH + "/eventDetails",
        data: {id: id, startDate: startDate},
        type: 'POST',

        success : function(data) {
            $("#result").append("<div id='" + id + "Detail' class='arrow_box_detail' style='top: " + (mouse.y-197) + "px; left: " + (mouse.x-165) + "px'></div>");
            $("#"+id+"Detail").html(data);
        },

        error : function(xhr, status, errorThrown) {
            alert('Search event failed with status: ' + status + '. ' + errorThrown);
        },
    });
}

function ajaxDeleteEvent(id) {

    $.ajax({
        url : PATH + '/delete',
        data : 'id=' + id,
        type : 'POST',

        success : function() {
            $('#'+id + "Detail").remove();
            $('.'+id).fadeOut(1000);
        },
        error : function(xhr, status, errorThrown) {
            alert('Delete event failed with status: ' + status + '. ' + errorThrown);
        }
    });
}

function popupAboutSpeech() {

    $("nav").append("<div id='aboutSpeechDetail' class='divDetail'> You can manage the application by voice. <br/><br/> " +
        "<span class='metaFont'>\'Create\' : </span>" + "opening page for creating events.<br/><br/> " +
        "<span class='metaFont'>\'Search\' : </span>" + "opening page for search events.<br/><br/> " +
        "<span class='metaFont'>\'Settings\' : </span>" + "opening settings page.<br/><br/> " +
        "<span class='metaFont'>\'Today\' : </span>" + "opening events for today.<br/><br/> " +
        "<span class='metaFont'>\'Exit\' : </span>" + "logout from application.<br/><br/> " +
        "</div>");
}

function popupAboutAttender() {

    $("#content").append("<div id='aboutAttenderDetail' class='divDetail' style='width: 250px'>" +
        "Enter the list of guests' email through ';' delimeter without spaces." +
        "</div>");
}



