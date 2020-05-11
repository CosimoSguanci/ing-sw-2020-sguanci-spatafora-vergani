package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZeusTest {
    @Test
    public void zeusCheckBuildTest() { // Zeus Worker can build under itself




        Zeus zeus = new Zeus();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        zeus.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        assertTrue(zeus.checkBuild(worker, worker.getPosition(), BlockType.LEVEL_ONE));
    }
}
