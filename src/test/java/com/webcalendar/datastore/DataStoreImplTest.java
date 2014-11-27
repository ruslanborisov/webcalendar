package com.webcalendar.datastore;

import com.webcalendar.dao.EventDAO;
import com.webcalendar.domain.*;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import static com.webcalendar.domain.EventReminder.EnumReminder.POPUP;
import static com.webcalendar.domain.EventRepeater.EnumRepeater.*;
import static com.webcalendar.domain.UserRole.EnumRole.ROLE_USER;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataStoreImplTest {

    private EventAttender attender = new EventAttender (null, "Denis", "Milyaev", "denis@ukr.net");

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
    private Event eventOnce = null;
    private User user = null;

    private EventDAO eventDAOMock;
    private DataStore dataStore;

    @Before
    public void setUp() {
        eventDAOMock = mock(EventDAO.class);
        dataStore = new DataStoreImpl(eventDAOMock);
        attenderSet.add(attender);
        reminderSet.add(reminder);
        userRoles.add(role);
        user = new User(UUID.randomUUID(), "username", "passw", "bor@ukr.net", true, LocalDate.now(), userRoles);

        Set<EventRepeater> repeaterSet = new HashSet<>();
        repeaterSet.add(repeaterOnce);
        eventOnce = new Event.EventBuilder()
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
    }

    @Test
    public void testPublish() {

        dataStore.publish(eventOnce);
        Event actualResult = dataStore.getEventById(eventOnce.getId());
        assertEquals(eventOnce, actualResult);
        verify(eventDAOMock, times(1)).addEvent(eventOnce);
    }

    @Test
    public void testRemove() {

        dataStore.publish(eventOnce);
        Event actualResult = dataStore.remove(eventOnce.getId());
        assertEquals(eventOnce, actualResult);
        verify(eventDAOMock, times(1)).addEvent(eventOnce);
    }

    @Test
    public void testRemoveNotExistsEvent() {

        DataStore dataStore = new DataStoreImpl(eventDAOMock);

        Event actualResult = dataStore.remove(UUID.randomUUID());
        assertNull(actualResult);
    }

    @Test
    public void testGetEventByTitle() {

        dataStore.publish(eventOnce);

        List<Event> expectedResult = new ArrayList<>();
        expectedResult.add(eventOnce);

        List<Event> actualResult = dataStore.getEventByTitle("GoodDay");
        assertEquals(expectedResult, actualResult);
        verify(eventDAOMock, times(1)).addEvent(eventOnce);
    }

    @Test
    public void testGetEventByTitleNotExistsEvent() {
        DataStore dataStore = new DataStoreImpl(eventDAOMock);

        List<Event> expectedResult = new ArrayList<>();
        List<Event> actualResult = dataStore.getEventByTitle("GoodDay");

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetEventByTitleStartWith() {

        dataStore.publish(eventOnce);

        List<Event> expectedResult = new ArrayList<>();
        expectedResult.add(eventOnce);

        List<Event> actualResult = dataStore.getEventByTitleStartWith("Goo");
        assertEquals(expectedResult, actualResult);
        verify(eventDAOMock, times(1)).addEvent(eventOnce);
    }

    @Test
    public void testGetEventByTitleStartWithNotExistsEvent() {
        DataStore dataStore = new DataStoreImpl(eventDAOMock);

        List<Event> expectedResult = new ArrayList<>();
        List<Event> actualResult = dataStore.getEventByTitle("Goo");

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetEventByAttender() {

        dataStore.publish(eventOnce);

        List<Event> expectedResult = new ArrayList<>();
        expectedResult.add(eventOnce);

        List<Event> actualResult = dataStore.getEventByAttender("denis@ukr.net");
        assertEquals(expectedResult, actualResult);
        verify(eventDAOMock, times(1)).addEvent(eventOnce);
    }

    @Test
    public void testGetEventByAttenderWithNotExistsEvent() {
        DataStore dataStore = new DataStoreImpl(eventDAOMock);

        List<Event> expectedResult = new ArrayList<>();
        List<Event> actualResult = dataStore.getEventByAttender("denis@ukr.net");

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetEventByDate() {

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

        Set<EventRepeater> repeaterSetMonthly = new HashSet<>();
        repeaterSetMonthly.add(repeaterMonthly);
        Event eventMonthly = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("GoodDay")
                .description("Very GoodDay")
                .allDay(false)
                .startDate(LocalDate.of(2020, Month.SEPTEMBER, 6))
                .startTime(LocalTime.of(12, 0))
                .endDate(LocalDate.of(2020, Month.SEPTEMBER, 6))
                .endTime(LocalTime.of(15,0))
                .minutesBeforeReminder(30)
                .numberOfOccurrence(null)
                .color("#B8B9FF")
                .attendersOfEvent(attenderSet)
                .repeatersOfEvent(repeaterSetMonthly)
                .remindersOfEvent(reminderSet)
                .user(user)
                .build();

        Set<EventRepeater> repeaterSetYearly = new HashSet<>();
        repeaterSetYearly.add(repeaterYearly);
        Event eventYearly = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("GoodDay")
                .description("Very GoodDay")
                .allDay(false)
                .startDate(LocalDate.of(2019, Month.OCTOBER, 6))
                .startTime(LocalTime.of(12, 0))
                .endDate(LocalDate.of(2019, Month.OCTOBER, 6))
                .endTime(LocalTime.of(15,0))
                .minutesBeforeReminder(30)
                .numberOfOccurrence(null)
                .color("#B8B9FF")
                .attendersOfEvent(attenderSet)
                .repeatersOfEvent(repeaterSetYearly)
                .remindersOfEvent(reminderSet)
                .user(user)
                .build();

        Set<EventRepeater> repeaterSetDayOfWeek = new HashSet<>();
        repeaterSetDayOfWeek.add(repeaterDayOfWeek);
        Event eventDayOfWeek = new Event.EventBuilder()
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
                .repeatersOfEvent(repeaterSetDayOfWeek)
                .remindersOfEvent(reminderSet)
                .user(user)
                .build();

        dataStore.publish(eventOnce);
        dataStore.publish(eventDaily);
        dataStore.publish(eventMonthly);
        dataStore.publish(eventYearly);
        dataStore.publish(eventDayOfWeek);

        List<Event> expectedResult = new ArrayList<>();
        expectedResult.add(eventOnce);
        expectedResult.add(eventDaily);
        expectedResult.add(eventMonthly);
        expectedResult.add(eventYearly);
        expectedResult.add(eventDayOfWeek);

        List<Event> actualResult = dataStore.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6));
        assertEquals(expectedResult, actualResult);
        verify(eventDAOMock, times(1)).addEvent(eventOnce);
        verify(eventDAOMock, times(1)).addEvent(eventDaily);
        verify(eventDAOMock, times(1)).addEvent(eventMonthly);
        verify(eventDAOMock, times(1)).addEvent(eventYearly);
        verify(eventDAOMock, times(1)).addEvent(eventDayOfWeek);
    }
}
