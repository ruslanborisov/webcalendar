package com.webcalendar.controller;

import com.webcalendar.domain.Event;
import com.webcalendar.domain.EventAdapter;
import com.webcalendar.exception.DateTimeFormatException;
import com.webcalendar.service.CalendarService;
import com.webcalendar.util.DateHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.inject.Inject;
import java.util.UUID;

/**
 * Provides handling requests from client for speech synthesis
 * and abilities of speech control.
 *
 *
 * @author Ruslan Borisov
 */
@Controller
public class SpeechController {

    private CalendarService calendarService;

    @Inject
    public SpeechController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }
    public SpeechController(){
    }

    /**
     * Handling request from client for get event for speech synthesis about it. Ajax request.
     *
     * @param id : Id of the event for speech synthesis
     * @param startDate : Start date of the event.
     * Uses for get right date for recurring event from data store.
     * @return @ResponseBody String of the text about event.
     * @throws DateTimeFormatException if an error occurred during parsing or format string/date
     */
    @RequestMapping(value = "/speechSynthesis", method = RequestMethod.POST)
    public @ResponseBody
    String speechSynthesis(@RequestParam(value = "id") UUID id,
                           @RequestParam(value = "startDate") String startDate) throws DateTimeFormatException {

        Event eventToSpeech = calendarService.searchById(id);
        EventAdapter eventAdapterToSpeech = eventToSpeech.getRigthDateForRepeatEvent(DateHelper.stringToDate(startDate));
        return eventAdapterToSpeech.getTextAboutEvent();
    }

    /**
     * Handling voice control request from client.
     *
     * @param command : Command for action
     * @return if command is "exit" : execute logout and return view of the login form
     */
    @RequestMapping(value = "/speechControl", method = RequestMethod.POST)
     public String speechCreate(@RequestParam(value = "command") String command) {
        switch (command) {
            case "exit":
                return "redirect:j_spring_security_logout";
            default:
                break;
        }
     return "redirect:home";
    }
}
