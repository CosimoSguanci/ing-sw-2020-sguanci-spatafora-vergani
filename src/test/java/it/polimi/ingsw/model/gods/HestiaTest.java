package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HestiaTest {

    @Test
    public void hestiaCheckMultipleBuildTest()  {

        Board.clearInstances();
        Match.clearInstances();

        Hestia hestia = new Hestia();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        hestia.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        assertTrue(hestia.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        hestia.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertTrue(hestia.checkBuild(worker, match.getMatchBoard().getCell(2, 2), BlockType.LEVEL_ONE));
        hestia.executeBuild(worker, match.getMatchBoard().getCell(2, 2), BlockType.LEVEL_ONE); // NOT Perimeter Space

        assertFalse(hestia.checkBuild(worker, match.getMatchBoard().getCell(0, 0), BlockType.LEVEL_ONE));

    }

    @Test
    public void demeterCheckMultipleBuildPerimeterSpaceTest()  {

        Board.clearInstances();
        Match.clearInstances();

        Hestia hestia = new Hestia();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        hestia.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        assertTrue(hestia.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        hestia.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertFalse(hestia.checkBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_TWO)); // 0,1

    }

    @Test
    public void getGodInfoTest() {
        Hestia hestia = new Hestia();

        Map<String, String> info = hestia.getGodInfo();

        assertEquals(info.get("name"), Hestia.NAME);
        assertEquals(info.get("description"), Hestia.DESCRIPTION);
        assertEquals(info.get("power_description"), Hestia.POWER_DESCRIPTION);
    }

    @Test
    public void hestiaEndTurnTest() {
        Board.clearInstances();
        Match.clearInstances();

        Hestia hestia = new Hestia();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        assertTrue(hestia.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        hestia.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        // cannot move again because the limit number of movements was reached
        assertFalse(hestia.checkMove(worker, match.getMatchBoard().getCell(0, 2)));

        assertTrue(hestia.checkBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_ONE));
        hestia.executeBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_ONE);

        assertTrue(hestia.checkBuild(worker, match.getMatchBoard().getCell(1, 1), BlockType.LEVEL_ONE));
        hestia.executeBuild(worker, match.getMatchBoard().getCell(1, 1), BlockType.LEVEL_ONE);

        assertTrue(hestia.checkEndTurn());
        hestia.endTurn(player);

        //Now I check that I can move/build again (so the values are correctly reinitialized
        assertTrue(hestia.checkMove(worker, match.getMatchBoard().getCell(1, 1)));
        hestia.executeMove(worker, match.getMatchBoard().getCell(1, 1));

        assertTrue(hestia.checkBuild(worker, match.getMatchBoard().getCell(1, 2), BlockType.LEVEL_ONE));
        hestia.executeBuild(worker, match.getMatchBoard().getCell(1, 2), BlockType.LEVEL_ONE);
    }
}
