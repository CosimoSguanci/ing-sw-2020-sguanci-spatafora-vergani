package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PanTest {
    @Test
    public void panWinConditionTest()  {

        Board.clearInstances();
        Match.clearInstances();

        Pan pan = new Pan();

        Match match = Match.getInstance(String.valueOf(Thread.currentThread().getId()), 3);
        Player player = new Player(UUID.randomUUID().toString(), match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        assertFalse(pan.checkWinCondition(worker));

        match.getMatchBoard().getCell(0, 1).setLevel(BlockType.LEVEL_ONE);
        pan.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        match.getMatchBoard().getCell(0, 2).setLevel(BlockType.LEVEL_TWO);
        pan.executeMove(worker, match.getMatchBoard().getCell(0, 2));

        pan.executeMove(worker, match.getMatchBoard().getCell(0, 3));

        assertTrue(pan.checkWinCondition(worker));

    }
}