package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PoseidonTest {
    @Test
    public void poseidonCheckBuildGroundTest() throws Exception { // Unmoved worker is on ground at the end of the turn
        Poseidon poseidon = new Poseidon();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker movedWorker = new Worker(player, match.getMatchBoard());
        Worker unmovedWorker = new Worker(player, match.getMatchBoard());

        player.setWorkerFirst(movedWorker);
        player.setWorkerSecond(unmovedWorker);

        movedWorker.setInitialPosition(0, 0);
        unmovedWorker.setInitialPosition(4, 0);

        poseidon.executeMove(movedWorker, match.getMatchBoard().getCell(1, 1));
        poseidon.executeBuild(movedWorker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertTrue(poseidon.checkBuild(unmovedWorker, match.getMatchBoard().getCell(4, 1), BlockType.LEVEL_ONE));
        poseidon.executeBuild(unmovedWorker, match.getMatchBoard().getCell(4, 1), BlockType.LEVEL_ONE);

        assertTrue(poseidon.checkBuild(unmovedWorker, match.getMatchBoard().getCell(4, 1), BlockType.LEVEL_TWO));
        poseidon.executeBuild(unmovedWorker, match.getMatchBoard().getCell(4, 1), BlockType.LEVEL_TWO);

        assertTrue(poseidon.checkBuild(unmovedWorker, match.getMatchBoard().getCell(4, 1), BlockType.LEVEL_THREE));
        poseidon.executeBuild(unmovedWorker, match.getMatchBoard().getCell(4, 1), BlockType.LEVEL_THREE);

        assertFalse(poseidon.checkBuild(unmovedWorker, match.getMatchBoard().getCell(4, 1), BlockType.DOME));

        Board.clearInstances();
        Match.clearInstances();
    }

    @Test
    public void poseidonCheckBuildNotGroundTest() throws Exception { // Unmoved worker is NOT on ground at the end of the turn
        Poseidon poseidon = new Poseidon();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker movedWorker = new Worker(player, match.getMatchBoard());
        Worker unmovedWorker = new Worker(player, match.getMatchBoard());

        player.setWorkerFirst(movedWorker);
        player.setWorkerSecond(unmovedWorker);

        movedWorker.setInitialPosition(0, 0);

        match.getMatchBoard().getCell(4, 0).setLevel(BlockType.LEVEL_ONE);
        unmovedWorker.setInitialPosition(4, 0);


        poseidon.executeMove(movedWorker, match.getMatchBoard().getCell(1, 1));
        poseidon.executeBuild(movedWorker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertFalse(poseidon.checkBuild(unmovedWorker, match.getMatchBoard().getCell(4, 1), BlockType.LEVEL_TWO));

        Board.clearInstances();
        Match.clearInstances();
    }
}