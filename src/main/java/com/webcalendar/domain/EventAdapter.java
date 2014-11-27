package com.webcalendar.domain;

import com.webcalendar.exception.DateTimeFormatException;
import com.webcalendar.util.DateHelper;
import org.hibernate.annotations.Type;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="Events")
public class EventAdapter implements Serializable, Comparable<EventAdapter> {

    @Id
    @Type(type="uuid-char")
    @Column(name = "id", nullable=false)
    private UUID id;
    @Column(name="title", nullable=false)
    private String title;
    @Column(name="description")
    private String description;

    @Column(name="allDay", nullable=false)
    private boolean allDay;
    @Column(name="startDate", nullable=false)
    private LocalDate startDate;
    @Column(name="startTime")
    private LocalTime startTime;
    @Column(name="endDate", nullable=false)
    private LocalDate endDate;
    @Column(name="endTime")
    private LocalTime endTime;
    @Column(name="minutesBeforeReminder")
    private Integer minutesBeforeReminder;
    @Column(name="occurrence")
    private Integer numberOfOccurrence;
    @Column(name="color", nullable=false)
    private String color;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="event_attender", joinColumns=@JoinColumn(name="event_id"),
            inverseJoinColumns=@JoinColumn(name="attender_id"))
    private Set<EventAttender> eventAttenders = new HashSet<EventAttender>();

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="event_repeater", joinColumns=@JoinColumn(name="event_id"),
            inverseJoinColumns=@JoinColumn(name="repeater_id"))
    private Set<EventRepeater> eventRepeaters = new HashSet<EventRepeater>();

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="event_reminder", joinColumns=@JoinColumn(name="event_id"),
            inverseJoinColumns=@JoinColumn(name="reminder_id"))
    private Set<EventReminder> eventReminders = new HashSet<>();

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isAllDay() {
        return allDay;
    }
    public void setAllDay(boolean isAllDay) {
        this.allDay = isAllDay;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    public Integer getMinutesBeforeReminder() {
        return minutesBeforeReminder;
    }
    public void setMinutesBeforeReminder(Integer minutesBeforeReminder) {
        this.minutesBeforeReminder = minutesBeforeReminder;
    }
    public Integer getNumberOfOccurrence() {
        return numberOfOccurrence;
    }
    public void setNumberOfOccurrence(Integer numberOfOccurrence) {
        this.numberOfOccurrence = numberOfOccurrence;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public Set<EventAttender> getEventAttenders() {
        return eventAttenders;
    }
    public void setEventAttenders(Set<EventAttender> eventAttenders) {
        this.eventAttenders = eventAttenders;
    }
    public Set<EventRepeater> getEventRepeaters() {
        return eventRepeaters;
    }
    public void setEventRepeaters(Set<EventRepeater> eventRepeaters) {
        this.eventRepeaters = eventRepeaters;
    }
    public Set<EventReminder> getEventReminders() {
        return eventReminders;
    }
    public void setEventReminders(Set<EventReminder> eventReminders) {
        this.eventReminders = eventReminders;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public EventAdapter(){};

    public EventAdapter(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.allDay = event.isAllDay();
        this.startDate = event.getStartDate();
        this.startTime = event.getStartTime();
        this.endDate = event.getEndDate();
        this.endTime = event.getEndTime();
        this.minutesBeforeReminder = event.getMinutesBeforeReminder();
        this.numberOfOccurrence = event.getNumberOfOccurrence();
        this.color = event.getColor();
        this.eventRepeaters = event.getEventRepeaters();
        this.eventAttenders = event.getEventAttenders();
        this.eventReminders = event.getEventReminders();
        this.user = event.getUser();
    }

    public JSONObject toJson() {
        JSONObject eventJson = new JSONObject();
        eventJson.put("id", id);
        eventJson.put("title", title);
        eventJson.put("description", description);
        eventJson.put("allDay", allDay);
        eventJson.put("color", color);

        try {
            eventJson.put("startDate", DateHelper.dateToString(startDate));
            eventJson.put("endDate", DateHelper.dateToString(endDate));

            if(!allDay) {
                eventJson.put("startTime", DateHelper.timeToString(startTime));
                eventJson.put("endTime", DateHelper.timeToString(endTime));
            }

        eventJson.put("reminderTime", minutesBeforeReminder);

        } catch (DateTimeFormatException e) {
            e.printStackTrace();
            return null;
        }

        if(numberOfOccurrence!=null)
            eventJson.put("numberOfOccurrence", numberOfOccurrence);

        JSONArray jsonArrayAttenders = new JSONArray();
        for (EventAttender eventAttender : eventAttenders)
            jsonArrayAttenders.put(eventAttender.toJson());

        eventJson.put("eventAttenders", jsonArrayAttenders);

        JSONArray jsonArrayRepeaters = new JSONArray();
        for (EventRepeater eventRepeater : eventRepeaters)
            jsonArrayRepeaters.put(eventRepeater.toJson());

        eventJson.put("eventRepeaters", jsonArrayRepeaters);

        JSONArray jsonArrayReminders = new JSONArray();
        for (EventReminder eventReminder : eventReminders)
            jsonArrayReminders.put(eventReminder.toJson());

        eventJson.put("eventReminders", jsonArrayReminders);

        return eventJson;
    }

    public String getTextAboutEvent() throws DateTimeFormatException {

        StringBuilder sbAttenders = new StringBuilder();
        String attenders = "No guests";
        if(description.length()==0)
            description = "No description";

        if (eventAttenders.size()>0) {
            attenders = "";
            for (EventAttender attender : eventAttenders)
                sbAttenders.append(attender.getTextAboutAttender()).append(", ");

            attenders = sbAttenders.toString().substring(0, sbAttenders.toString().length() - 2);
        }

        StringBuilder sbEvent = new StringBuilder("Event with title ");

        if (allDay) {
            sbEvent.append(title).append(". \n")
                    .append("Description of event: ").append(description)
                    .append(". \n")
                    .append("Period of event: ")
                    .append(DateHelper.periodToStringDescription(startDate, endDate))
                    .append(". \n")
                    .append("Guests of event: ")
                    .append(attenders);
        } else {
            sbEvent.append(this.title)
                    .append(". \n")
                    .append("Description of event: ")
                    .append(this.description)
                    .append(". \n")
                    .append("Period of event: ")
                    .append(DateHelper.periodWithTimeToStringDescription(LocalDateTime.of(startDate, startTime),
                            LocalDateTime.of(endDate, endTime))).append(". \n")
                    .append("Guests of event: ")
                    .append(attenders)
                    .append(".");
        }
        return sbEvent.toString();
    }

    @Override
    public int compareTo(EventAdapter event) {
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
        result = endDate.compareTo(event.endDate);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventAdapter)) return false;

        EventAdapter that = (EventAdapter) o;

        if (allDay != that.allDay) return false;
        if (!color.equals(that.color)) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (!endDate.equals(that.endDate)) return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;
        if (eventAttenders != null ? !eventAttenders.equals(that.eventAttenders) : that.eventAttenders != null)
            return false;
        if (eventReminders != null ? !eventReminders.equals(that.eventReminders) : that.eventReminders != null)
            return false;
        if (!eventRepeaters.equals(that.eventRepeaters)) return false;
        if (minutesBeforeReminder != null ? !minutesBeforeReminder.equals(that.minutesBeforeReminder) :
                that.minutesBeforeReminder != null)
            return false;
        if (numberOfOccurrence != null ? !numberOfOccurrence.equals(that.numberOfOccurrence) :
                that.numberOfOccurrence != null)
            return false;
        if (!startDate.equals(that.startDate)) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;
        if (!title.equals(that.title)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (allDay ? 1 : 0);
        result = 31 * result + startDate.hashCode();
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + endDate.hashCode();
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (minutesBeforeReminder != null ? minutesBeforeReminder.hashCode() : 0);
        result = 31 * result + (numberOfOccurrence != null ? numberOfOccurrence.hashCode() : 0);
        result = 31 * result + color.hashCode();
        result = 31 * result + (eventAttenders != null ? eventAttenders.hashCode() : 0);
        result = 31 * result + eventRepeaters.hashCode();
        result = 31 * result + (eventReminders != null ? eventReminders.hashCode() : 0);
        return result;
    }
}
