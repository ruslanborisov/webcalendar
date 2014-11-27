package com.webcalendar.domain;

import com.webcalendar.exception.DateTimeFormatException;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static com.webcalendar.domain.EventReminder.EnumReminder.*;

public class EventReminderTest {

    @Test
    public void testToJson() throws DateTimeFormatException {

        EventReminder reminder = new EventReminder (1, POPUP);

        JSONObject expectedResult = new JSONObject();
        expectedResult.put("id", 1);
        expectedResult.put("reminder", "POPUP");

        JSONObject actualResult = reminder.toJson();

        assertEquals(expectedResult.toString(), actualResult.toString());
    }
}
