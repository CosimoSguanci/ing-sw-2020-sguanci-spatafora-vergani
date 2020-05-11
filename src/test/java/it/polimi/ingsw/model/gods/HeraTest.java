package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class HeraTest {

    @Test
    public void heraWinConstraintsTest()  {




        Hera hera = new Hera();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker opponentWorker = player.getWorkerFirst();

        // Pick a god that does not have additional Win Conditions for opponent...
        Apollo apollo = new Apollo();

        opponentWorker.setInitialPosition(0, 0);

        // Constructing win situation...
        match.getMatchBoard().getCell(0,1).setLevel(BlockType.LEVEL_TWO);
        opponentWorker.move(match.getMatchBoard().getCell(0, 1));
        match.getMatchBoard().getCell(0,2).setLevel(BlockType.LEVEL_THREE);
        opponentWorker.move(match.getMatchBoard().getCell(0, 2));

        // Win conditions should be satisfied, but...
        assertTrue(apollo.checkWinCondition(opponentWorker));

        // Win constraints should NOT be satisfied.
        assertFalse(apollo.checkWinCondition(opponentWorker) && hera.checkWinConstraints(opponentWorker, match.getMatchBoard().getCell(0, 2)));

    }

}