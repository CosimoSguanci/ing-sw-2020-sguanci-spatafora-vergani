package it.polimi.ingsw.controller;


import it.polimi.ingsw.controller.commands.GamePreparationCommand;
import it.polimi.ingsw.controller.commands.GodChoiceCommand;
import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.exceptions.InvalidColorException;
import it.polimi.ingsw.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.exceptions.WrongPlayerException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gods.Apollo;
import it.polimi.ingsw.model.gods.GodStrategy;
import it.polimi.ingsw.model.gods.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    @Test
    public void updatePlayerCommandNormalTest() {

        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea",  match);
        Player p2 = new Player("Cosimo",  match);
        Player p3 = new Player("Roberto",  match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Worker w1A = p1.getWorkerFirst();
        Worker w1B = p1.getWorkerSecond();

        Worker w2A = p2.getWorkerFirst();
        Worker w2B = p2.getWorkerSecond();

        Worker w3A = p3.getWorkerFirst();
        Worker w3B = p3.getWorkerSecond();

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

        model.nextGamePhase();  //Choose_Gods phase
        model.nextGamePhase();  //Game_Preparation phase
        model.nextGamePhase();  //Real_Game phase

        PlayerCommand playerCommand = PlayerCommand.parseInput("move w1 C4");
        playerCommand.setPlayer(p1);
        controller.update(playerCommand);

        assertEquals(p1, model.getCurrentPlayer());
        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());

        Board.clearInstances();
        Match.clearInstances();
    }


    @Test
    public void updatePlayerCommandGodPowerTest() {
        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea",  match);
        Player p2 = new Player("Cosimo", match);
        match.addPlayer(p1);
        match.addPlayer(p2);

        Worker w1A = p1.getWorkerFirst();
        Worker w1B = p1.getWorkerSecond();

        Worker w2A = p2.getWorkerFirst();
        Worker w2B = p2.getWorkerSecond();

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

        model.nextGamePhase();  //Choose_Gods phase
        model.nextGamePhase();  //Game_Preparation phase
        model.nextGamePhase();  //Real_Game phase

        PlayerCommand playerCommand = PlayerCommand.parseInput("move w1 C4");
        playerCommand.setPlayer(p1);
        controller.update(playerCommand);  //Apollo's power must be invoked

        assertEquals(p1, model.getCurrentPlayer());
        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1,3), w2B.getPosition());

        match.nextTurn();  //Cosimo's turn
        assertEquals(p2, model.getCurrentPlayer());

        playerCommand = PlayerCommand.parseInput("move w2 e1");
        playerCommand.setPlayer(p1);

        PlayerCommand finalPlayerCommand = playerCommand;
        assertThrows(WrongPlayerException.class, () -> controller.handlePlayerCommand(finalPlayerCommand));

        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1,3), w2B.getPosition());

        playerCommand = PlayerCommand.parseInput("move w2 e4");
        playerCommand.setPlayer(p2);
        controller.update(playerCommand);  //impossible move should not be allowed

        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1,3), w2B.getPosition());

        playerCommand = PlayerCommand.parseInput("move w1 e4");
        playerCommand.setPlayer(p2);
        controller.update(playerCommand);

        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,3), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1,3), w2B.getPosition());

        Board.clearInstances();
        Match.clearInstances();
    }


    @Test
    public void updatePlayerCommandImpossibleMoveAndBuildTest() {
        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea", match);
        Player p2 = new Player("Cosimo", match);
        match.addPlayer(p1);
        match.addPlayer(p2);

        Worker w1A = p1.getWorkerFirst();
        Worker w1B = p1.getWorkerSecond();

        Worker w2A = p2.getWorkerFirst();
        Worker w2B = p2.getWorkerSecond();

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

        model.nextGamePhase();  //Choose_Gods phase
        model.nextGamePhase();  //Game_Preparation phase
        model.nextGamePhase();  //Real_Game phase

        PlayerCommand playerCommand = PlayerCommand.parseInput("move w1 C4");
        playerCommand.setPlayer(p1);
        controller.update(playerCommand);  //Apollo's power must be invoked

        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1,3), w2B.getPosition());

        playerCommand = PlayerCommand.parseInput("build w1 b2");
        playerCommand.setPlayer(p1);
        controller.update(playerCommand);  //impossible build should not be allowed

        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1,3), w2B.getPosition());
        assertEquals(BlockType.GROUND, match.getMatchBoard().getCell(1,1).getLevel());

        match.nextTurn();  //Cosimo's turn

        playerCommand = PlayerCommand.parseInput("move w2 e1");
        playerCommand.setPlayer(p1);


        PlayerCommand finalPlayerCommand = playerCommand;
        assertThrows(WrongPlayerException.class, () -> controller.handlePlayerCommand(finalPlayerCommand));

        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1,3), w2B.getPosition());

        playerCommand = PlayerCommand.parseInput("move w1 e4");
        playerCommand.setPlayer(p2);
        controller.update(playerCommand);

        assertEquals(match.getMatchBoard().getCell(2,3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4,3), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1,3), w2B.getPosition());

        Board.clearInstances();
        Match.clearInstances();
    }


    @Test
    public void updatePlayerCommandCompleteTest() {
        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea", match);
        Player p2 = new Player("Cosimo",  match);
        Player p3 = new Player("Roberto",  match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Worker w1A = p1.getWorkerFirst();
        Worker w1B = p1.getWorkerSecond();

        Worker w2A = p2.getWorkerFirst();
        Worker w2B = p2.getWorkerSecond();

        Worker w3A = p3.getWorkerFirst();
        Worker w3B = p3.getWorkerSecond();


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

        model.nextGamePhase();  //Choose_Gods phase
        model.nextGamePhase();  //Game_Preparation phase
        model.nextGamePhase();  //Real_Game phase

        PlayerCommand playerCommand = PlayerCommand.parseInput("move w1 e3");
        playerCommand.setPlayer(p3);
        controller.update(playerCommand);  //impossible move should not be allowed

        assertEquals(match.getMatchBoard().getCell(1,2), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(0,4), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(2,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,3), w2B.getPosition());
        assertEquals(match.getMatchBoard().getCell(0,1), w3A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,1), w3B.getPosition());

        playerCommand = PlayerCommand.parseInput("move w2 C1");
        playerCommand.setPlayer(p3);
        controller.update(playerCommand);  //possible move should be allowed

        assertEquals(match.getMatchBoard().getCell(1,2), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(0,4), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(2,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,3), w2B.getPosition());
        assertEquals(match.getMatchBoard().getCell(0,1), w3A.getPosition());
        assertEquals(match.getMatchBoard().getCell(2,0), w3B.getPosition());

        playerCommand = PlayerCommand.parseInput("build w2 E1");
        playerCommand.setPlayer(p3);
        controller.update(playerCommand);  //impossible build should not be allowed

        assertEquals(match.getMatchBoard().getCell(1,2), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(0,4), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(2,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,3), w2B.getPosition());
        assertEquals(match.getMatchBoard().getCell(0,1), w3A.getPosition());
        assertEquals(match.getMatchBoard().getCell(2,0), w3B.getPosition());

        playerCommand = PlayerCommand.parseInput("build w2 b2");
        playerCommand.setPlayer(p3);
        controller.update(playerCommand);  //possible build should be allowed

        assertEquals(match.getMatchBoard().getCell(1,2), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(0,4), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(2,4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3,3), w2B.getPosition());
        assertEquals(match.getMatchBoard().getCell(0,1), w3A.getPosition());
        assertEquals(match.getMatchBoard().getCell(2,0), w3B.getPosition());
        assertEquals(BlockType.LEVEL_ONE, match.getMatchBoard().getCell(1,1).getLevel());

        playerCommand = PlayerCommand.parseInput("end");
        playerCommand.setPlayer(p3);
        controller.update(playerCommand);  //now it's p1's turn
        assertEquals(p1, model.getCurrentPlayer());
    }


    @Test
    public void updateGodChoiceCommandNormalTest() throws NoSuchFieldException, IllegalAccessException {

        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea",  match);
        Player p2 = new Player("Cosimo",  match);
        Player p3 = new Player("Roberto",  match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Model model = new Model(match);
        Controller controller = new Controller(model);

        model.nextGamePhase();  //Choose_Gods phase

        List<String> chosenGods = new ArrayList<>();
        chosenGods.add("apollo");
        chosenGods.add("athena");
        chosenGods.add("hestia");
        //controller.prepareMatch(); // todo FIX Test with new InitialPhase
        ArrayList<Player> players = new ArrayList<>(model.getPlayers());
        GodChoiceCommand godChoiceCommand = new GodChoiceCommand(chosenGods, true);
        godChoiceCommand.setPlayer(model.getCurrentPlayer());
        model.getCurrentPlayer().setAsGodChooser();

        Field godChooserPlayer = Controller.class.getDeclaredField("godChooserPlayer");
        godChooserPlayer.setAccessible(true);
        godChooserPlayer.set(controller, model.getCurrentPlayer());
        godChooserPlayer.setAccessible(false);

        controller.update(godChoiceCommand);

        List<String> godPlayer = new ArrayList<>();
        godPlayer.add("hestia");
        godChoiceCommand = new GodChoiceCommand(godPlayer, false);
        godChoiceCommand.setPlayer(model.getCurrentPlayer());
        controller.update(godChoiceCommand);

        godPlayer= new ArrayList<>();
        godPlayer.add("apollo");
        godChoiceCommand = new GodChoiceCommand(godPlayer, false);
        godChoiceCommand.setPlayer(model.getCurrentPlayer());
        controller.update(godChoiceCommand);

        int apollo = 0, hestia = 0, athena = 0;
        for(Player p : players) {
            if(p.getGodStrategy() instanceof Apollo && !p.isGodChooser())  apollo++;
            else if(p.getGodStrategy() instanceof Hestia && !p.isGodChooser())  hestia++;
            else if(p.getGodStrategy() instanceof Athena && p.isGodChooser())  athena++;
        }
        assertEquals(1, hestia);
        assertEquals(1, apollo);
        assertEquals(1, athena);

        Board.clearInstances();
        Match.clearInstances();
    }


    @Test
    public void updateGodChoiceCommand2PlayersTest() throws NoSuchFieldException, IllegalAccessException {

        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea",  match);
        Player p2 = new Player("Marco",  match);
        match.addPlayer(p1);
        match.addPlayer(p2);

        Model model = new Model(match);
        Controller controller = new Controller(model);

        model.nextGamePhase();  //Choose_Gods phase

        List<String> chosenGods = new ArrayList<>();
        chosenGods.add("eros");
        chosenGods.add("minotaur");
        //controller.prepareMatch(); // todo FIX Test with new InitialPhase
        ArrayList<Player> players = new ArrayList<>(model.getPlayers());
        GodChoiceCommand godChoiceCommand = new GodChoiceCommand(chosenGods, true);
        godChoiceCommand.setPlayer(model.getCurrentPlayer());
        model.getCurrentPlayer().setAsGodChooser();

        Field godChooserPlayer = Controller.class.getDeclaredField("godChooserPlayer");
        godChooserPlayer.setAccessible(true);
        godChooserPlayer.set(controller, model.getCurrentPlayer());
        godChooserPlayer.setAccessible(false);

        controller.update(godChoiceCommand);

        List<String> godPlayer = new ArrayList<>();
        godPlayer.add("eros");
        godChoiceCommand = new GodChoiceCommand(godPlayer, false);
        godChoiceCommand.setPlayer(model.getCurrentPlayer());
        controller.update(godChoiceCommand);

        int eros=0, minotaur=0;
        for(Player p : players) {
            if(p.getGodStrategy() instanceof Eros && !p.isGodChooser())  eros++;
            else if(p.getGodStrategy() instanceof Minotaur && p.isGodChooser())  minotaur++;
        }
        assertEquals(1, eros);
        assertEquals(1, minotaur);

    }


    @Test
    public void updateInitialInfoCommandTest() {

        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea",  match);
        Player p2 = new Player("Cosimo",  match);
        Player p3 = new Player("Roberto", match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Model model = new Model(match);
        Controller controller = new Controller(model);
        controller.initialPhase();  //random first player to play

        String nick1 = "pippo";
        String nick2 = "pluto";
        String nick3 = "tex";
        PrintableColour colour1 = PrintableColour.BLUE;
        PrintableColour colour2 = PrintableColour.GREEN;
        PrintableColour colour3 = PrintableColour.RED;
        InitialInfoCommand initialInfoCommand = new InitialInfoCommand(nick1, colour1);
        initialInfoCommand.setPlayer(model.getCurrentPlayer());
        controller.update(initialInfoCommand);

        initialInfoCommand = new InitialInfoCommand(nick2, colour2);
        initialInfoCommand.setPlayer(model.getCurrentPlayer());
        controller.update(initialInfoCommand);

        initialInfoCommand = new InitialInfoCommand(nick3, colour3);
        initialInfoCommand.setPlayer(model.getCurrentPlayer());
        controller.update(initialInfoCommand);

        ArrayList<Player> players = new ArrayList<>(model.getPlayers());
        int pippoBlue = 0;
        int plutoGreen = 0;
        int texRed = 0;
        for(Player player : players) {
            if(player.getNickname().equals("pippo") && player.getColor() == PrintableColour.BLUE) {
                pippoBlue++;
            }
            else if(player.getNickname().equals("pluto") && player.getColor() == PrintableColour.GREEN) {
                plutoGreen++;
            }
            else if(player.getNickname().equals("tex") && player.getColor() == PrintableColour.RED) {
                texRed++;
            }
        }

        assertEquals(1, pippoBlue);
        assertEquals(1, plutoGreen);
        assertEquals(1, texRed);
    }

    @Test
    public void updateInitialInfoCommandNicknameAlreadyTakenExceptionTest() {

        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea",  match);
        Player p2 = new Player("Cosimo",  match);
        Player p3 = new Player("Roberto", match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Model model = new Model(match);
        Controller controller = new Controller(model);
        controller.initialPhase();  //random first player to play

        String nick1 = "pippo";
        String nick2 = "pluto";
        PrintableColour colour1 = PrintableColour.BLUE;
        PrintableColour colour2 = PrintableColour.GREEN;
        PrintableColour colour3 = PrintableColour.RED;
        InitialInfoCommand initialInfoCommand = new InitialInfoCommand(nick1, colour1);
        initialInfoCommand.setPlayer(model.getCurrentPlayer());
        controller.update(initialInfoCommand);

        initialInfoCommand = new InitialInfoCommand(nick2, colour2);
        initialInfoCommand.setPlayer(model.getCurrentPlayer());
        controller.update(initialInfoCommand);

        ArrayList<Player> players = new ArrayList<>(model.getPlayers());
        int pippoBlue = 0;
        int plutoGreen = 0;
        for(Player player : players) {
            if(player.getNickname() != null && player.getColor() != null) {
                if (player.getNickname().equals("pippo") && player.getColor() == PrintableColour.BLUE) {
                    pippoBlue++;
                } else if (player.getNickname().equals("pluto") && player.getColor() == PrintableColour.GREEN) {
                    plutoGreen++;
                }
            }
        }

        assertEquals(1, pippoBlue);
        assertEquals(1, plutoGreen);

        final InitialInfoCommand initialInfoCommandExc = new InitialInfoCommand(nick2, colour3);
        initialInfoCommandExc.setPlayer(model.getCurrentPlayer());
        assertThrows(NicknameAlreadyTakenException.class,
                () -> controller.handleInitialInfoCommand(initialInfoCommandExc));
    }

    @Test
    public void updateInitialInfoCommandInvalidColorExceptionTest() {

        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea",  match);
        Player p2 = new Player("Cosimo",  match);
        Player p3 = new Player("Roberto", match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Model model = new Model(match);
        Controller controller = new Controller(model);
        controller.initialPhase();  //random first player to play

        String nick1 = "pippo";
        String nick2 = "pluto";
        PrintableColour colour = PrintableColour.YELLOW;
        InitialInfoCommand initialInfoCommand = new InitialInfoCommand(nick1, colour);
        initialInfoCommand.setPlayer(model.getCurrentPlayer());
        controller.update(initialInfoCommand);

        ArrayList<Player> players = new ArrayList<>(model.getPlayers());
        int pippoYellow = 0;
        for(Player player : players) {
            if(player.getNickname() != null && player.getColor() != null) {
                if (player.getNickname().equals("pippo") && player.getColor() == PrintableColour.YELLOW) {
                    pippoYellow++;
                }
            }
        }

        assertEquals(1, pippoYellow);

        final InitialInfoCommand initialInfoCommandExc = new InitialInfoCommand(nick2, colour);
        initialInfoCommandExc.setPlayer(model.getCurrentPlayer());
        assertThrows(InvalidColorException.class,
                () -> controller.handleInitialInfoCommand(initialInfoCommandExc));
    }

    @Test
    public void updateInitialInfoCommand2PlayerTest() {

        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea",  match);
        Player p2 = new Player("Marco", match);
        match.addPlayer(p1);
        match.addPlayer(p2);

        Model model = new Model(match);
        Controller controller = new Controller(model);
        controller.initialPhase();  //random first player to play

        String nick1 = "pippo";
        String nick2 = "pluto";
        PrintableColour colour1 = PrintableColour.GREEN;
        PrintableColour colour2 = PrintableColour.RED;
        InitialInfoCommand initialInfoCommand = new InitialInfoCommand(nick1, colour1);
        initialInfoCommand.setPlayer(model.getCurrentPlayer());
        controller.update(initialInfoCommand);

        initialInfoCommand = new InitialInfoCommand(nick2, colour2);
        initialInfoCommand.setPlayer(model.getCurrentPlayer());
        controller.update(initialInfoCommand);

        ArrayList<Player> players = new ArrayList<>(model.getPlayers());
        int pippoGreen = 0;
        int plutoRed = 0;
        for(Player player : players) {
            if(player.getNickname().equals("pippo") && player.getColor() == PrintableColour.GREEN) {
                pippoGreen++;
            }
            else if(player.getNickname().equals("pluto") && player.getColor() == PrintableColour.RED) {
                plutoRed++;
            }
        }

        assertEquals(1, pippoGreen);
        assertEquals(1, plutoRed);
    }

    @Test
    public void updateGamePreparationCommandTest() {

        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea",  match);
        Player p2 = new Player("Cosimo",  match);
        Player p3 = new Player("Roberto",  match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Model model = new Model(match);
        Controller controller = new Controller(model);

        model.nextGamePhase();  //Choose_Gods phase
        model.nextGamePhase();

        Apollo apollo = new Apollo();
        Hera hera = new Hera();
        Athena athena = new Athena();

        p1.setGodStrategy(apollo);
        p2.setGodStrategy(hera);
        p3.setGodStrategy(athena);

        GamePreparationCommand gamePreparationCommand = new GamePreparationCommand(1,3, 3, 4);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        model.endTurn();
        model.endTurn();  //now it's again his turn
        assertEquals(1, model.getCurrentPlayer().getWorkerFirst().getPosition().getRowIdentifier());
        assertEquals(3, model.getCurrentPlayer().getWorkerFirst().getPosition().getColIdentifier());
        assertEquals(3, model.getCurrentPlayer().getWorkerSecond().getPosition().getRowIdentifier());
        assertEquals(4, model.getCurrentPlayer().getWorkerSecond().getPosition().getColIdentifier());
        model.endTurn();  //next turn

        gamePreparationCommand = new GamePreparationCommand(2,1, 0, 0);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        model.endTurn();
        model.endTurn();  //now it's again his turn
        assertEquals(2, model.getCurrentPlayer().getWorkerFirst().getPosition().getRowIdentifier());
        assertEquals(1, model.getCurrentPlayer().getWorkerFirst().getPosition().getColIdentifier());
        assertEquals(0, model.getCurrentPlayer().getWorkerSecond().getPosition().getRowIdentifier());
        assertEquals(0, model.getCurrentPlayer().getWorkerSecond().getPosition().getColIdentifier());
        model.endTurn();  //next turn

        gamePreparationCommand = new GamePreparationCommand(4,3, 4, 2);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        model.endTurn();
        model.endTurn();  //now it's again his turn
        assertEquals(4, model.getCurrentPlayer().getWorkerFirst().getPosition().getRowIdentifier());
        assertEquals(3, model.getCurrentPlayer().getWorkerFirst().getPosition().getColIdentifier());
        assertEquals(4, model.getCurrentPlayer().getWorkerSecond().getPosition().getRowIdentifier());
        assertEquals(2, model.getCurrentPlayer().getWorkerSecond().getPosition().getColIdentifier());
        model.endTurn();  //next turn
    }

    @Test
    public void updateGamePreparationCommand2PlayerTest() {

        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea",  match);
        Player p2 = new Player("Cosimo",  match);
        match.addPlayer(p1);
        match.addPlayer(p2);

        Model model = new Model(match);
        Controller controller = new Controller(model);

        model.nextGamePhase();  //Choose_Gods phase
        model.nextGamePhase();

        Apollo apollo = new Apollo();
        Hera hera = new Hera();

        p1.setGodStrategy(apollo);
        p2.setGodStrategy(hera);

        GamePreparationCommand gamePreparationCommand = new GamePreparationCommand(1,3, 3, 4);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        model.endTurn();  //now it's again his turn
        assertEquals(1, model.getCurrentPlayer().getWorkerFirst().getPosition().getRowIdentifier());
        assertEquals(3, model.getCurrentPlayer().getWorkerFirst().getPosition().getColIdentifier());
        assertEquals(3, model.getCurrentPlayer().getWorkerSecond().getPosition().getRowIdentifier());
        assertEquals(4, model.getCurrentPlayer().getWorkerSecond().getPosition().getColIdentifier());
        model.endTurn();  //next turn

        gamePreparationCommand = new GamePreparationCommand(2,1, 0, 0);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        model.endTurn();  //now it's again his turn
        assertEquals(2, model.getCurrentPlayer().getWorkerFirst().getPosition().getRowIdentifier());
        assertEquals(1, model.getCurrentPlayer().getWorkerFirst().getPosition().getColIdentifier());
        assertEquals(0, model.getCurrentPlayer().getWorkerSecond().getPosition().getRowIdentifier());
        assertEquals(0, model.getCurrentPlayer().getWorkerSecond().getPosition().getColIdentifier());
        model.endTurn();  //next turn
    }

    @Test
    public void updateGamePreparationCommandSameCellTest() {

        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea",  match);
        Player p2 = new Player("Cosimo",  match);
        Player p3 = new Player("Roberto",  match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Model model = new Model(match);
        Controller controller = new Controller(model);

        model.nextGamePhase();  //Choose_Gods phase
        model.nextGamePhase();

        Apollo apollo = new Apollo();
        Hera hera = new Hera();
        Athena athena = new Athena();

        p1.setGodStrategy(apollo);
        p2.setGodStrategy(hera);
        p3.setGodStrategy(athena);

        GamePreparationCommand gamePreparationCommand = new GamePreparationCommand(1,3, 3, 4);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        model.endTurn();
        model.endTurn();  //now it's again his turn
        assertEquals(1, model.getCurrentPlayer().getWorkerFirst().getPosition().getRowIdentifier());
        assertEquals(3, model.getCurrentPlayer().getWorkerFirst().getPosition().getColIdentifier());
        assertEquals(3, model.getCurrentPlayer().getWorkerSecond().getPosition().getRowIdentifier());
        assertEquals(4, model.getCurrentPlayer().getWorkerSecond().getPosition().getColIdentifier());
        model.endTurn();  //next turn

        gamePreparationCommand = new GamePreparationCommand(2,1, 0, 0);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        model.endTurn();
        model.endTurn();  //now it's again his turn
        assertEquals(2, model.getCurrentPlayer().getWorkerFirst().getPosition().getRowIdentifier());
        assertEquals(1, model.getCurrentPlayer().getWorkerFirst().getPosition().getColIdentifier());
        assertEquals(0, model.getCurrentPlayer().getWorkerSecond().getPosition().getRowIdentifier());
        assertEquals(0, model.getCurrentPlayer().getWorkerSecond().getPosition().getColIdentifier());
        model.endTurn();  //next turn

        gamePreparationCommand = new GamePreparationCommand(4,3, 0, 0);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        assertEquals(null, model.getCurrentPlayer().getWorkerFirst().getPosition());
        assertEquals(null, model.getCurrentPlayer().getWorkerFirst().getPosition());
        assertEquals(null, model.getCurrentPlayer().getWorkerSecond().getPosition());
        assertEquals(null, model.getCurrentPlayer().getWorkerSecond().getPosition());
        model.endTurn();  //next turn
    }

    @Test
    public void updateGamePreparationCommandGodPowerTest() {

        Board.clearInstances();
        Match.clearInstances();

        int playersNum = 3;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p1 = new Player("Andrea",  match);
        Player p2 = new Player("Cosimo",  match);
        Player p3 = new Player("Roberto",  match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Model model = new Model(match);
        Controller controller = new Controller(model);

        model.nextGamePhase();  //Choose_Gods phase
        model.nextGamePhase();

        Eros eros = new Eros();
        ArrayList<Player> players = new ArrayList<>(model.getPlayers());

        GamePreparationCommand gamePreparationCommand = new GamePreparationCommand(1,3, 3, 4);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        model.getCurrentPlayer().setGodStrategy(eros);
        controller.update(gamePreparationCommand);
        for(Player player : players) {
            assertEquals(null, player.getWorkerFirst().getPosition());
            assertEquals(null, player.getWorkerFirst().getPosition());
            assertEquals(null, player.getWorkerSecond().getPosition());
            assertEquals(null, player.getWorkerSecond().getPosition());
        }

        gamePreparationCommand = new GamePreparationCommand(0,2, 2, 4);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        for(Player player : players) {
            assertEquals(null, player.getWorkerFirst().getPosition());
            assertEquals(null, player.getWorkerFirst().getPosition());
            assertEquals(null, player.getWorkerSecond().getPosition());
            assertEquals(null, player.getWorkerSecond().getPosition());
        }

        gamePreparationCommand = new GamePreparationCommand(0,2, 3, 2);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        for(Player player : players) {
            assertEquals(null, player.getWorkerFirst().getPosition());
            assertEquals(null, player.getWorkerFirst().getPosition());
            assertEquals(null, player.getWorkerSecond().getPosition());
            assertEquals(null, player.getWorkerSecond().getPosition());
        }

        gamePreparationCommand = new GamePreparationCommand(3, 0, 1, 4);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        model.endTurn();
        model.endTurn();  //now it's again his turn
        assertEquals(3, model.getCurrentPlayer().getWorkerFirst().getPosition().getRowIdentifier());
        assertEquals(0, model.getCurrentPlayer().getWorkerFirst().getPosition().getColIdentifier());
        assertEquals(1, model.getCurrentPlayer().getWorkerSecond().getPosition().getRowIdentifier());
        assertEquals(4, model.getCurrentPlayer().getWorkerSecond().getPosition().getColIdentifier());
    }

}