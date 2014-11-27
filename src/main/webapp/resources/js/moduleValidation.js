
var checks = new check();
var counter = new count();
var repeater = new repeat();

function check(){
	this.checkTarget = function(idInput,target) {

        var a=null;
		var info=null;

        switch (target) {
            case 'emailList': {
                a = /^(([a-zA-Z0-9_\-]+)@([a-zA-Z0-9_\-]+)\.([a-zA-Z]{2,5}){1,25})+([;.](([a-zA-Z0-9_\-]+)@([a-zA-Z0-9_\-]+)\.([a-zA-Z]{2,5}){1,25})+)*$/;
                break;
            }
            case 'email': {
                a = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/;
                break;
            }
            case 'latinAndNumber': {
                a = /^[a-zA-Z0-9 ]+$/;
                break;
            }

            case 'integer': {
                a = /^[0-9 ]+$/;
                break;
            }

            default: {
                //do nothing
            }
        }

		var inp = $("#"+idInput);
		
		if ( inp.val() != '') {
			
			if (inp.val().search(a)!=-1) {
                $("#"+idInput).css("border", "");
				
			} else {
                $("#"+idInput).css("border", "1px solid #E53136");
			}		
		} 
	};
	this.hide = function(idInput) {
        $("#"+idInput).css("border", "");
	};
}

function count(){
	this.countCreate = function(idInput,number) {
		
		var inp = $("#"+idInput);
		
		var content = inp.val();
		var length = content.length;
		var left = number - length;

        $("#"+idInput).closest("div").append("<div id='" + idInput + "Count' class='count'></div>");
		$("#"+idInput+"Count").text(left + ' symbols left');
		
	};
	
	this.countPress = function(idInput,number) {
		
		var inp = $("#"+idInput);
		
		var content = inp.val();
		var length = content.length;
		
		if (length>number) {
			inp.val(content.substr(0,number));
		}
		
		var count =  $("#"+idInput+"Count");
		var left = number - length;
		
		if (left<0) {
			left=0;
		}
		
		count.text(left + ' symbols left');
	};
	
	this.countHide = function(idInput) {
		$("#"+idInput+"Count").remove();
	};
}

function repeat() {
    this.correctRepeat = function (idInput, idInputCompare) {

        var el1 = $("#" + idInput);
        var el2 = $("#" + idInputCompare);

        if (!(el1.val() == el2.val())) {
            $("#" + idInput).css("border", "1px solid #E53136");

        };

        this.hide = function (idInput) {
            $("#" + idInput).css("border", "");
        };
    }
}

function submitDeleteAcc(eventObject) {
    eventObject.preventDefault();

    var a = /^[a-zA-Z0-9 ]+$/;
    if ( $("#checkPass").val().search(a)!=-1) {

        ajaxDeleteAcc();

    } else {

        $("#exceptionDeleteAcc").removeClass("dispNone").html("Not correctly entered data. <br/> Please, check input with red border");
    }
}

function submitChangeEmail(eventObject) {
    eventObject.preventDefault();

    var a =/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/;
    if ( $("#newEmail").val().search(a)!=-1) {

        ajaxChangeEmail();

    } else {
        $("#exceptionChangeEmail").removeClass("dispNone").html("Not correctly entered email.");
    }
}

function submitRegistration() {

    var a =/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/;
    var b = /^[a-zA-Z0-9 ]+$/;

    if ($("#email").val().search(a)!=-1 && $("#username").val().search(b)!=-1 &&
        $("#pass").val().search(b)!=-1 && $("#pass").val()== $("#repeatPass").val()) {

        ajaxReg();
    } else {

        $("#exceptionReg").removeClass("dispNone").html("Not correctly entered data. <br/> Please, check input with red border");
    }
}

function submitRemindPass() {
    var a =/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/;
    if ( $("#emailRemindPass").val().search(a)!=-1) {

        ajaxRemingPass();

    } else {
        $("#exceptionRemindPass").removeClass("dispNone").html("Not correctly entered email.");
    }

}

function submitCreateOrUpdate(eventObject, param) {
    eventObject.preventDefault();

    var a = /^[0-9]+$/;
    var b = /^(([a-zA-Z0-9_\-]+)@([a-zA-Z0-9_\-]+)\.([a-zA-Z]{2,5}){1,25})+([;.](([a-zA-Z0-9_\-]+)@([a-zA-Z0-9_\-]+)\.([a-zA-Z]{2,5}){1,25})+)*$/;
    var total = $("input[name=repeatDay]:checked").length;

    if ($("#attenders").val().search(b)!=-1 ||
        $("#attenders").val()=="")  {

        if ($('#never').is( ":checked" ) &&
            $('#numberOfOccurrence').val()=="" ||
            $('#after').is( ":checked" ) &&
            $('#numberOfOccurrence').val().search(a)!=-1)

                if($('#repeatDay').is( ":checked" ) && total > 0
                    || $('#repeatPeriodic').is( ":checked" ) ||
                    $('#once').is( ":checked" )) {

                    ajaxCreateOrUpdate(param);
                }
            else
                $("#exceptionCreate").removeClass("dispNone").html("Not selected any one day");
        else
            $("#exceptionCreate").removeClass("dispNone").html("Not correctly entered data about occurrences");
    } else
        $("#exceptionCreate").removeClass("dispNone").html("Not correctly entered emails.");



}

function submitSearch(eventObject) {
    eventObject.preventDefault();

    switch ($("#searchSelect").val()) {
        case 'searchByTitle':
        case 'searchByTitleStartWith':
        case 'searchByDate':
        case 'searchIntoPeriod':
        case 'searchFreeTime':
            ajaxSearch()
            break;
        case 'searchByAttender':
        case 'searchByAttenderIntoPeriod':
        case 'isAttenderFree':
            attenderCheckSubmite();
            break;
        default:
            //do nothing\
            break;
    }
}

function attenderCheckSubmite() {
    var a = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/;
    if ($("#attender").val().search(a)!=-1) {

        ajaxSearch()

    } else {

        $("#exceptionSearch").removeClass("dispNone").html("Not correctly entered email.");
    }
}
