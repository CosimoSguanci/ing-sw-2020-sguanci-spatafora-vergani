package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.gods.GodStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;

public class GodsUtilsTest {

    @Test
    public void godsFactoryDifferentInstancesTest() {
        GodStrategy apollo1 = GodsUtils.godsFactory("apollo");
        GodStrategy apollo2 = GodsUtils.godsFactory("apollo");

        assertNotSame(apollo1, apollo2);

    }
}
