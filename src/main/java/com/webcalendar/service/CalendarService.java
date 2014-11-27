package com.webcalendar.service;

import com.webcalendar.domain.*;
import com.webcalendar.exception.DateTimeFormatException;
import com.webcalendar.exception.OrderOfArgumentsException;
import com.webcalendar.exception.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.webcalendar.datastore.DataStore;
import com.webcalendar.dao.EventDAO;

/**
 * Provides ability to manipulate with event from {@link DataStore} and databases.
 * Type of operations: create, add, remove and many various of search.
 *
 *
 * @author Ruslan Borisov
 */
public interface CalendarService {

    /**
     * Provides ability to publish event to the data store
     * and at once synchronize with database.
     * Invokes method of {@link DataStore}:  {@link DataStore#publish(Event)}
     * Invokes method of {@link EventDAO}: {@link EventDAO#addEvent(Event)}
     *
     * @param event : Event for add, not null
     * @return true if event was added or false otherwise
     * @throws ValidationException if happen exception for validate event.
     */
    boolean add(Event event) throws ValidationException;

    /**
     * Provides ability to remove event from the data store
     * and at once synchronize with database.
     * Invokes method of {@link DataStore}:  {@link DataStore#remove(UUID)})}
     * Invokes method of {@link EventDAO}: {@link EventDAO#deleteEvent(Event)})}
     *
     * @param id : Id of the event for remove, not null
     * @return removed event or null if there was no mapping for the given id
     */
    Event remove(UUID id);

    /**
     * Provides ability to create event with given string descriptions in json format and user.
     * After create, event adds into data store and at once synchronize with database.
     * Invokes method of {@link DataStore}:  {@link DataStore#publish(Event)}
     * Invokes method of {@link EventDAO}: {@link EventDAO#addEvent(Event)}
     * @param eventDescription : String in json format with all needs descriptions for create event.
     * eventDescription example:{"title":"New Year",
     *                           "description":"Happy New Year",
     *                           "allDay":"false",
     *                           "color":"#B8B9FF",
     *                           "startDate":"06-10-2020",
     *                           "endDate":"08-10-2020",
     *                           "startTime":"12:00",
     *                           "endTime":"15:00",
     *                           "reminderTime":"30",
     *                           "numberOfOccurrence":"null",
     *                           "eventAttenders":["bor@ukr.net","kate@ukr.net"],
     *                           "eventRepeaters":["once"],
     *                           "eventReminders":["popup"]}
     * @param user : Owner of the events
     * @return created event
     * @throws ValidationException if happen exception for validate event.
     * @throws DateTimeFormatException if an error occurred during parsing or format string/date
     */
    Event createEvent(String eventDescription, User user) throws ValidationException, DateTimeFormatException;

    /**
     * Provides ability to search events by id from the data store.
     * Invokes method of {@link DataStore}:  {@link DataStore#getEventById(UUID)}
     *
     * @param id : Id of the event for search
     * @return event with given id or null if there was no mapping for given id
     */
    Event searchById(UUID id);

    /**
     * Provides ability to search events by title from the data store.
     * Invokes method of {@link DataStore}:  {@link DataStore#getEventByTitle(String)}
     *
     * @param title : Title of the event for search
     * @return list of events with given title or empty list if there was no mapping for given title
     */
    List<Event> searchByTitle(String title);

    /**
     * Provides ability to search events by given title that starts with the prefix from the data store.
     * Invokes method of {@link DataStore}:  {@link DataStore#getEventByTitleStartWith(String)}
     *
     * @param prefix : Prefix of title of the event for search
     * @return list of events with given prefix or empty list if there was no mapping for given prefix
     */
    List<Event> searchEventByTitleStartWith(String prefix);

    /**
     * Provides ability to search events by given attender from the data store.
     * Invokes method of {@link DataStore}:  {@link DataStore#getEventByAttender(String)}
     *
     * @param email : Email of the attender for search
     * @return List of events by given email or empty list if there was no mapping for given email
     */
    List<Event> searchByAttender(String email);

    /**
     * Provides ability to search events by given date from the data store.
     * Invokes method of {@link DataStore}:  {@link DataStore#getEventByDate(LocalDate)}
     *
     * @param date : Date for search
     * @return List of events by given date or empty list if there was no mapping for given date
     */
    List<EventAdapter> searchByDate(LocalDate date);

    /**
     * Provides ability to search events by given range of dates from the data store.
     * Invokes method of {@link DataStore}:  {@link DataStore#getEventByDate(LocalDate)}
     *
     * @param startDate : Start date for search
     * @param endDate : End date for search
     * @return List of events by given range of dates or empty list if there was no mapping for given range of dates
     * @throws OrderOfArgumentsException if start date is after end date
     */
    List<EventAdapter> searchIntoPeriod(LocalDate startDate, LocalDate endDate) throws OrderOfArgumentsException;

    /**
     * Provides ability to search events by given range of dates from the data store and synchronize
     * it with guest schedule
     * Invokes method of {@link DataStore}:  {@link DataStore#getEventByDate(LocalDate)}
     * Invokes method of {@link DataStore}:  {@link DataStore#getEventByAttender(String)}
     *
     * @param startDate : Start date for search
     * @param endDate : End date for search
     * @param email :  Email of the attender for search
     * @return List of events by given range of dates and concrete attender or empty list
     * if there was no mapping for given range of dates
     * @throws OrderOfArgumentsException if start date is after end date
     */
    List<EventAdapter> searchByAttenderIntoPeriod(String email, LocalDate startDate, LocalDate endDate)
            throws OrderOfArgumentsException, DateTimeFormatException;

    /**
     * Provides ability to search free time from the data store in a given period
     * Invokes method of {@link DataStore}:  {@link DataStore#getEventByDate(LocalDate)}
     *
     * @param startDateTime Start date with time for search
     * @param endDateTime : End date with time for search
     * @return list of free periods or empty list if there are no free time
     * @throws OrderOfArgumentsException if start date is after end date
     */
    List<List<LocalDateTime>> searchFreeTime(LocalDateTime startDateTime, LocalDateTime endDateTime)
            throws OrderOfArgumentsException, DateTimeFormatException;

    /**
     * Check an attender is free in a given period
     *
     * @param startDateTime : Start date with time for search
     * @param endDateTime : End date with time for search
     * @param email :  Email of the attender for search
     * @return List of events by given range of dates and attender if attender is not free
     * or empty list otherwise
     * @throws OrderOfArgumentsException if start date is after end date
     */
    List<EventAdapter> isAttenderFree(String email, LocalDateTime startDateTime, LocalDateTime endDateTime)
            throws OrderOfArgumentsException, DateTimeFormatException;

    /**
     * Get all event from data store
     *
     * @return List of events or empty list if there was no events
     */
    List<Event> searchAllEventsFromDataStore();

    /**
     * set data store for given List of events
     *
     * @param eventList: List of the events which need set to datastore
     */
    void fillDataStore(List<Event> eventList);

    /**
     * Delete all records from data store
     *
     */
    void clearDataStore();

    /**
     * Get attender by email from database
     *
     * @param email : Email of the attender for search
     * @return : attender for given email or null if there was no mapping for the given email
     */
    public EventAttender searchAttenderByEmailFromDB(String email);

    /**
     * Get all events for current user
     *
     * @param user : Owner of the events
     * @return list of the events for given user or empty list if there was no events
     */
    List<Event> searchAllUserEventsFromDB(User user);

    /**
     * Get all events for remind.
     *
     * @return list of the events for remind or empty list if there was no events for remind
     */
    List<Event> searchAllEventsForRemindFromDB();
}
