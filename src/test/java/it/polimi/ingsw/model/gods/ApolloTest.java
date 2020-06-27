package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.utils.GodsUtils;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ApolloTest {

    @Test
    public void apolloCheckMoveTest()  {

         // TODO Use @BeforeEach

        Apollo apollo = new Apollo();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker worker = player.getWorkerFirst();
        Player oppositePlayer = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker oppositeWorker = oppositePlayer.getWorkerFirst();

        worker.setInitialPosition(0, 0);
        oppositeWorker.setInitialPosition(0, 1);

        assertTrue(apollo.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        assertFalse(apollo.checkMove(worker, match.getMatchBoard().getCell(0, 2)));
    }

    @Test
    public void apolloExecuteMoveTest()  {
        Apollo apollo = new Apollo();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker worker = player.getWorkerFirst();
        Player oppositePlayer = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker oppositeWorker = oppositePlayer.getWorkerFirst();

        worker.setInitialPosition(0, 0);
        oppositeWorker.setInitialPosition(0, 1);

        apollo.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        assertEquals(match.getMatchBoard().getCell(0, 1), worker.getPosition());
        assertEquals(match.getMatchBoard().getCell(0, 0), oppositeWorker.getPosition());

        assertEquals(worker, match.getMatchBoard().getCell(0, 1).getWorker());
        assertEquals(oppositeWorker, match.getMatchBoard().getCell(0, 0).getWorker());

        assertFalse(apollo.checkMove(worker, match.getMatchBoard().getCell(0, 0)));
    }

    @Test
    public void apolloCannotMoveTest() {
        Apollo apollo = new Apollo();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker workerFirst = player.getWorkerFirst();
        Worker workerSecond = player.getWorkerSecond();

        workerFirst.setInitialPosition(0, 0);
        workerSecond.setInitialPosition(0, 4);

        match.getMatchBoard().getCell(0, 1).setLevel(BlockType.DOME);
        match.getMatchBoard().getCell(1, 1).setLevel(BlockType.DOME);
        match.getMatchBoard().getCell(1, 0).setLevel(BlockType.DOME);

        match.getMatchBoard().getCell(0, 3).setLevel(BlockType.DOME);
        match.getMatchBoard().getCell(1, 3).setLevel(BlockType.DOME);
        match.getMatchBoard().getCell(1, 4).setLevel(BlockType.DOME);

        assertFalse(apollo.canMove(match.getMatchBoard(), player));
    }

    @Test
    public void equalsTest() {
        Apollo apollo = new Apollo();
        Apollo apollo2 = new Apollo();
        assertEquals(apollo, apollo2);

        GodStrategy apollo3 = GodsUtils.godsFactory("apollo");
        GodStrategy apollo4 = GodsUtils.godsFactory("apollo");
        assertNotSame(apollo3, apollo4);
    }

}
