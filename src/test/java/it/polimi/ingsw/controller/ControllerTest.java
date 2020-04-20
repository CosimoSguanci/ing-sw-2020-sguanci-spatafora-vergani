package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gods.*;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class ControllerTest {

    //@Ignore
    @Test
    public void updateNormalTest() throws Exception {
        Match match = new Match(3);
        Board board = new Board();
        Player p1 = new Player("Andrea", "and", match);
        Player p2 = new Player("Cosimo", "cosimo", match);
        Player p3 = new Player("Roberto", "rob", match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Worker w1A = new Worker(p1, board);
        Worker w1B = new Worker(p1, board);
        p1.setWorkerFirst(w1A);
        p1.setWorkerSecond(w1B);
        Worker w2A = new Worker(p2, board);
        Worker w2B = new Worker(p2, board);
        p2.setWorkerFirst(w2A);
        p2.setWorkerSecond(w2B);
        Worker w3A = new Worker(p3, board);
        Worker w3B = new Worker(p3, board);
        p3.setWorkerFirst(w3A);
        p3.setWorkerSecond(w3B);

        GodStrategy divinity = new Apollo();
        God god = new God("Apollo", "God of music", "move in non-empty cell", divinity);
        p1.setGod(god);
        p2.setGod(god);
        p3.setGod(god);

        w1A.setInitialPosition(1,2);
        w1B.setInitialPosition(0,4);
        w2A.setInitialPosition(2,4);
        w2B.setInitialPosition(3,3);
        w3A.setInitialPosition(0,1);
        w3B.setInitialPosition(3,1);

        assertEquals(board.getCell(1,2), w1A.getPosition());

        Model model = new Model(match);
        Controller controller = new Controller(model);
        PlayerCommand playerCommand = PlayerCommand.parseInput(p1, "move w1 C4");
        controller.update(playerCommand);

        assertEquals(p1, model.getCurrentPlayer());
        assertEquals(board.getCell(2,3), w1A.getPosition());
    }
}