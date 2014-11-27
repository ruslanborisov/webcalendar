package com.webcalendar.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class EncodeHelperTest {

    @Test
    public void testEncode() {

        EncodeHelper encodeHelper = new EncodeHelper();
        String expectedEncodePassword = "dc0fa7df3d07904a09288bd2d2bb5f40";
        String actualEncodePassword = encodeHelper.encode("7777777");
        assertEquals(expectedEncodePassword, actualEncodePassword);
    }

    @Test
    public void testMatchesTrue() {

        EncodeHelper encodeHelper = new EncodeHelper();

        String encodePassword = encodeHelper.encode("7777777");
        assertTrue(encodeHelper.matches("7777777", encodePassword));
    }

    @Test
    public void testMatchesFalse() {

        EncodeHelper encodeHelper = new EncodeHelper();

        String encodePassword = encodeHelper.encode("6666666");
        assertFalse(encodeHelper.matches("7777777", encodePassword));
    }
}
