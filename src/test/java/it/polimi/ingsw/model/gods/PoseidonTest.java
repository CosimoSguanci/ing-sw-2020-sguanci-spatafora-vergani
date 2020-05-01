package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PoseidonTest {
    @Test
    public void poseidonCheckBuildGroundTest()  { // Unmoved worker is on ground at the end of the turn

        Board.clearInstances();
        Match.clearInstances();

        Poseidon poseidon = new Poseidon();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(),  match);
        Worker movedWorker = player.getWorkerFirst();
        Worker unmovedWorker = player.getWorkerSecond();

        movedWorker.setInitialPosition(0, 0);
        unmovedWorker.setInitialPosition(4, 0);

        poseidon.executeMove(movedWorker, match.getMatchBoard().getCell(1, 1));
        assertTrue(poseidon.checkBuild(movedWorker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        poseidon.executeBuild(movedWorker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertTrue(poseidon.checkBuild(unmovedWorker, match.getMatchBoard().getCell(4, 1), BlockType.LEVEL_ONE));
        poseidon.executeBuild(unmovedWorker, match.getMatchBoard().getCell(4, 1), BlockType.LEVEL_ONE);

        assertTrue(poseidon.checkBuild(unmovedWorker, match.getMatchBoard().getCell(4, 1), BlockType.LEVEL_TWO));
        poseidon.executeBuild(unmovedWorker, match.getMatchBoard().getCell(4, 1), BlockType.LEVEL_TWO);

        assertTrue(poseidon.checkBuild(unmovedWorker, match.getMatchBoard().getCell(4, 1), BlockType.LEVEL_THREE));
        poseidon.executeBuild(unmovedWorker, match.getMatchBoard().getCell(4, 1), BlockType.LEVEL_THREE);

        assertFalse(poseidon.checkBuild(unmovedWorker, match.getMatchBoard().getCell(4, 1), BlockType.DOME));
    }

    @Test
    public void poseidonCheckBuildNotGroundTest()  { // Unmoved worker is NOT on ground at the end of the turn

        Board.clearInstances();
        Match.clearInstances();

        Poseidon poseidon = new Poseidon();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(),  match);
        Worker movedWorker = player.getWorkerFirst();
        Worker unmovedWorker = player.getWorkerSecond();

        movedWorker.setInitialPosition(0, 0);

        match.getMatchBoard().getCell(4, 0).setLevel(BlockType.LEVEL_ONE);
        unmovedWorker.setInitialPosition(4, 0);


        poseidon.executeMove(movedWorker, match.getMatchBoard().getCell(1, 1));
        poseidon.executeBuild(movedWorker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertFalse(poseidon.checkBuild(unmovedWorker, match.getMatchBoard().getCell(4, 1), BlockType.LEVEL_TWO));
    }

    @Test
    public void getGodInfoTest() {
        Poseidon poseidon = new Poseidon();

        Map<String, String> info = poseidon.getGodInfo();

        assertEquals(info.get("name"), Poseidon.NAME);
        assertEquals(info.get("description"), Poseidon.DESCRIPTION);
        assertEquals(info.get("power_description"), Poseidon.POWER_DESCRIPTION);
    }

    @Test
    public void poseidonEndTurnTest() {
        Board.clearInstances();
        Match.clearInstances();

        Poseidon poseidon = new Poseidon();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(),  match);
        Worker worker = player.getWorkerFirst();
        Worker unmovedWorker = player.getWorkerSecond();

        worker.setInitialPosition(0, 0);
        unmovedWorker.setInitialPosition(3, 2);

        assertTrue(poseidon.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        poseidon.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        // cannot move again because the limit number of movements was reached
        assertFalse(poseidon.checkMove(worker, match.getMatchBoard().getCell(0, 2)));

        assertTrue(poseidon.checkBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_ONE));
        poseidon.executeBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_ONE);

        assertTrue(poseidon.checkBuild(unmovedWorker, match.getMatchBoard().getCell(3, 3), BlockType.LEVEL_ONE));
        poseidon.executeBuild(unmovedWorker, match.getMatchBoard().getCell(3, 3), BlockType.LEVEL_ONE);

        assertTrue(poseidon.checkBuild(unmovedWorker, match.getMatchBoard().getCell(3, 3), BlockType.LEVEL_TWO));
        poseidon.executeBuild(unmovedWorker, match.getMatchBoard().getCell(3, 3), BlockType.LEVEL_TWO);

        assertTrue(poseidon.checkBuild(unmovedWorker, match.getMatchBoard().getCell(3, 3), BlockType.LEVEL_THREE));
        poseidon.executeBuild(unmovedWorker, match.getMatchBoard().getCell(3, 3), BlockType.LEVEL_THREE);


        assertTrue(poseidon.checkEndTurn());
        poseidon.endTurn(player);

        //Now I check that I can move/build again (so the values are correctly reinitialized) -> repeat the turn:

        assertTrue(poseidon.checkMove(worker, match.getMatchBoard().getCell(1, 1)));
        poseidon.executeMove(worker, match.getMatchBoard().getCell(1, 1));

        assertTrue(poseidon.checkBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_TWO));
        poseidon.executeBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_TWO);

        assertTrue(poseidon.checkBuild(unmovedWorker, match.getMatchBoard().getCell(4, 3), BlockType.LEVEL_ONE));
        poseidon.executeBuild(unmovedWorker, match.getMatchBoard().getCell(4, 3), BlockType.LEVEL_ONE);

        assertTrue(poseidon.checkBuild(unmovedWorker, match.getMatchBoard().getCell(4, 3), BlockType.LEVEL_TWO));
        poseidon.executeBuild(unmovedWorker, match.getMatchBoard().getCell(4, 3), BlockType.LEVEL_TWO);

        assertTrue(poseidon.checkBuild(unmovedWorker, match.getMatchBoard().getCell(4, 3), BlockType.LEVEL_THREE));
        poseidon.executeBuild(unmovedWorker, match.getMatchBoard().getCell(4, 3), BlockType.LEVEL_THREE);

    }
}