package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.exceptions.UnknownGodException;
import it.polimi.ingsw.model.gods.GodStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GodsUtilsTest {

    @Test
    public void godsFactoryDifferentInstancesTest() {
        GodStrategy apollo1 = GodsUtils.godsFactory("apollo");
        GodStrategy apollo2 = GodsUtils.godsFactory("apollo");

        assertNotSame(apollo1, apollo2);

        assertThrows(UnknownGodException.class, () -> GodsUtils.godsFactory("not a valid god"));
    }

    @Test
    public void isValidGodTest() {
        assertTrue(GodsUtils.isValidGod("APOLLO"));
        assertFalse(GodsUtils.isValidGod("not a valid god"));
    }

    @Test
    public void parseGodNameTest() {
        String god = "apollo";
        assertEquals(GodsUtils.getGodsInfo().get(god), GodsUtils.parseGodName(god));

        god = "artemis";
        assertEquals(GodsUtils.getGodsInfo().get(god), GodsUtils.parseGodName(god));

        god = "athena";
        assertEquals(GodsUtils.getGodsInfo().get(god), GodsUtils.parseGodName(god));

        god = "atlas";
        assertEquals(GodsUtils.getGodsInfo().get(god), GodsUtils.parseGodName(god));

        god = "demeter";
        assertEquals(GodsUtils.getGodsInfo().get(god), GodsUtils.parseGodName(god));

        god = "eros";
        assertEquals(GodsUtils.getGodsInfo().get(god), GodsUtils.parseGodName(god));

        god = "hera";
        assertEquals(GodsUtils.getGodsInfo().get(god), GodsUtils.parseGodName(god));

        god = "hestia";
        assertEquals(GodsUtils.getGodsInfo().get(god), GodsUtils.parseGodName(god));

        god = "minotaur";
        assertEquals(GodsUtils.getGodsInfo().get(god), GodsUtils.parseGodName(god));

        god = "pan";
        assertEquals(GodsUtils.getGodsInfo().get(god), GodsUtils.parseGodName(god));

        god = "prometheus";
        assertEquals(GodsUtils.getGodsInfo().get(god), GodsUtils.parseGodName(god));

        god = "poseidon";
        assertEquals(GodsUtils.getGodsInfo().get(god), GodsUtils.parseGodName(god));

        god = "zeus";
        assertEquals(GodsUtils.getGodsInfo().get(god), GodsUtils.parseGodName(god));

        god = "hephaestus";
        assertEquals(GodsUtils.getGodsInfo().get(god), GodsUtils.parseGodName(god));

        assertThrows(UnknownGodException.class, () -> GodsUtils.parseGodName("not a valid god"));
    }
}
