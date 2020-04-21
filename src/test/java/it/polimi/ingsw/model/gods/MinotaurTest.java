package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MinotaurTest {
    @Test
    public void minotaurCheckMoveTest() throws Exception {
        Minotaur minotaur = new Minotaur();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player1 = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker1 = new Worker(player1, match.getMatchBoard());
        Player player2 = new Player(UUID.randomUUID().toString(), "nickname2", match);
        Worker worker2 = new Worker(player2, match.getMatchBoard());

        worker1.setInitialPosition(0, 0);
        worker2.setInitialPosition(0, 1);

        assertTrue(minotaur.checkMove(worker1, match.getMatchBoard().getCell(0, 1)));

        match.getMatchBoard().getCell(0, 2).setLevel(BlockType.DOME); // Occupy Worker 2's backward position

        assertFalse(minotaur.checkMove(worker1, match.getMatchBoard().getCell(0, 1)));

        Board.clearInstances();
        Match.clearInstances();
    }

    @Test
    public void minotaurExecuteMoveTest() throws Exception {
        Minotaur minotaur = new Minotaur();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player1 = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker1 = new Worker(player1, match.getMatchBoard());
        Player player2 = new Player(UUID.randomUUID().toString(), "nickname2", match);
        Worker worker2 = new Worker(player2, match.getMatchBoard());

        worker1.setInitialPosition(0, 0);
        worker2.setInitialPosition(0, 1);

        // Not Diagonal Test
        minotaur.checkMove(worker1, match.getMatchBoard().getCell(0, 1)); // needed to compute backward cell
        minotaur.executeMove(worker1, match.getMatchBoard().getCell(0, 1));

        assertEquals(match.getMatchBoard().getCell(0, 1), worker1.getPosition());
        assertEquals(match.getMatchBoard().getCell(0, 2), worker2.getPosition()); // Forced one space straight backwards

        // Diagonal Test
        worker2.move(match.getMatchBoard().getCell(1, 2));
        minotaur.checkMove(worker1, match.getMatchBoard().getCell(1, 2)); // needed to compute backward cell
        minotaur.executeMove(worker1, match.getMatchBoard().getCell(1, 2));
        assertEquals(match.getMatchBoard().getCell(1, 2), worker1.getPosition());
        assertEquals(match.getMatchBoard().getCell(2, 3), worker2.getPosition()); // Forced one space straight backwards

        Board.clearInstances();
        Match.clearInstances();
    }
}