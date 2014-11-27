package com.webcalendar.dao;

import com.webcalendar.domain.EventAttender;
import com.webcalendar.domain.Event;
import com.webcalendar.domain.User;
import javassist.NotFoundException;
import org.hibernate.HibernateException;
import java.util.List;

/**
 * Layer that allows to perform various manipulations
 * with the events in the database
 *
 * @author Ruslan Borisov
 */
public interface EventDAO {

    /**
     * Add event to database
     *
     * @param event : Event for add, not null
     * @throws HibernateException if happen database exception.
     */
    public void addEvent(Event event) throws HibernateException;

    /**
     * Delete event from database
     *
     * @param event : Event for delete, not null
     * @throws HibernateException if happen database exception.
     */
    public void deleteEvent(Event event) throws HibernateException;

    /**
     * Get events of given user
     *
     * @param user : User which events must be found
     * @return list of events of the given user or empty list if events not found
     * @throws HibernateException if happen database exception.
     */
    public List<Event> getUserEvents(User user) throws HibernateException;

    /**
     * Get events for remind of the current user
     *
     * @return list of events of the current user or empty list if events not found
     * @throws HibernateException if happen database exception.
     */
    public List<Event> getAllEventsForRemind() throws HibernateException;


    /**
     * Get attender by email
     *
     * @param email : Email address of the attender which must be found
     * @return attender for given email address
     * @throws HibernateException if happen database exception.
     * @throws NotFoundException if attender not exists in database
     */
    public EventAttender getAttenderByEmail(String email) throws HibernateException, NotFoundException;
}
