package com.webcalendar.domain;

import org.json.JSONObject;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Reminders")
public class EventReminder implements Serializable {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "reminder", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumReminder reminder;

    @ManyToMany(mappedBy="eventReminders")
    private List<EventAdapter> events = new ArrayList<>();


    public EnumReminder getReminder() {
        return reminder;
    }
    public void setReminder(EnumReminder reminder) {
        this.reminder = reminder;
    }

    public EventReminder() {
    }

    public EventReminder(Integer id, EnumReminder reminder) {
        this.id = id;
        this.reminder = reminder;
    }

    public JSONObject toJson() {
        JSONObject eventJson = new JSONObject();
        eventJson.put("id", id);
        eventJson.put("reminder", reminder.name());
        return eventJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventReminder)) return false;

        EventReminder that = (EventReminder) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (reminder != that.reminder) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (reminder != null ? reminder.hashCode() : 0);
        return result;
    }

    public enum EnumReminder {
        POPUP, EMAIL
    }
}


