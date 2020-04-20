package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class HephaestusTest {
    @Test
    public void hephaestusCheckMultipleBuildTest() throws Exception {
        Hephaestus hephaestus = new Hephaestus();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = new Worker(player, match.getMatchBoard());

        worker.setInitialPosition(0, 0);

        hephaestus.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        //assertTrue(hephaestus.checkBuild()); Check also "normal" things?

        hephaestus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertTrue(hephaestus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_TWO));

        hephaestus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_TWO);

        assertFalse(hephaestus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_THREE));

        Board.clearInstances();
        Match.clearInstances();
    }

    @Test
    public void hephaestusCheckMultipleBuildAnotherCellTest() throws Exception { // check multiple build on different positions, should always fail
        Hephaestus hephaestus = new Hephaestus();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = new Worker(player, match.getMatchBoard());

        worker.setInitialPosition(0, 0);

        hephaestus.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        //assertTrue(hephaestus.checkBuild()); Check also "normal" things?

        hephaestus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertFalse(hephaestus.checkBuild(worker, match.getMatchBoard().getCell(1, 0), BlockType.LEVEL_TWO));

        Board.clearInstances();
        Match.clearInstances();
    }

    @Test
    public void hephaestusCheckMultipleBuildDomeTest() throws Exception { // check multiple build on same position but with a dome, should fail
        Hephaestus hephaestus = new Hephaestus();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = new Worker(player, match.getMatchBoard());

        worker.setInitialPosition(0, 0);

        hephaestus.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        //assertTrue(hephaestus.checkBuild()); Check also "normal" things?

        match.getMatchBoard().getCell(0, 1).setLevel(BlockType.LEVEL_TWO);

        hephaestus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_THREE);

        assertFalse(hephaestus.checkBuild(worker, match.getMatchBoard().getCell(1, 0), BlockType.DOME));

        Board.clearInstances();
        Match.clearInstances();
    }
}