package com.webcalendar.util;

import com.webcalendar.domain.Event;
import com.webcalendar.domain.EventAdapter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides ability to convert {@link EventAdapter} to {@link Event}
 *
 *
 * @author Ruslan Borisov
 */
public class AdapterToOrigin {

    public static Event eventAdapterToEvent(EventAdapter eventAdapter) {
        if(eventAdapter == null)
            throw new IllegalArgumentException();

        return new Event.EventBuilder()
                .id(eventAdapter.getId())
                .title(eventAdapter.getTitle())
                .description(eventAdapter.getDescription())
                .allDay(eventAdapter.isAllDay())
                .startDate(eventAdapter.getStartDate())
                .startTime(eventAdapter.getStartTime())
                .endDate(eventAdapter.getEndDate())
                .endTime(eventAdapter.getEndTime())
                .minutesBeforeReminder(eventAdapter.getMinutesBeforeReminder())
                .numberOfOccurrence(eventAdapter.getNumberOfOccurrence())
                .color(eventAdapter.getColor())
                .attendersOfEvent(eventAdapter.getEventAttenders())
                .repeatersOfEvent(eventAdapter.getEventRepeaters())
                .remindersOfEvent(eventAdapter.getEventReminders())
                .user(eventAdapter.getUser())
                .build();
    }

    public static List<Event> eventAdapterListToEventList(List<EventAdapter> eventAdapters) {
        if(eventAdapters == null) throw new IllegalArgumentException();

        return eventAdapters.stream()
                .map(AdapterToOrigin::eventAdapterToEvent)
                .collect(Collectors.toList());
    }
}
