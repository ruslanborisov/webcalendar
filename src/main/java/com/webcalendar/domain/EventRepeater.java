package com.webcalendar.domain;

import org.json.JSONObject;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Repeaters")
public class EventRepeater implements Serializable {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "repeater", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumRepeater repeater;

    @ManyToMany(mappedBy = "eventRepeaters")
    private List<EventAdapter> events = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EnumRepeater getRepeater() {
        return repeater;
    }

    public void setRepeater(EnumRepeater repeater) {
        this.repeater = repeater;
    }

    public EventRepeater() {
    }

    public EventRepeater(Integer id, EnumRepeater repeater) {
        this.id = id;
        this.repeater = repeater;
    }

    public JSONObject toJson() {
        JSONObject eventJson = new JSONObject();
        eventJson.put("id", id);
        eventJson.put("repeater", repeater.name());
        return eventJson;
    }

    @Override
    public String toString() {
        switch (repeater) {
            case ONCE:
                return "Once";
            case DAILY:
                return "Daily";
            case MONTHLY:
                return "Monthly";
            case YEARLY:
                return "Yearly";
            case MONDAY:
                return "Monday";
            case TUESDAY:
                return "Tuesday";
            case WEDNESDAY:
                return "Wednesday";
            case THURSDAY:
                return "Thursday";
            case FRIDAY:
                return "Friday";
            case SATURDAY:
                return "Saturday";
            case SUNDAY:
                return "Sunday";
        }
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventRepeater)) return false;

        EventRepeater that = (EventRepeater) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (repeater != that.repeater) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (repeater != null ? repeater.hashCode() : 0);
        return result;
    }

    public enum EnumRepeater {
        ONCE, DAILY, MONTHLY, YEARLY,
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}

