function ajaxInitPopupReminder() {

    setInterval(function() {
        $.ajax({
            url : PATH + '/reminderRequest',
            type : 'POST',

            success : function(data) {
                if(data.length > 40) {
                    var audio = new Audio(PATH + '/resources/audio/alert.mp3');
                    audio.play();
                    $("#reminderContent").removeClass("dispNone").html(data).dialog({
                        modal: true,
                        show : true,
                        title: "Alert!",
                        buttons: {
                            Ok: function() {
                                $( this ).dialog( "close" );
                                audio.pause();
                                audio.currentTime = 0;
                            }
                        }
                    });
                    $('.ui-widget-overlay').css('background', 'black').css('opacity', '0.6');
                    result = null;
                    data = null;
                }
            },
            error : function(xhr, status, errorThrown) {
                alert('Error reminder request with status: ' + status + '. ' + errorThrown);
            }
        })
    }, 60000);
}
