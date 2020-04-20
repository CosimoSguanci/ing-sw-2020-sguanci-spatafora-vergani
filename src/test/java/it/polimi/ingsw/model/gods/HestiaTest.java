package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HestiaTest {

    @Test
    public void hestiaCheckMultipleBuildTest() throws Exception {
        Hestia hestia = new Hestia();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = new Worker(player, match.getMatchBoard());

        worker.setInitialPosition(0, 0);

        hestia.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        assertTrue(hestia.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        hestia.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertTrue(hestia.checkBuild(worker, match.getMatchBoard().getCell(2, 2), BlockType.LEVEL_ONE));
        hestia.executeBuild(worker, match.getMatchBoard().getCell(2, 2), BlockType.LEVEL_ONE); // NOT Perimeter Space

        assertFalse(hestia.checkBuild(worker, match.getMatchBoard().getCell(0, 0), BlockType.LEVEL_ONE));

        Board.clearInstances();
        Match.clearInstances();
    }

    @Test
    public void demeterCheckMultipleBuildPerimeterSpaceTest() throws Exception {
        Hestia hestia = new Hestia();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = new Worker(player, match.getMatchBoard());

        worker.setInitialPosition(0, 0);

        hestia.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        assertTrue(hestia.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        hestia.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertFalse(hestia.checkBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_TWO)); // 0,1

        Board.clearInstances();
        Match.clearInstances();
    }
}
