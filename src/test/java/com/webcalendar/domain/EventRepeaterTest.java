package com.webcalendar.domain;

import com.webcalendar.exception.DateTimeFormatException;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static com.webcalendar.domain.EventRepeater.EnumRepeater.*;

public class EventRepeaterTest {

    @Test
    public void testToJson() throws DateTimeFormatException {

        EventRepeater repeater = new EventRepeater (1, ONCE);

        JSONObject expectedResult = new JSONObject();
        expectedResult.put("id", 1);
        expectedResult.put("repeater", "ONCE");

        JSONObject actualResult = repeater.toJson();

        assertEquals(expectedResult.toString(), actualResult.toString());
    }
}



