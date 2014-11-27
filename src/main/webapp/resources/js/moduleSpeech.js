function ajaxSpeechSynthesis(id, startDate) {

    if (!('speechSynthesis' in window)) {
        // do nothing

    } else {

        $.ajax({
            url: PATH + '/speechSynthesis',
            data: {id: id, startDate: startDate},
            type: 'POST',

            success: function (data) {
                mySpeechSynthesis(data);
            },
            error: function (xhr, status, errorThrown) {
                alert('Delete event failed with status: ' + status + '. ' + errorThrown);
            }
        });
    }
}

function mySpeechSynthesis(data) {
    if(data.length==0)
        data = "No events for today";
    var utterance = new SpeechSynthesisUtterance();
    utterance.lang = 'en-US';
    utterance.text = data;
    utterance.rate = 0.5;
    utterance.volume = 1;
    window.speechSynthesis.speak(utterance);
}

function speechRecognize(instruction, button, result) {

    var finalTranscript = '';
    var recognizing = false;

    if (!('webkitSpeechRecognition' in window)) {
       // do nothing

    } else {
        var recognition = new webkitSpeechRecognition();
        recognition.continuous = true;
        recognition.interimResults = true;
        recognition.lang = 'en-US';

        recognition.onstart = function() {
            recognizing = true;
            $('#'+instruction).css("color", "#E53136").html('Speak..');
            $('#'+button).attr("src", PATH + "/resources/images/micred.png").css("opacity", "1");

        };
        recognition.onerror = function(event) {
            console.log("There was a recognition error...");
        };
        recognition.onend = function() {
            recognizing = false;
            $('#'+instruction).css("color", "#444644").html('');
            $('#'+button).attr("src", PATH + "/resources/images/micblack.png").css("opacity", "");;

            if (result=='resultCommand') {
                if(!mySpeechController(finalTranscript)) {
                    $('#instructionsCommand').css("color", "#E53136").html("Command: '" + finalTranscript + "' not found");
                    $('#instructionsCommand').show().delay(5000).fadeOut(500);
                }
            } else {
                $('#'+instruction).html('');
            }

        };
        recognition.onresult = function(event) {
            var interimTranscript = '';
            for (var i = event.resultIndex; i < event.results.length; ++i) {
                if (event.results[i].isFinal) {
                    finalTranscript += event.results[i][0].transcript;
                } else {
                    interimTranscript += event.results[i][0].transcript;
                }
            }
            if(finalTranscript.length > 0) {
                $('#' + result).val(finalTranscript);
                checks.checkTarget($('#' + result).attr('id'), 'latinAndNumber');
                recognition.stop();
                recognizing = false;
            }
        };

        $('#'+button).click(function(e) {
            e.preventDefault();

            if (recognizing) {
                recognition.stop();
                recognizing = false;
            } else {
                finalTranscript = '';
                recognition.start();
                $('#'+instruction).css("color", "#444644").html('Allow use your mic');
            }
        });
    }
}

function mySpeechController(command) {
    var result = true;
    switch(command) {
        case "create":
            ajaxCreatePage();
            break;
        case "search":
            ajaxSearchPage();
            break;
        case "settings":
            ajaxSettingsPage();
            ajaxSettingsAside()
            break;
        case "today":
            ajaxToday();
            break;
        case "exit":
            $("#formVoiceCommand").submit();
            break;
        default:
            result=false;
            break;
    }
    return result;
}



