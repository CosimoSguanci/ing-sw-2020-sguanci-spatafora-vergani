package it.polimi.ingsw.controller;


import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.exceptions.InvalidColorException;
import it.polimi.ingsw.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.exceptions.WrongPlayerException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gods.*;
import it.polimi.ingsw.model.updates.LoseUpdate;
import it.polimi.ingsw.model.utils.GodsUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ControllerTest {

    @Test
    public void updatePlayerCommandNormalTest() {

        int playersNum = 3;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
        Player p3 = new Player("Roberto", new Model(match), match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Worker w1A = p1.getWorkerFirst();
        Worker w1B = p1.getWorkerSecond();

        Worker w2A = p2.getWorkerFirst();
        Worker w2B = p2.getWorkerSecond();

        Worker w3A = p3.getWorkerFirst();
        Worker w3B = p3.getWorkerSecond();

        GodStrategy divinity1 = new Apollo();
        GodStrategy divinity2 = new Athena();
        GodStrategy divinity3 = new Demeter();
        p1.setGodStrategy(divinity1);
        p2.setGodStrategy(divinity2);
        p3.setGodStrategy(divinity3);

        w1A.setInitialPosition(1, 2);
        w1B.setInitialPosition(0, 4);
        w2A.setInitialPosition(2, 4);
        w2B.setInitialPosition(3, 3);
        w3A.setInitialPosition(0, 1);
        w3B.setInitialPosition(3, 1);

        assertEquals(match.getMatchBoard().getCell(1, 2), w1A.getPosition());

        Model model = new Model(match);
        Controller controller = new Controller(model);

        model.nextGamePhase();  //Choose_Gods phase
        model.nextGamePhase();  //Game_Preparation phase
        model.nextGamePhase();  //Real_Game phase

        PlayerCommand playerCommand = PlayerCommand.parseInput("move w1 C4");
        playerCommand.setPlayerID("Andrea");
        controller.update(playerCommand);

        assertEquals(p1, model.getCurrentPlayer());
        assertEquals(match.getMatchBoard().getCell(2, 3), w1A.getPosition());


    }


    @Test
    public void updatePlayerCommandGodPowerTest() {


        int playersNum = 2;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
        match.addPlayer(p1);
        match.addPlayer(p2);

        Worker w1A = p1.getWorkerFirst();
        Worker w1B = p1.getWorkerSecond();

        Worker w2A = p2.getWorkerFirst();
        Worker w2B = p2.getWorkerSecond();

        GodStrategy divinity = new Apollo();
        GodStrategy divine = new Artemis();
        p1.setGodStrategy(divinity);
        p2.setGodStrategy(divine);

        w1A.setInitialPosition(1, 3);
        w1B.setInitialPosition(3, 1);
        w2A.setInitialPosition(4, 4);
        w2B.setInitialPosition(2, 3);

        assertEquals(match.getMatchBoard().getCell(1, 3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3, 1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4, 4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(2, 3), w2B.getPosition());

        Model model = new Model(match);
        Controller controller = new Controller(model);

        model.nextGamePhase();  //Choose_Gods phase
        model.nextGamePhase();  //Game_Preparation phase
        model.nextGamePhase();  //Real_Game phase

        PlayerCommand playerCommand = PlayerCommand.parseInput("move w1 C4");
        playerCommand.setPlayer(p1);
        controller.update(playerCommand);  //Apollo's power must be invoked

        assertEquals(p1, model.getCurrentPlayer());
        assertEquals(match.getMatchBoard().getCell(2, 3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3, 1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4, 4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1, 3), w2B.getPosition());

        match.nextTurn();  //Cosimo's turn
        assertEquals(p2, model.getCurrentPlayer());

        playerCommand = PlayerCommand.parseInput("move w2 e1");
        playerCommand.setPlayer(p1);

        PlayerCommand finalPlayerCommand = playerCommand;
        assertThrows(WrongPlayerException.class, () -> controller.handlePlayerCommand(finalPlayerCommand));

        assertEquals(match.getMatchBoard().getCell(2, 3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3, 1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4, 4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1, 3), w2B.getPosition());

        match.nextTurn();  //Andrea's turn
        assertEquals(p1, model.getCurrentPlayer());
        match.nextTurn();  //Cosimo's turn
        assertEquals(p2, model.getCurrentPlayer());

        playerCommand = PlayerCommand.parseInput("move w2 e4");
        playerCommand.setPlayer(p2);
        controller.update(playerCommand);  //impossible move should not be allowed

        assertEquals(match.getMatchBoard().getCell(2, 3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3, 1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4, 4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1, 3), w2B.getPosition());

        playerCommand = PlayerCommand.parseInput("move w1 e4");
        playerCommand.setPlayer(p2);
        controller.update(playerCommand);

        assertEquals(match.getMatchBoard().getCell(2, 3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3, 1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4, 3), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1, 3), w2B.getPosition());


    }


    @Test
    public void updatePlayerCommandImpossibleMoveAndBuildTest() {

        int playersNum = 2;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
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

        w1A.setInitialPosition(1, 3);
        w1B.setInitialPosition(3, 1);
        w2A.setInitialPosition(4, 4);
        w2B.setInitialPosition(2, 3);

        assertEquals(match.getMatchBoard().getCell(1, 3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3, 1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4, 4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(2, 3), w2B.getPosition());

        Model model = new Model(match);
        Controller controller = new Controller(model);

        model.nextGamePhase();  //Choose_Gods phase
        model.nextGamePhase();  //Game_Preparation phase
        model.nextGamePhase();  //Real_Game phase

        PlayerCommand playerCommand = PlayerCommand.parseInput("move w1 C4");
        playerCommand.setPlayer(p1);
        controller.update(playerCommand);  //Apollo's power must be invoked

        assertEquals(match.getMatchBoard().getCell(2, 3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3, 1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4, 4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1, 3), w2B.getPosition());

        playerCommand = PlayerCommand.parseInput("build w1 b2");
        playerCommand.setPlayer(p1);
        controller.update(playerCommand);  //impossible build should not be allowed

        assertEquals(match.getMatchBoard().getCell(2, 3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3, 1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4, 4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1, 3), w2B.getPosition());
        assertEquals(BlockType.GROUND, match.getMatchBoard().getCell(1, 1).getLevel());

        match.nextTurn();  //Cosimo's turn

        playerCommand = PlayerCommand.parseInput("move w2 e1");
        playerCommand.setPlayer(p1);


        PlayerCommand finalPlayerCommand = playerCommand;
        assertThrows(WrongPlayerException.class, () -> controller.handlePlayerCommand(finalPlayerCommand));

        assertEquals(match.getMatchBoard().getCell(2, 3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3, 1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4, 4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1, 3), w2B.getPosition());

        playerCommand = PlayerCommand.parseInput("move w1 e4");
        playerCommand.setPlayer(p2);
        controller.update(playerCommand);

        assertEquals(match.getMatchBoard().getCell(2, 3), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3, 1), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(4, 3), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(1, 3), w2B.getPosition());


    }


    @Test
    public void updatePlayerCommandCompleteTest() {

        int playersNum = 3;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
        Player p3 = new Player("Roberto", new Model(match), match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Worker w1A = p1.getWorkerFirst();
        Worker w1B = p1.getWorkerSecond();

        Worker w2A = p2.getWorkerFirst();
        Worker w2B = p2.getWorkerSecond();

        Worker w3A = p3.getWorkerFirst();
        Worker w3B = p3.getWorkerSecond();


        GodStrategy divinity1 = new Apollo();
        GodStrategy divinity2 = new Poseidon();
        GodStrategy divinity3 = new Zeus();
        p1.setGodStrategy(divinity1);
        p2.setGodStrategy(divinity2);
        p3.setGodStrategy(divinity3);

        w1A.setInitialPosition(1, 2);
        w1B.setInitialPosition(0, 4);
        w2A.setInitialPosition(2, 4);
        w2B.setInitialPosition(3, 3);
        w3A.setInitialPosition(0, 1);
        w3B.setInitialPosition(3, 1);

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

        assertEquals(match.getMatchBoard().getCell(1, 2), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(0, 4), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(2, 4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3, 3), w2B.getPosition());
        assertEquals(match.getMatchBoard().getCell(0, 1), w3A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3, 1), w3B.getPosition());

        playerCommand = PlayerCommand.parseInput("move w2 C1");
        playerCommand.setPlayer(p3);
        controller.update(playerCommand);  //possible move should be allowed

        assertEquals(match.getMatchBoard().getCell(1, 2), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(0, 4), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(2, 4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3, 3), w2B.getPosition());
        assertEquals(match.getMatchBoard().getCell(0, 1), w3A.getPosition());
        assertEquals(match.getMatchBoard().getCell(2, 0), w3B.getPosition());

        playerCommand = PlayerCommand.parseInput("build w2 E1");
        playerCommand.setPlayer(p3);
        controller.update(playerCommand);  //impossible build should not be allowed

        assertEquals(match.getMatchBoard().getCell(1, 2), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(0, 4), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(2, 4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3, 3), w2B.getPosition());
        assertEquals(match.getMatchBoard().getCell(0, 1), w3A.getPosition());
        assertEquals(match.getMatchBoard().getCell(2, 0), w3B.getPosition());

        playerCommand = PlayerCommand.parseInput("build w2 b2");
        playerCommand.setPlayer(p3);
        controller.update(playerCommand);  //possible build should be allowed

        assertEquals(match.getMatchBoard().getCell(1, 2), w1A.getPosition());
        assertEquals(match.getMatchBoard().getCell(0, 4), w1B.getPosition());
        assertEquals(match.getMatchBoard().getCell(2, 4), w2A.getPosition());
        assertEquals(match.getMatchBoard().getCell(3, 3), w2B.getPosition());
        assertEquals(match.getMatchBoard().getCell(0, 1), w3A.getPosition());
        assertEquals(match.getMatchBoard().getCell(2, 0), w3B.getPosition());
        assertEquals(BlockType.LEVEL_ONE, match.getMatchBoard().getCell(1, 1).getLevel());

        playerCommand = PlayerCommand.parseInput("end");
        playerCommand.setPlayer(p3);
        controller.update(playerCommand);  //now it's p1's turn
        assertEquals(p1, model.getCurrentPlayer());
    }


    @Test
    public void updateGodChoiceCommandNormalTest() throws NoSuchFieldException, IllegalAccessException {

        int playersNum = 3;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
        Player p3 = new Player("Roberto", new Model(match), match);
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
        ArrayList<Player> players = new ArrayList<>(model.getPlayers());
        GodChoiceCommand godChoiceCommand = new GodChoiceCommand(chosenGods);
        godChoiceCommand.setPlayer(model.getCurrentPlayer());
        model.getCurrentPlayer().setAsGodChooser();

        Field godChooserPlayer = Controller.class.getDeclaredField("godChooserPlayer");
        godChooserPlayer.setAccessible(true);
        godChooserPlayer.set(controller, model.getCurrentPlayer());
        godChooserPlayer.setAccessible(false);

        controller.update(godChoiceCommand);

        List<String> godPlayer = new ArrayList<>();
        godPlayer.add("hestia");
        godChoiceCommand = new GodChoiceCommand(godPlayer);
        godChoiceCommand.setPlayer(model.getCurrentPlayer());
        controller.update(godChoiceCommand);

        godPlayer = new ArrayList<>();
        godPlayer.add("apollo");
        godChoiceCommand = new GodChoiceCommand(godPlayer);
        godChoiceCommand.setPlayer(model.getCurrentPlayer());
        controller.update(godChoiceCommand);

        int apollo = 0, hestia = 0, athena = 0;
        for (Player p : players) {
            if (p.getGodStrategy() instanceof Apollo && !p.isGodChooser()) apollo++;
            else if (p.getGodStrategy() instanceof Hestia && !p.isGodChooser()) hestia++;
            else if (p.getGodStrategy() instanceof Athena && p.isGodChooser()) athena++;
        }
        assertEquals(1, hestia);
        assertEquals(1, apollo);
        assertEquals(1, athena);


    }


    @Test
    public void updateGodChoiceCommand2PlayersTest() throws NoSuchFieldException, IllegalAccessException {

        int playersNum = 2;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Marco", new Model(match), match);
        match.addPlayer(p1);
        match.addPlayer(p2);

        Model model = new Model(match);
        Controller controller = new Controller(model);

        model.nextGamePhase();  //Choose_Gods phase

        List<String> chosenGods = new ArrayList<>();
        chosenGods.add("eros");
        chosenGods.add("minotaur");
        ArrayList<Player> players = new ArrayList<>(model.getPlayers());
        GodChoiceCommand godChoiceCommand = new GodChoiceCommand(chosenGods);
        godChoiceCommand.setPlayer(model.getCurrentPlayer());
        model.getCurrentPlayer().setAsGodChooser();

        Field godChooserPlayer = Controller.class.getDeclaredField("godChooserPlayer");
        godChooserPlayer.setAccessible(true);
        godChooserPlayer.set(controller, model.getCurrentPlayer());
        godChooserPlayer.setAccessible(false);

        controller.update(godChoiceCommand);

        List<String> godPlayer = new ArrayList<>();
        godPlayer.add("eros");
        godChoiceCommand = new GodChoiceCommand(godPlayer);
        godChoiceCommand.setPlayer(model.getCurrentPlayer());
        controller.update(godChoiceCommand);

        int eros = 0, minotaur = 0;
        for (Player p : players) {
            if (p.getGodStrategy() instanceof Eros && !p.isGodChooser()) eros++;
            else if (p.getGodStrategy() instanceof Minotaur && p.isGodChooser()) minotaur++;
        }
        assertEquals(1, eros);
        assertEquals(1, minotaur);

    }


    @Test
    public void updateInitialInfoCommandTest() {

        int playersNum = 3;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
        Player p3 = new Player("Roberto", new Model(match), match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Model model = new Model(match);
        Controller controller = new Controller(model);
        controller.initialPhase();  //random first player to play

        String nick1 = "pippo";
        String nick2 = "pluto";
        String nick3 = "tex";
        PrintableColor colour1 = PrintableColor.BLUE;
        PrintableColor colour2 = PrintableColor.GREEN;
        PrintableColor colour3 = PrintableColor.RED;
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
        for (Player player : players) {
            if (player.getNickname().equals("pippo") && player.getColor() == PrintableColor.BLUE) {
                pippoBlue++;
            } else if (player.getNickname().equals("pluto") && player.getColor() == PrintableColor.GREEN) {
                plutoGreen++;
            } else if (player.getNickname().equals("tex") && player.getColor() == PrintableColor.RED) {
                texRed++;
            }
        }

        assertEquals(1, pippoBlue);
        assertEquals(1, plutoGreen);
        assertEquals(1, texRed);
    }

    @Test
    public void updateInitialInfoCommandNicknameAlreadyTakenExceptionTest() {

        int playersNum = 3;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
        Player p3 = new Player("Roberto", new Model(match), match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Model model = new Model(match);
        Controller controller = new Controller(model);
        controller.initialPhase();  //random first player to play

        String nick1 = "pippo";
        String nick2 = "pluto";
        PrintableColor colour1 = PrintableColor.BLUE;
        PrintableColor colour2 = PrintableColor.GREEN;
        PrintableColor colour3 = PrintableColor.RED;
        InitialInfoCommand initialInfoCommand = new InitialInfoCommand(nick1, colour1);
        initialInfoCommand.setPlayer(model.getCurrentPlayer());
        controller.update(initialInfoCommand);

        initialInfoCommand = new InitialInfoCommand(nick2, colour2);
        initialInfoCommand.setPlayer(model.getCurrentPlayer());
        controller.update(initialInfoCommand);

        ArrayList<Player> players = new ArrayList<>(model.getPlayers());
        int pippoBlue = 0;
        int plutoGreen = 0;
        for (Player player : players) {
            if (player.getNickname() != null && player.getColor() != null) {
                if (player.getNickname().equals("pippo") && player.getColor() == PrintableColor.BLUE) {
                    pippoBlue++;
                } else if (player.getNickname().equals("pluto") && player.getColor() == PrintableColor.GREEN) {
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


        int playersNum = 3;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
        Player p3 = new Player("Roberto", new Model(match), match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Model model = new Model(match);
        Controller controller = new Controller(model);
        controller.initialPhase();  //random first player to play

        String nick1 = "pippo";
        String nick2 = "pluto";
        PrintableColor colour = PrintableColor.YELLOW;
        InitialInfoCommand initialInfoCommand = new InitialInfoCommand(nick1, colour);
        initialInfoCommand.setPlayer(model.getCurrentPlayer());
        controller.update(initialInfoCommand);

        ArrayList<Player> players = new ArrayList<>(model.getPlayers());
        int pippoYellow = 0;
        for (Player player : players) {
            if (player.getNickname() != null && player.getColor() != null) {
                if (player.getNickname().equals("pippo") && player.getColor() == PrintableColor.YELLOW) {
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

        int playersNum = 2;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Marco", new Model(match), match);
        match.addPlayer(p1);
        match.addPlayer(p2);

        Model model = new Model(match);
        Controller controller = new Controller(model);
        controller.initialPhase();  //random first player to play

        String nick1 = "pippo";
        String nick2 = "pluto";
        PrintableColor colour1 = PrintableColor.GREEN;
        PrintableColor colour2 = PrintableColor.RED;
        InitialInfoCommand initialInfoCommand = new InitialInfoCommand(nick1, colour1);
        initialInfoCommand.setPlayer(model.getCurrentPlayer());
        controller.update(initialInfoCommand);

        initialInfoCommand = new InitialInfoCommand(nick2, colour2);
        initialInfoCommand.setPlayer(model.getCurrentPlayer());
        controller.update(initialInfoCommand);

        ArrayList<Player> players = new ArrayList<>(model.getPlayers());
        int pippoGreen = 0;
        int plutoRed = 0;
        for (Player player : players) {
            if (player.getNickname().equals("pippo") && player.getColor() == PrintableColor.GREEN) {
                pippoGreen++;
            } else if (player.getNickname().equals("pluto") && player.getColor() == PrintableColor.RED) {
                plutoRed++;
            }
        }

        assertEquals(1, pippoGreen);
        assertEquals(1, plutoRed);
    }

    @Test
    public void updateGamePreparationCommandTest() {

        int playersNum = 3;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
        Player p3 = new Player("Roberto", new Model(match), match);
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

        GamePreparationCommand gamePreparationCommand = new GamePreparationCommand(1, 3, 3, 4);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        model.endTurn();
        model.endTurn();  //now it's again his turn
        assertEquals(1, model.getCurrentPlayer().getWorkerFirst().getPosition().getRowIdentifier());
        assertEquals(3, model.getCurrentPlayer().getWorkerFirst().getPosition().getColIdentifier());
        assertEquals(3, model.getCurrentPlayer().getWorkerSecond().getPosition().getRowIdentifier());
        assertEquals(4, model.getCurrentPlayer().getWorkerSecond().getPosition().getColIdentifier());
        model.endTurn();  //next turn

        gamePreparationCommand = new GamePreparationCommand(2, 1, 0, 0);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        model.endTurn();
        model.endTurn();  //now it's again his turn
        assertEquals(2, model.getCurrentPlayer().getWorkerFirst().getPosition().getRowIdentifier());
        assertEquals(1, model.getCurrentPlayer().getWorkerFirst().getPosition().getColIdentifier());
        assertEquals(0, model.getCurrentPlayer().getWorkerSecond().getPosition().getRowIdentifier());
        assertEquals(0, model.getCurrentPlayer().getWorkerSecond().getPosition().getColIdentifier());
        model.endTurn();  //next turn

        gamePreparationCommand = new GamePreparationCommand(4, 3, 4, 2);
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

        int playersNum = 3;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
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

        GamePreparationCommand gamePreparationCommand = new GamePreparationCommand(1, 3, 3, 4);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        model.endTurn();  //now it's again his turn
        assertEquals(1, model.getCurrentPlayer().getWorkerFirst().getPosition().getRowIdentifier());
        assertEquals(3, model.getCurrentPlayer().getWorkerFirst().getPosition().getColIdentifier());
        assertEquals(3, model.getCurrentPlayer().getWorkerSecond().getPosition().getRowIdentifier());
        assertEquals(4, model.getCurrentPlayer().getWorkerSecond().getPosition().getColIdentifier());
        model.endTurn();  //next turn

        gamePreparationCommand = new GamePreparationCommand(2, 1, 0, 0);
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

        int playersNum = 3;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
        Player p3 = new Player("Roberto", new Model(match), match);
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

        GamePreparationCommand gamePreparationCommand = new GamePreparationCommand(1, 3, 3, 4);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        model.endTurn();
        model.endTurn();  //now it's again his turn
        assertEquals(1, model.getCurrentPlayer().getWorkerFirst().getPosition().getRowIdentifier());
        assertEquals(3, model.getCurrentPlayer().getWorkerFirst().getPosition().getColIdentifier());
        assertEquals(3, model.getCurrentPlayer().getWorkerSecond().getPosition().getRowIdentifier());
        assertEquals(4, model.getCurrentPlayer().getWorkerSecond().getPosition().getColIdentifier());
        model.endTurn();  //next turn

        gamePreparationCommand = new GamePreparationCommand(2, 1, 0, 0);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        model.endTurn();
        model.endTurn();  //now it's again his turn
        assertEquals(2, model.getCurrentPlayer().getWorkerFirst().getPosition().getRowIdentifier());
        assertEquals(1, model.getCurrentPlayer().getWorkerFirst().getPosition().getColIdentifier());
        assertEquals(0, model.getCurrentPlayer().getWorkerSecond().getPosition().getRowIdentifier());
        assertEquals(0, model.getCurrentPlayer().getWorkerSecond().getPosition().getColIdentifier());
        model.endTurn();  //next turn

        gamePreparationCommand = new GamePreparationCommand(4, 3, 0, 0);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        assertNull(model.getCurrentPlayer().getWorkerFirst().getPosition());
        assertNull(model.getCurrentPlayer().getWorkerFirst().getPosition());
        assertNull(model.getCurrentPlayer().getWorkerSecond().getPosition());
        assertNull(model.getCurrentPlayer().getWorkerSecond().getPosition());
        model.endTurn();  //next turn
    }

    @Test
    public void updateGamePreparationCommandGodPowerTest() {


        int playersNum = 3;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
        Player p3 = new Player("Roberto", new Model(match), match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Model model = new Model(match);
        Controller controller = new Controller(model);

        model.nextGamePhase();  //Choose_Gods phase
        model.nextGamePhase();

        Eros eros = new Eros();
        ArrayList<Player> players = new ArrayList<>(model.getPlayers());

        GamePreparationCommand gamePreparationCommand = new GamePreparationCommand(1, 3, 3, 4);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        model.getCurrentPlayer().setGodStrategy(eros);

        p2.setGodStrategy(new Apollo());
        p3.setGodStrategy(new Atlas());

        controller.update(gamePreparationCommand);
        for (Player player : players) {
            assertNull(player.getWorkerFirst().getPosition());
            assertNull(player.getWorkerFirst().getPosition());
            assertNull(player.getWorkerSecond().getPosition());
            assertNull(player.getWorkerSecond().getPosition());
        }

        gamePreparationCommand = new GamePreparationCommand(0, 2, 2, 4);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        for (Player player : players) {
            assertNull(player.getWorkerFirst().getPosition());
            assertNull(player.getWorkerFirst().getPosition());
            assertNull(player.getWorkerSecond().getPosition());
            assertNull(player.getWorkerSecond().getPosition());
        }

        gamePreparationCommand = new GamePreparationCommand(0, 2, 3, 2);
        gamePreparationCommand.setPlayer(model.getCurrentPlayer());
        controller.update(gamePreparationCommand);
        for (Player player : players) {
            assertNull(player.getWorkerFirst().getPosition());
            assertNull(player.getWorkerFirst().getPosition());
            assertNull(player.getWorkerSecond().getPosition());
            assertNull(player.getWorkerSecond().getPosition());
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

    @Test
    public void checkCanMoveOtherGodsConstraintsTest() {

        int playersNum = 3;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
        Player p3 = new Player("Roberto", new Model(match), match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Model model = new Model(match);
        Controller controller = new Controller(model);

        p1.setGodStrategy(GodStrategy.instantiateGod("athena"));
        p2.setGodStrategy(GodStrategy.instantiateGod("atlas"));
        p3.setGodStrategy(GodStrategy.instantiateGod("poseidon"));

        p1.getWorkerFirst().setInitialPosition(3, 3);
        p1.getWorkerSecond().setInitialPosition(3, 4);

        p2.getWorkerFirst().setInitialPosition(0, 0);
        p2.getWorkerSecond().setInitialPosition(0, 1);

        p3.getWorkerFirst().setInitialPosition(3, 2);
        p3.getWorkerSecond().setInitialPosition(2, 4);

        match.getMatchBoard().getCell(1, 0).setLevel(BlockType.DOME);
        match.getMatchBoard().getCell(0, 2).setLevel(BlockType.DOME);
        match.getMatchBoard().getCell(2, 3).setLevel(BlockType.LEVEL_ONE);
        match.getMatchBoard().getCell(1, 1).setLevel(BlockType.LEVEL_ONE);
        match.getMatchBoard().getCell(1, 2).setLevel(BlockType.LEVEL_ONE);

        ///
        model.setInitialTurn(0);

        model.nextGamePhase();
        model.nextGamePhase();
        model.nextGamePhase();

        PlayerCommand playerCommand1 = new PlayerCommand(CommandType.MOVE, Command.WORKER_SECOND, 2, 3, null);
        playerCommand1.setPlayer(p1);
        controller.update(playerCommand1);

        PlayerCommand playerCommand2 = new PlayerCommand(CommandType.BUILD, Command.WORKER_SECOND, 1, 4, BlockType.LEVEL_ONE);
        playerCommand2.setPlayer(p1);
        controller.update(playerCommand2);

        PlayerCommand playerCommand3 = new PlayerCommand(CommandType.END_TURN, Command.WORKER_SECOND, -1, -1, null);
        playerCommand3.setPlayer(p1);
        controller.update(playerCommand3);

        // Atlas must have been removed from the match
        assertSame(playersNum - 1, model.getPlayers().size()); // Atlas must have been removed from the match
        assertFalse(model.getPlayers().contains(p2));
        assertTrue(model.getPlayers().contains(p1) && model.getPlayers().contains(p3));

        // Reflection
      /*  Method privateMethod = Controller.class.
                getDeclaredMethod("checkCanMoveOtherGodsConstraints", Player.class);

        privateMethod.setAccessible(true);

        boolean returnValue = (boolean)
                privateMethod.invoke(controller, p2);


        assertFalse(returnValue); */
    }

    @Test
    public void updateExceptions() {
        int playersNum = 3;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
        Player p3 = new Player("Roberto", new Model(match), match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        //Model model = new Model(match);
        // Model model = mock(Model.class);
        Model model = Mockito.spy(new Model(match));
        Controller controller = new Controller(model);

        p1.setGodStrategy(GodStrategy.instantiateGod("athena"));
        p2.setGodStrategy(GodStrategy.instantiateGod("atlas"));
        p3.setGodStrategy(GodStrategy.instantiateGod("poseidon"));

        p1.getWorkerFirst().setInitialPosition(3, 3);
        p1.getWorkerSecond().setInitialPosition(3, 4);

        p2.getWorkerFirst().setInitialPosition(0, 0);
        p2.getWorkerSecond().setInitialPosition(0, 1);

        p3.getWorkerFirst().setInitialPosition(3, 2);
        p3.getWorkerSecond().setInitialPosition(2, 4);

        controller.initialPhase();
        model.setInitialTurn(0);

        PlayerCommand playerCommand = PlayerCommand.parseInput("move w1 a1");
        playerCommand.setPlayerID("Andrea");
        playerCommand.setPlayer(p1);

        controller.update(playerCommand);

        verify(model, times(1)).reportError(p1, CommandType.MOVE, ErrorType.WRONG_GAME_PHASE, null);

        InitialInfoCommand initialInfoCommand = new InitialInfoCommand("nickname", PrintableColor.GREEN);
        initialInfoCommand.setPlayerID("Cosimo");
        initialInfoCommand.setPlayer(p2);

        controller.update(initialInfoCommand);

        verify(model, times(1)).reportError(p2, CommandType.PICK, ErrorType.WRONG_TURN, null);

        // Nickname Already Taken error test

        InitialInfoCommand initialInfoCommand2 = new InitialInfoCommand("nickname", PrintableColor.GREEN);
        initialInfoCommand2.setPlayerID("Andrea");
        initialInfoCommand2.setPlayer(p1);

        controller.update(initialInfoCommand2);

        model.endTurn();
        model.setInitialTurn(1);

        InitialInfoCommand initialInfoCommand3 = new InitialInfoCommand("nickname", PrintableColor.GREEN);
        initialInfoCommand3.setPlayerID("Cosimo");
        initialInfoCommand3.setPlayer(p2);

        controller.update(initialInfoCommand3);

        verify(model, times(1)).reportError(p2, CommandType.PICK, ErrorType.ALREADY_TAKEN_NICKNAME, null);

        // Invalid Color error test

        InitialInfoCommand initialInfoCommand4 = new InitialInfoCommand("nickname2", PrintableColor.GREEN);
        initialInfoCommand4.setPlayerID("Cosimo");
        initialInfoCommand4.setPlayer(p2);

        controller.update(initialInfoCommand4);

        verify(model, times(1)).reportError(p2, CommandType.PICK, ErrorType.INVALID_COLOR, null);

        // Invalid God Exception

        model.nextGamePhase();

        Optional<Player> godChooserOpt = model.getPlayers().stream().filter(Player::isGodChooser).findFirst();

        if (!godChooserOpt.isPresent()) throw new IllegalArgumentException();

        Player godChooser = godChooserOpt.get();

        List<String> gods = new ArrayList<>();
        gods.add("apollo");
        gods.add("athena");
        gods.add("hestia");

        GodChoiceCommand godChoiceCommand = new GodChoiceCommand(gods);
        godChoiceCommand.setPlayerID(godChooser.getPlayerID());
        godChoiceCommand.setPlayer(godChooser);

        int turn = godChooser.equals(p1) ? 0 : godChooser.equals(p2) ? 1 : 2;

        model.setInitialTurn(turn);

        controller.update(godChoiceCommand);

        turn = turn == 2 ? 0 : turn + 1;

        model.setInitialTurn(turn);

        List<String> chosenGods = new ArrayList<>();
        chosenGods.add("minotaur");

        GodChoiceCommand godChoiceCommand1 = new GodChoiceCommand(chosenGods);
        Player turnPlayer = model.getPlayers().get(turn);
        godChoiceCommand1.setPlayerID(turnPlayer.getPlayerID());
        godChoiceCommand1.setPlayer(turnPlayer);

        controller.update(godChoiceCommand1);

        verify(model, times(1)).reportError(turnPlayer, CommandType.SELECT, ErrorType.INVALID_GOD, null);

        // Selectable god but wrong Player

        chosenGods = new ArrayList<>();
        chosenGods.add("apollo");

        GodChoiceCommand godChoiceCommand2 = new GodChoiceCommand(chosenGods);
        turn = turn == 2 ? 0 : turn + 1;
        Player wrongPlayer = model.getPlayers().get(turn);
        godChoiceCommand2.setPlayerID(wrongPlayer.getPlayerID());
        godChoiceCommand2.setPlayer(wrongPlayer);

        controller.update(godChoiceCommand2);

        verify(model, times(1)).reportError(wrongPlayer, CommandType.SELECT, ErrorType.WRONG_TURN, null);

        // Game Preparation Wrong Player test

        model.nextGamePhase();
        model.setInitialTurn(0);

        GamePreparationCommand gamePreparationCommand = GamePreparationCommand.parseInput("place w1 a1 w2 a2");

        gamePreparationCommand.setPlayer(p2);
        gamePreparationCommand.setPlayerID(p2.getPlayerID());

        controller.update(gamePreparationCommand);

        verify(model, times(1)).reportError(p2, CommandType.PLACE, ErrorType.WRONG_TURN, null);

    }

    @Test
    public void startMatchCalledTest() {
        int playersNum = 3;
        Match match = new Match(playersNum);
        Model model = Mockito.spy(new Model(match));

        Player p1 = new Player("Andrea", model, match);
        Player p2 = new Player("Cosimo", model, match);
        Player p3 = new Player("Roberto", model, match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Controller controller = new Controller(model);

        p1.setNickname("nick1");
        p1.setColor(PrintableColor.RED);

        p1.setNickname("nick2");
        p1.setColor(PrintableColor.GREEN);

        p1.setNickname("nick3");
        p1.setColor(PrintableColor.BLUE);

        p1.setGodStrategy(GodStrategy.instantiateGod("athena"));
        p2.setGodStrategy(GodStrategy.instantiateGod("atlas"));
        p3.setGodStrategy(GodStrategy.instantiateGod("poseidon"));

        controller.initialPhase();
        model.nextGamePhase();
        model.nextGamePhase();

        Optional<Player> godChooserOpt = model.getPlayers().stream().filter(Player::isGodChooser).findFirst();

        if (!godChooserOpt.isPresent()) throw new IllegalArgumentException();

        Player godChooser = godChooserOpt.get();

        int turn = godChooser.equals(p1) ? 1 : godChooser.equals(p2) ? 2 : 0;

        model.setInitialTurn(turn);

        GamePreparationCommand gamePreparationCommand1 = GamePreparationCommand.parseInput("place w1 a1 w2 a2");

        gamePreparationCommand1.setPlayer(model.getCurrentPlayer());
        gamePreparationCommand1.setPlayerID(model.getCurrentPlayer().getPlayerID());

        controller.update(gamePreparationCommand1);

        GamePreparationCommand gamePreparationCommand2 = GamePreparationCommand.parseInput("place w1 b1 w2 e2");

        gamePreparationCommand2.setPlayer(model.getCurrentPlayer());
        gamePreparationCommand2.setPlayerID(model.getCurrentPlayer().getPlayerID());

        controller.update(gamePreparationCommand2);

        GamePreparationCommand gamePreparationCommand3 = GamePreparationCommand.parseInput("place w1 c3 w2 e5");

        gamePreparationCommand3.setPlayer(model.getCurrentPlayer());
        gamePreparationCommand3.setPlayerID(model.getCurrentPlayer().getPlayerID());

        controller.update(gamePreparationCommand3);

        verify(model, times(1)).matchStartedUpdate();
    }

    @Test
    public void checkAllMoveConstraintsFailed() {
        int playersNum = 3;
        Match match = new Match(playersNum);
        Model model = Mockito.spy(new Model(match));

        Player p1 = new Player("Andrea", model, match);
        Player p2 = new Player("Cosimo", model, match);
        Player p3 = new Player("Roberto", model, match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Controller controller = new Controller(model);

        p1.setNickname("nick1");
        p1.setColor(PrintableColor.RED);

        p1.setNickname("nick2");
        p1.setColor(PrintableColor.GREEN);

        p1.setNickname("nick3");
        p1.setColor(PrintableColor.BLUE);

        p1.setGodStrategy(GodStrategy.instantiateGod("athena"));
        p2.setGodStrategy(GodStrategy.instantiateGod("atlas"));
        p3.setGodStrategy(GodStrategy.instantiateGod("poseidon"));

        p1.getWorkerFirst().setInitialPosition(3, 3);
        p1.getWorkerSecond().setInitialPosition(3, 4);

        p2.getWorkerFirst().setInitialPosition(0, 0);
        p2.getWorkerSecond().setInitialPosition(0, 1);

        p3.getWorkerFirst().setInitialPosition(3, 2);
        p3.getWorkerSecond().setInitialPosition(2, 4);

        controller.initialPhase();
        model.nextGamePhase();
        model.nextGamePhase();
        model.nextGamePhase();

        model.setInitialTurn(0);

        model.getBoard().getCell(4, 4).setLevel(BlockType.LEVEL_ONE);

        PlayerCommand playerCommand = PlayerCommand.parseInput("move w2 e5");
        playerCommand.setPlayerID(p1.getPlayerID());

        controller.update(playerCommand);

        playerCommand = PlayerCommand.parseInput("build w2 d5");
        playerCommand.setPlayerID(p1.getPlayerID());

        controller.update(playerCommand);

        playerCommand = PlayerCommand.parseInput("end");
        playerCommand.setPlayerID(p1.getPlayerID());

        controller.update(playerCommand);

        model.getBoard().getCell(0, 2).setLevel(BlockType.LEVEL_ONE);

        playerCommand = PlayerCommand.parseInput("move w2 a3");
        playerCommand.setPlayerID(p2.getPlayerID());

        controller.update(playerCommand);

        verify(model, times(1)).reportError(p2, CommandType.MOVE, ErrorType.DENIED_BY_OPPONENT_GOD, GodsUtils.parseGodName("athena"));
    }

    @Test
    public void matchWonTest() {
        int playersNum = 3;
        Match match = new Match(playersNum);
        Model model = Mockito.spy(new Model(match));

        Player p1 = new Player("Andrea", model, match);
        Player p2 = new Player("Cosimo", model, match);
        Player p3 = new Player("Roberto", model, match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Controller controller = new Controller(model);

        p1.setNickname("nick1");
        p1.setColor(PrintableColor.RED);

        p1.setNickname("nick2");
        p1.setColor(PrintableColor.GREEN);

        p1.setNickname("nick3");
        p1.setColor(PrintableColor.BLUE);

        p1.setGodStrategy(GodStrategy.instantiateGod("athena"));
        p2.setGodStrategy(GodStrategy.instantiateGod("atlas"));
        p3.setGodStrategy(GodStrategy.instantiateGod("poseidon"));

        model.getBoard().getCell(4, 4).setLevel(BlockType.LEVEL_TWO);
        model.getBoard().getCell(3, 4).setLevel(BlockType.LEVEL_THREE);

        p1.getWorkerFirst().setInitialPosition(3, 3);
        p1.getWorkerSecond().setInitialPosition(3, 4);

        p2.getWorkerFirst().setInitialPosition(0, 0);
        p2.getWorkerSecond().setInitialPosition(0, 1);

        p3.getWorkerFirst().setInitialPosition(3, 2);
        p3.getWorkerSecond().setInitialPosition(2, 4);

        controller.initialPhase();
        model.nextGamePhase();
        model.nextGamePhase();
        model.nextGamePhase();

        model.setInitialTurn(0);

        p1.getWorkerSecond().move(model.getBoard().getCell(4, 4));
        p1.getWorkerSecond().reinitializeBuiltMoved();

        PlayerCommand playerCommand = PlayerCommand.parseInput("move w2 d5");
        playerCommand.setPlayerID(p1.getPlayerID());

        controller.update(playerCommand);

        verify(model, times(1)).winUpdate(p1);
    }

    @Test
    public void checkEndTurnFailedTest() {
        int playersNum = 3;
        Match match = new Match(playersNum);
        Model model = Mockito.spy(new Model(match));

        Player p1 = new Player("Andrea", model, match);
        Player p2 = new Player("Cosimo", model, match);
        Player p3 = new Player("Roberto", model, match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Controller controller = new Controller(model);

        p1.setNickname("nick1");
        p1.setColor(PrintableColor.RED);

        p1.setNickname("nick2");
        p1.setColor(PrintableColor.GREEN);

        p1.setNickname("nick3");
        p1.setColor(PrintableColor.BLUE);

        p1.setGodStrategy(GodStrategy.instantiateGod("athena"));
        p2.setGodStrategy(GodStrategy.instantiateGod("atlas"));
        p3.setGodStrategy(GodStrategy.instantiateGod("poseidon"));

        p1.getWorkerFirst().setInitialPosition(3, 3);
        p1.getWorkerSecond().setInitialPosition(3, 4);

        p2.getWorkerFirst().setInitialPosition(0, 0);
        p2.getWorkerSecond().setInitialPosition(0, 1);

        p3.getWorkerFirst().setInitialPosition(3, 2);
        p3.getWorkerSecond().setInitialPosition(2, 4);

        controller.initialPhase();
        model.nextGamePhase();
        model.nextGamePhase();
        model.nextGamePhase();

        model.setInitialTurn(0);

        PlayerCommand playerCommand = PlayerCommand.parseInput("move w2 e5");
        playerCommand.setPlayerID(p1.getPlayerID());

        controller.update(playerCommand);

        playerCommand = PlayerCommand.parseInput("end");
        playerCommand.setPlayerID(p1.getPlayerID());

        controller.update(playerCommand);

        verify(model, times(1)).reportError(p1, CommandType.END_TURN, ErrorType.DENIED_BY_PLAYER_GOD, null);
    }

    @Test
    public void loseConditionMoveTest() {
        int playersNum = 3;
        Match match = new Match(playersNum);
        Model model = Mockito.spy(new Model(match));

        Player p1 = new Player("Andrea", model, match);
        Player p2 = new Player("Cosimo", model, match);
        Player p3 = new Player("Roberto", model, match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Controller controller = new Controller(model);

        p1.setNickname("nick1");
        p1.setColor(PrintableColor.RED);

        p1.setNickname("nick2");
        p1.setColor(PrintableColor.GREEN);

        p1.setNickname("nick3");
        p1.setColor(PrintableColor.BLUE);

        p1.setGodStrategy(GodStrategy.instantiateGod("athena"));
        p2.setGodStrategy(GodStrategy.instantiateGod("atlas"));
        p3.setGodStrategy(GodStrategy.instantiateGod("poseidon"));

        p1.getWorkerFirst().setInitialPosition(0, 0);
        p1.getWorkerSecond().setInitialPosition(0, 1);

        p2.getWorkerFirst().setInitialPosition(1, 0);
        p2.getWorkerSecond().setInitialPosition(1, 1);

        p3.getWorkerFirst().setInitialPosition(0, 3);
        p3.getWorkerSecond().setInitialPosition(1, 2);

        controller.initialPhase();
        model.nextGamePhase();
        model.nextGamePhase();
        model.nextGamePhase();

        model.setInitialTurn(2);

        PlayerCommand playerCommand = PlayerCommand.parseInput("move w1 a3");
        playerCommand.setPlayerID(p3.getPlayerID());

        controller.update(playerCommand);

        playerCommand = PlayerCommand.parseInput("build w1 a4");
        playerCommand.setPlayerID(p3.getPlayerID());

        controller.update(playerCommand);

        playerCommand = PlayerCommand.parseInput("end");
        playerCommand.setPlayerID(p3.getPlayerID());

        controller.update(playerCommand);

        verify(model, times(1)).onPlayerLose(p1, LoseUpdate.LoseCause.CANT_MOVE);
    }

    @Test
    public void loseConditionBuildTest() {
        int playersNum = 3;
        Match match = new Match(playersNum);
        Model model = Mockito.spy(new Model(match));

        Player p1 = new Player("Andrea", model, match);
        Player p2 = new Player("Cosimo", model, match);
        Player p3 = new Player("Roberto", model, match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Controller controller = new Controller(model);

        p1.setNickname("nick1");
        p1.setColor(PrintableColor.RED);

        p1.setNickname("nick2");
        p1.setColor(PrintableColor.GREEN);

        p1.setNickname("nick3");
        p1.setColor(PrintableColor.BLUE);

        p1.setGodStrategy(GodStrategy.instantiateGod("athena"));
        p2.setGodStrategy(GodStrategy.instantiateGod("apollo"));
        p3.setGodStrategy(GodStrategy.instantiateGod("poseidon"));

        p1.getWorkerFirst().setInitialPosition(0, 0);
        p1.getWorkerSecond().setInitialPosition(0, 1);

        p2.getWorkerFirst().setInitialPosition(1, 0);
        p2.getWorkerSecond().setInitialPosition(1, 1);

        p3.getWorkerFirst().setInitialPosition(0, 3);
        p3.getWorkerSecond().setInitialPosition(1, 3);

        controller.initialPhase();
        model.nextGamePhase();
        model.nextGamePhase();
        model.nextGamePhase();

        model.setInitialTurn(1);

        PlayerCommand playerCommand = PlayerCommand.parseInput("move w1 a1");
        playerCommand.setPlayerID(p2.getPlayerID());

        controller.update(playerCommand);

        verify(model, times(1)).onPlayerLose(p2, LoseUpdate.LoseCause.CANT_BUILD);
    }

    @Test
    public void onPlayerDisconnectedTest() {
        int playersNum = 3;
        Match match = new Match(playersNum);
        Model model = Mockito.spy(new Model(match));

        Player p1 = new Player("Andrea", model, match);
        Player p2 = new Player("Cosimo", model, match);
        Player p3 = new Player("Roberto", model, match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        Controller controller = new Controller(model);

        p1.getWorkerFirst().setInitialPosition(0, 0);
        p1.getWorkerSecond().setInitialPosition(3, 3);

        controller.onPlayerDisconnected("Andrea");

        assertEquals(2, model.getPlayers().size());
        assertNull(model.getBoard().getCell(0, 0).getWorker());
        assertNull(model.getBoard().getCell(3, 3).getWorker());
        verify(model, times(1)).disconnectedPlayerUpdate(p1);
        verify(model, times(1)).gamePhaseUpdate(GamePhase.MATCH_ENDED);
    }

    @Test
    public void impossibleExceptionsTest() {


        int playersNum = 2;
        Match match = new Match(playersNum);
        Player p1 = new Player("Andrea", new Model(match), match);
        Player p2 = new Player("Cosimo", new Model(match), match);
        match.addPlayer(p1);
        match.addPlayer(p2);

        Model model = Mockito.spy(new Model(match));
        Controller controller = new Controller(model);
        controller.initialPhase();

        match.setInitialTurn(0);

        String nick1 = "pippo";
        PrintableColor colour = PrintableColor.YELLOW;
        InitialInfoCommand initialInfoCommand = new InitialInfoCommand(nick1, colour);
        initialInfoCommand.setPlayer(model.getCurrentPlayer());

        controller.update(initialInfoCommand);

        model.nextGamePhase();
        match.setInitialTurn(0);

        String wrongID = "pippo_WRONG";
        GodChoiceCommand godChoiceCommand = new GodChoiceCommand(Arrays.asList("athena", "atlas"));
        godChoiceCommand.setPlayerID(wrongID);

        controller.update(godChoiceCommand);

        verify(model, times(1)).reportError(null, CommandType.SELECT, ErrorType.GENERIC_ERROR, null);


        model.nextGamePhase();
        match.setInitialTurn(0);

        GamePreparationCommand gamePreparationCommand = new GamePreparationCommand(-1, 100, 10000, 4);
        gamePreparationCommand.setPlayerID(model.getPlayers().get(0).getPlayerID());

        controller.update(gamePreparationCommand);

        verify(model, times(1)).reportError(p1, CommandType.PLACE, ErrorType.INVALID_CELL, null);

    }
}