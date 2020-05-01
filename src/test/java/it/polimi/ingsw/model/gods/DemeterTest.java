package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DemeterTest {

    @Test
    public void demeterCheckMultipleBuildTest()  {

        Board.clearInstances();
        Match.clearInstances();

        Demeter demeter = new Demeter();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(),  match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        demeter.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        assertTrue(demeter.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        demeter.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertTrue(demeter.checkBuild(worker, match.getMatchBoard().getCell(1, 0), BlockType.LEVEL_ONE));
        demeter.executeBuild(worker, match.getMatchBoard().getCell(1, 0), BlockType.LEVEL_ONE);

        assertFalse(demeter.checkBuild(worker, match.getMatchBoard().getCell(0, 0), BlockType.LEVEL_ONE));

    }

    @Test
    public void demeterCheckMultipleBuildSameSpaceTest()  {

        Board.clearInstances();
        Match.clearInstances();

        Demeter demeter = new Demeter();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(),  match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        demeter.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        assertTrue(demeter.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        demeter.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertFalse(demeter.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_TWO));

    }

    @Test
    public void getGodInfoTest() {
        Demeter demeter = new Demeter();

        Map<String, String> info = demeter.getGodInfo();

        assertEquals(info.get("name"), Demeter.NAME);
        assertEquals(info.get("description"), Demeter.DESCRIPTION);
        assertEquals(info.get("power_description"), Demeter.POWER_DESCRIPTION);
    }

    @Test
    public void demeterEndTurnTest() {
        Board.clearInstances();
        Match.clearInstances();

        Demeter demeter = new Demeter();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(),  match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        assertTrue(demeter.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        demeter.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        // cannot move again because the limit number of movements was reached
        assertFalse(demeter.checkMove(worker, match.getMatchBoard().getCell(0, 2)));

        assertTrue(demeter.checkBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_ONE));
        demeter.executeBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_ONE);

        assertTrue(demeter.checkBuild(worker, match.getMatchBoard().getCell(1, 1), BlockType.LEVEL_ONE));
        demeter.executeBuild(worker, match.getMatchBoard().getCell(1, 1), BlockType.LEVEL_ONE);

        assertTrue(demeter.checkEndTurn());
        demeter.endTurn(player);

        //Now I check that I can move/build again (so the values are correctly reinitialized
        assertTrue(demeter.checkMove(worker, match.getMatchBoard().getCell(1, 1)));
        demeter.executeMove(worker, match.getMatchBoard().getCell(1, 1));

        assertTrue(demeter.checkBuild(worker, match.getMatchBoard().getCell(1, 2), BlockType.LEVEL_ONE));
        demeter.executeBuild(worker, match.getMatchBoard().getCell(1, 2), BlockType.LEVEL_ONE);
    }
}
