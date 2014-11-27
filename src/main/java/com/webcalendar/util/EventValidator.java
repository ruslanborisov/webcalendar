package com.webcalendar.util;

import com.webcalendar.domain.*;
import com.webcalendar.exception.ValidationException;
import java.time.LocalDate;

/**
 * Validate event before it will be added to data store and databases
 *
 *
 * @author Ruslan Borisov
 */
public class EventValidator {

    public static void validate(Event event) throws ValidationException {
        if(event==null) throw new IllegalArgumentException();

    //null check
        if(event.getId()==null) throw new ValidationException("Null value of Id of event");
        if(event.getTitle()==null) throw new ValidationException("Null value of Title of event");
        if(event.getStartDate()==null) throw new ValidationException("Null value of StartDate of event");
        if(event.getEndDate()==null) throw new ValidationException("Null value of EndDate of event");
        if(event.getColor()==null) throw new ValidationException("Null value of Color of event");

    //not specified
        if(event.getTitle().length()==0) throw new ValidationException("Not specified event title");

    //mistakes of logic
        if(event.getStartDate().isAfter(event.getEndDate()))
            throw new ValidationException("Start date after end date");
        if(!event.isAllDay() && event.getStartDate().equals(event.getEndDate()) &&
                event.getStartTime().isAfter(event.getEndTime()))
            throw new ValidationException("Start time after end time");
        if(event.getStartDate().isBefore(LocalDate.now()))
            throw new ValidationException("Start date before current date");
    }
}

