package com.webcalendar.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

import static com.webcalendar.domain.EventRepeater.EnumRepeater.*;

public class Event implements Serializable, Comparable<Event> {

    private final UUID id;
    private final String title;
    private final String description;

    private final boolean allDay;
    private final LocalDate startDate;
    private final LocalTime startTime;
    private final LocalDate endDatе;
    private final LocalTime endTime;
    private final Integer minutesBeforeReminder;
    private final Integer numberOfOccurrence;
    private final String color;
    private final Set<EventAttender> eventAttenders;
    private final Set<EventRepeater> eventRepeaters;
    private final Set<EventReminder> eventReminders;
    private final User user;

    public UUID getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public boolean isAllDay() {
        return allDay;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public LocalDate getEndDate() {
        return endDatе;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
    public Integer getMinutesBeforeReminder() {
        return minutesBeforeReminder;
    }
    public Integer getNumberOfOccurrence() {
        return numberOfOccurrence;
    }
    public String getColor() {
        return color;
    }
    public Set<EventAttender> getEventAttenders() {
        return eventAttenders;
    }
    public Set<EventRepeater> getEventRepeaters() {
        return eventRepeaters;
    }
    public Set<EventReminder> getEventReminders() {
        return eventReminders;
    }
    public User getUser() {
        return user;
    }

    private Event(EventBuilder eventBuilder) {

        this.id = eventBuilder.id;
        this.title = eventBuilder.title;
        this.description = eventBuilder.description;
        this.allDay = eventBuilder.allDay;
        this.startDate = eventBuilder.startDate;
        this.startTime = eventBuilder.startTime;
        this.endDatе = eventBuilder.endDate;
        this.endTime = eventBuilder.endTime;
        this.minutesBeforeReminder = eventBuilder.minutesBeforeReminder;
        this.numberOfOccurrence = eventBuilder.numberOfOccurrence;
        this.color = eventBuilder.color;
        this.eventAttenders = eventBuilder.eventAttenders;
        this.eventRepeaters = eventBuilder.eventRepeaters;
        this.eventReminders = eventBuilder.eventReminders;
        this.user = eventBuilder.user;
    }

    public EventAdapter getRigthDateForRepeatEvent(LocalDate currentDate) {

            EventAdapter eventAdapter = new EventAdapter(this);
            Set<EventRepeater> eventRepeater = eventAdapter.getEventRepeaters();

            if (eventRepeater.contains(new EventRepeater(1, ONCE))) {
                // do nothing

            } else if (eventRepeater.contains(new EventRepeater(3, MONTHLY))) {

                while (true) {
                    if (currentDate.isBefore(eventAdapter.getEndDate()) ||
                            currentDate.isEqual(eventAdapter.getEndDate()))
                        break;

                    eventAdapter.setStartDate(eventAdapter.getStartDate().plusMonths(1));
                    eventAdapter.setEndDate(eventAdapter.getEndDate().plusMonths(1));
                }

            } else if (eventRepeater.contains(new EventRepeater(4, YEARLY))) {

                while (true) {
                    if (currentDate.isBefore(eventAdapter.getEndDate()) ||
                            currentDate.isEqual(eventAdapter.getEndDate()))
                        break;

                    eventAdapter.setStartDate(eventAdapter.getStartDate().plusYears(1));
                    eventAdapter.setEndDate(eventAdapter.getEndDate().plusYears(1));
                }

            } else {
// MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, DAILY
                eventAdapter.setStartDate(currentDate);
                eventAdapter.setEndDate(currentDate);
            }
            return eventAdapter;
        }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        if (allDay != event.allDay) return false;
        if (!color.equals(event.color)) return false;
        if (description != null ? !description.equals(event.description) : event.description != null) return false;
        if (!endDatе.equals(event.endDatе)) return false;
        if (endTime != null ? !endTime.equals(event.endTime) : event.endTime != null) return false;
        if (eventAttenders != null ? !eventAttenders.equals(event.eventAttenders) : event.eventAttenders != null)
            return false;
        if (eventReminders != null ? !eventReminders.equals(event.eventReminders) : event.eventReminders != null)
            return false;
        if (!eventRepeaters.equals(event.eventRepeaters)) return false;
        if (minutesBeforeReminder != null ? !minutesBeforeReminder.equals(event.minutesBeforeReminder) :
                event.minutesBeforeReminder != null)
            return false;
        if (numberOfOccurrence != null ? !numberOfOccurrence.equals(event.numberOfOccurrence) :
                event.numberOfOccurrence != null)
            return false;
        if (!startDate.equals(event.startDate)) return false;
        if (startTime != null ? !startTime.equals(event.startTime) : event.startTime != null) return false;
        if (!title.equals(event.title)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (allDay ? 1 : 0);
        result = 31 * result + startDate.hashCode();
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + endDatе.hashCode();
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (minutesBeforeReminder != null ? minutesBeforeReminder.hashCode() : 0);
        result = 31 * result + (numberOfOccurrence != null ? numberOfOccurrence.hashCode() : 0);
        result = 31 * result + color.hashCode();
        result = 31 * result + (eventAttenders != null ? eventAttenders.hashCode() : 0);
        result = 31 * result + eventRepeaters.hashCode();
        result = 31 * result + (eventReminders != null ? eventReminders.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Event event) {
        int result = 0;
        if (allDay && event.isAllDay()) result = 0;
        if (!allDay && event.isAllDay()) result = 1;
        if (allDay && !event.isAllDay()) result = -1;
        if (result != 0) return result;

        result = startDate.compareTo(event.startDate);
        if (result != 0) return (int) (result / Math.abs(result));
        if(startTime!=null && event.startTime!=null)
            result = startTime.compareTo(event.startTime);
        if (result != 0) return (int) (result / Math.abs(result));
        result = endDatе.compareTo(event.endDatе);
        if (result != 0) return (int) (result / Math.abs(result));
        if(endTime!=null && event.endTime!=null)
            result = endTime.compareTo(event.endTime);
        if (result != 0) return (int) (result / Math.abs(result));
        result = title.compareTo(event.title);
        if (result != 0) return (int) (result / Math.abs(result));
        result = description.compareTo(event.description);

        return (result != 0) ? (int) (result / Math.abs(result)) : 0;
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder("Event { ");

        if (startTime==null && endTime==null) {
            sb.append(id).append(", ")
                    .append(title).append(", ")
                    .append(description).append(", ")
                    .append(startDate).append(", ")
                    .append(endDatе).append(", ")
                    .append(eventAttenders).append(" } \n");
        } else {
            sb.append(id).append(", ")
                    .append(title).append(", ")
                    .append(description).append(", ")
                    .append(startDate).append(" ").append(startTime).append("-")
                    .append(endDatе).append(" ").append(endTime).append(", ")
                    .append(eventAttenders).append(" } \n");
        }

        return sb.toString();
    }

    public static class EventBuilder {
        private UUID id;
        private String title;
        private String description;

        private boolean allDay;
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalDate endDate;
        private LocalTime endTime;
        private Integer minutesBeforeReminder;
        private Integer numberOfOccurrence;
        private String color;

        private Set<EventAttender> eventAttenders;
        private Set<EventRepeater> eventRepeaters;
        private Set<EventReminder> eventReminders;
        private User user;

        public EventBuilder() {
        }

        public EventBuilder(Event originalEvent) {
            this.id = originalEvent.id;
            this.title = originalEvent.title;
            this.description = originalEvent.description;

            this.allDay = originalEvent.allDay;
            this.startDate = originalEvent.startDate;
            this.startTime = originalEvent.startTime;
            this.endDate = originalEvent.endDatе;
            this.endTime = originalEvent.endTime;
            this.minutesBeforeReminder = originalEvent.minutesBeforeReminder;
            this.numberOfOccurrence = originalEvent.numberOfOccurrence;
            this.color = originalEvent.color;

            this.eventAttenders = originalEvent.eventAttenders;
            this.eventRepeaters = originalEvent.eventRepeaters;
            this.eventReminders = originalEvent.eventReminders;
            this.user = originalEvent.user;
        }

        public EventBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public EventBuilder title(String title) {
            this.title = title;
            return this;
        }

        public EventBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EventBuilder allDay(boolean allDay) {
            this.allDay = allDay;
            return this;
        }

        public EventBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public EventBuilder startTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public EventBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public EventBuilder endTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public EventBuilder minutesBeforeReminder(Integer minutesBeforeReminder) {
            this.minutesBeforeReminder = minutesBeforeReminder;
            return this;
        }

        public EventBuilder numberOfOccurrence(Integer numberOfOccurrence) {
            this.numberOfOccurrence = numberOfOccurrence;
            return this;
        }

        public EventBuilder color(String color) {
            this.color = color;
            return this;
        }

        public EventBuilder attendersOfEvent(Set<EventAttender> eventAttenders) {
            this.eventAttenders = eventAttenders;
            return this;
        }

        public EventBuilder repeatersOfEvent(Set<EventRepeater> eventRepeaters) {
            this.eventRepeaters = eventRepeaters;
            return this;
        }
        public EventBuilder remindersOfEvent(Set<EventReminder> eventReminders) {
            this.eventReminders = eventReminders;
            return this;
        }

        public EventBuilder user(User user) {
            this.user = user;
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }
}



