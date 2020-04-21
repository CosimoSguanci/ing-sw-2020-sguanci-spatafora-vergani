package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZeusTest {
    @Test
    public void zeusCheckBuildTest() throws Exception{ // Zeus Worker can build under itself
        Zeus zeus = new Zeus();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = new Worker(player, match.getMatchBoard());

        worker.setInitialPosition(0, 0);

        zeus.executeMove(worker, match.getMatchBoard().getCell(1, 1)); // needed to set the right selected worker

        assertTrue(zeus.checkBuild(worker, worker.getPosition(), BlockType.LEVEL_ONE));
    }
}
