package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DemeterTest {

    private Demeter demeter;
    private Match match;
    private Player player;
    private Worker worker;

    @BeforeEach
    public void initGameProperties() {

        this.demeter = new Demeter();
        this.match = new Match(2);
        this.player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        this.worker = player.getWorkerFirst();
        this.worker.setInitialPosition(0, 0);
    }

    @Test
    public void demeterCheckMultipleBuildTest() {

        demeter.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        assertTrue(demeter.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        demeter.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertTrue(demeter.checkBuild(worker, match.getMatchBoard().getCell(1, 0), BlockType.LEVEL_ONE));
        demeter.executeBuild(worker, match.getMatchBoard().getCell(1, 0), BlockType.LEVEL_ONE);

        assertFalse(demeter.checkBuild(worker, match.getMatchBoard().getCell(0, 0), BlockType.LEVEL_ONE));

    }

    @Test
    public void demeterCheckMultipleBuildSameSpaceTest() {

        demeter.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        assertTrue(demeter.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        demeter.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertFalse(demeter.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_TWO));

    }


    @Test
    public void demeterEndTurnTest() {


        assertTrue(demeter.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        demeter.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        // cannot move again because the limit number of movements was reached
        assertFalse(demeter.checkMove(worker, match.getMatchBoard().getCell(0, 2)));

        assertTrue(demeter.checkBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_ONE));
        demeter.executeBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_ONE);

        assertTrue(demeter.checkBuild(worker, match.getMatchBoard().getCell(1, 1), BlockType.LEVEL_ONE));
        demeter.executeBuild(worker, match.getMatchBoard().getCell(1, 1), BlockType.LEVEL_ONE);

        assertTrue(demeter.checkEndTurn());
        demeter.endPlayerTurn(player);

        //Now I check that I can move/build again (so the values are correctly reinitialized
        assertTrue(demeter.checkMove(worker, match.getMatchBoard().getCell(1, 1)));
        demeter.executeMove(worker, match.getMatchBoard().getCell(1, 1));

        assertTrue(demeter.checkBuild(worker, match.getMatchBoard().getCell(1, 2), BlockType.LEVEL_ONE));
        demeter.executeBuild(worker, match.getMatchBoard().getCell(1, 2), BlockType.LEVEL_ONE);
    }
}
