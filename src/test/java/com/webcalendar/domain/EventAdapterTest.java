package com.webcalendar.domain;

import com.webcalendar.exception.DateTimeFormatException;
import org.json.JSONArray;
import org.json.JSONObject;
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
import static org.junit.Assert.assertEquals;

public class EventAdapterTest {

    private EventAttender attender = new EventAttender (UUID.randomUUID(), "Denis", "Milyaev", "denis@ukr.net");
    private EventRepeater repeater = new EventRepeater(1, ONCE);
    private EventReminder reminder = new EventReminder(1, POPUP);
    private UserRole role = new UserRole(1, ROLE_USER);

    private Set<EventAttender> attenderSet = new HashSet<>();
    private Set<EventReminder> reminderSet = new HashSet<>();
    private Set<EventRepeater> repeaterSet = new HashSet<>();

    private Set<UserRole> userRoles = new HashSet<>();
    private Event event;
    private EventAdapter eventAdapter;

    @Before
    public void setUp() {

        attenderSet.add(attender);
        reminderSet.add(reminder);
        repeaterSet.add(repeater);
        userRoles.add(role);
        User user = new User(UUID.randomUUID(), "username", "passw", "bor@ukr.net", true, LocalDate.now(), userRoles);


        repeaterSet.add(repeater);
        event = new Event.EventBuilder()
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

        eventAdapter = new EventAdapter(event);
    }

    @Test
    public void testToJson() {

        JSONArray jsonArrayAttenders = new JSONArray();
        jsonArrayAttenders.put(attender.toJson());
        JSONArray jsonArrayRepeaters = new JSONArray();
        jsonArrayRepeaters.put(repeater.toJson());
        JSONArray jsonArrayReminders = new JSONArray();
        jsonArrayReminders.put(reminder.toJson());

        JSONObject expectedResult = new JSONObject();
        expectedResult.put("id", eventAdapter.getId());
        expectedResult.put("title", "GoodDay");
        expectedResult.put("description", "Very GoodDay");
        expectedResult.put("allDay", false);
        expectedResult.put("color", "#B8B9FF");
        expectedResult.put("startDate", "06-10-2020");
        expectedResult.put("endDate", "06-10-2020");
        expectedResult.put("startTime", "12:00");
        expectedResult.put("endTime", "15:00");
        expectedResult.put("reminderTime", 30);
        expectedResult.put("eventAttenders", jsonArrayAttenders);
        expectedResult.put("eventRepeaters", jsonArrayRepeaters);
        expectedResult.put("eventReminders", jsonArrayReminders);


        JSONObject actualResult = eventAdapter.toJson();
        assertEquals(expectedResult.toString(), actualResult.toString());
    }

    @Test
    public void testGetTextAboutEvent() throws DateTimeFormatException {

        String expectedResult = "Event with title GoodDay. \nDescription of event: " +
                "Very GoodDay. \nPeriod of event: 6, October 2020, 12:00 - 15:00. \n" +
                "Guests of event: Denis Milyaev. Email: denis@ukr.net.";

        String actualResult = eventAdapter.getTextAboutEvent();
        assertEquals(expectedResult, actualResult);
    }
}
