package com.webcalendar.util;

import com.webcalendar.domain.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import static com.webcalendar.domain.EventRepeater.EnumRepeater.*;

/**
 * Prepare default events for set to new user
 *
 *
 * @author Ruslan Borisov
 */
public class DefaultEvents {

    public static List<Event> getDefaultEvents(User user) {

// Attenders prepare
        UUID attenderId = UUID.fromString("1a04fddd-acfa-4b5e-bd5c-669414294d05");
        EventAttender eventAttender = new EventAttender(attenderId, null, null, "friend@ukr.net");
        Set<EventAttender> eventAttenderSet = new HashSet<>();
        eventAttenderSet.add(eventAttender);

// Repeaters prepare
        EventRepeater eventRepeaterOnce = new EventRepeater(1, ONCE);
        EventRepeater eventRepeaterDaily = new EventRepeater(2, DAILY);
        EventRepeater eventRepeaterMonthly = new EventRepeater(3, MONTHLY);
        EventRepeater eventRepeaterYearly = new EventRepeater(4, YEARLY);
        EventRepeater eventRepeaterTuesday = new EventRepeater(6, TUESDAY);
        EventRepeater eventRepeaterThursday = new EventRepeater(8, THURSDAY);
        Set<EventRepeater> eventRepeaterSetOnce = new HashSet<>();
        eventRepeaterSetOnce.add(eventRepeaterOnce);
        Set<EventRepeater> eventRepeaterSetDaily = new HashSet<>();
        eventRepeaterSetDaily.add(eventRepeaterDaily);
        Set<EventRepeater> eventRepeaterSetMonthly = new HashSet<>();
        eventRepeaterSetMonthly.add(eventRepeaterMonthly);
        Set<EventRepeater> eventRepeaterSetYearly = new HashSet<>();
        eventRepeaterSetYearly.add(eventRepeaterYearly);
        Set<EventRepeater> eventRepeaterSetDayOfWeek = new HashSet<>();
        eventRepeaterSetDayOfWeek.add(eventRepeaterTuesday);
        eventRepeaterSetDayOfWeek.add(eventRepeaterThursday);

// Once past event
        Event event1 = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Meeting")
                .description("Meeting with friends")
                .allDay(false)
                .startDate(LocalDate.now().minusDays(1))
                .startTime(LocalTime.of(18, 0))
                .endDate(LocalDate.now().minusDays(1))
                .endTime(LocalTime.of(20, 0))
                .minutesBeforeReminder(null)
                .numberOfOccurrence(null)
                .color("#7AE7BF")
                .attendersOfEvent(eventAttenderSet)
                .repeatersOfEvent(eventRepeaterSetOnce)
                .remindersOfEvent(null)
                .user(user)
                .build();
// Once event
        Event event2 = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Meeting")
                .description("Meeting with friends")
                .allDay(false)
                .startDate(LocalDate.now())
                .startTime(LocalTime.of(20, 0))
                .endDate(LocalDate.now())
                .endTime(LocalTime.of(22, 0))
                .minutesBeforeReminder(null)
                .numberOfOccurrence(null)
                .color("#FFB878")
                .attendersOfEvent(eventAttenderSet)
                .repeatersOfEvent(eventRepeaterSetOnce)
                .remindersOfEvent(null)
                .user(user)
                .build();
// Once event
        Event event3 = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Interview")
                .description("Interview in IT company")
                .allDay(false)
                .startDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(15, 0))
                .endDate(LocalDate.now().plusDays(5))
                .endTime(LocalTime.of(16, 0))
                .minutesBeforeReminder(null)
                .numberOfOccurrence(null)
                .color("#7AE7BF")
                .attendersOfEvent(eventAttenderSet)
                .repeatersOfEvent(eventRepeaterSetOnce)
                .remindersOfEvent(null)
                .user(user)
                .build();
// Once "long" event
        Event event4 = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Festival")
                .description("Rock music festival")
                .allDay(false)
                .startDate(LocalDate.now().plusDays(7))
                .startTime(LocalTime.of(18, 0))
                .endDate(LocalDate.now().plusDays(8))
                .endTime(LocalTime.of(20, 0))
                .minutesBeforeReminder(null)
                .numberOfOccurrence(null)
                .color("#7AE7BF")
                .attendersOfEvent(eventAttenderSet)
                .repeatersOfEvent(eventRepeaterSetOnce)
                .remindersOfEvent(null)
                .user(user)
                .build();
// Once "long-allDay" event
        Event event5 = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Course")
                .description("Refresher course")
                .allDay(true)
                .startDate(LocalDate.now().plusDays(10))
                .startTime(null)
                .endDate(LocalDate.now().plusDays(15))
                .endTime(null)
                .minutesBeforeReminder(null)
                .numberOfOccurrence(null)
                .color("#E1E1E1")
                .attendersOfEvent(eventAttenderSet)
                .repeatersOfEvent(eventRepeaterSetOnce)
                .remindersOfEvent(null)
                .user(user)
                .build();
// Daily event
        Event event6 = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Skype conference")
                .description("Skype conference with the customer")
                .allDay(false)
                .startDate(LocalDate.now().minusDays(2))
                .startTime(LocalTime.of(10, 0))
                .endDate(LocalDate.now().minusDays(2))
                .endTime(LocalTime.of(10, 30))
                .minutesBeforeReminder(null)
                .numberOfOccurrence(5)
                .color("#46D6DB")
                .attendersOfEvent(eventAttenderSet)
                .repeatersOfEvent(eventRepeaterSetDaily)
                .remindersOfEvent(null)
                .user(user)
                .build();
// Day of week event
        Event event7 = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Training")
                .description("Training in the gym")
                .allDay(false)
                .startDate(LocalDate.now().minusDays(7))
                .startTime(LocalTime.of(18, 0))
                .endDate(LocalDate.now().minusDays(7))
                .endTime(LocalTime.of(20, 0))
                .minutesBeforeReminder(null)
                .numberOfOccurrence(null)
                .color("#B8B9FF")
                .attendersOfEvent(eventAttenderSet)
                .repeatersOfEvent(eventRepeaterSetDayOfWeek)
                .remindersOfEvent(null)
                .user(user)
                .build();
// Monthly event
        Event event8 = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Meeting")
                .description("Meeting with parents")
                .allDay(false)
                .startDate(LocalDate.of(2014, 12, 1))
                .startTime(LocalTime.of(15, 0))
                .endDate(LocalDate.of(2014, 12, 1))
                .endTime(LocalTime.of(18, 0))
                .minutesBeforeReminder(null)
                .numberOfOccurrence(null)
                .color("#51B749")
                .attendersOfEvent(eventAttenderSet)
                .repeatersOfEvent(eventRepeaterSetMonthly)
                .remindersOfEvent(null)
                .user(user)
                .build();
        Event event9 = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Payment")
                .description("Payment for an apartment")
                .allDay(false)
                .startDate(LocalDate.now().plusDays(3))
                .startTime(LocalTime.of(12, 0))
                .endDate(LocalDate.now().plusDays(3))
                .endTime(LocalTime.of(13, 0))
                .minutesBeforeReminder(null)
                .numberOfOccurrence(null)
                .color("#FBD75B")
                .attendersOfEvent(eventAttenderSet)
                .repeatersOfEvent(eventRepeaterSetMonthly)
                .remindersOfEvent(null)
                .user(user)
                .build();
// Yearly event
        Event event10 = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("New Year")
                .description("Happy New Year")
                .allDay(false)
                .startDate(LocalDate.of(2014, 12, 31))
                .startTime(LocalTime.of(20, 0))
                .endDate(LocalDate.of(2015, 1, 1))
                .endTime(LocalTime.of(12, 0))
                .minutesBeforeReminder(null)
                .numberOfOccurrence(null)
                .color("#DBADFF")
                .attendersOfEvent(eventAttenderSet)
                .repeatersOfEvent(eventRepeaterSetYearly)
                .remindersOfEvent(null)
                .user(user)
                .build();

        List<Event> eventList = new ArrayList<>();
        eventList.add(event1);
        eventList.add(event2);
        eventList.add(event3);
        eventList.add(event4);
        eventList.add(event5);
        eventList.add(event6);
        eventList.add(event7);
        eventList.add(event8);
        eventList.add(event9);
        eventList.add(event10);

        return eventList;
    }
}
