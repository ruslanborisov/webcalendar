package com.webcalendar.util;

import com.webcalendar.domain.*;
import org.junit.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import static com.webcalendar.domain.EventReminder.EnumReminder.POPUP;
import static com.webcalendar.domain.EventRepeater.EnumRepeater.ONCE;
import static com.webcalendar.domain.UserRole.EnumRole.ROLE_USER;
import static org.junit.Assert.*;

public class AdapterToOriginTest {

    @Test
    public void testEventAdapterToEvent() {

        EventAttender attender = new EventAttender (null, "Denis", "Milyaev", "denis@ukr.net");
        EventRepeater repeater = new EventRepeater(1, ONCE);
        EventReminder reminder = new EventReminder(1, POPUP);
        UserRole role = new UserRole(1, ROLE_USER);
        Set<EventAttender> attenderSet = new HashSet<>();
        Set<EventRepeater> repeaterSet = new HashSet<>();
        Set<EventReminder> reminderSet = new HashSet<>();
        Set<UserRole> userRoles = new HashSet<>();
        attenderSet.add(attender);
        repeaterSet.add(repeater);
        reminderSet.add(reminder);
        userRoles.add(role);
        User user = new User(UUID.randomUUID(), "username", "passw", "bor@ukr.net", true, LocalDate.now(), userRoles);

        Event event = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("GoodDay")
                .description("Very GoodDay")
                .allDay(false)
                .startDate(LocalDate.of(2020, Month.OCTOBER, 6))
                .startTime(LocalTime.of(12, 0))
                .endDate(LocalDate.of(2020, Month.OCTOBER, 6))
                .endTime(LocalTime.of(15,0))
                .minutesBeforeReminder(30)
                .numberOfOccurrence(null)
                .color("#B8B9FF")
                .attendersOfEvent(attenderSet)
                .repeatersOfEvent(repeaterSet)
                .remindersOfEvent(reminderSet)
                .user(user)
                .build();
        EventAdapter eventAdapter = new EventAdapter(event);

        assertEquals(event, AdapterToOrigin.eventAdapterToEvent(eventAdapter));
    }

    @Test
    public void testEventAdapterListToEventList() {

        EventAttender attender1 = new EventAttender (null, "Denis", "Milyaev", "denis@ukr.net");
        EventRepeater repeater1 = new EventRepeater(1, ONCE);
        EventReminder reminder1 = new EventReminder(1, POPUP);
        UserRole role1 = new UserRole(1, ROLE_USER);
        Set<EventAttender> attenderSet1 = new HashSet<>();
        Set<EventRepeater> repeaterSet1 = new HashSet<>();
        Set<EventReminder> reminderSet1 = new HashSet<>();
        Set<UserRole> userRoles1 = new HashSet<>();
        attenderSet1.add(attender1);
        repeaterSet1.add(repeater1);
        reminderSet1.add(reminder1);
        userRoles1.add(role1);
        User user1 = new User(UUID.randomUUID(), "username", "passw", "bor@ukr.net", true, LocalDate.now(), userRoles1);

        Event event1 = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("GoodDay")
                .description("Very GoodDay")
                .allDay(false)
                .startDate(LocalDate.of(2020, Month.OCTOBER, 6))
                .startTime(LocalTime.of(12, 0))
                .endDate(LocalDate.of(2020, Month.OCTOBER, 6))
                .endTime(LocalTime.of(15,0))
                .minutesBeforeReminder(30)
                .numberOfOccurrence(null)
                .color("#B8B9FF")
                .attendersOfEvent(attenderSet1)
                .repeatersOfEvent(repeaterSet1)
                .remindersOfEvent(reminderSet1)
                .user(user1)
                .build();

        EventAttender attender2 = new EventAttender (null, "Igor", "Derkach", "igor@ukr.net");
        EventRepeater repeater2 = new EventRepeater(1, ONCE);
        EventReminder reminder2 = new EventReminder(1, POPUP);
        UserRole role2 = new UserRole(1, ROLE_USER);
        Set<EventAttender> attenderSet2 = new HashSet<>();
        Set<EventRepeater> repeaterSet2 = new HashSet<>();
        Set<EventReminder> reminderSet2 = new HashSet<>();
        Set<UserRole> userRoles2 = new HashSet<>();
        attenderSet2.add(attender2);
        repeaterSet2.add(repeater2);
        reminderSet2.add(reminder2);
        userRoles2.add(role2);
        User user2 = new User(UUID.randomUUID(), "username", "passw", "bor@ukr.net", true, LocalDate.now(), userRoles2);

        Event event2 = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("GoodDay2")
                .description("Very GoodDay2")
                .allDay(false)
                .startDate(LocalDate.of(2020, Month.OCTOBER, 6))
                .startTime(LocalTime.of(12, 0))
                .endDate(LocalDate.of(2020, Month.OCTOBER, 6))
                .endTime(LocalTime.of(15,0))
                .minutesBeforeReminder(30)
                .numberOfOccurrence(null)
                .color("#B8B9FF")
                .attendersOfEvent(attenderSet2)
                .repeatersOfEvent(repeaterSet2)
                .remindersOfEvent(reminderSet2)
                .user(user2)
                .build();

        List<Event> eventList = new ArrayList<>();
        eventList.add(event1);
        eventList.add(event2);

        EventAdapter eventAdapter1 = new EventAdapter(event1);
        EventAdapter eventAdapter2 = new EventAdapter(event2);
        List<EventAdapter> eventAdapterList = new ArrayList<>();
        eventAdapterList.add(eventAdapter1);
        eventAdapterList.add(eventAdapter2);

        assertEquals(eventList, AdapterToOrigin.eventAdapterListToEventList(eventAdapterList));
    }
}
