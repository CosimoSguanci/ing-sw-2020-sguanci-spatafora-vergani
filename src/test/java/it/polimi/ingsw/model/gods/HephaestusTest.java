package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class HephaestusTest {
    @Test
    public void hephaestusCheckMultipleBuildTest()  {

        Board.clearInstances();
        Match.clearInstances();

        Hephaestus hephaestus = new Hephaestus();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(),  match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        hephaestus.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        //assertTrue(hephaestus.checkBuild()); Check also "normal" things?

        hephaestus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertTrue(hephaestus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_TWO));

        hephaestus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_TWO);

        assertFalse(hephaestus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_THREE));

    }

    @Test
    public void hephaestusCheckMultipleBuildAnotherCellTest()  { // check multiple build on different positions, should always fail

        Board.clearInstances();
        Match.clearInstances();

        Hephaestus hephaestus = new Hephaestus();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(),  match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        hephaestus.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        //assertTrue(hephaestus.checkBuild()); Check also "normal" things?

        hephaestus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertFalse(hephaestus.checkBuild(worker, match.getMatchBoard().getCell(1, 0), BlockType.LEVEL_TWO));

    }

    @Test
    public void hephaestusCheckMultipleBuildDomeTest()  { // check multiple build on same position but with a dome, should fail

        Board.clearInstances();
        Match.clearInstances();

        Hephaestus hephaestus = new Hephaestus();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(),  match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        hephaestus.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        //assertTrue(hephaestus.checkBuild()); Check also "normal" things?

        match.getMatchBoard().getCell(0, 1).setLevel(BlockType.LEVEL_TWO);

        hephaestus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_THREE);

        assertFalse(hephaestus.checkBuild(worker, match.getMatchBoard().getCell(1, 0), BlockType.DOME));

    }

    @Test
    public void hephaestusEndTurnTest() { // TODO Add tests to check previousCell was reset?
        Board.clearInstances();
        Match.clearInstances();

        Hephaestus hephaestus = new Hephaestus();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(),  match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        assertTrue(hephaestus.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        hephaestus.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        // cannot move again because the limit number of movements was reached
        assertFalse(hephaestus.checkMove(worker, match.getMatchBoard().getCell(0, 2)));

        assertTrue(hephaestus.checkBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_ONE));
        hephaestus.executeBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_ONE);

        assertTrue(hephaestus.checkBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_TWO));
        hephaestus.executeBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_TWO);

        assertTrue(hephaestus.checkEndTurn());
        hephaestus.endTurn(player);

        //Now I check that I can move/build again (so the values are correctly reinitialized
        assertTrue(hephaestus.checkMove(worker, match.getMatchBoard().getCell(1, 1)));
        hephaestus.executeMove(worker, match.getMatchBoard().getCell(1, 1));

        assertTrue(hephaestus.checkBuild(worker, match.getMatchBoard().getCell(1, 2), BlockType.LEVEL_ONE));
        hephaestus.executeBuild(worker, match.getMatchBoard().getCell(1, 2), BlockType.LEVEL_ONE);
    }
}