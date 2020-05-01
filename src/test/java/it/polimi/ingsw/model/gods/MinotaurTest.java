package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MinotaurTest {
    @Test
    public void minotaurCheckMoveTest() {
        Board.clearInstances();
        Match.clearInstances();

        Minotaur minotaur = new Minotaur();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(),  match);
        Worker worker = player.getWorkerFirst();
        Player oppositePlayer = new Player(UUID.randomUUID().toString(), match);
        Worker oppositeWorker = oppositePlayer.getWorkerFirst();

        worker.setInitialPosition(0, 0);
        oppositeWorker.setInitialPosition(0, 1);

        assertTrue(minotaur.checkMove(worker, match.getMatchBoard().getCell(0, 1)));

        match.getMatchBoard().getCell(0, 2).setLevel(BlockType.DOME); // Occupy Worker 2's backward position

        assertFalse(minotaur.checkMove(worker, match.getMatchBoard().getCell(0, 1)));

        Board.clearInstances();
        Match.clearInstances();
    }

    @Test
    public void minotaurExecuteMoveTest() {
        Board.clearInstances();
        Match.clearInstances();

        Minotaur minotaur = new Minotaur();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(),  match);
        Worker worker = player.getWorkerFirst();
        Player oppositePlayer = new Player(UUID.randomUUID().toString(),  match);
        Worker oppositeWorker = oppositePlayer.getWorkerFirst();

        worker.setInitialPosition(0, 0);
        oppositeWorker.setInitialPosition(0, 1);

        // Not Diagonal Test
        minotaur.checkMove(worker, match.getMatchBoard().getCell(0, 1)); // needed to compute backward cell
        minotaur.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        assertEquals(match.getMatchBoard().getCell(0, 1), worker.getPosition());
        assertEquals(match.getMatchBoard().getCell(0, 2), oppositeWorker.getPosition()); // Forced one space straight backwards

        // Diagonal Test
        oppositeWorker.move(match.getMatchBoard().getCell(1, 2));
        minotaur.checkMove(worker, match.getMatchBoard().getCell(1, 2)); // needed to compute backward cell
        minotaur.executeMove(worker, match.getMatchBoard().getCell(1, 2));
        assertEquals(match.getMatchBoard().getCell(1, 2), worker.getPosition());
        assertEquals(match.getMatchBoard().getCell(2, 3), oppositeWorker.getPosition()); // Forced one space straight backwards
    }

    @Test
    public void getGodInfoTest() {
        Minotaur minotaur = new Minotaur();

        Map<String, String> info = minotaur.getGodInfo();

        assertEquals(info.get("name"), Minotaur.NAME);
        assertEquals(info.get("description"), Minotaur.DESCRIPTION);
        assertEquals(info.get("power_description"), Minotaur.POWER_DESCRIPTION);
    }
}