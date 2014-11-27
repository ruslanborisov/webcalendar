package com.webcalendar.controller;

import com.webcalendar.domain.Event;
import com.webcalendar.domain.EventAdapter;
import com.webcalendar.exception.DateTimeFormatException;
import com.webcalendar.exception.OrderOfArgumentsException;
import com.webcalendar.service.CalendarService;
import com.webcalendar.util.DateHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Provides handling requests from client for search events and
 * other features for search.
 *
 *
 * @author Ruslan Borisov
 */
@Controller
public class SearchController {

    private CalendarService calendarService;

    @Inject
    public SearchController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }
    public SearchController() {
    }

    /**
     * Handling request from client for display search page. Ajax request.
     *
     * @return view of search page
     */
    @RequestMapping(value = "/searchPage", method = RequestMethod.POST)
    public String searchPage() {
        return "views/search";
    }

    /**
     * Handling request from client for display page for search free time intervals. Ajax request.
     *
     * @return view of page for search free time intervals
     */
    @RequestMapping(value="/searchFreePage", method = RequestMethod.POST)
    public String searchFree() {
        return "views/searchFree";
    }

    /**
     * Handling request from client for search events with given title.
     * Ajax request.
     *
     * @param title : Title of events for search
     * @return view with displayed events for given title
     * or message "No events.." if there are no mapping for given title
     */
    @RequestMapping(value = "/searchByTitle", method = RequestMethod.POST)
    public String searchByTitle(ModelMap model,
                                @RequestParam(value = "title") String title ) {

        List<Event> searchEvents = calendarService.searchByTitle(title);
        String displayedPeriod = "Results of search by title '" + title + "'";
        model.addAttribute("currentPeriod", displayedPeriod);

        if (searchEvents.size()>0) {
            model.addAttribute("today", LocalDateTime.now());
            model.addAttribute("events", searchEvents);
        } else
            model.addAttribute("noEvents", "No events..");

        return "includes/searchResult";
    }

    /**
     * Handling request from client for search events with given prefix.
     * Ajax request.
     *
     * @param titleStart : Prefix of events for search
     * @return view with displayed events for given prefix
     * or message "No events.." if there are no mapping for given prefix
     */
    @RequestMapping(value = "/searchByTitleStartWith", method = RequestMethod.POST)
    public String searchByTitleStartWith(ModelMap model,
                                         @RequestParam(value = "titleStart") String titleStart) {

        List<Event> searchEvents = calendarService.searchEventByTitleStartWith(titleStart);
        String displayedPeriod = "Results of search by title start with '" + titleStart + "'";
        model.addAttribute("currentPeriod", displayedPeriod);

        if (searchEvents.size()>0) {
            model.addAttribute("today", LocalDateTime.now());
            model.addAttribute("events", searchEvents);
        } else
            model.addAttribute("noEvents", "No events..");

        return "includes/searchResult";
    }

    /**
     * Handling request from client for search events with given email.
     * Ajax request.
     *
     * @param email : Email of the attender for search
     * @return view with displayed events for given email
     * or message "No events.." if there are no mapping for given email
     */
    @RequestMapping(value = "/searchByAttender", method = RequestMethod.POST)
    public String searchByAttender(ModelMap model,
                                   @RequestParam(value = "attender") String email) {

        List<Event> searchEvents = calendarService.searchByAttender(email);
        String displayedPeriod = "Results of search by attender '" + email + "'";
        model.addAttribute("currentPeriod", displayedPeriod);

        if (searchEvents.size()>0) {
            model.addAttribute("today", LocalDateTime.now());
            model.addAttribute("events", searchEvents);
        } else
            model.addAttribute("noEvents", "No events..");

        return "includes/searchResult";
    }

    /**
     * Handling request from client for search events with given date.
     * Ajax request.
     *
     * @param dateStr : Date of the event for search in string form
     * @return view with displayed events for given date
     * or message "No events.." if there are no mapping for given date
     */
    @RequestMapping(value = "/searchByDate", method = RequestMethod.POST)
    public String searchByDate(ModelMap model,
                              @RequestParam(value = "date") String dateStr) {

        LocalDate date;
        try {
            date = DateHelper.stringToDate(dateStr);
        } catch (DateTimeFormatException e) {
            return "redirect:exceptionSearch";
        }

        List<EventAdapter> searchEvents = calendarService.searchByDate(date);
        String displayedPeriod = "Results of search by day '" + DateHelper.dateToStringDescription(date) + "'";
        model.addAttribute("currentPeriod", displayedPeriod);

        if (searchEvents.size()>0) {
            model.addAttribute("today", LocalDateTime.now());
            model.addAttribute("events", searchEvents);
        } else
            model.addAttribute("noEvents", "No events..");

        return "includes/searchResult";
    }

    /**
     * Handling request from client for search events with given range of dates.
     * Ajax request.
     *
     * @param startDateStr : Start date of the event for search in string form
     * @param endDateStr : End date of the event for search in string form
     * @return view with displayed events for given range of dates.
     * or message "No events.." if there are no mapping for given range of dates.
     */
    @RequestMapping(value = "/searchIntoPeriod", method = RequestMethod.POST)
    public String searchIntoPeriod(ModelMap model,
                                   @RequestParam(value = "startDate") String startDateStr,
                                   @RequestParam(value = "endDate") String endDateStr) {

        LocalDate startDate;
        LocalDate endDate;
        List<EventAdapter> searchEvents;
        try {
            startDate = DateHelper.stringToDate(startDateStr);
            endDate = DateHelper.stringToDate(endDateStr);
            searchEvents = calendarService.searchIntoPeriod(startDate, endDate);
        } catch (OrderOfArgumentsException | DateTimeFormatException e) {
            return "redirect:exceptionSearch";
        }

        String displayedPeriod = "Results of search by period '" +
                DateHelper.periodToStringDescription(startDate, endDate) + "'";
        model.addAttribute("currentPeriod", displayedPeriod);

        if (searchEvents.size()>0) {
            model.addAttribute("today", LocalDateTime.now());
            model.addAttribute("events", searchEvents);
        } else
            model.addAttribute("noEvents", "No events..");

        return "includes/searchResult";
    }

    /**
     * Handling request from client for search events with given range of dates and attender
     * Ajax request.
     *
     * @param startDateStr : Start date of the event for search in string form
     * @param endDateStr : End date of the event for search in string form
     * @param email : Email of the attender for search
     * @return view with displayed events for given range of dates and attender
     * or message "No events.." if there are no mapping for given range of dates and attender
     */
    @RequestMapping(value = "/searchByAttenderIntoPeriod", method = RequestMethod.POST)
    public String searchByAttenderIntoPeriod(ModelMap model,
                                             @RequestParam(value = "startDate") String startDateStr,
                                             @RequestParam(value = "endDate") String endDateStr,
                                             @RequestParam(value = "attender") String email) {

        LocalDate startDate;
        LocalDate endDate;
        List<EventAdapter> searchEvents;
        try {
            startDate = DateHelper.stringToDate(startDateStr);
            endDate =  DateHelper.stringToDate(endDateStr);
            searchEvents = calendarService.searchByAttenderIntoPeriod(email, startDate, endDate);
        } catch (OrderOfArgumentsException | DateTimeFormatException e) {
            return "redirect:exceptionSearch";
        }

        String displayedPeriod = "Results of search by period '" +
                DateHelper.periodToStringDescription(startDate, endDate) + "'" ;
        model.addAttribute("currentPeriod", displayedPeriod);

        if (searchEvents.size()>0) {
            model.addAttribute("today", LocalDateTime.now());
            model.addAttribute("events", searchEvents);
        } else
            model.addAttribute("noEvents", "No events..");

        return "includes/searchResult";
    }

    /**
     * Handling request from client for search free time
     * Ajax request.
     *
     * @param startDateStr : Start date of the event for search in string form
     * @param endDateStr : End date of the event for search in string form
     * @param startTimeStr : Start time of the event for search in string form
     * @param endTimeStr : End time of the event for search in string formin string form
     * @return view with displayed free intervals for given range of dates
     * or message "No free time.." if there are no free time
     */
    @RequestMapping(value = "/searchFreeTime", method = RequestMethod.POST)
    public String searchFreeTime(ModelMap model,
                                 @RequestParam(value = "startDate") String startDateStr,
                                 @RequestParam(value = "endDate") String endDateStr,
                                 @RequestParam(value = "startTime") String startTimeStr,
                                 @RequestParam(value = "endTime") String endTimeStr) {

        String startDateTimeStr = startDateStr + " " + startTimeStr;
        String endDateTimeStr = endDateStr + " " + endTimeStr;

        List<List<LocalDateTime>> freeTimeList;
        try {
            LocalDateTime startDateTime = DateHelper.stringToDateTime(startDateTimeStr);
            LocalDateTime endDateTime =  DateHelper.stringToDateTime(endDateTimeStr);
            freeTimeList = calendarService.searchFreeTime(startDateTime, endDateTime);
        } catch (OrderOfArgumentsException | DateTimeFormatException e) {
            return "redirect:exceptionSearch";
        }

        if (freeTimeList.size() < 1) {
            model.addAttribute("noFreeTime", "No free time");
        } else
            model.addAttribute("freeTime", freeTimeList);

        return "includes/intervals";
    }

    /**
     * Handling request from client for search free attender
     * Ajax request.
     *
     * @param startDayStr : Start date of the event for search in string form
     * @param endDayStr : End date of the event for search in string form
     * @param startTimeStr : Start time of the event for search in string form
     * @param endTimeStr : End time of the event for search in string form
     * @param email : Email of the attender for search
     * @return view with displayed events for given range of dates and attender
     * or message "Attender free" if there are no events for given period
     */
    @RequestMapping(value = "/searchIsAttenderFree", method = RequestMethod.POST)
    public String isAttenderFree(ModelMap model,
                           @RequestParam(value = "startDate") String startDayStr,
                           @RequestParam(value = "endDate") String endDayStr,
                           @RequestParam(value = "startTime") String startTimeStr,
                           @RequestParam(value = "endTime") String endTimeStr,
                           @RequestParam(value = "attender") String email) {

        String startDateTimeStr = startDayStr + " " + startTimeStr;
        String endDateTimeStr = endDayStr + " " + endTimeStr;

        List<EventAdapter> crossingEvent;
        try {
            LocalDateTime startDateTime = DateHelper.stringToDateTime(startDateTimeStr);
            LocalDateTime endDateTime = DateHelper.stringToDateTime(endDateTimeStr);
            crossingEvent = calendarService.isAttenderFree(email, startDateTime, endDateTime);
        } catch (OrderOfArgumentsException | DateTimeFormatException e ) {
            return "redirect:exceptionSearch";
        }

        if (crossingEvent.isEmpty())
            model.addAttribute("currentPeriod", "Attender free");
        else {
            model.addAttribute("currentPeriod", "Attender NOT free. Crossing events:");
            model.addAttribute("events", crossingEvent);
        }
        return "includes/searchResult";
    }

    @RequestMapping(value = "/exceptionSearch", method = RequestMethod.GET)
    public @ResponseBody
    String exceptionSearch() {
        return "WrongDateTime";
    }
}
