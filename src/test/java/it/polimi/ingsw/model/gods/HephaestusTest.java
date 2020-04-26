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
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = new Worker(player, match.getMatchBoard());

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
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = new Worker(player, match.getMatchBoard());

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
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = new Worker(player, match.getMatchBoard());

        worker.setInitialPosition(0, 0);

        hephaestus.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        //assertTrue(hephaestus.checkBuild()); Check also "normal" things?

        match.getMatchBoard().getCell(0, 1).setLevel(BlockType.LEVEL_TWO);

        hephaestus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_THREE);

        assertFalse(hephaestus.checkBuild(worker, match.getMatchBoard().getCell(1, 0), BlockType.DOME));

    }

    @Test
    public void getGodInfoTest() {
        Hephaestus hephaestus = new Hephaestus();

        Map<String, String> info = hephaestus.getGodInfo();

        assertEquals(info.get("name"), Hephaestus.NAME);
        assertEquals(info.get("description"), Hephaestus.DESCRIPTION);
        assertEquals(info.get("power_description"), Hephaestus.POWER_DESCRIPTION);
    }
}