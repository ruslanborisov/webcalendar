package com.webcalendar.service;

import com.webcalendar.dao.EventDAO;
import com.webcalendar.datastore.DataStore;
import com.webcalendar.domain.*;
import com.webcalendar.exception.DateTimeFormatException;
import com.webcalendar.exception.OrderOfArgumentsException;
import com.webcalendar.exception.ValidationException;
import javassist.NotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import static com.webcalendar.domain.EventReminder.EnumReminder.POPUP;
import static com.webcalendar.domain.EventRepeater.EnumRepeater.*;
import static com.webcalendar.domain.UserRole.EnumRole.ROLE_USER;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
public class CalendarServiceImplTest {

    private EventAttender attender = new EventAttender (UUID.randomUUID(), null, null, "denis@ukr.net");
    private EventRepeater repeater = new EventRepeater(1, ONCE);
    private EventReminder reminder = new EventReminder(1, POPUP);
    private UserRole role = new UserRole(1, ROLE_USER);

    private Set<EventAttender> attenderSet = new HashSet<>();
    private Set<EventReminder> reminderSet = new HashSet<>();
    private Set<EventRepeater> repeaterSet = new HashSet<>();

    private Set<UserRole> userRoles = new HashSet<>();
    private Event event;
    private EventAdapter eventAdapter;
    private List<Event> eventList = new ArrayList<>();
    private List<EventAdapter> eventListAdapter = new ArrayList<>();

    private EventDAO eventDAOMock;
    private DataStore dataStoreMock;
    private CalendarService calendarService;
    private User user;

    @Before
    public void setUp() {
        attenderSet.add(attender);
        reminderSet.add(reminder);
        repeaterSet.add(repeater);
        userRoles.add(role);
        user = new User(UUID.randomUUID(), "username", "passw", "bor@ukr.net", true, LocalDate.now(), userRoles);

        repeaterSet.add(repeater);
        event = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("GoodDay")
                .description("Very GoodDay")
                .allDay(false)
                .startDate(LocalDate.of(2020, Month.OCTOBER, 6))
                .startTime(LocalTime.of(12, 0))
                .endDate(LocalDate.of(2020, Month.OCTOBER, 8))
                .endTime(LocalTime.of(15,0))
                .minutesBeforeReminder(30)
                .numberOfOccurrence(null)
                .color("#B8B9FF")
                .attendersOfEvent(attenderSet)
                .repeatersOfEvent(repeaterSet)
                .remindersOfEvent(reminderSet)
                .user(user)
                .build();

        eventAdapter = new EventAdapter(event);

        eventList.add(event);
        eventListAdapter.add(eventAdapter);

        eventDAOMock = mock(EventDAO.class);
        dataStoreMock = mock(DataStore.class);
        calendarService = new CalendarServiceImpl(dataStoreMock, eventDAOMock);
    }

    @Test
    public void testAdd() throws ValidationException {

        boolean actualResult = calendarService.add(event);
        assertTrue(actualResult);
        verify(dataStoreMock, times(1)).publish(event);
    }

    @Test
    public void testRemove() throws IOException, SQLException, NotFoundException {

        when(dataStoreMock.remove(event.getId())).thenReturn(event);
        Event actualResult = calendarService.remove(event.getId());
        assertEquals(event, actualResult);
        verify(dataStoreMock, times(1)).remove(event.getId());
    }

    @Test
    public void testCreateEvent() throws ValidationException, DateTimeFormatException {

        JSONArray jsonArrayAttenders = new JSONArray();
        jsonArrayAttenders.put("denis@ukr.net");
        JSONArray jsonArrayRepeaters = new JSONArray();
        jsonArrayRepeaters.put("once");
        JSONArray jsonArrayReminders = new JSONArray();
        jsonArrayReminders.put("popup");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "GoodDay");
        jsonObject.put("description", "Very GoodDay");
        jsonObject.put("allDay", false);
        jsonObject.put("color", "#B8B9FF");
        jsonObject.put("startDate", "06-10-2020");
        jsonObject.put("endDate", "08-10-2020");
        jsonObject.put("startTime", "12:00");
        jsonObject.put("endTime", "15:00");
        jsonObject.put("reminderTime", "30");
        jsonObject.put("numberOfOccurrence", "null");
        jsonObject.put("eventAttenders", jsonArrayAttenders);
        jsonObject.put("eventRepeaters", jsonArrayRepeaters);
        jsonObject.put("eventReminders", jsonArrayReminders);

        Event actualResult = calendarService.createEvent(jsonObject.toString(), user);

        assertEquals(event, actualResult);
        verify(dataStoreMock, times(1)).publish(event);
    }

    @Test
    public void testSearchById() {

        when(dataStoreMock.getEventById(event.getId())).thenReturn(event);
        Event actualResult = calendarService.searchById(event.getId());
        assertEquals(event, actualResult);
        verify(dataStoreMock, times(1)).getEventById(event.getId());
    }

    @Test
    public void testSearchByIdNotExistsEvent() {

        when(dataStoreMock.getEventById(event.getId())).thenReturn(null);
        Event actualResult = calendarService.searchById(event.getId());
        assertNull(actualResult);
        verify(dataStoreMock, times(1)).getEventById(event.getId());
    }

    @Test
    public void testSearchByTitle() {

        when(dataStoreMock.getEventByTitle(event.getTitle())).thenReturn(eventList);
        List<Event> actualResult = calendarService.searchByTitle(event.getTitle());
        assertEquals(eventList, actualResult);
        verify(dataStoreMock, times(1)).getEventByTitle(event.getTitle());
    }

    @Test
    public void testSearchByTitleNotExistsEvent() {

        List<Event> expectedResult = new ArrayList<>();
        when(dataStoreMock.getEventByTitle(event.getTitle())).thenReturn(expectedResult);
        List<Event> actualResult = calendarService.searchByTitle(event.getTitle());
        assertEquals(expectedResult, actualResult);
        verify(dataStoreMock, times(1)).getEventByTitle(event.getTitle());
    }

    @Test
    public void testSearchByTitleStartWith() {

        when(dataStoreMock.getEventByTitleStartWith("Goo")).thenReturn(eventList);
        List<Event> actualResult = calendarService.searchEventByTitleStartWith("Goo");
        assertEquals(eventList, actualResult);
        verify(dataStoreMock, times(1)).getEventByTitleStartWith("Goo");
    }

    @Test
    public void testSearchByTitleStartWithNotExistsEvent() {

        List<Event> expectedResult = new ArrayList<>();
        when(dataStoreMock.getEventByTitleStartWith("Mee")).thenReturn(expectedResult);
        List<Event> actualResult = calendarService.searchEventByTitleStartWith("Mee");
        assertEquals(expectedResult, actualResult);
        verify(dataStoreMock, times(1)).getEventByTitleStartWith("Mee");
    }

    @Test
    public void testSearchByAttender() {

        when(dataStoreMock.getEventByAttender("denis@ukr.net")).thenReturn(eventList);
        List<Event> actualResult = calendarService.searchByAttender("denis@ukr.net");
        assertEquals(eventList, actualResult);
        verify(dataStoreMock, times(1)).getEventByAttender("denis@ukr.net");
    }

    @Test
    public void testSearchByAttenderNotExistsEvent() {

        List<Event> expectedResult = new ArrayList<>();
        when(dataStoreMock.getEventByAttender("alexandr@ukr.net")).thenReturn(expectedResult);
        List<Event> actualResult = calendarService.searchByAttender("alexandr@ukr.net");
        assertEquals(expectedResult, actualResult);
        verify(dataStoreMock, times(1)).getEventByAttender("alexandr@ukr.net");
    }

    @Test
    public void testSearchByDate() {

        LocalDate date = LocalDate.of(2020, Month.OCTOBER, 6);
        when(dataStoreMock.getEventByDate(date)).thenReturn(eventList);
        List<EventAdapter> actualResult = calendarService.searchByDate(date);
        assertEquals(eventListAdapter, actualResult);
        verify(dataStoreMock, times(1)).getEventByDate(date);
    }

    @Test
    public void testSearchByDateNotExistsEvent() {

        LocalDate date = LocalDate.of(2020, Month.OCTOBER, 6);
        List<EventAdapter> expectedResult = new ArrayList<>();
        when(dataStoreMock.getEventByDate(date)).thenReturn(new ArrayList<>());
        List<EventAdapter> actualResult = calendarService.searchByDate(date);
        assertEquals(expectedResult, actualResult);
        verify(dataStoreMock, times(1)).getEventByDate(date);
    }

    @Test
    public void testSearchIntoPeriod() throws OrderOfArgumentsException {

        LocalDate firstDate = LocalDate.of(2020, Month.OCTOBER, 6);
        LocalDate endDate = LocalDate.of(2020, Month.OCTOBER, 8);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6))).thenReturn(eventList);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 7))).thenReturn(eventList);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 8))).thenReturn(eventList);

        List<EventAdapter> actualResult = calendarService.searchIntoPeriod(firstDate, endDate);
        assertEquals(eventListAdapter, actualResult);

        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6));
        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 7));
        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 8));
    }

    @Test
    public void testSearchIntoPeriodNotExistsEvent() throws OrderOfArgumentsException {


        List<EventAdapter> expectedResult = new ArrayList<>();
        LocalDate firstDate = LocalDate.of(2020, Month.OCTOBER, 6);
        LocalDate endDate = LocalDate.of(2020, Month.OCTOBER, 8);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6))).thenReturn(new ArrayList<>());
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 7))).thenReturn(new ArrayList<>());
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 8))).thenReturn(new ArrayList<>());

        List<EventAdapter> actualResult = calendarService.searchIntoPeriod(firstDate, endDate);
        assertEquals(expectedResult, actualResult);

        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6));
        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 7));
        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 8));
    }

    @Test
    public void testSearchByAttenderIntoPeriod() throws OrderOfArgumentsException, DateTimeFormatException {

        LocalDate firstDate = LocalDate.of(2020, Month.OCTOBER, 6);
        LocalDate endDate = LocalDate.of(2020, Month.OCTOBER, 8);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6))).thenReturn(eventList);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 7))).thenReturn(eventList);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 8))).thenReturn(eventList);
        when(dataStoreMock.getEventByAttender("denis@ukr.net")).thenReturn(eventList);

        List<EventAdapter> actualResult = calendarService.searchByAttenderIntoPeriod("denis@ukr.net", firstDate, endDate);
        assertEquals(eventListAdapter, actualResult);

        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6));
        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 7));
        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 8));
        verify(dataStoreMock, times(1)).getEventByAttender("denis@ukr.net");
    }

    @Test
    public void testSearchByAttenderIntoPeriodNotExistsEvent() throws OrderOfArgumentsException, DateTimeFormatException {

        List<EventAdapter> expectedResult = new ArrayList<>();
        LocalDate firstDate = LocalDate.of(2020, Month.OCTOBER, 6);
        LocalDate endDate = LocalDate.of(2020, Month.OCTOBER, 8);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6))).thenReturn(new ArrayList<>());
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 7))).thenReturn(new ArrayList<>());
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 8))).thenReturn(new ArrayList<>());
        when(dataStoreMock.getEventByAttender("denis@ukr.net")).thenReturn(new ArrayList<>());

        List<EventAdapter> actualResult = calendarService.searchByAttenderIntoPeriod("denis@ukr.net", firstDate, endDate);
        assertEquals(expectedResult, actualResult);

        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6));
        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 7));
        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 8));
        verify(dataStoreMock, times(1)).getEventByAttender("denis@ukr.net");
    }

    @Test
    public void testSearchFreeTime() throws OrderOfArgumentsException, DateTimeFormatException {

        List<List<LocalDateTime>> expectedResult = new ArrayList<>();
        LocalDateTime left_interval_1 = LocalDateTime.of(2020, Month.OCTOBER, 6, 10, 0);
        LocalDateTime rigth_interval_1 = LocalDateTime.of(2020, Month.OCTOBER, 6, 12, 0);

        LocalDateTime left_interval_2 = LocalDateTime.of(2020, Month.OCTOBER, 8, 15, 0);
        LocalDateTime rigth_interval_2 = LocalDateTime.of(2020, Month.OCTOBER, 9, 18, 0);

        List<LocalDateTime> interval_1 = new ArrayList<>();
        List<LocalDateTime> interval_2 = new ArrayList<>();

        interval_1.add(left_interval_1);
        interval_1.add(rigth_interval_1);

        interval_2.add(left_interval_2);
        interval_2.add(rigth_interval_2);

        expectedResult.add(interval_1);
        expectedResult.add(interval_2);

        LocalDateTime firstDateTime = LocalDateTime.of(2020, Month.OCTOBER, 6, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2020, Month.OCTOBER, 9, 18, 0);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6))).thenReturn(eventList);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 7))).thenReturn(eventList);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 8))).thenReturn(eventList);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 9))).thenReturn(eventList);

        List<List<LocalDateTime>> actualResult = calendarService.searchFreeTime(firstDateTime, endDateTime);
        assertEquals(expectedResult, actualResult);

        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6));
        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 7));
        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 8));
        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 9));
    }

    @Test
    public void testSearchFreeTimeNoFree() throws OrderOfArgumentsException, DateTimeFormatException {

        List<List<LocalDateTime>> expectedResult = new ArrayList<>();

        LocalDateTime firstDateTime = LocalDateTime.of(2020, Month.OCTOBER, 6, 12, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2020, Month.OCTOBER, 6, 15, 0);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6))).thenReturn(eventList);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 7))).thenReturn(eventList);

        List<List<LocalDateTime>> actualResult = calendarService.searchFreeTime(firstDateTime, endDateTime);
        assertEquals(expectedResult, actualResult);

        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6));
    }

    @Test
    public void testIsAttenderFree() throws OrderOfArgumentsException, DateTimeFormatException {

        LocalDateTime firstDateTime = LocalDateTime.of(2020, Month.OCTOBER, 6, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2020, Month.OCTOBER, 7, 18, 0);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6))).thenReturn(eventList);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 7))).thenReturn(eventList);
        when(dataStoreMock.getEventByAttender("denis@ukr.net")).thenReturn(eventList);

        List<EventAdapter> actualResult = calendarService.isAttenderFree("denis@ukr.net", firstDateTime, endDateTime);
        assertEquals(eventListAdapter, actualResult);

        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6));
        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 7));
        verify(dataStoreMock, times(1)).getEventByAttender("denis@ukr.net");
    }

    @Test
    public void testIsAttenderFreeNoFree() throws OrderOfArgumentsException, DateTimeFormatException {

        List<EventAdapter> expectedResult = new ArrayList<>();

        LocalDateTime firstDateTime = LocalDateTime.of(2020, Month.OCTOBER, 6, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2020, Month.OCTOBER, 7, 18, 0);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6))).thenReturn(eventList);
        when(dataStoreMock.getEventByDate(LocalDate.of(2020, Month.OCTOBER, 7))).thenReturn(eventList);
        when(dataStoreMock.getEventByAttender("alexandr@ukr.net")).thenReturn(new ArrayList<>());

        List<EventAdapter> actualResult = calendarService.isAttenderFree("denis@ukr.net", firstDateTime, endDateTime);
        assertEquals(expectedResult, actualResult);

        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 6));
        verify(dataStoreMock, times(1)).getEventByDate(LocalDate.of(2020, Month.OCTOBER, 7));
        verify(dataStoreMock, times(1)).getEventByAttender("denis@ukr.net");
    }
}
