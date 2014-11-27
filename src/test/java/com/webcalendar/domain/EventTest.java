package com.webcalendar.domain;

import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import static com.webcalendar.domain.EventReminder.EnumReminder.POPUP;
import static com.webcalendar.domain.EventRepeater.EnumRepeater.*;
import static com.webcalendar.domain.UserRole.EnumRole.ROLE_USER;
import static org.junit.Assert.*;

public class EventTest {

    private EventAttender attender = new EventAttender (UUID.randomUUID(), "Denis", "Milyaev", "denis@ukr.net");

    private EventRepeater repeaterOnce = new EventRepeater(1, ONCE);
    private EventRepeater repeaterDaily = new EventRepeater(2, DAILY);
    private EventRepeater repeaterMonthly = new EventRepeater(3, MONTHLY);
    private EventRepeater repeaterYearly = new EventRepeater(4, YEARLY);
    private EventRepeater repeaterDayOfWeek = new EventRepeater(6, TUESDAY);

    private EventReminder reminder = new EventReminder(1, POPUP);
    private UserRole role = new UserRole(1, ROLE_USER);

    private Set<EventAttender> attenderSet = new HashSet<>();
    private Set<EventReminder> reminderSet = new HashSet<>();

    private Set<UserRole> userRoles = new HashSet<>();
    private User user = null;


    @Before
    public void setUp() {

        attenderSet.add(attender);
        reminderSet.add(reminder);
        userRoles.add(role);
        user = new User(UUID.randomUUID(), "username", "passw", "bor@ukr.net", true, LocalDate.now(), userRoles);
    }

    @Test
    public void testGetRigthDateForOnceEvent() {

        Set<EventRepeater> repeaterSet = new HashSet<>();
        repeaterSet.add(repeaterOnce);
        Event eventOnce = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("GoodDay")
                .description("Very GoodDay")
                .allDay(false)
                .startDate(LocalDate.of(2020, Month.OCTOBER, 6))
                .startTime(LocalTime.of(12, 0))
                .endDate(LocalDate.of(2020, Month.OCTOBER, 20))
                .endTime(LocalTime.of(15,0))
                .minutesBeforeReminder(30)
                .numberOfOccurrence(null)
                .color("#B8B9FF")
                .attendersOfEvent(attenderSet)
                .repeatersOfEvent(repeaterSet)
                .remindersOfEvent(reminderSet)
                .user(user)
                .build();

        LocalDate expectedResult = LocalDate.of(2020, Month.OCTOBER, 6);

        EventAdapter eventAdapter = eventOnce.getRigthDateForRepeatEvent(LocalDate.of(2020, Month.OCTOBER, 15));
        assertEquals(expectedResult, eventAdapter.getStartDate());
    }

    @Test
    public void testGetRigthDateForDailyEvent() {

        Set<EventRepeater> repeaterSetDaily = new HashSet<>();
        repeaterSetDaily.add(repeaterDaily);
        Event eventDaily = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("GoodDay")
                .description("Very GoodDay")
                .allDay(false)
                .startDate(LocalDate.of(2020, Month.OCTOBER, 4))
                .startTime(LocalTime.of(12, 0))
                .endDate(LocalDate.of(2020, Month.OCTOBER, 4))
                .endTime(LocalTime.of(15,0))
                .minutesBeforeReminder(30)
                .numberOfOccurrence(null)
                .color("#B8B9FF")
                .attendersOfEvent(attenderSet)
                .repeatersOfEvent(repeaterSetDaily)
                .remindersOfEvent(reminderSet)
                .user(user)
                .build();

        LocalDate expectedResult = LocalDate.of(2020, Month.OCTOBER, 6);

        EventAdapter eventAdapter = eventDaily.getRigthDateForRepeatEvent(LocalDate.of(2020, Month.OCTOBER, 6));
        assertEquals(expectedResult, eventAdapter.getStartDate());
    }

    @Test
    public void testGetRigthDateForDayOfWeekEvent() {

        Set<EventRepeater> repeaterSet = new HashSet<>();
        repeaterSet.add(repeaterDayOfWeek);
        Event eventDaily = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("GoodDay")
                .description("Very GoodDay")
                .allDay(false)
                .startDate(LocalDate.of(2020, Month.SEPTEMBER, 22))
                .startTime(LocalTime.of(12, 0))
                .endDate(LocalDate.of(2020, Month.SEPTEMBER, 22))
                .endTime(LocalTime.of(15,0))
                .minutesBeforeReminder(30)
                .numberOfOccurrence(null)
                .color("#B8B9FF")
                .attendersOfEvent(attenderSet)
                .repeatersOfEvent(repeaterSet)
                .remindersOfEvent(reminderSet)
                .user(user)
                .build();
        LocalDate expectedResult = LocalDate.of(2022, Month.OCTOBER, 6);

        EventAdapter eventAdapter = eventDaily.getRigthDateForRepeatEvent(LocalDate.of(2022, Month.OCTOBER, 6));
        assertEquals(expectedResult, eventAdapter.getStartDate());
    }

    @Test
    public void testGetRigthDateForMonthlyEvent() {

        Set<EventRepeater> repeaterSet = new HashSet<>();
        repeaterSet.add(repeaterMonthly);
        Event eventDaily = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("GoodDay")
                .description("Very GoodDay")
                .allDay(false)
                .startDate(LocalDate.of(2020, Month.SEPTEMBER, 4))
                .startTime(LocalTime.of(12, 0))
                .endDate(LocalDate.of(2020, Month.OCTOBER, 4))
                .endTime(LocalTime.of(15,0))
                .minutesBeforeReminder(30)
                .numberOfOccurrence(null)
                .color("#B8B9FF")
                .attendersOfEvent(attenderSet)
                .repeatersOfEvent(repeaterSet)
                .remindersOfEvent(reminderSet)
                .user(user)
                .build();
        LocalDate expectedResult = LocalDate.of(2020, Month.DECEMBER, 4);

        EventAdapter eventAdapter = eventDaily.getRigthDateForRepeatEvent(LocalDate.of(2020, Month.DECEMBER, 20));
        assertEquals(expectedResult, eventAdapter.getStartDate());

    }

    @Test
    public void testGetRigthDateForYearlyEvent() {

        Set<EventRepeater> repeaterSet = new HashSet<>();
        repeaterSet.add(repeaterYearly);
        Event eventDaily = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("GoodDay")
                .description("Very GoodDay")
                .allDay(false)
                .startDate(LocalDate.of(2020, Month.DECEMBER, 25))
                .startTime(LocalTime.of(12, 0))
                .endDate(LocalDate.of(2021, Month.JANUARY, 15))
                .endTime(LocalTime.of(15,0))
                .minutesBeforeReminder(30)
                .numberOfOccurrence(null)
                .color("#B8B9FF")
                .attendersOfEvent(attenderSet)
                .repeatersOfEvent(repeaterSet)
                .remindersOfEvent(reminderSet)
                .user(user)
                .build();
        LocalDate expectedResult = LocalDate.of(2020, Month.DECEMBER, 25);

        EventAdapter eventAdapter = eventDaily.getRigthDateForRepeatEvent(LocalDate.of(2021, Month.JANUARY, 10));
        assertEquals(expectedResult, eventAdapter.getStartDate());
    }
}
