package com.webcalendar.controller;

import com.webcalendar.domain.EventAdapter;
import com.webcalendar.domain.EventReminder;
import com.webcalendar.exception.DateTimeFormatException;
import com.webcalendar.exception.OrderOfArgumentsException;
import com.webcalendar.service.CalendarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import static com.webcalendar.domain.EventReminder.EnumReminder.*;

/**
 * Provides handling requests from client for reminder about events.
 * Type of the notification: popup with audio.
 *
 *
 * @author Ruslan Borisov
 */
@Controller
public class ReminderController {

    private CalendarService calendarService;

    @Inject
    public ReminderController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }
    public ReminderController() {
    }

    /**
     * Handling request from client for reminder about events.
     * Ajax request.
     * This request from client sends every 1 minutes.
     *
     * @return view with displayed events for remind
     * or empty view if there are no events for remind for current minute
     * @throws DateTimeFormatException if an error occurred during parsing or format string/date
     * @throws OrderOfArgumentsException if start date is after end date
     */
    @RequestMapping(value = "/reminderRequest", method = RequestMethod.POST)
    public String reminderRequest(ModelMap model) throws DateTimeFormatException, OrderOfArgumentsException {

        List<EventAdapter> eventList = calendarService.searchIntoPeriod(LocalDate.now(), LocalDate.now().plusDays(1));
        List<EventAdapter> eventListForRimind = new ArrayList<>();

        for (EventAdapter event : eventList) {
            if (event.getEventReminders().contains(new EventReminder(1, POPUP))) {

                LocalDateTime reminderDateTime;
                if (event.isAllDay())
                    reminderDateTime = LocalDateTime.of(event.getStartDate(), LocalTime.of(0, 0))
                            .minusMinutes(event.getMinutesBeforeReminder());
                else
                    reminderDateTime = LocalDateTime.of(event.getStartDate(), event.getStartTime())
                            .minusMinutes(event.getMinutesBeforeReminder());

                if (reminderDateTime.getHour() == LocalTime.now().getHour() &&
                        reminderDateTime.getMinute() == LocalTime.now().getMinute())
                    eventListForRimind.add(event);
            }
        }

        model.addAttribute("events", eventListForRimind);
        return "includes/remind";
    }
}
