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
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import static java.time.temporal.TemporalAdjusters.*;

/**
 * Provides handling requests from client for navigate
 * between days, weeks or months
 *
 *
 * @author Ruslan Borisov
 */
@Controller
public class NavigationController {

    private CalendarService calendarService;

    @Inject
    public NavigationController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }
    public NavigationController(){
    }

    /**
     * Handling request from user for navigate with mini calendar.
     * Ajax request.
     *
     * @param displayedPeriod : Date which selected on mini calendar.
     * @return view with events for selected date
     * @throws DateTimeFormatException if an error occurred during parsing or format string/date
     */
    @RequestMapping(value = "/quickSelection", method = RequestMethod.POST)
    public String quickSelection(ModelMap model, HttpSession session,
                             @RequestParam(value = "displayedPeriod") String displayedPeriod)
            throws DateTimeFormatException {

        session.setAttribute("displayedPeriod", DateHelper.stringToDate(displayedPeriod));
        return day(model, session);
    }

     /**
     * Handling request from user for navigate with previous button. Ajax request.
     * Displayed period decrease on one point.
     * (for day: minus day, for week: minus week, for month: minus month)
     *
     * @param filter : Type of view (month, week or day) which current displayed.
     * @return view with events for current displayed period after decrease.
     * @throws DateTimeFormatException if an error occurred during parsing or format string/date
     * @throws OrderOfArgumentsException if start date is after end date
     */
    @RequestMapping(value = "/prev", method = {RequestMethod.POST, RequestMethod.GET})
    public String prevPeriod(ModelMap model, HttpSession session,
                      @RequestParam(value = "filter") String filter) throws OrderOfArgumentsException,
            DateTimeFormatException {

        LocalDate displayedPeriod = (LocalDate) session.getAttribute("displayedPeriod");
        switch (filter) {
            case "day":
                session.setAttribute("displayedPeriod", displayedPeriod.minusDays(1));
                return day(model, session);
            case "week":
                session.setAttribute("displayedPeriod", displayedPeriod.with(previous(DayOfWeek.MONDAY)));
                return week(model, session);
            case "month":
                session.setAttribute("displayedPeriod", displayedPeriod.minusMonths(1).with(firstDayOfMonth()));
                return month(model, session);
        }
        return "Error";
    }

    /**
     * Handling request from user for navigate with next button. Ajax request.
     * Displayed period increase on one point.
     * (for day: plus day, for week: plus week, for month: plus month)
     *
     * @param filter : Type of view (month, week or day) which current displayed.
     * @return view with events for current displayed period after increase.
     * @throws DateTimeFormatException if an error occurred during parsing or format string/date
     * @throws OrderOfArgumentsException if start date is after end date
     */
    @RequestMapping(value = "/next", method = {RequestMethod.POST, RequestMethod.GET})
    public String nextPeriod(ModelMap model, HttpSession session,
                       @RequestParam(value = "filter") String filter) throws OrderOfArgumentsException,
            DateTimeFormatException {

        LocalDate displayedPeriod = (LocalDate) session.getAttribute("displayedPeriod");
        switch (filter) {
            case "day":
                session.setAttribute("displayedPeriod", displayedPeriod.plusDays(1));
                return day(model, session);
            case "week":
                session.setAttribute("displayedPeriod", displayedPeriod.with(next(DayOfWeek.MONDAY)));
                return week(model, session);
            case "month":
                session.setAttribute("displayedPeriod", displayedPeriod.plusMonths(1).with(firstDayOfMonth()));
                return month(model, session);
        }
        return "Error";
    }

    /**
     * Handling request from user for display events for today. Ajax request.
     * Displayed period with "today" parameter:
     * (for day: today, for week: current week, for month: current month)
     *
     * @param filter : Type of view (month, week or day) which current displayed.
     * @return view with events for current displayed period with "today" parameter.
     * @throws DateTimeFormatException if an error occurred during parsing or format string/date
     * @throws OrderOfArgumentsException if start date is after end date
     */
    @RequestMapping(value = "/today", method = {RequestMethod.POST, RequestMethod.GET})
    public String today(ModelMap model, HttpSession session,
                        @RequestParam(value = "filter") String filter) throws OrderOfArgumentsException,
            DateTimeFormatException {

        session.setAttribute("displayedPeriod", LocalDate.now());
        switch (filter) {
            case "day":
                return day(model, session);
            case "week":
                return week(model, session);
            case "month":
                return month(model, session);
        }
        return "Error";
    }

    /**
     * Handling request from user for display events for selected day. Ajax request.
     *
     * @return view with events for selected day.
     * @throws DateTimeFormatException if an error occurred during parsing or format string/date
     */
    @RequestMapping(value = "/day", method = {RequestMethod.POST, RequestMethod.GET})
    public String day(ModelMap model, HttpSession session) throws DateTimeFormatException {

        LocalDate displayedPeriod = (LocalDate) session.getAttribute("displayedPeriod");
        List<EventAdapter> eventList = calendarService.searchByDate(displayedPeriod);

        if (eventList.size()>0)
            model.addAttribute("events", eventList);
        else
            model.addAttribute("noEvents", "no events..");

        model.addAttribute("currentPeriod", DateHelper.dateToStringDescription(displayedPeriod));
        model.addAttribute("miniCalendar", DateHelper.dateToString(displayedPeriod));
        model.addAttribute("today", LocalDateTime.now());
        model.addAttribute("currentDate", displayedPeriod);

        return "views/day";
    }

    /**
     * Handling request from user for display events for selected week. Ajax request.
     *
     * @return view with events for selected week.
     * @throws DateTimeFormatException if an error occurred during parsing or format string/date
     */
    @RequestMapping(value = "/week", method = {RequestMethod.POST, RequestMethod.GET})
    public String week(ModelMap model, HttpSession session) throws OrderOfArgumentsException,
            DateTimeFormatException {

        LocalDate displayedPeriod = (LocalDate) session.getAttribute("displayedPeriod");
        LocalDate startDate = displayedPeriod.with(previousOrSame(DayOfWeek.MONDAY));
        LocalDate endDate = displayedPeriod.with(nextOrSame(DayOfWeek.SUNDAY));
        List<EventAdapter> eventList = calendarService.searchIntoPeriod(startDate,endDate);


        if (eventList.size()>0)
            model.addAttribute("events", eventList);
        else
            model.addAttribute("noEvents", "no events..");

        List<LocalDate> weekPeriod = new ArrayList<>();
        LocalDate tempDate = displayedPeriod.with(previousOrSame(DayOfWeek.MONDAY));
        for (int i = 0; i < 7; i++) {
            weekPeriod.add(tempDate);
            tempDate = tempDate.plusDays(1);
        }
        model.addAttribute("week", weekPeriod);
        model.addAttribute("currentPeriod", DateHelper.periodToStringDescription(startDate, endDate));
        model.addAttribute("miniCalendar", DateHelper.dateToString(displayedPeriod));
        model.addAttribute("today", LocalDateTime.now());

        return "views/week";
    }

    /**
     * Handling request from user for display events for selected month. Ajax request.
     *
     * @return view with events for selected month.
     * @throws DateTimeFormatException if an error occurred during parsing or format string/date
     */
    @RequestMapping(value = "/month", method = {RequestMethod.POST, RequestMethod.GET})
    public String month(ModelMap model, HttpSession session) throws OrderOfArgumentsException,
            DateTimeFormatException {

        LocalDate displayedPeriod = (LocalDate) session.getAttribute("displayedPeriod");
        LocalDate tempStart = displayedPeriod.with(firstDayOfMonth()).with(previousOrSame(DayOfWeek.MONDAY));
        LocalDate tempEnd = tempStart.with(next(DayOfWeek.SUNDAY));
        List<List<EventAdapter>> eventMonth = new ArrayList<>();
        for (int i=0; i<6; i++) {
            List<EventAdapter> eventWeekList = calendarService.searchIntoPeriod(tempStart, tempEnd);
            eventMonth.add(eventWeekList);
            tempStart = tempStart.plusDays(7);
            tempEnd = tempEnd.plusDays(7);
        }

        if (eventMonth.size()>0)
            model.addAttribute("events", eventMonth);
        else
            model.addAttribute("noEvents", "no events..");

        List<List<LocalDate>> monthPeriod = new ArrayList<>();
        LocalDate tempDate = displayedPeriod.withDayOfMonth(1);
        tempDate = tempDate.with(previousOrSame(DayOfWeek.MONDAY));
        List<LocalDate> listDate = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                listDate.add(tempDate);
                tempDate = tempDate.plusDays(1);
            }
            monthPeriod.add(listDate);
            listDate = new ArrayList<>();
        }
        model.addAttribute("month", monthPeriod);
        model.addAttribute("currentPeriod", DateHelper.monthToStringDescription(displayedPeriod));
        model.addAttribute("miniCalendar", DateHelper.dateToString(displayedPeriod));
        model.addAttribute("today", LocalDateTime.now());

        return "views/month";
    }

    /**
     * Handling request from user for get details about event. Ajax request.
     *
     * @return view with event details.
     * @throws DateTimeFormatException if an error occurred during parsing or format string/date
     */
    @RequestMapping(value = "/eventDetails", method = RequestMethod.POST)
    public String eventDetails(ModelMap model,
                               @RequestParam(value="id") UUID id,
                               @RequestParam(value="startDate") String startDate) throws OrderOfArgumentsException,
            DateTimeFormatException {

        Event event = calendarService.searchById(id);
        EventAdapter eventAdapter = event.getRigthDateForRepeatEvent(DateHelper.stringToDate(startDate));
        model.addAttribute("event", eventAdapter);

        if (event.isAllDay())
            model.addAttribute("period", DateHelper.periodToStringDescription(eventAdapter.getStartDate(),
                    eventAdapter.getEndDate()));
        else
            model.addAttribute("period", DateHelper.periodWithTimeToStringDescription(LocalDateTime.of(eventAdapter.getStartDate(),
                            eventAdapter.getStartTime()),
                    LocalDateTime.of(eventAdapter.getEndDate(), eventAdapter.getEndTime())));

        return "includes/details";
    }

    @RequestMapping(value = "/aboutapp", method = RequestMethod.GET)
    public String aboutapp() {
        return "views/aboutapp";
    }


}
