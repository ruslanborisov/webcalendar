package com.webcalendar.domain;

import com.webcalendar.exception.DateTimeFormatException;
import org.json.JSONObject;
import org.junit.Test;
import java.util.UUID;
import static org.junit.Assert.assertEquals;

public class EventAttenderTest {

    @Test
    public void testToJson() throws DateTimeFormatException {

        EventAttender attender = new EventAttender (UUID.randomUUID(),
                                            "Denis", "Milyaev", "denis@ukr.net");

        JSONObject expectedResult = new JSONObject();
        expectedResult.put("id", attender.getId());
        expectedResult.put("name", "Denis");
        expectedResult.put("lastName", "Milyaev");
        expectedResult.put("email", "denis@ukr.net");

        JSONObject actualResult = attender.toJson();

        assertEquals(expectedResult.toString(), actualResult.toString());
    }

    @Test
    public void testGetTextAboutAttender() throws DateTimeFormatException {
        EventAttender attender = new EventAttender (UUID.randomUUID(),
                "Denis", "Milyaev", "denis@ukr.net");

        String expectedResult = "Denis Milyaev. Email: denis@ukr.net";

        String actualResult = attender.getTextAboutAttender();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetTextAboutAttenderWithOutName() throws DateTimeFormatException {
        EventAttender attender = new EventAttender (UUID.randomUUID(),
                null, null, "denis@ukr.net");

        String expectedResult = "Email: denis@ukr.net";

        String actualResult = attender.getTextAboutAttender();
        assertEquals(expectedResult, actualResult);
    }
}
