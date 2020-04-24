package it.polimi.ingsw.controller;


import it.polimi.ingsw.exceptions.WrongPlayerException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gods.Apollo;
import it.polimi.ingsw.model.gods.GodStrategy;
import it.polimi.ingsw.model.gods.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    @Test
    public void updatePlayerCommandNormalTest()  {

        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea", "and", match);
        Player p2 = new Player("Cosimo", "cosimo", match);
        Player p3 = new Player("Roberto", "rob", match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Worker w1A = new Worker(p1, match.getMatchBoard());
        Worker w1B = new Worker(p1, match.getMatchBoard());
        p1.setWorkerFirst(w1A);
        p1.setWorkerSecond(w1B);
        Worker w2A = new Worker(p2, match.getMatchBoard());
        Worker w2B = new Worker(p2, match.getMatchBoard());
        p2.setWorkerFirst(w2A);
        p2.setWorkerSecond(w2B);
        Worker w3A = new Worker(p3, match.getMatchBoard());
        Worker w3B = new Worker(p3, match.getMatchBoard());
        p3.setWorkerFirst(w3A);
        p3.setWorkerSecond(w3B);

        GodStrategy divinity = new Apollo();
        p1.setGodStrategy(divinity);
        p2.setGodStrategy(divinity);
        p3.setGodStrategy(divinity);

        w1A.setInitialPosition(1,2);
        w1B.setInitialPosition(0,4);
        w2A.setInitialPosition(2,4);
        w2B.setInitialPosition(3,3);
        w3A.setInitialPosition(0,1);
        w3B.setInitialPosition(3,1);

        assertEquals(match.getMatchBoard().getCell(1,2), w1A.getPosition());

        Model model = new Model(match);
        Controller controller = new Controller(model);
        PlayerCommand playerCommand = PlayerCommand.parseInput("Andrea", "move w1 C4");
        controller.update(playerCommand);

        assertEquals(p1, model.getCurrentPlayer());
        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());

        Board.clearInstances();
        Match.clearInstances();
    }


    @Test
    public void updatePlayerCommandGodPowerTest()  {
        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea", "and", match);
        Player p2 = new Player("Cosimo", "cosimo", match);
        match.addPlayer(p1);
        match.addPlayer(p2);

        Worker w1A = new Worker(p1, match.getMatchBoard());
        Worker w1B = new Worker(p1, match.getMatchBoard());
        p1.setWorkerFirst(w1A);
        p1.setWorkerSecond(w1B);
        Worker w2A = new Worker(p2, match.getMatchBoard());
        Worker w2B = new Worker(p2, match.getMatchBoard());
        p2.setWorkerFirst(w2A);
        p2.setWorkerSecond(w2B);

        GodStrategy divinity = new Apollo();
        p1.setGodStrategy(divinity);
        p2.setGodStrategy(divinity);

        w1A.setInitialPosition(1,3);
        w1B.setInitialPosition(3,1);
        w2A.setInitialPosition(4,4);
        w2B.setInitialPosition(2,3);

        assertEquals(match.getMatchBoard().getCell(1,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(2,3), w2B.getPosition());

        Model model = new Model(match);
        Controller controller = new Controller(model);
        PlayerCommand playerCommand = PlayerCommand.parseInput("Andrea", "move w1 C4");
        controller.update(playerCommand);  //Apollo's power must be invoked

        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1,3), w2B.getPosition());

        match.nextTurn();  //Cosimo's turn

        playerCommand = PlayerCommand.parseInput("Andrea", "move w2 e1");


        PlayerCommand finalPlayerCommand = playerCommand;
        assertThrows(WrongPlayerException.class, () -> controller.update(finalPlayerCommand));

        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1,3), w2B.getPosition());

        playerCommand = PlayerCommand.parseInput("Cosimo", "move w2 e4");
        controller.update(playerCommand);  //impossible move should not be allowed

        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1,3), w2B.getPosition());

        playerCommand = PlayerCommand.parseInput("Cosimo", "move w1 e4");
        controller.update(playerCommand);

        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,3), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1,3), w2B.getPosition());

        Board.clearInstances();
        Match.clearInstances();
    }

    @Test
    public void updatePlayerCommandImpossibleMoveAndBuildTest()  {
        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea", "and", match);
        Player p2 = new Player("Cosimo", "cosimo", match);
        match.addPlayer(p1);
        match.addPlayer(p2);

        Worker w1A = new Worker(p1, match.getMatchBoard());
        Worker w1B = new Worker(p1, match.getMatchBoard());
        p1.setWorkerFirst(w1A);
        p1.setWorkerSecond(w1B);
        Worker w2A = new Worker(p2, match.getMatchBoard());
        Worker w2B = new Worker(p2, match.getMatchBoard());
        p2.setWorkerFirst(w2A);
        p2.setWorkerSecond(w2B);

        GodStrategy divinity = new Apollo();
        GodStrategy divine = new Minotaur();
        p1.setGodStrategy(divinity);
        p2.setGodStrategy(divine);

        w1A.setInitialPosition(1,3);
        w1B.setInitialPosition(3,1);
        w2A.setInitialPosition(4,4);
        w2B.setInitialPosition(2,3);

        assertEquals(match.getMatchBoard().getCell(1,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(2,3), w2B.getPosition());

        Model model = new Model(match);
        Controller controller = new Controller(model);
        PlayerCommand playerCommand = PlayerCommand.parseInput("Andrea", "move w1 C4");
        controller.update(playerCommand);  //Apollo's power must be invoked

        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1,3), w2B.getPosition());

        playerCommand = PlayerCommand.parseInput("Andrea", "build w1 b2");
        controller.update(playerCommand);  //impossible build should not be allowed

        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1,3), w2B.getPosition());
        assertEquals(BlockType.GROUND, match.getMatchBoard().getCell(1,1).getLevel());

        match.nextTurn();  //Cosimo's turn

        playerCommand = PlayerCommand.parseInput("Andrea", "move w2 e1");


        PlayerCommand finalPlayerCommand = playerCommand;
        assertThrows(WrongPlayerException.class, () -> controller.update(finalPlayerCommand));

        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1,3), w2B.getPosition());

        playerCommand = PlayerCommand.parseInput("Cosimo", "move w1 e4");
        controller.update(playerCommand);

        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,3), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1,3), w2B.getPosition());

        Board.clearInstances();
        Match.clearInstances();
    }

    @Test
    public void updatePlayerCommandCompleteTest()  {
        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea", "and", match);
        Player p2 = new Player("Cosimo", "cosimo", match);
        Player p3 = new Player("Roberto", "rob", match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Worker w1A = new Worker(p1, match.getMatchBoard());
        Worker w1B = new Worker(p1, match.getMatchBoard());
        p1.setWorkerFirst(w1A);
        p1.setWorkerSecond(w1B);
        Worker w2A = new Worker(p2, match.getMatchBoard());
        Worker w2B = new Worker(p2, match.getMatchBoard());
        p2.setWorkerFirst(w2A);
        p2.setWorkerSecond(w2B);
        Worker w3A = new Worker(p3, match.getMatchBoard());
        Worker w3B = new Worker(p3, match.getMatchBoard());
        p3.setWorkerFirst(w3A);
        p3.setWorkerSecond(w3B);

        GodStrategy divinity = new Apollo();
        p1.setGodStrategy(divinity);
        p2.setGodStrategy(divinity);
        p3.setGodStrategy(divinity);

        w1A.setInitialPosition(1,2);
        w1B.setInitialPosition(0,4);
        w2A.setInitialPosition(2,4);
        w2B.setInitialPosition(3,3);
        w3A.setInitialPosition(0,1);
        w3B.setInitialPosition(3,1);

        Model model = new Model(match);
        Controller controller = new Controller(model);
        match.nextTurn();
        match.nextTurn();
        assertEquals(p3, model.getCurrentPlayer());  //Roberto's turn

        PlayerCommand playerCommand = PlayerCommand.parseInput("Roberto", "move w1 e3");
        controller.update(playerCommand);  //impossible move should not be allowed

        assertEquals(match.getMatchBoard().getCell(1,2), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(0,4), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(2,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,3), w2B.getPosition());
        assertEquals(match.getMatchBoard().getCell(0,1), w3A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w3B.getPosition());

        playerCommand = PlayerCommand.parseInput("Roberto", "move w2 C1");
        controller.update(playerCommand);  //possible move should be allowed

        assertEquals(match.getMatchBoard().getCell(1,2), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(0,4), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(2,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,3), w2B.getPosition());
        assertEquals(match.getMatchBoard().getCell(0,1), w3A.getPosition());
        assertEquals(match.getMatchBoard().getCell(2,0), w3B.getPosition());

        playerCommand = PlayerCommand.parseInput("Roberto", "build w2 E1");
        controller.update(playerCommand);  //impossible build should not be allowed

        assertEquals(match.getMatchBoard().getCell(1,2), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(0,4), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(2,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,3), w2B.getPosition());
        assertEquals(match.getMatchBoard().getCell(0,1), w3A.getPosition());
        assertEquals(match.getMatchBoard().getCell(2,0), w3B.getPosition());

        playerCommand = PlayerCommand.parseInput("Roberto", "build w2 b2");
        controller.update(playerCommand);  //possible build should be allowed

        assertEquals(match.getMatchBoard().getCell(1,2), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(0,4), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(2,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,3), w2B.getPosition());
        assertEquals(match.getMatchBoard().getCell(0,1), w3A.getPosition());
        assertEquals(match.getMatchBoard().getCell(2,0), w3B.getPosition());
        assertEquals(BlockType.LEVEL_ONE, match.getMatchBoard().getCell(1,1).getLevel());

        playerCommand = PlayerCommand.parseInput("Roberto", "end");
        controller.update(playerCommand);  //now it's p1's turn
        assertEquals(p1, model.getCurrentPlayer());

        Board.clearInstances();
        Match.clearInstances();
    }


    @Test
    public void updateGodChoiceCommandNormalTest()  {

        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea", "and", match);
        Player p2 = new Player("Cosimo", "cosimo", match);
        Player p3 = new Player("Roberto", "rob", match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Worker w1A = new Worker(p1, match.getMatchBoard());
        Worker w1B = new Worker(p1, match.getMatchBoard());
        p1.setWorkerFirst(w1A);
        p1.setWorkerSecond(w1B);
        Worker w2A = new Worker(p2, match.getMatchBoard());
        Worker w2B = new Worker(p2, match.getMatchBoard());
        p2.setWorkerFirst(w2A);
        p2.setWorkerSecond(w2B);
        Worker w3A = new Worker(p3, match.getMatchBoard());
        Worker w3B = new Worker(p3, match.getMatchBoard());
        p3.setWorkerFirst(w3A);
        p3.setWorkerSecond(w3B);

        Model model = new Model(match);
        Controller controller = new Controller(model);

        List chosenGods = new ArrayList();
        chosenGods.add("apollo");
        chosenGods.add("athena");
        chosenGods.add("hestia");
        controller.startMatch();
        ArrayList<Player> players = new ArrayList<>(model.getPlayers());
        GodChoiceCommand godChoiceCommand = new GodChoiceCommand(chosenGods, true);
        controller.update(godChoiceCommand);

        List godPlayer = new ArrayList();
        godPlayer.add("hestia");
        godChoiceCommand = new GodChoiceCommand(godPlayer, false);
        controller.update(godChoiceCommand);

        godPlayer= new ArrayList();
        godPlayer.add("apollo");
        godChoiceCommand = new GodChoiceCommand(godPlayer, false);
        controller.update(godChoiceCommand);

        int apollo=0, hestia=0, athena=0;
        for(Player p : players) {
            if(p.getGodStrategy() instanceof Apollo && !p.isGodChooser())  apollo++;
            else if(p.getGodStrategy() instanceof Hestia && !p.isGodChooser())  hestia++;
            else if(p.getGodStrategy() instanceof Athena && p.isGodChooser())  athena++;
        }
        assertTrue(hestia==1);
        assertTrue(apollo==1);
        assertTrue(athena==1);

        Board.clearInstances();
        Match.clearInstances();
    }


    @Test
    public void updateGodChoiceCommand2PlayersTest()  {

        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea", "and", match);
        Player p2 = new Player("Marco", "mc", match);
        match.addPlayer(p1);
        match.addPlayer(p2);

        Worker w1A = new Worker(p1, match.getMatchBoard());
        Worker w1B = new Worker(p1, match.getMatchBoard());
        p1.setWorkerFirst(w1A);
        p1.setWorkerSecond(w1B);
        Worker w2A = new Worker(p2, match.getMatchBoard());
        Worker w2B = new Worker(p2, match.getMatchBoard());
        p2.setWorkerFirst(w2A);
        p2.setWorkerSecond(w2B);

        Model model = new Model(match);
        Controller controller = new Controller(model);

        List chosenGods = new ArrayList();
        chosenGods.add("eros");
        chosenGods.add("minotaur");
        controller.startMatch();
        ArrayList<Player> players = new ArrayList<>(model.getPlayers());
        GodChoiceCommand godChoiceCommand = new GodChoiceCommand(chosenGods, true);
        controller.update(godChoiceCommand);

        List godPlayer = new ArrayList();
        godPlayer.add("eros");
        godChoiceCommand = new GodChoiceCommand(godPlayer, false);
        controller.update(godChoiceCommand);

        int eros=0, minotaur=0;
        for(Player p : players) {
            if(p.getGodStrategy() instanceof Eros && !p.isGodChooser())  eros++;
            else if(p.getGodStrategy() instanceof Minotaur && p.isGodChooser())  minotaur++;
        }
        assertTrue(eros==1);
        assertTrue(minotaur==1);


        Board.clearInstances();
        Match.clearInstances();
    }

}