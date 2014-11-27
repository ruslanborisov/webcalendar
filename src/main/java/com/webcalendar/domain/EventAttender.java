package com.webcalendar.domain;

import org.hibernate.annotations.Type;
import org.json.JSONObject;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="Attenders")
public class EventAttender implements Serializable, Comparable<EventAttender> {

    @Id
    @Type(type="uuid-char")
    @Column(name="id", nullable=false)
    private UUID id;

    @Column(name="name")
    private String name;

    @Column(name="lastName")
    private String lastName;

    @Column(name="email")
    private String email;

    @ManyToMany(mappedBy="eventAttenders")
    private List<EventAdapter> events = new ArrayList<>();

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    public EventAttender(UUID id, String name, String lastName, String email) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }
    public EventAttender(){
    }

    public String getTextAboutAttender() {

        StringBuilder sbAttenders = new StringBuilder();
        if (name!=null && lastName!=null) {
            sbAttenders.append(name)
                       .append(" ")
                       .append(lastName)
                       .append(". ")
                       .append("Email: ")
                       .append(email);
        } else {
            sbAttenders.append("Email: ")
                       .append(email);
        }
        return sbAttenders.toString();
    }

    public JSONObject toJson() {
        JSONObject eventJson = new JSONObject();
        eventJson.put("id", id);
        if (name!=null && lastName!=null) {
            eventJson.put("name", name);
            eventJson.put("lastName", lastName);
            eventJson.put("email", email);
        } else {
            eventJson.put("email", email);
        }
        return eventJson;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof EventAttender)) return false;
        if (this == obj) return true;

        EventAttender eventAttender = (EventAttender) obj;

        if (email != null ? !email.equals(eventAttender.email) : eventAttender.email != null) return false;
        if (lastName != null ? !lastName.equals(eventAttender.lastName) : eventAttender.lastName != null) return false;
        if (name != null ? !name.equals(eventAttender.name) : eventAttender.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (name==null && lastName==null) {
            sb.append(email);
            return sb.toString();
        } else {
            sb.append(name).append(" ")
              .append(lastName).append(" - ")
              .append(email);
            return sb.toString();
        }
    }

    @Override
    public int compareTo(EventAttender obj) {
        if (obj == null) return 1;
        EventAttender eventAttender = (EventAttender) obj;
        int result = name.compareTo(eventAttender.name);
        if (result != 0) return (int) (result / Math.abs(result));
        result = lastName.compareTo(eventAttender.lastName);
        if (result != 0) return (int) (result / Math.abs(result));
        result = email.compareTo(eventAttender.email);
        return (result != 0) ? (int) (result / Math.abs(result)) : 0;
    }
}
