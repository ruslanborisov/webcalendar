package com.webcalendar.util;

import com.webcalendar.domain.*;
import com.webcalendar.exception.DateTimeFormatException;
import com.webcalendar.exception.ValidationException;
import org.junit.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import static com.webcalendar.domain.EventRepeater.EnumRepeater.*;
import static com.webcalendar.domain.EventReminder.EnumReminder.*;
import static com.webcalendar.domain.UserRole.EnumRole.*;

public class EventValidatorTest {

    @Test
    public void testValidate() throws ValidationException {

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
                .startTime(LocalTime.of(12,0))
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

        EventValidator.validate(event);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWithNullField() throws DateTimeFormatException, ValidationException {
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
// null field -> exception
                .title(null)
                .description("Very GoodDay")
                .allDay(false)
                .startDate(LocalDate.of(2020, Month.OCTOBER, 6))
                .startTime(LocalTime.of(12,0))
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

        EventValidator.validate(event);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWithNotFillField() throws ValidationException {
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
// length of field = 0 -> exception
                .title("")
                .description("Hello, world!")
                .allDay(false)
                .startDate(LocalDate.of(2020, Month.OCTOBER, 6))
                .startTime(LocalTime.of(12,0))
                .endDate(LocalDate.of(2020, Month.OCTOBER, 6))
                .endTime(LocalTime.of(15, 0))
                .minutesBeforeReminder(30)
                .numberOfOccurrence(null)
                .color("#B8B9FF")
                .attendersOfEvent(attenderSet)
                .repeatersOfEvent(repeaterSet)
                .remindersOfEvent(reminderSet)
                .user(user)
                .build();

        EventValidator.validate(event);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWithStartDateAfterEndDate() throws ValidationException {
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
// startDate after enDate -> exception
                .startDate(LocalDate.of(2020, Month.OCTOBER, 8))
                .startTime(LocalTime.of(12,0))
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

        EventValidator.validate(event);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWithStartDateBeforeCurrentDate() throws ValidationException {
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
// startDate before current date -> exception
                .startDate(LocalDate.now().minusDays(1))
                .startTime(LocalTime.of(12,0))
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

        EventValidator.validate(event);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWithStartTimeAfterEndTime() throws ValidationException {
        EventAttender attender = new EventAttender(null, "Denis", "Milyaev", "denis@ukr.net");
        EventRepeater repeater = new EventRepeater(6, MONDAY);
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
// Start time after end time"
                .startDate(LocalDate.of(2020, Month.OCTOBER, 6))
                .startTime(LocalTime.of(15, 0))
                .endDate(LocalDate.of(2020, Month.OCTOBER, 6))
                .endTime(LocalTime.of(12, 0))
                .minutesBeforeReminder(30)
                .numberOfOccurrence(null)
                .color("#B8B9FF")
                .attendersOfEvent(attenderSet)
                .repeatersOfEvent(repeaterSet)
                .remindersOfEvent(reminderSet)
                .user(user)
                .build();

        EventValidator.validate(event);
    }
}
