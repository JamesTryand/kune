package org.ourproject.kune.platf.client.ui;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

public class KuneStringUtilsTest {

    @Test
    public void testTagStripsSimple() {
        String tagsString = "ab cd";
        ArrayList<String> tags = KuneStringUtils.splitTags(tagsString);
        assertEquals("ab", tags.get(0));
        assertEquals("cd", tags.get(1));
    }

    @Test
    public void testTagStripsWithQuotes() {
        String tagsString = "ab \"cd\"";
        ArrayList<String> tags = KuneStringUtils.splitTags(tagsString);
        assertEquals("ab", tags.get(0));
        assertEquals("cd", tags.get(1));
    }

    @Test
    public void testTagStripsWithSpaces() {
        String tagsString = "    ab       cd    ";
        ArrayList<String> tags = KuneStringUtils.splitTags(tagsString);
        assertEquals("ab", tags.get(0));
        assertEquals("cd", tags.get(1));
    }

    @Test
    public void testTagStripsWithCommas() {
        String tagsString = "ab,cd";
        ArrayList<String> tags = KuneStringUtils.splitTags(tagsString);
        assertEquals("ab", tags.get(0));
        assertEquals("cd", tags.get(1));
    }

    @Test
    public void testTagStripsWithCommasAndSpaces() {
        String tagsString = "ab, cd";
        ArrayList<String> tags = KuneStringUtils.splitTags(tagsString);
        assertEquals("ab", tags.get(0));
        assertEquals("cd", tags.get(1));
    }

}