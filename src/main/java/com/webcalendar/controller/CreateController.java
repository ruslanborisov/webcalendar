package com.webcalendar.controller;

import com.webcalendar.domain.*;
import com.webcalendar.exception.DateTimeFormatException;
import com.webcalendar.exception.ValidationException;
import com.webcalendar.service.CalendarService;
import com.webcalendar.util.DateHelper;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.inject.Inject;
import java.util.*;

/**
 * Provides handling requests from client for create,
 * update and delete events
 *
 *
 * @author Ruslan Borisov
 */
@Controller
public class CreateController {

    private CalendarService calendarService;

    @Inject
    public CreateController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }
    public CreateController(){
    }

    /**
     * Handling request from client for display create page. Ajax request.
     *
     * @return view of create page
     */
    @RequestMapping(value="/createPage", method = RequestMethod.POST)
    public String createPage() {
        return "views/create";
    }

    /**
     * Handling request from user for create event. Ajax request.
     * Invokes method of {@link CalendarService}:  {@link CalendarService#createEvent(String, User)}
     * @param eventDescription : String in json format with all needs descriptions for create event.
     * eventDescription example:{"title":"New Year",
     *                           "description":"Happy New Year",
     *                           "allDay":"false",
     *                           "color":"#B8B9FF",
     *                           "startDate":"06-10-2020",
     *                           "endDate":"08-10-2020",
     *                           "startTime":"12:00",
     *                           "endTime":"15:00",
     *                           "reminderTime":"30",
     *                           "numberOfOccurrence":"null",
     *                           "eventAttenders":["bor@ukr.net","kate@ukr.net"],
     *                           "eventRepeaters":["once"],
     *                           "eventReminders":["popup"]}
     * @return @ResponseBody string status of result creating in json format:
     * {"result":"created"} if created successful or {"exception":"Failed creat event"} otherwise.
     */
    @RequestMapping(value="/create", method = RequestMethod.POST)
    public @ResponseBody
    String create(@RequestParam(value="data") String eventDescription) {

        JSONObject data = new JSONObject();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Event eventCreated;
        try {
            eventCreated = calendarService.createEvent(eventDescription, user);
        } catch (ValidationException | DateTimeFormatException e) {
            data.put("exception", e.getMessage());
            return data.toString();
        }

        if(eventCreated!=null)
            data.put("result", "created");
        else
            data.put("exception", "Failed create event");

        return data.toString();
    }

    /**
     * Handling request from client for get event for update. Ajax request.
     *
     * @param id : Id of the event for update
     * @param startDate : Start date of the event which need update.
     * Uses for get right date for recurring event from data store.
     * @return @ResponseBody String in json format with all needs descriptions for create event.
     */
    @RequestMapping(value="/getEventForUpdate", method = RequestMethod.POST)
    public @ResponseBody
    String editPage(@RequestParam(value="id") UUID id,
                    @RequestParam(value="startDate") String startDate) {

        Event eventForUpdate = calendarService.searchById(id);

        EventAdapter eventAdapterForUpdate;
        try {
            eventAdapterForUpdate = eventForUpdate.getRigthDateForRepeatEvent(DateHelper.stringToDate(startDate));
        } catch (DateTimeFormatException e) {
            return "redirect:exceptionCreate";
        }

        return eventAdapterForUpdate.toJson().toString();
    }


    /**
     * Handling request from client for delete event. Ajax request.
     *
     * @param id : Id of the event for update
     * @return @ResponseBody String status of the result of delete event.
     * "ok" if event successful deleted or "exception" otherwise.
     */
    @RequestMapping(value="/delete", method = RequestMethod.POST)
    public @ResponseBody
    String deleteEvent(@RequestParam(value="id") UUID id) {

        Event event = calendarService.remove(id);
        if (event!=null)
            return "ok";
        else
            return "exception";
    }

    @RequestMapping(value = "/exceptionCreate", method = RequestMethod.GET)
    public @ResponseBody
    String exceptionCreate() {
        return "WrongDateTime";
    }
}
