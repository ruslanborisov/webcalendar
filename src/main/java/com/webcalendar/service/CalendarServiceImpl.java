package com.webcalendar.service;

import com.webcalendar.dao.EventDAO;
import com.webcalendar.domain.*;
import com.webcalendar.datastore.DataStore;
import com.webcalendar.exception.DateTimeFormatException;
import com.webcalendar.exception.OrderOfArgumentsException;
import com.webcalendar.exception.ValidationException;
import com.webcalendar.util.DateHelper;
import com.webcalendar.util.EventValidator;
import javassist.NotFoundException;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import static com.webcalendar.domain.EventReminder.EnumReminder.EMAIL;
import static com.webcalendar.domain.EventReminder.EnumReminder.POPUP;
import static com.webcalendar.domain.EventRepeater.EnumRepeater.*;
import static com.webcalendar.domain.EventRepeater.EnumRepeater.SATURDAY;

public class CalendarServiceImpl implements CalendarService {

    private static final Logger logger = Logger.getLogger(CalendarServiceImpl.class);
    final static int MINUTE_INTERVAL = 15;

    private final DataStore dataStore;
    private final EventDAO eventDAO;

    public CalendarServiceImpl(DataStore dataStore, EventDAO eventDAO) {
        this.dataStore = dataStore;
        this.eventDAO = eventDAO;
    }

    @Override
    public boolean add(Event event) throws ValidationException {
        if (event == null) throw new IllegalArgumentException();

//  Validate
        logger.info("Validation event with title '" + event.getTitle() + "'");
        EventValidator.validate(event);
        logger.info("Event successfully validated");

//  Add
        try {
            logger.info("Adding event with title '" + event.getTitle() + "'");
            dataStore.publish(event);
            logger.info("Event successfully added");
            return true;
        } catch (HibernateException e) {
            logger.error(e);
            return false;
        }
    }

    @Override
    public Event remove(UUID id) {
        if (id == null) throw new IllegalArgumentException();

        try {
            logger.info("Removing event with id: '" + id + "'");
            Event event = dataStore.remove(id);
            if (event != null) {
                logger.info("Event successfully removed");
                return event;
            } else {
                logger.info("There is no such Event");
                return null;
            }
        } catch (HibernateException e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public Event createEvent(String eventDescripton, User user) throws ValidationException, DateTimeFormatException {
        if (eventDescripton == null) throw new IllegalArgumentException();

        JSONObject eventJson = new JSONObject(eventDescripton);
        String title = eventJson.getString("title");
        String desc = eventJson.getString("description");
        boolean isAllDay = eventJson.getBoolean("allDay");
        String color = eventJson.getString("color");
        String reminderTimeStr = eventJson.getString("reminderTime");
        JSONArray attenders = eventJson.getJSONArray("eventAttenders");
        JSONArray repeaters = eventJson.getJSONArray("eventRepeaters");
        JSONArray reminders = eventJson.getJSONArray("eventReminders");
        String numberOfOccurrenceStr = eventJson.getString("numberOfOccurrence");

        Integer numberOfOccurrence = null;
        if (!numberOfOccurrenceStr.equals("null"))
            numberOfOccurrence = Integer.parseInt(numberOfOccurrenceStr);

        Set<EventRepeater> eventRepeaters = setRepeaters(repeaters);
        Set<EventAttender> eventAttenders = setAttenders(attenders);

        if (eventRepeaters.contains(new EventRepeater(5, MONDAY)) ||
                eventRepeaters.contains(new EventRepeater(6, TUESDAY)) ||
                eventRepeaters.contains(new EventRepeater(7, WEDNESDAY)) ||
                eventRepeaters.contains(new EventRepeater(8, THURSDAY)) ||
                eventRepeaters.contains(new EventRepeater(9, FRIDAY)) ||
                eventRepeaters.contains(new EventRepeater(10, SATURDAY)) ||
                eventRepeaters.contains(new EventRepeater(11, SUNDAY))) {
            eventJson.put("startDate", DateHelper.dateToString(LocalDate.now()));
            eventJson.put("endDate", DateHelper.dateToString(LocalDate.now()));
        }

        LocalDate startDate;
        LocalDate endDate;
        LocalTime startTime = null;
        LocalTime endTime = null;
        if (isAllDay) {
            startDate = DateHelper.stringToDate(eventJson.getString("startDate"));
            endDate = DateHelper.stringToDate(eventJson.getString("endDate"));
        } else {
            startDate = DateHelper.stringToDate(eventJson.getString("startDate"));
            startTime = DateHelper.stringToTime(eventJson.getString("startTime"));
            endDate = DateHelper.stringToDate(eventJson.getString("endDate"));
            endTime = DateHelper.stringToTime(eventJson.getString("endTime"));
        }

        Integer reminderTime = null;
        Set<EventReminder> eventReminders = new HashSet<>();
        if (!reminderTimeStr.equals("null")) {
            reminderTime = Integer.parseInt(reminderTimeStr);
            eventReminders = setReminders(reminders);
        }

        logger.info("Creating event with title '" + title + "'");
        Event event = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title(title)
                .description(desc)
                .allDay(isAllDay)
                .startDate(startDate)
                .startTime(startTime)
                .endDate(endDate)
                .endTime(endTime)
                .minutesBeforeReminder(reminderTime)
                .numberOfOccurrence(numberOfOccurrence)
                .color(color)
                .attendersOfEvent(eventAttenders)
                .repeatersOfEvent(eventRepeaters)
                .remindersOfEvent(eventReminders)
                .user(user)
                .build();
        logger.info("Event successfully created");

        if (add(event)) {
            logger.info("Event successfully added");
            return event;
        } else {
            logger.error("Add event failed");
            return null;
        }
    }

    @Override
    public Event searchById(UUID id) {
        if (id == null) throw new IllegalArgumentException();

        logger.info("Searching by id '" + id + "':");
        Event event = dataStore.getEventById(id);
        if (event == null)
            logger.info("Event not found!");
        else
            logger.info("Event found");

        return event;
    }

    @Override
    public List<Event> searchByTitle(String title) {
        if (title == null) throw new IllegalArgumentException();

        logger.info("Searching by title '" + title + "':");
        List<Event> events = dataStore.getEventByTitle(title);
        if (events.size() < 1)
            logger.info("Events not found!");
        else {
            Collections.sort(events);
            logger.info("Found " + events.size() + " events");
        }
        return events;
    }
    @Override
    public List<Event> searchEventByTitleStartWith(String prefix) {
        if (prefix == null) throw new IllegalArgumentException();

        logger.info("Searching events by title start with '" + prefix + "'");
        List<Event> events = dataStore.getEventByTitleStartWith(prefix);

        if (events.size() < 1)
            logger.info("Events not found!");
        else {
            Collections.sort(events);
            logger.info("Found " + events.size() + " events");
        }
        return events;
    }

    @Override
    public List<Event> searchByAttender(String email) {
        if (email == null) throw new IllegalArgumentException();

        logger.info("Searching by attender '" + email + "':");
        List<Event> events = dataStore.getEventByAttender(email);
        if (events.size() < 1)
            logger.info("Events not found!");
        else {
            Collections.sort(events);
            logger.info("Found " + events.size() + " events");
        }
        return events;
    }

    @Override
    public List<EventAdapter> searchByDate(LocalDate date) {
        if (date == null) throw new IllegalArgumentException();

        logger.info("Searching by day '" + date + "':");
        List<Event> events = dataStore.getEventByDate(date);

        List<EventAdapter> eventList = new ArrayList<>();
        if (events.size() > 0) {
            eventList.addAll(events.stream()
                    .map(event -> event.getRigthDateForRepeatEvent(date))
                    .sorted()
                    .collect(Collectors.toList()));

            logger.info("Found " + eventList.size() + " events");

        } else
            logger.info("Events not found!");

        return eventList;
    }

    @Override
    public List<EventAdapter> searchIntoPeriod(LocalDate startDate, LocalDate endDate) throws OrderOfArgumentsException {
        if (startDate == null || endDate == null) throw new IllegalArgumentException();
        if (startDate.isAfter(endDate)) throw new OrderOfArgumentsException();

        logger.info("Searching events into period from '" + startDate + "' to '" + endDate + "'");

        List<EventAdapter> eventList = new ArrayList<>();
        while (startDate.isBefore(endDate) || startDate.equals(endDate)) {
            List<EventAdapter> tempEventList = searchByDate(startDate);
            eventList.addAll(tempEventList);
            startDate = startDate.plusDays(1);
        }

// Delete Duplicate only for "long" events
        List<EventAdapter> eventListWithAllLongEvent = new ArrayList<>();
        eventListWithAllLongEvent.addAll(eventList.stream()
                .filter(event -> !event.getStartDate().equals(event.getEndDate()))
                .collect(Collectors.toList()));
        eventList.removeAll(eventListWithAllLongEvent);

        Set<EventAdapter> eventListWithOutDuplicateForLongEvent = new HashSet<>();
        eventListWithOutDuplicateForLongEvent.addAll(eventListWithAllLongEvent.stream()
                .collect(Collectors.toSet()));

        eventList.addAll(eventListWithOutDuplicateForLongEvent);
//

        Collections.sort(eventList);
        logger.info("Result of search into period from '" + startDate +
                "' to '" + endDate + "'." + " Found " + eventList.size() + " events");

        return eventList;
    }

    @Override
    public List<EventAdapter> searchByAttenderIntoPeriod(String email, LocalDate startDate,
                                                  LocalDate endDate) throws OrderOfArgumentsException, DateTimeFormatException {
        if (email == null || startDate == null || endDate == null) throw new IllegalArgumentException();
        if (startDate.isAfter(endDate)) throw new OrderOfArgumentsException();

        logger.info("Searching events by attender '" + email + "' into period from " +
                DateHelper.dateToString(startDate) + " to '" + DateHelper.dateToString(endDate) + "'");

        List<EventAdapter> eventListIntoPeriod = searchIntoPeriod(startDate, endDate);
        List<Event> eventListByAttender = searchByAttender(email);
        List<EventAdapter> eventListByAttenderIntoPeriod = new ArrayList<>();

        for (EventAdapter eventPeriod : eventListIntoPeriod) {
            eventListByAttenderIntoPeriod.addAll(eventListByAttender.stream()
                    .filter(eventAttender -> eventPeriod.getId().equals(eventAttender.getId()))
                    .map(eventAttender -> eventPeriod)
                    .collect(Collectors.toList()));
        }

        if (eventListByAttenderIntoPeriod.isEmpty())
            logger.info("Events not found!");
        else
            logger.info("Found " + eventListByAttenderIntoPeriod.size() + " events");

        return eventListByAttenderIntoPeriod;
    }

    @Override
    public List<List<LocalDateTime>> searchFreeTime(LocalDateTime startDateTime, LocalDateTime endDateTime)
            throws OrderOfArgumentsException, DateTimeFormatException {
        if (startDateTime == null || endDateTime == null)
            throw new IllegalArgumentException();
        if (startDateTime.isAfter(endDateTime))
            throw new OrderOfArgumentsException();

        List<EventAdapter> eventListIntoPeriod =
                searchIntoPeriod(startDateTime.toLocalDate(), endDateTime.toLocalDate());
        List<EventAdapter> eventListIntoPeriodWithOutAllDay = new ArrayList<>();
        eventListIntoPeriodWithOutAllDay.addAll(eventListIntoPeriod.stream()
                                            .filter(event -> !event.isAllDay())
                                            .sorted()
                                            .collect(Collectors.toList()));

        List<List<LocalDateTime>> freeIntervalList = new ArrayList<>();
        LocalDateTime tempStartDateTime = startDateTime;
        logger.info("Searching free time into period from " +
                DateHelper.dateTimeToString(startDateTime) + " to " + DateHelper.dateTimeToString(endDateTime));
        while (tempStartDateTime.isBefore(endDateTime)) {
            LocalDateTime tempEndDateTime = tempStartDateTime.plusMinutes(MINUTE_INTERVAL);
            boolean isFree = true;
            for (EventAdapter eventAdapter : eventListIntoPeriodWithOutAllDay) {
                if (isEventAndPeriodCrossing(eventAdapter, tempStartDateTime, tempEndDateTime)) {
                    isFree = false;
                    break;
                }
            }
            if (isFree)
                freeIntervalList.add(Arrays.asList(tempStartDateTime, tempEndDateTime));

            tempStartDateTime = tempStartDateTime.plusMinutes(MINUTE_INTERVAL);
        }

        if (freeIntervalList.size() > 1) {
            logger.info("Found " + mergeSolidInterval(freeIntervalList).size() + " free intervals");
            return mergeSolidInterval(freeIntervalList);
        }
        else
            return freeIntervalList;
    }

    @Override
    public List<EventAdapter> isAttenderFree(String email, LocalDateTime startDateTime, LocalDateTime endDateTime)
            throws OrderOfArgumentsException, DateTimeFormatException {
        if (startDateTime == null || endDateTime == null)
            throw new IllegalArgumentException();
        if (startDateTime.isAfter(endDateTime))
            throw new OrderOfArgumentsException();

        List<EventAdapter> eventListIntoPeriod =
                searchIntoPeriod(startDateTime.toLocalDate(), endDateTime.toLocalDate());
        List<EventAdapter> eventListIntoPeriodWithOutAllDay = new ArrayList<>();
        eventListIntoPeriodWithOutAllDay.addAll(eventListIntoPeriod.stream()
                .filter(event -> !event.isAllDay())
                .sorted()
                .collect(Collectors.toList()));

        List<Event> eventListByAttender = searchByAttender(email);

        List<EventAdapter> eventListForCheckCrossingWithAttender = new ArrayList<>();
        for (EventAdapter event : eventListIntoPeriodWithOutAllDay) {
            eventListForCheckCrossingWithAttender.addAll(eventListByAttender.stream()
                    .filter(eventAttender -> event.getId().equals(eventAttender.getId()))
                    .map(eventAttender -> event)
                    .collect(Collectors.toList()));
        }

        List<EventAdapter> crossingEventList = new ArrayList<>();

        logger.info("Searching event into period with attender");
        LocalDateTime tempStartDateTime = startDateTime;
        for (EventAdapter eventAdapter : eventListForCheckCrossingWithAttender) {
            while (tempStartDateTime.isBefore(endDateTime)) {
                LocalDateTime tempEndDateTime = tempStartDateTime.plusMinutes(MINUTE_INTERVAL);
                if (isEventAndPeriodCrossing(eventAdapter, tempStartDateTime, tempEndDateTime) ) {
                    crossingEventList.add(eventAdapter);
                    break;
                }
                tempStartDateTime = tempStartDateTime.plusMinutes(MINUTE_INTERVAL);
            }
        }

        logger.info("Found "  + eventListIntoPeriodWithOutAllDay.size() + " events");
        return crossingEventList;
    }


    @Override
    public List<Event> searchAllEventsFromDataStore() {
        Collection<Event> eventCollection = dataStore.getAllEvents();
        return new ArrayList<>(eventCollection);
    }
    @Override
    public void fillDataStore(List<Event> eventList) {
         dataStore.fill(eventList);
    }
    @Override
    public void clearDataStore() {
        dataStore.clear();
    }
    @Override
    public EventAttender searchAttenderByEmailFromDB(String email) throws IllegalArgumentException {
        try {
            return eventDAO.getAttenderByEmail(email);
        } catch (NotFoundException e) {
            logger.info(e);
            return null;
        } catch (HibernateException e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<Event> searchAllUserEventsFromDB(User user) {
        if (user == null) throw new IllegalArgumentException();

        try {
            logger.info("Starting init events of user '" + user + "'");
            List<Event> eventList = eventDAO.getUserEvents(user);
            logger.info("Init events of user successful");
            return eventList;
        } catch (HibernateException e) {
            logger.error(e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Event> searchAllEventsForRemindFromDB() {
        try {
            logger.info("Starting search all events for remind");
            List<Event> eventList = eventDAO.getAllEventsForRemind();
            logger.info("search events for remind successful");
            return eventList;
        } catch (HibernateException e) {
            logger.error(e);
            return new ArrayList<>();
        }
    }

    private Set<EventAttender> setAttenders(JSONArray attendersArray) {
        Set<EventAttender> eventAttenderSet = new HashSet<>();
        int len = attendersArray.length();
        if (len<1)
            return eventAttenderSet;

        for (int i=0;i<len;i++) {
            String email = attendersArray.get(i).toString();
            EventAttender eventAttender = searchAttenderByEmailFromDB(email);
            if (eventAttender != null)
                eventAttenderSet.add(eventAttender);
            else
                eventAttenderSet.add(new EventAttender(UUID.randomUUID(), null, null, email));
        }
        return eventAttenderSet;
    }
    private Set<EventRepeater> setRepeaters(JSONArray repeatersArray) {

        Set<EventRepeater> eventRepeaters = new HashSet<>();
        int len = repeatersArray.length();
        for (int i=0;i<len;i++){
            String repeater = repeatersArray.get(i).toString();
            if("once".equals(repeater)) eventRepeaters.add(new EventRepeater(1, ONCE));
            if("daily".equals(repeater)) eventRepeaters.add(new EventRepeater(2, DAILY));
            if("monthly".equals(repeater)) eventRepeaters.add(new EventRepeater(3, MONTHLY));
            if("yearly".equals(repeater)) eventRepeaters.add(new EventRepeater(4, YEARLY));
            if("monday".equals(repeater)) eventRepeaters.add(new EventRepeater(5, MONDAY));
            if("tuesday".equals(repeater)) eventRepeaters.add(new EventRepeater(6, TUESDAY));
            if("wednesday".equals(repeater)) eventRepeaters.add(new EventRepeater(7, WEDNESDAY));
            if("thursday".equals(repeater)) eventRepeaters.add(new EventRepeater(8, THURSDAY));
            if("friday".equals(repeater)) eventRepeaters.add(new EventRepeater(9, FRIDAY));
            if("saturday".equals(repeater)) eventRepeaters.add(new EventRepeater(10, SATURDAY));
            if("sunday".equals(repeater)) eventRepeaters.add(new EventRepeater(11, SUNDAY));
        }
        return eventRepeaters;
    }
    private Set<EventReminder> setReminders(JSONArray remindersJson) {

        Set<EventReminder> eventReminders = new HashSet<>();
        int len = remindersJson.length();
        for (int i=0;i<len;i++){
            String reminder = remindersJson.get(i).toString();
            if("popup".equals(reminder)) eventReminders.add(new EventReminder(1, POPUP));
            if("email".equals(reminder)) eventReminders.add(new EventReminder(2, EMAIL));
        }
        return eventReminders;
    }

    private List<List<LocalDateTime>> mergeSolidInterval(List<List<LocalDateTime>> intervalList) {

        List<List<LocalDateTime>> solidFreeIntervalList = new ArrayList<>();
        LocalDateTime left  = intervalList.get(0).get(0);
        LocalDateTime rigth = intervalList.get(0).get(1);

        for (int i = 0; i < intervalList.size()-1; i++) {
            List<LocalDateTime> leftInterval = intervalList.get(i);
            List<LocalDateTime> rigthInterval = intervalList.get(i+1);
            if (leftInterval.get(1).equals(rigthInterval.get(0)))
                rigth = rigthInterval.get(1);
            else {
                solidFreeIntervalList.add(Arrays.asList(left,rigth));
                left = intervalList.get(i+1).get(0);
                rigth = intervalList.get(i+1).get(1);
            }
        }
        solidFreeIntervalList.add(Arrays.asList(left,intervalList.get(intervalList.size()-1).get(1)));
        return solidFreeIntervalList;
    }
    private boolean isEventAndPeriodCrossing(EventAdapter eventAdapter, LocalDateTime startDate, LocalDateTime endDate)
            throws OrderOfArgumentsException {
        if (eventAdapter == null || startDate == null || endDate == null)
            throw new IllegalArgumentException();
        if (startDate.isAfter(endDate))
            throw new OrderOfArgumentsException();

        LocalDateTime startDateTime = LocalDateTime.of(eventAdapter.getStartDate(), eventAdapter.getStartTime());
        LocalDateTime endDateTime = LocalDateTime.of(eventAdapter.getEndDate(), eventAdapter.getEndTime());

        return startDateTime.isAfter(startDate) && startDateTime.isBefore(endDate)
                || endDateTime.isAfter(startDate) && endDateTime.isBefore(endDate)
                || startDateTime.isBefore(startDate) && endDateTime.isAfter(endDate)
                || startDateTime.equals(startDate) || endDateTime.equals(endDate);
    }
}


