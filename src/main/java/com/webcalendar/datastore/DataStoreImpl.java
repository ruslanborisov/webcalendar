package com.webcalendar.datastore;

import com.webcalendar.domain.*;
import com.webcalendar.dao.EventDAO;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import com.webcalendar.domain.EventRepeater.EnumRepeater;
import org.hibernate.HibernateException;

public class DataStoreImpl implements DataStore, Serializable {

    private Map<UUID,Event> eventStore = new HashMap<>();
    private Map<String, List<UUID>> indexTitle = new HashMap<>();
    private Map<String, List<UUID>> indexAttender = new HashMap<>();
    private Map<LocalDate, List<UUID>> indexOnce = new HashMap<>();
    private Map<DayOfWeek, List<UUID>> indexDayOfWeek = new HashMap<>();
    private Map<LocalDate, List<UUID>> indexDaily = new HashMap<>();
    private Map<Integer, Set<UUID>> indexMonthly = new HashMap<>();
    private Map<String, Set<UUID>> indexYearly = new HashMap<>();

    private final EventDAO eventDAO;

    public DataStoreImpl(EventDAO eventDAO) {
       this.eventDAO = eventDAO;
    }


    @Override
    public void publish(Event event) throws HibernateException {
        if (event==null)
            throw new IllegalArgumentException();

        eventDAO.addEvent(event);
        eventStore.put(event.getId(), event);
        createIndexTitle(event);
        createIndexAttender(event);
        createIndexForRepeatEvent(event);
   }

    @Override
    public Event remove(UUID id) throws HibernateException {
       if (id==null)
           throw new IllegalArgumentException();

       eventDAO.deleteEvent(eventStore.get(id));
       Event event = eventStore.remove(id);

       if (event!=null) {
           removeIndexTitle(event);
           removeIndexAttender(event);
           removeIndexForRepeater(event);
       }
      return event;
   }

    @Override
    public Event getEventById(UUID id) {
        if (id==null)
            throw new IllegalArgumentException();

        return eventStore.get(id);
   }

    @Override
    public List<Event> getEventByTitle(String title) {
        if (title==null)
            throw new IllegalArgumentException();

        List<UUID> ids = indexTitle.get(title);
        List<Event> events = new ArrayList<>();
        if (ids!=null)
            for (UUID id : ids) {
                Event event = eventStore.get(id);
                events.add(event);
            }

        return events;
   }

    @Override
    public List<Event> getEventByTitleStartWith(String prefix) {
        if (prefix == null) throw new IllegalArgumentException();
        List<Event> presentInEventList = new ArrayList<>();
        indexTitle.keySet().stream().filter(title -> title.startsWith(prefix))
                .forEach(title -> presentInEventList.addAll(indexTitle.get(title).stream()
                    .map(eventStore::get)
                    .collect(Collectors.toList())));

        return presentInEventList;
    }

    @Override
    public List<Event> getEventByAttender(String email) {
        if (email==null) throw new IllegalArgumentException();

        List<UUID> ids = indexAttender.get(email);
        List<Event> events = new ArrayList<>();
        if (ids!=null)
            for (UUID id : ids) {
                Event event = eventStore.get(id);
                events.add(event);
            }
        return events;
    }

    @Override
    public List<Event> getEventByDate(LocalDate date) {
        if (date==null) throw new IllegalArgumentException();

        List<Event> events = new ArrayList<>();

        List<Event> eventOnce = getEventOnce(date);
        if (eventOnce != null) events.addAll(eventOnce);

        List<Event> eventDaily = getEventDaily(date);
        if (eventDaily != null) events.addAll(eventDaily);

        List<Event> eventMonthly = getEventMonthly(date);
        if (eventMonthly != null) events.addAll(eventMonthly);

        List<Event> eventYearly = getEventYearly(date);
        if (eventYearly != null) events.addAll(eventYearly);

        List<Event> eventDayOfWeek = getEventDayOfWeek(date);
        if (eventDayOfWeek != null) events.addAll(eventDayOfWeek);

        return events;
    }

    @Override
    public Collection<Event> getAllEvents() {
        return eventStore.values();
    }

    @Override
    public void fill(List<Event> eventList) {
        eventStore.clear();
        for(Event event : eventList) {
            eventStore.put(event.getId(), event);
            createIndexTitle(event);
            createIndexAttender(event);
            createIndexForRepeatEvent(event);
        }
    }

    @Override
    public void clear() {
        eventStore.clear();
        indexTitle.clear();;
        indexAttender.clear();
        indexOnce.clear();
        indexDaily.clear();;
        indexMonthly.clear();
        indexYearly.clear();
        indexDayOfWeek.clear();
    }

    private List<Event> getEventOnce(LocalDate date) {
        if (date==null) throw new IllegalArgumentException();

        List<UUID> ids = indexOnce.get(date);
        List<Event> events = new ArrayList<>();
        if (ids!=null)
            for (UUID id : ids) {
                Event event = eventStore.get(id);
                events.add(event);
            }
        return events;
    }

    private List<Event> getEventDayOfWeek(LocalDate date) {
        List<Event> eventList = new ArrayList<>();
        List<UUID> uuids = indexDayOfWeek.get(date.getDayOfWeek());
        if (uuids != null) eventList.addAll(uuids.stream()
                .filter(uuid -> eventStore.get(uuid).getStartDate().isBefore(date)
                        || eventStore.get(uuid).getStartDate().isEqual(date))
                .filter(uuid -> eventStore.get(uuid).getNumberOfOccurrence() == null ||
                        date.isBefore(eventStore.get(uuid).getStartDate()
                                .plusDays(eventStore.get(uuid).getNumberOfOccurrence() * 7)))
                .map(eventStore::get)
                .collect(Collectors.toList()));
        return eventList;
    }

    private List<Event> getEventDaily(LocalDate date) {
        if (date==null) throw new IllegalArgumentException();
        List<Event> eventList = new ArrayList<>();
        indexDaily.keySet().stream()
                .filter(eventStartDate -> date.isEqual(eventStartDate) || date.isAfter(eventStartDate))
                .forEach(eventStartDate -> {
                    List<UUID> uuids = indexDaily.get(eventStartDate);
                    if (uuids != null) eventList.addAll(uuids.stream()
                            .filter(uuid -> eventStore.get(uuid).getNumberOfOccurrence() == null ||
                                    date.isBefore(eventStore.get(uuid).getStartDate()
                                            .plusDays(eventStore.get(uuid).getNumberOfOccurrence())))
                            .map(eventStore::get)
                            .collect(Collectors.toList()));
                });
        return eventList;
    }

    private List<Event> getEventMonthly(LocalDate date) {
        List<Event> eventList = new ArrayList<>();
        Set<UUID> uuids = indexMonthly.get(date.getDayOfMonth());
        if (uuids != null) eventList.addAll(uuids.stream()
                .filter(uuid -> eventStore.get(uuid).getStartDate().isBefore(date)
                        || eventStore.get(uuid).getStartDate().isEqual(date))
                .filter(uuid -> eventStore.get(uuid).getNumberOfOccurrence()==null ||
                        date.isBefore(eventStore.get(uuid).getStartDate()
                                .plusMonths(eventStore.get(uuid).getNumberOfOccurrence())))
                .map(eventStore::get)
                .collect(Collectors.toList()));
        return eventList;
    }

    private List<Event> getEventYearly(LocalDate date) {
        List<Event> eventList = new ArrayList<>();
        String dayAndMonth = date.format(DateTimeFormatter.ofPattern("dd-MM"));
        Set<UUID> uuids = indexYearly.get(dayAndMonth);
        if (uuids != null) eventList.addAll(uuids.stream()
                .filter(uuid -> eventStore.get(uuid).getStartDate().isBefore(date)
                        || eventStore.get(uuid).getStartDate().isEqual(date))
                .filter(uuid -> eventStore.get(uuid).getNumberOfOccurrence()==null ||
                date.isBefore(eventStore.get(uuid).getStartDate()
                        .plusYears(eventStore.get(uuid).getNumberOfOccurrence())))
                .map(eventStore::get)
                .collect(Collectors.toList()));
        return eventList;
    }

    private void createIndexForRepeatEvent(Event event) {
        Set<EventRepeater> repeaters = event.getEventRepeaters();
        for (EventRepeater repeater : repeaters) {
            EnumRepeater enumRepeater = repeater.getRepeater();
            switch (enumRepeater) {
                case ONCE:
                    createIndexOnce(event);
                    break;
                case DAILY:
                    createIndexDaily(event);
                    break;
                case MONTHLY:
                    createIndexMonthly(event);
                    break;
                case YEARLY:
                    createIndexYearly(event);
                    break;
                case MONDAY:
                case TUESDAY:
                case WEDNESDAY:
                case THURSDAY:
                case FRIDAY:
                case SATURDAY:
                case SUNDAY:
                    createIndexDayOfWeek(event, enumRepeater);
                    break;
            }
        }
    }
    private void removeIndexForRepeater(Event event) {
        Set<EventRepeater> repeaters = event.getEventRepeaters();
        for (EventRepeater repeater : repeaters) {
            EnumRepeater enumRepeater = repeater.getRepeater();
            switch (enumRepeater) {
                case ONCE:
                    removeIndexOnce(event);
                    break;
                case DAILY:
                    removeIndexDaily(event);
                    break;
                case MONTHLY:
                    removeIndexMonthly(event);
                    break;
                case YEARLY:
                    removeIndexYearly(event);
                    break;
                case MONDAY:
                case TUESDAY:
                case WEDNESDAY:
                case THURSDAY:
                case FRIDAY:
                case SATURDAY:
                case SUNDAY:
                    removeIndexDayOfWeek(event, enumRepeater);
                    break;
            }
        }
    }

    private void createIndexOnce(Event event) {
        LocalDate startDay = event.getStartDate();
        LocalDate endDay = event.getEndDate();

        while(startDay.isBefore(endDay) || startDay.equals(endDay)) {
            List<UUID> idsDate = indexOnce.get(startDay);
            if(idsDate==null) {
                idsDate = new ArrayList<>();
                idsDate.add(event.getId());
                indexOnce.put(startDay, idsDate);
            } else
                idsDate.add(event.getId());

            startDay = startDay.plusDays(1);
        }
    }
    private void createIndexDaily(Event event) {
        List<UUID> idsDate = indexDaily.get(event.getStartDate());
        if (idsDate == null) {
            idsDate = new ArrayList<>();
            idsDate.add(event.getId());
            indexDaily.put(event.getStartDate(), idsDate);
        } else
            idsDate.add(event.getId());
    }
    private void createIndexMonthly(Event event) {
        LocalDate startDay = event.getStartDate();
        LocalDate endDay = event.getEndDate();

        while (startDay.isBefore(endDay) || startDay.equals(endDay)) {
            Set<UUID> idsDate = indexMonthly.get(startDay.getDayOfMonth());
            if (idsDate == null) {
                idsDate = new HashSet<>();
                idsDate.add(event.getId());
                indexMonthly.put(startDay.getDayOfMonth(), idsDate);
            } else
                idsDate.add(event.getId());

            startDay = startDay.plusDays(1);
        }
    }
    private void createIndexYearly(Event event) {
        LocalDate startDay = event.getStartDate();
        LocalDate endDay = event.getEndDate();

        while (startDay.isBefore(endDay) || startDay.equals(endDay)) {
            String dayAndMonth = startDay.format(DateTimeFormatter.ofPattern("dd-MM"));
            Set<UUID> idsDate = indexYearly.get(dayAndMonth);
            if (idsDate == null) {
                idsDate = new HashSet<>();
                idsDate.add(event.getId());
                indexYearly.put(dayAndMonth, idsDate);
            } else
                idsDate.add(event.getId());

            startDay = startDay.plusDays(1);
        }
    }
    private void createIndexDayOfWeek(Event event, EnumRepeater repeater) {
        List<UUID> idsDate = indexDayOfWeek.get(DayOfWeek.valueOf(repeater.name()));
        if (idsDate == null) {
            idsDate = new ArrayList<>();
            idsDate.add(event.getId());
            indexDayOfWeek.put(DayOfWeek.valueOf(repeater.name()), idsDate);
        } else
            idsDate.add(event.getId());
    }
    private void createIndexTitle(Event event) {
        List<UUID> idsTitle = indexTitle.get(event.getTitle());
        if (idsTitle == null) {
            idsTitle = new ArrayList<>();
            idsTitle.add(event.getId());
            indexTitle.put(event.getTitle(), idsTitle);
        } else
            idsTitle.add(event.getId());
    }
    private void createIndexAttender(Event event) {
        Set<EventAttender> eventAttenders = event.getEventAttenders();
        for (EventAttender eventAttender : eventAttenders) {
            List<UUID> idsAttender = indexAttender.get(eventAttender.getEmail());
            if (idsAttender == null) {
                idsAttender = new ArrayList<>();
                idsAttender.add(event.getId());
                indexAttender.put(eventAttender.getEmail(), idsAttender);
            } else
                idsAttender.add(event.getId());
        }
    }

    private void removeIndexOnce(Event event) {
        LocalDate startDay = event.getStartDate();
        LocalDate endDay = event.getEndDate();

        while(startDay.isBefore(endDay) || startDay.equals(endDay)) {
            List<UUID> idsDate = indexOnce.get(startDay);

            if (idsDate.size() <= 1)
                indexOnce.remove(startDay);
            else
                idsDate.remove(event.getId());

            startDay = startDay.plusDays(1);
        }
    }
    private void removeIndexDaily(Event event) {
        List<UUID> idsDate = indexDaily.get(event.getStartDate());
        if (idsDate!= null && idsDate.size() <= 1)
            indexDaily.remove(event.getStartDate());
        else
            idsDate.remove(event.getId());
    }
    private void removeIndexMonthly(Event event) {
        LocalDate startDay = event.getStartDate();
        LocalDate endDay = event.getEndDate();

        while (startDay.isBefore(endDay) || startDay.equals(endDay)) {
            Set<UUID> idsDate = indexMonthly.get(startDay.getDayOfMonth());
            if(idsDate!=null)
                idsDate.remove(event.getId());

            startDay = startDay.plusDays(1);
        }
    }
    private void removeIndexYearly(Event event) {
        LocalDate startDay = event.getStartDate();
        LocalDate endDay = event.getEndDate();

        while (startDay.isBefore(endDay) || startDay.equals(endDay)) {
            String dayAndMonth = startDay.format(DateTimeFormatter.ofPattern("dd-MM"));
            Set<UUID> idsDate = indexYearly.get(dayAndMonth);
            if(idsDate!=null)
                idsDate.remove(event.getId());

            startDay = startDay.plusDays(1);
        }
    }
    private void removeIndexDayOfWeek(Event event, EnumRepeater repeater) {
        List<UUID> idsDate = indexDayOfWeek.get(DayOfWeek.valueOf(repeater.name()));
        if (idsDate.size() <= 1) {
            indexDayOfWeek.remove(DayOfWeek.valueOf(repeater.name()));
        } else idsDate.remove(event.getId());
    }
    private void removeIndexTitle(Event event) {
        List<UUID> idsTitle = indexTitle.get(event.getTitle());
        if (idsTitle.size() <= 1)
            indexTitle.remove(event.getTitle());
        else
            idsTitle.remove(event.getId());

    }
    private void removeIndexAttender(Event event) {
        Set<EventAttender> eventAttenders = event.getEventAttenders();
        for (EventAttender eventAttender : eventAttenders) {
            List<UUID> idsAttender = indexAttender.get(eventAttender.getEmail());
            if (idsAttender.size() <= 1)
                indexAttender.remove(eventAttender.getEmail());
            else
                idsAttender.remove(event.getId());
        }
    }
}
