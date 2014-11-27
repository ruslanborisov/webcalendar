package com.webcalendar.datastore;

import com.webcalendar.domain.Event;
import org.hibernate.HibernateException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * After created session with concrete user, all events of this user
 * loaded from database to this datastore in general store map.
 * After that creates indexes in index maps.
 *
 * Contains one general store map and seven index map which use
 * for increase performance work with datastore.
 *
 * Allows you to perform various manipulations
 * with the events in the data store.
 *
 * Operations of publish and remove execute with using datastore and at once
 * synchronize with database.
 *
 * Operations of search executes with using datastore only
 *
 *
 * @author Ruslan Borisov
 */
public interface DataStore {

    /**
     * Publish event to datastore and at once synchronize with database.
     *
     * @param event : Event for publish, not null
     * @throws HibernateException if happen database exception.
     */
    void publish(Event event) throws HibernateException;

    /**
     * Removes event for given id from the datastore
     * and at once synchronize with database.
     *
     * @param id : Id of the event for remove
     * @return removed event return event with given title or null if there was no mapping for given title
     * @throws HibernateException if happen database exception.
     */
    Event remove(UUID id) throws HibernateException;

    /**
     * Search event for given id in the datastore and return it
     *
     * @param id : Id of the event for search
     * @return event by given id or null if there was no mapping for given id
     */
    Event getEventById(UUID id);

    /**
     * Search event for given title in the data store and return it.
     * Uses index map.
     *
     * @param title : Tile of the event for search
     * @return List of events by given title or empty list if there was no mapping for given title
     */
    List<Event> getEventByTitle(String title);

    /**
     * Search event by given title that starts with the prefix
     *
     * @param prefix : Prefix of title of the event for search
     * @return List of events by given title that starts with the prefix
     * or empty list if there was no mapping for given prefix
     */
    List<Event> getEventByTitleStartWith(String prefix);

    /**
     * Search event for given particular date in the data store and return it.
     * Uses index map.
     *
     * @param date: Date for search
     * @return List of events by given date or empty list if there was no mapping for given date
     */
    List<Event> getEventByDate(LocalDate date);

    /**
     * Search event for given attender in the data store and return it.
     *
     * @param email: Email of the event for search
     * @return List of events by given attender or empty list if there was no mapping for given attender
     */
    List<Event> getEventByAttender(String email);

    /**
     * Get all events from data store
     *
     * @return List of events or empty list if there was no events
     */
    Collection<Event> getAllEvents();

    /**
     * Set data store for given List of events
     *
     * @param eventList: List of the events which need set to datastore
     */
    void fill(List<Event> eventList);

    /**
     * Delete all records from data store
     *
     */
    void clear();
}
