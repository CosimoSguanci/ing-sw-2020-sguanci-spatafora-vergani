package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PrometheusTest {

    @Test
    public void prometheusBuildThenMoveTest()  {


        Match.clearInstances();

        Prometheus prometheus = new Prometheus();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(),  match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        assertTrue(prometheus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        prometheus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertTrue(prometheus.checkMove(worker, match.getMatchBoard().getCell(1, 1)));
        prometheus.executeMove(worker, match.getMatchBoard().getCell(1, 1));
    }

    @Test
    public void prometheusCannotMoveUpIfBuiltBeforeMovingTest()  {


        Match.clearInstances();

        Prometheus prometheus = new Prometheus();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        assertTrue(prometheus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        prometheus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertFalse(prometheus.checkMove(worker, match.getMatchBoard().getCell(0, 1)));

    }

    @Test
    public void prometheusCannotMultipleBuildIfMoveBeforeBuildTest()  {


        Match.clearInstances();

        Prometheus prometheus = new Prometheus();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        assertTrue(prometheus.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        prometheus.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        assertTrue(prometheus.checkBuild(worker, match.getMatchBoard().getCell(1, 1), BlockType.LEVEL_ONE));
        prometheus.executeBuild(worker, match.getMatchBoard().getCell(1, 1), BlockType.LEVEL_ONE);

        assertFalse(prometheus.checkBuild(worker, match.getMatchBoard().getCell(1, 1), BlockType.LEVEL_TWO));
    }


    @Test
    public void prometheusEndTurnTest() {

        Match.clearInstances();

        Prometheus prometheus = new Prometheus();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        assertTrue(prometheus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        prometheus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertTrue(prometheus.checkMove(worker, match.getMatchBoard().getCell(1, 1)));
        prometheus.executeMove(worker, match.getMatchBoard().getCell(1, 1));

        // cannot move again because the limit number of movements was reached
        assertFalse(prometheus.checkMove(worker, match.getMatchBoard().getCell(0, 0)));

        assertTrue(prometheus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_TWO));
        prometheus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_TWO);

        assertFalse(prometheus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_THREE));

        assertTrue(prometheus.checkEndTurn());
        prometheus.endPlayerTurn(player);

        //Now I check that I can move/build again (so the values are correctly reinitialized
        assertTrue(prometheus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_THREE));
    }
}