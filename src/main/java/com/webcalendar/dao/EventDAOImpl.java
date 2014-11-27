package com.webcalendar.dao;

import com.webcalendar.domain.*;
import com.webcalendar.util.AdapterToOrigin;
import javassist.NotFoundException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.*;

public class EventDAOImpl implements EventDAO, Serializable {

    private final SessionFactory sessionFactory;

    public EventDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addEvent(Event event) throws HibernateException {
        if (event == null)
            throw new IllegalArgumentException("ADD EVENT FAILED: You are trying to add a null event");

        for (EventAttender eventAttender : event.getEventAttenders())
            sessionFactory.getCurrentSession().saveOrUpdate(eventAttender);

        sessionFactory.getCurrentSession().save(new EventAdapter(event));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteEvent(Event event) throws HibernateException {
        if (event == null)
            throw new IllegalArgumentException("DELETE EVENT FAILED: Id cannot be null");

        sessionFactory.getCurrentSession().delete(new EventAdapter(event));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Event> getAllEventsForRemind() throws HibernateException {

        Query query = sessionFactory.getCurrentSession().
                createQuery("from EventAdapter where minutesBeforeReminder != null");

        @SuppressWarnings("unchecked")
        List<EventAdapter> eventAdapterList = (List<EventAdapter>) query.list();

        List<Event> eventList = new ArrayList<>();
        for(EventAdapter eventAdapter : eventAdapterList) {
            Event event = AdapterToOrigin.eventAdapterToEvent(eventAdapter);
            eventList.add(event);
        }

        return eventList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Event> getUserEvents(User user) throws HibernateException {
        if (user == null)
            throw new IllegalArgumentException("GET EVENTs FAILED: user cannot be null");

        Query query = sessionFactory.getCurrentSession().
                createQuery("from EventAdapter where user =:user");
        query.setParameter("user", user);

        @SuppressWarnings("unchecked")
        List<EventAdapter> eventAdapterList = (List<EventAdapter>) query.list();

        List<Event> eventList = new ArrayList<>();
        if (eventAdapterList.size() > 0)
            for (EventAdapter eventAdapter : eventAdapterList) {
                 Event event = AdapterToOrigin.eventAdapterToEvent(eventAdapter);
                 eventList.add(event);
            }
        return eventList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public EventAttender getAttenderByEmail(String email) throws HibernateException, NotFoundException {
        if (email==null)
            throw new IllegalArgumentException("GET ATTENDER FAILED: email cannot be null");

        Query query = sessionFactory.getCurrentSession().createQuery("from EventAttender a where a.email=:email ");
        query.setParameter("email", email);

        EventAttender eventAttender = (EventAttender) query.uniqueResult();

        if(eventAttender==null)
            throw new NotFoundException("attender: " + email + " not found!");

        return eventAttender;
    }
}
