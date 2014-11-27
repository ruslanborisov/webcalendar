package com.webcalendar.notifier;

import com.webcalendar.domain.Event;
import com.webcalendar.domain.EventAdapter;
import com.webcalendar.domain.EventReminder;
import com.webcalendar.domain.EventRepeater;
import com.webcalendar.exception.DateTimeFormatException;
import com.webcalendar.service.CalendarService;
import com.webcalendar.service.EmailService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.webcalendar.domain.EventReminder.EnumReminder.EMAIL;

/**
 * Provides ability to send notification about event.
 * Use {@link Executors#newSingleThreadScheduledExecutor} for create thread which executes
 * every 1 minute and send necessary notifications.
 *
 *
 * @author Ruslan Borisov
 */
public class Notifier implements Runnable{

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private CalendarService calendarService;
    private EmailService emailService;

    public Notifier(CalendarService calendarService, EmailService emailService) {
        this.calendarService = calendarService;
        this.emailService = emailService;
    }
    public Notifier() {
    }

    public void init () {
        scheduler.scheduleWithFixedDelay(this, 0, 1, TimeUnit.MINUTES);
    }

    public void shutdown () {
        scheduler.shutdownNow();
    }

    @Override
    public void run() {

        try {
            List<EventAdapter> preparedList = prepareEventListForRemind(calendarService.searchAllEventsForRemindFromDB());
            for (EventAdapter eventAdapter : preparedList) {

                sendNotification(eventAdapter);

            }
        } catch (DateTimeFormatException e) {
            e.printStackTrace();
        }
    }

    private List<EventAdapter> prepareEventListForRemind(List<Event> eventList) {

        List<Event> eventListForRemind = new ArrayList<>();
        eventList.stream()
            .filter(event -> event.getEventReminders().contains(new EventReminder(2, EMAIL)))
            .forEach(event -> {
                for (EventRepeater repeater : event.getEventRepeaters()) {
                    switch (repeater.getRepeater()) {
                        case ONCE:
                            if (LocalDate.now().isEqual(event.getStartDate()))
                                eventListForRemind.add(event);
                            break;
                        case DAILY:
                            if (event.getStartDate().isBefore(LocalDate.now()) ||
                                    event.getStartDate().isEqual(LocalDate.now())) {
                                if (event.getNumberOfOccurrence() == null ||
                                        LocalDate.now().isBefore(event.getStartDate().plusDays(event.getNumberOfOccurrence())))
                                    eventListForRemind.add(event);
                            }
                            break;
                        case MONTHLY:
                            if (event.getStartDate().isBefore(LocalDate.now()) ||
                                    event.getStartDate().isEqual(LocalDate.now())) {
                                if (event.getNumberOfOccurrence() == null ||
                                        LocalDate.now().isBefore(event.getStartDate().plusMonths(event.getNumberOfOccurrence()))) {
                                    eventListForRemind.add(event);
                                }
                            }
                            break;
                        case YEARLY:
                            if (event.getStartDate().isBefore(LocalDate.now()) ||
                                    event.getStartDate().isEqual(LocalDate.now())) {
                                if (event.getNumberOfOccurrence() == null ||
                                        LocalDate.now().isBefore(event.getStartDate().plusYears(event.getNumberOfOccurrence()))) {
                                    eventListForRemind.add(event);
                                }
                            }
                            break;
                        case MONDAY:
                        case TUESDAY:
                        case WEDNESDAY:
                        case THURSDAY:
                        case FRIDAY:
                        case SATURDAY:
                        case SUNDAY:
                            if (event.getStartDate().isBefore(LocalDate.now()) ||
                                    event.getStartDate().isEqual(LocalDate.now())) {
                                if (event.getNumberOfOccurrence() == null ||
                                        LocalDate.now().isBefore(event.getStartDate().plusDays(event.getNumberOfOccurrence() * 7))) {
                                    eventListForRemind.add(event);
                                }
                            }
                            break;
                    }
                }
            });

        return eventListForRemind.stream()
                    .map(event -> event.getRigthDateForRepeatEvent(LocalDate.now()))
                    .collect(Collectors.toList());
    }

    private void sendNotification(EventAdapter eventAdapter) throws DateTimeFormatException {

        LocalDateTime reminderDateTime;
        if (eventAdapter.isAllDay())
            reminderDateTime = LocalDateTime.of(eventAdapter.getStartDate(), LocalTime.of(0, 0))
                    .minusMinutes(eventAdapter.getMinutesBeforeReminder());
        else
            reminderDateTime = LocalDateTime.of(eventAdapter.getStartDate(), eventAdapter.getStartTime())
                    .minusMinutes(eventAdapter.getMinutesBeforeReminder());


        if (reminderDateTime.getHour() == LocalTime.now().getHour() &&
                reminderDateTime.getMinute() == LocalTime.now().getMinute())

            emailService.sendEmail("Alert!", eventAdapter.getTextAboutEvent(), eventAdapter.getUser().getEmail());
    }
}
