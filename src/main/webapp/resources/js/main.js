

Main = {

    initLogin: function() {

        $("#modal_trigger").leanModal({top: 200, overlay: 0.6});

        $(function () {

            $("#login_form_btn").click(function () {
                $(".social_login_form").hide();
                $(".user_login_form").show();
                return false;
            });

            $("#register_form_btn").click(function () {
                validationOnRegPage();
                $(".social_login_form").hide();
                $(".user_register_form").show();
                $(".header_title").text('Register');
                return false;
            });

            $(".back_btn").click(function () {
                $(".user_login_form").hide();
                $(".user_register_form").hide();
                $(".social_login_form").show();
                $(".header_title").text('Login');
                return false;
            });

            $(".forgot_password_btn").click(function () {
                validationOnForgotPassPage();
                $(".user_login_form").hide();
                $(".remind_pass_form").show();
                $(".header_title").text('Remind password');
                return false;
            });

            $(".back_remind_btn").click(function () {
                $(".remind_pass_form").hide();
                $(".user_login_form").show();
                $(".header_title").text('Login');
                return false;
            });

        })

        $('#loginButton').on('click', function () {
            ajaxUserLogin();
        });

        $('#usernameLogin, #passLogin').on('focus', function() {
            $("#exceptionLogin").html("");
        });

        $('#registerButton').on('click', function () {
            submitRegistration();
        });

        $('#remindButton').on('click', function () {
            submitRemindPass();
        });

        $("#miniCalendar").datepicker({
            firstDay: 1,
            showOtherMonths: true,
            selectOtherMonths: true,
            dateFormat: 'dd-mm-yy',
            onSelect: function(dateText)
            {
                $("#today").prop("checked", false);
                ajaxQuickSelection(dateText);
            },
        });
    },

    initGeneral: function() {

        ajaxToday();
        ajaxInitPopupReminder();
        speechRecognize('instructionsCommand','micCommand','resultCommand');

        $('#headerButton').on('click', function () {
            ajaxToday();
        });

        $('#buttonCreate').on('click', function () {
            ajaxCreatePage();
        });

        $('#searchButton').on('click', function () {
            ajaxSearchPage();
        });

        $('#settingsButton').on('click', function () {
           ajaxSettingsPage();
           ajaxSettingsAside();
        });

        $('#homeButton').on('click', function () {
            ajaxToday();
        });

        $('#aboutSpeech').on('click', function () {
            popupAboutSpeech();
        });

        $('#prev').on('click', function () {
            ajaxPrev()
        });

        $('#next').on('click', function () {
            ajaxNext()
        });

        $("#miniCalendar").datepicker({
            firstDay: 1,
            showOtherMonths: true,
            selectOtherMonths: true,
            dateFormat: 'dd-mm-yy',
            onSelect: function(dateText)
            {
                $("#today").prop("checked", false);
                ajaxQuickSelection(dateText);
            },
        });

        $('input[name="periodFilter"]').on('change', function() {
            ajaxFilterPeriod($(this).attr('id'));
        });
        $('input[name="todayFilter"]').on('change', function() {
            ajaxToday();
        });

        $(document).mouseup(function (e)
        {
            var container = $(".arrow_box_detail");

            if (!container.is(e.target)
                && container.has(e.target).length === 0)
            {
                container.remove();
            }
        });

        $(document).mouseup(function (e)
        {
            var container = $(".divDetail");

            if (!container.is(e.target)
                && container.has(e.target).length === 0)
            {
                container.remove();
            }
        });

        $('.popup-with-zoom-anim').magnificPopup({
            type: 'inline',
            fixedContentPos: false,
            fixedBgPos: true,

            overflowY: 'auto',

            closeBtnInside: true,
            preloader: false,

            midClick: true,
            removalDelay: 300,
            mainClass: 'my-mfp-zoom-in',

            callbacks: {
                beforeOpen: function() {
                  $('.popup-with-zoom-anim').css("border" , "1px solid #CCCCCC").css("outline","none");
                }

            }
        });

        $("nav").append("<div id='detail' class='arrow_box_detail' style='top: -55px; right: 340px'></div>");
        $('.popup-with-zoom-anim').css("border","1px solid #FF0F22");
        $("#detail").html("Before you start using the application, click here to see general information about development.").css("border","1px solid #FF0F22");
    },

    initCreatePage: function() {

        speechRecognize('instructionsTitle','micTitle','title');
        speechRecognize('instructionsDescription','micDescription','description');
        selectionOnCreatePage();
        validationOnCreatePage();

        $("#miniCalendarForCreatePage").datepicker({
            firstDay: 1,
            showOtherMonths: true,
            selectOtherMonths: true,
            dateFormat: 'dd-mm-yy',
        });

        $( "#startDate" ).datepicker({
            firstDay: 1,
            showOtherMonths: true,
            selectOtherMonths: true,
//            minDate: 0,
            dateFormat: 'dd-mm-yy',
            showAnim: "slideDown",
            showButtonPanel: true,
            onClose: function( selectedDate ) {
                $( "#endDate" ).datepicker( "option", "minDate", selectedDate );
            },
        });
        $('#startTime').timepicker();

        $( "#endDate" ).datepicker({
            firstDay: 1,
            showOtherMonths: true,
            selectOtherMonths: true,
//            minDate: 0,
            dateFormat: 'dd-mm-yy',
            showAnim: "slideDown",
            showButtonPanel: true,
            onClose: function( selectedDate ) {
                $( "#startDate" ).datepicker( "option", "maxDate", selectedDate );
            }
        });
        $('#endTime').timepicker();

        $('#formCreate').submit(function (eventObject) {
            submitCreateOrUpdate(eventObject);
        });

        $('#aboutAttenderInput').on('click', function () {
            popupAboutAttender();
        });

    },

    initSearchPage: function() {

        selectionOnSearchPage();
        validationOnSearchPage();

        $("#date").datepicker({
            firstDay: 1,
            showOtherMonths: true,
            selectOtherMonths: true,
            dateFormat: 'dd-mm-yy',
        });

        $( "#startDate" ).datepicker({
            firstDay: 1,
            showOtherMonths: true,
            selectOtherMonths: true,
//            minDate: 0,
            dateFormat: 'dd-mm-yy',
            showAnim: "slideDown",
            showButtonPanel: true,
            onClose: function( selectedDate ) {
                $( "#endDate" ).datepicker( "option", "minDate", selectedDate );
            },
        });
        $('#startTime').timepicker();

        $( "#endDate" ).datepicker({
            firstDay: 1,
            showOtherMonths: true,
            selectOtherMonths: true,
//            minDate: 0,
            dateFormat: 'dd-mm-yy',
            showAnim: "slideDown",
            showButtonPanel: true,
            onClose: function( selectedDate ) {
                $( "#startDate" ).datepicker( "option", "maxDate", selectedDate );
            }
        });
        $('#endTime').timepicker();


        $('#formSearch').submit(function (eventObject) {
            submitSearch(eventObject);
        });
        $('#formSearchFree').submit(function (eventObject) {
            submitSearch(eventObject);
        });
    },

    initSettingsPage: function() {

        validationOnSettingsPage();

        $("#formChangeEmail").submit(function (eventObject) {
            submitChangeEmail(eventObject);
        });

        $("#formDeleteAcc").submit(function (eventObject) {
            submitDeleteAcc(eventObject);
        });

    },

};// end Main

	


