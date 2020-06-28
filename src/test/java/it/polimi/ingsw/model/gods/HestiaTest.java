package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HestiaTest {

    @Test
    public void hestiaCheckMultipleBuildTest() {


        Hestia hestia = new Hestia();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
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
    public void demeterCheckMultipleBuildPerimeterSpaceTest() {


        Hestia hestia = new Hestia();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        hestia.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        assertTrue(hestia.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        hestia.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertFalse(hestia.checkBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_TWO)); // 0,1

    }


    @Test
    public void hestiaEndTurnTest() {


        Hestia hestia = new Hestia();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
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
        hestia.endPlayerTurn(player);

        //Now I check that I can move/build again (so the values are correctly reinitialized
        assertTrue(hestia.checkMove(worker, match.getMatchBoard().getCell(1, 1)));
        hestia.executeMove(worker, match.getMatchBoard().getCell(1, 1));

        assertTrue(hestia.checkBuild(worker, match.getMatchBoard().getCell(1, 2), BlockType.LEVEL_ONE));
        hestia.executeBuild(worker, match.getMatchBoard().getCell(1, 2), BlockType.LEVEL_ONE);
    }
}
