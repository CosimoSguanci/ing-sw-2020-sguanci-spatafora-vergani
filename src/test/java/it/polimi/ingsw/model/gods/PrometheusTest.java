package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PrometheusTest {

    @Test
    public void prometheusBuildThenMoveTest()  {

        Prometheus prometheus = new Prometheus();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        assertTrue(prometheus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        prometheus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertTrue(prometheus.checkMove(worker, match.getMatchBoard().getCell(1, 1)));
        prometheus.executeMove(worker, match.getMatchBoard().getCell(1, 1));
    }

    @Test
    public void prometheusCannotMoveUpIfBuiltBeforeMovingTest()  {

        Prometheus prometheus = new Prometheus();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        assertTrue(prometheus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        prometheus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertFalse(prometheus.checkMove(worker, match.getMatchBoard().getCell(0, 1)));

    }

    @Test
    public void prometheusCannotMultipleBuildIfMoveBeforeBuildTest()  {
        Prometheus prometheus = new Prometheus();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
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

        Prometheus prometheus = new Prometheus();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
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

    @Test
    public void prometheusEndTurnTest_standard() {

        Prometheus prometheus = new Prometheus();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        assertTrue(prometheus.checkMove(worker, match.getMatchBoard().getCell(1, 1)));
        prometheus.executeMove(worker, match.getMatchBoard().getCell(1, 1));

        assertTrue(prometheus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        prometheus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertTrue(prometheus.checkEndTurn());
        prometheus.endPlayerTurn(player);

        //Now I check that I can move/build again (so the values are correctly reinitialized
        assertTrue(prometheus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_TWO));
    }

    @Test
    public void prometheusCannotMoveAfterBuildTest_standardCantMove() {

        Prometheus prometheus = new Prometheus();

        Match match = new Match(2);
        Model model = new Model(match);
        Player player = new Player(UUID.randomUUID().toString(), model, match);
        player.setGodStrategy(prometheus);

        Player player2 = new Player(UUID.randomUUID().toString(), model, match);
        player2.setGodStrategy(new Athena());

        match.addPlayer(player);
        match.addPlayer(player2);

        player.getWorkerFirst().setInitialPosition(0, 0);
        player.getWorkerSecond().setInitialPosition(3, 3);

        player2.getWorkerFirst().setInitialPosition(2, 2);
        player2.getWorkerSecond().setInitialPosition(4, 4);

        model.getBoard().getCell(1, 0).setLevel(BlockType.DOME);
        model.getBoard().getCell(1, 1).setLevel(BlockType.DOME);
        model.getBoard().getCell(0, 1).setLevel(BlockType.LEVEL_ONE);

        assertTrue(prometheus.canMove(match.getMatchBoard(), player));

        assertTrue(prometheus.checkBuild(player.getWorkerFirst(), model.getBoard().getCell(0, 1), BlockType.LEVEL_TWO));

        prometheus.executeBuild(player.getWorkerFirst(), model.getBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        // Prometheus should have lost

        assertEquals(1, model.getPlayers().size());
        assertFalse(model.getPlayers().contains(player));
        assertTrue(model.getPlayers().contains(player2));
    }

    @Test
    public void prometheusCannotMoveAfterBuildTest_PrometheusConstraints() {

        Prometheus prometheus = new Prometheus();

        Match match = new Match(2);
        Model model = new Model(match);
        Player player = new Player(UUID.randomUUID().toString(), model, match);
        player.setGodStrategy(prometheus);

        Player player2 = new Player(UUID.randomUUID().toString(), model, match);
        player2.setGodStrategy(new Athena());

        match.addPlayer(player);
        match.addPlayer(player2);

        player.getWorkerFirst().setInitialPosition(0, 0);
        player.getWorkerSecond().setInitialPosition(3, 3);

        player2.getWorkerFirst().setInitialPosition(2, 2);
        player2.getWorkerSecond().setInitialPosition(4, 4);

        model.getBoard().getCell(1, 0).setLevel(BlockType.DOME);
        model.getBoard().getCell(1, 1).setLevel(BlockType.LEVEL_THREE);
        model.getBoard().getCell(0, 1).setLevel(BlockType.LEVEL_ONE);

        assertTrue(prometheus.canMove(match.getMatchBoard(), player));

        assertTrue(prometheus.checkBuild(player.getWorkerFirst(), model.getBoard().getCell(1, 1), BlockType.DOME));

        prometheus.executeBuild(player.getWorkerFirst(), model.getBoard().getCell(1, 1), BlockType.DOME);

        // Prometheus should have lost

        assertEquals(1, model.getPlayers().size());
        assertFalse(model.getPlayers().contains(player));
        assertTrue(model.getPlayers().contains(player2));
    }

    @Test
    public void cannotBuildWithWorkerNotSelectedPrometheus() {
        Prometheus prometheus = new Prometheus();

        Match match = new Match(2);
        Model model = new Model(match);
        Player player = new Player(UUID.randomUUID().toString(), model, match);
        player.setGodStrategy(prometheus);

        player.getWorkerFirst().setInitialPosition(0, 0);
        player.getWorkerSecond().setInitialPosition(3, 3);

        assertTrue(prometheus.checkMove(player.getWorkerFirst(), match.getMatchBoard().getCell(0, 1)));
        prometheus.executeMove(player.getWorkerFirst(), match.getMatchBoard().getCell(0, 1));

        assertFalse(prometheus.checkBuild(player.getWorkerSecond(), match.getMatchBoard().getCell(3, 4), null));
    }
}