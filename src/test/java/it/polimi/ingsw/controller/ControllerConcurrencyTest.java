package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.GamePreparationCommand;
import it.polimi.ingsw.controller.commands.GodChoiceCommand;
import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gods.Apollo;
import it.polimi.ingsw.model.gods.Athena;
import it.polimi.ingsw.model.gods.Atlas;
import it.polimi.ingsw.model.utils.GodsUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ControllerConcurrencyTest {

    @Disabled
    @Test
    public void concurrencyStressTest() {

        // Initial Info Concurrency Test

        System.out.println("TESTING INITIAL INFO CONCURRENCY ...");
        System.out.println();

        for(int i = 0; i < 500; i++) {
            Match match = new Match(3);
            Model model = new Model(match);
            Controller controller = new Controller(model);

            Player p1 = new Player(UUID.randomUUID().toString(), model, match);
            Player p2 = new Player(UUID.randomUUID().toString(), model, match);
            Player p3 = new Player(UUID.randomUUID().toString(), model, match);

            match.addPlayer(p1);
            match.addPlayer(p2);
            match.addPlayer(p3);

            PrintableColor color1 = PrintableColor.RED;
            PrintableColor color2 = PrintableColor.BLUE;
            PrintableColor color3 = PrintableColor.GREEN;

            String nickname1 = UUID.randomUUID().toString();
            String nickname2 = UUID.randomUUID().toString();
            String nickname3 = UUID.randomUUID().toString();


            controller.initialPhase();

            Thread[] threads = new Thread[150];
            int k = 0;
            for (int j = 0; j < 50; j++) {

                threads[k] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        InitialInfoCommand initialInfoCommand = new InitialInfoCommand(nickname1, color1);
                        initialInfoCommand.setPlayer(p1);
                        controller.update(initialInfoCommand);

                    }
                });

                threads[k].start();

                k++;

                threads[k] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        InitialInfoCommand initialInfoCommand = new InitialInfoCommand(nickname2, color2);
                        initialInfoCommand.setPlayer(p2);
                        controller.update(initialInfoCommand);
                    }
                });

                threads[k].start();

                k++;

                threads[k] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        InitialInfoCommand initialInfoCommand = new InitialInfoCommand(nickname3, color3);
                        initialInfoCommand.setPlayer(p3);
                        controller.update(initialInfoCommand);
                    }
                });

                threads[k].start();

                k++;

            }

            for (int z = 0; z < k; z++) {
                try {
                    threads[z].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(i + "th MATCH");

            try {
                // if assert fails -> synchronization failed in Controller's handleInitialInfoCommand (at any moment model.getCurrentPlayer() != command.getPlayer())
                assertTrue(model.getPlayers().get(0).getNickname().equals(nickname1) &&
                        model.getPlayers().get(0).getColor().equals(color1) &&
                        model.getPlayers().get(1).getNickname().equals(nickname2) &&
                        model.getPlayers().get(1).getColor().equals(color2) &&
                        model.getPlayers().get(2).getNickname().equals(nickname3) &&
                        model.getPlayers().get(2).getColor().equals(color3));
            } catch(NullPointerException e) {
                System.out.println();
                System.out.println("NULL POINTER EXCEPTION CAUGHT: Maybe all threads from a Player who's not the God Chooser where executed before the God Chooser?");
                System.out.println();
            }


        }

        // God Choice Concurrency Test
        System.out.println();
        System.out.println("TESTING GOD CHOICE CONCURRENCY ...");
        System.out.println();

        for(int i = 0; i < 500; i++) {
            Match match = new Match(3);
            Model model = new Model(match);
            Controller controller = new Controller(model);

            Player p1 = new Player(UUID.randomUUID().toString(), model, match);
            Player p2 = new Player(UUID.randomUUID().toString(), model, match);
            Player p3 = new Player(UUID.randomUUID().toString(), model, match);

            PrintableColor color1 = PrintableColor.RED;
            PrintableColor color2 = PrintableColor.BLUE;
            PrintableColor color3 = PrintableColor.GREEN;

            String nickname1 = UUID.randomUUID().toString();
            String nickname2 = UUID.randomUUID().toString();
            String nickname3 = UUID.randomUUID().toString();

            p1.setNickname(nickname1);
            p2.setNickname(nickname2);
            p3.setNickname(nickname3);

            p1.setColor(color1);
            p2.setColor(color2);
            p3.setColor(color3);

            match.addPlayer(p1);
            match.addPlayer(p2);
            match.addPlayer(p3);

            controller.initialPhase();
            model.nextGamePhase();

            Player godChooser = model.getCurrentPlayer();

            Thread [] threads = new Thread[150];
            int k = 0;
            for(int j = 0; j < 50; j++) {

                threads[k] = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if(p1.equals(godChooser)) {
                            List<String> gods = new ArrayList<>();
                            gods.add("athena");
                            gods.add("apollo");
                            gods.add("atlas");
                            GodChoiceCommand godChoiceCommand = new GodChoiceCommand(gods);
                            godChoiceCommand.setPlayer(p1);
                            controller.update(godChoiceCommand);
                        } else {
                            List<String> gods = new ArrayList<>();
                            gods.add("athena");
                            GodChoiceCommand godChoiceCommand = new GodChoiceCommand(gods);
                            godChoiceCommand.setPlayer(p1);
                            controller.update(godChoiceCommand);
                        }
                    }
                });

                threads[k].start();

                k++;

                threads[k] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(p2.equals(godChooser)) {
                            List<String> gods = new ArrayList<>();
                            gods.add("athena");
                            gods.add("apollo");
                            gods.add("atlas");
                            GodChoiceCommand godChoiceCommand = new GodChoiceCommand(gods);
                            godChoiceCommand.setPlayer(p2);
                            controller.update(godChoiceCommand);
                        } else {
                            List<String> gods = new ArrayList<>();
                            gods.add("apollo");
                            GodChoiceCommand godChoiceCommand = new GodChoiceCommand(gods);
                            godChoiceCommand.setPlayer(p2);
                            controller.update(godChoiceCommand);
                        }
                    }
                });

                threads[k].start();

                k++;

                threads[k] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(p3.equals(godChooser)) {
                            List<String> gods = new ArrayList<>();
                            gods.add("athena");
                            gods.add("apollo");
                            gods.add("atlas");
                            GodChoiceCommand godChoiceCommand = new GodChoiceCommand(gods);
                            godChoiceCommand.setPlayer(p3);
                            controller.update(godChoiceCommand);
                        } else {
                            List<String> gods = new ArrayList<>();
                            gods.add("atlas");
                            GodChoiceCommand godChoiceCommand = new GodChoiceCommand(gods);
                            godChoiceCommand.setPlayer(p3);
                            controller.update(godChoiceCommand);
                        }
                    }
                });

                threads[k].start();

                k++;

            }

            for(int z = 0; z < k; z++) {
                try {
                    threads[z].join();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(i+ "th MATCH");

            try {
                assertTrue(model.getPlayers().get(0).getGodStrategy().equals(new Athena()) &&
                        model.getPlayers().get(1).getGodStrategy().equals(new Apollo()) &&
                        model.getPlayers().get(2).getGodStrategy().equals(new Atlas())
                );


            } catch(NullPointerException e) {
                System.out.println();
                System.out.println("NULL POINTER EXCEPTION CAUGHT: Maybe all threads from a Player who's not the model.getCurrentPlayer() where executed before model.getCurrentPlayer()?");
                System.out.println();
            }

        }

        // Game Prep Concurrency Test

        System.out.println();
        System.out.println("TESTING GAME PREPARATION CONCURRENCY ...");
        System.out.println();

        for(int i = 0; i < 500; i++) {
            Match match = new Match(3);
            Model model = new Model(match);
            Controller controller = new Controller(model);

            Player p1 = new Player(UUID.randomUUID().toString(), model, match);
            Player p2 = new Player(UUID.randomUUID().toString(), model, match);
            Player p3 = new Player(UUID.randomUUID().toString(), model, match);

            PrintableColor color1 = PrintableColor.RED;
            PrintableColor color2 = PrintableColor.BLUE;
            PrintableColor color3 = PrintableColor.GREEN;

            String nickname1 = UUID.randomUUID().toString();
            String nickname2 = UUID.randomUUID().toString();
            String nickname3 = UUID.randomUUID().toString();

            p1.setNickname(nickname1);
            p2.setNickname(nickname2);
            p3.setNickname(nickname3);

            p1.setColor(color1);
            p2.setColor(color2);
            p3.setColor(color3);

            p1.setGodStrategy(GodsUtils.godsFactory("athena"));
            p2.setGodStrategy(GodsUtils.godsFactory("apollo"));
            p3.setGodStrategy(GodsUtils.godsFactory("atlas"));

            match.addPlayer(p1);
            match.addPlayer(p2);
            match.addPlayer(p3);

            controller.initialPhase(); // Initial Info
            model.nextGamePhase(); // God Choice
            model.nextGamePhase(); // Game Prep

            model.endTurn();


            Thread [] threads = new Thread[150];
            int k = 0;
            for(int j = 0; j < 50; j++) {

                threads[k] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GamePreparationCommand gamePreparationCommand = GamePreparationCommand.parseInput("place w1 a1 w2 a4");
                        gamePreparationCommand.setPlayer(p1);
                        controller.update(gamePreparationCommand);
                    }
                });

                threads[k].start();

                k++;

                threads[k] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GamePreparationCommand gamePreparationCommand = GamePreparationCommand.parseInput("place w1 b4 w2 c3");
                        gamePreparationCommand.setPlayer(p2);
                        controller.update(gamePreparationCommand);
                    }
                });

                threads[k].start();

                k++;

                threads[k] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GamePreparationCommand gamePreparationCommand = GamePreparationCommand.parseInput("place w1 d4 w2 e5");
                        gamePreparationCommand.setPlayer(p3);
                        controller.update(gamePreparationCommand);
                    }
                });

                threads[k].start();

                k++;

            }

            for(int z = 0; z < k; z++) {
                try {
                    threads[z].join();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }

        /*    model.endTurn();
           if(model.getCurrentPlayer().equals(p2)) {
               GamePreparationCommand gamePreparationCommand2 = GamePreparationCommand.parseInput("place w1 b4 w2 c3");
               gamePreparationCommand2.setPlayer(p2);
               controller.update(gamePreparationCommand2);

               GamePreparationCommand gamePreparationCommand3 = GamePreparationCommand.parseInput("place w1 d4 w2 e5");
               gamePreparationCommand3.setPlayer(p3);
               controller.update(gamePreparationCommand3);

               GamePreparationCommand gamePreparationCommand = GamePreparationCommand.parseInput("place w1 a1 w2 a4");
               gamePreparationCommand.setPlayer(p1);
               controller.update(gamePreparationCommand);

           } else if(model.getCurrentPlayer().equals(p3)) {
               GamePreparationCommand gamePreparationCommand3 = GamePreparationCommand.parseInput("place w1 d4 w2 e5");
               gamePreparationCommand3.setPlayer(p3);
               controller.update(gamePreparationCommand3);

               GamePreparationCommand gamePreparationCommand = GamePreparationCommand.parseInput("place w1 a1 w2 a4");
               gamePreparationCommand.setPlayer(p1);
               controller.update(gamePreparationCommand);

               GamePreparationCommand gamePreparationCommand2 = GamePreparationCommand.parseInput("place w1 b4 w2 c3");
               gamePreparationCommand2.setPlayer(p2);
               controller.update(gamePreparationCommand2);
           } else {
               GamePreparationCommand gamePreparationCommand = GamePreparationCommand.parseInput("place w1 a1 w2 a4");
               gamePreparationCommand.setPlayer(p1);
               controller.update(gamePreparationCommand);

               GamePreparationCommand gamePreparationCommand2 = GamePreparationCommand.parseInput("place w1 b4 w2 c3");
               gamePreparationCommand2.setPlayer(p2);
               controller.update(gamePreparationCommand2);

               GamePreparationCommand gamePreparationCommand3 = GamePreparationCommand.parseInput("place w1 d4 w2 e5");
               gamePreparationCommand3.setPlayer(p3);
               controller.update(gamePreparationCommand3);
           } */




            System.out.println(i+ "th MATCH");

            try {
                assertTrue(
                        model.getPlayers().get(0).getWorkerFirst().getPosition().equals(match.getMatchBoard().getCell(0, 0)) &&
                                model.getPlayers().get(0).getWorkerSecond().getPosition().equals(match.getMatchBoard().getCell(0, 3)) &&

                                model.getPlayers().get(1).getWorkerFirst().getPosition().equals(match.getMatchBoard().getCell(1, 3)) &&
                                model.getPlayers().get(1).getWorkerSecond().getPosition().equals(match.getMatchBoard().getCell(2, 2)) &&

                                model.getPlayers().get(2).getWorkerFirst().getPosition().equals(match.getMatchBoard().getCell(3, 3)) &&
                                model.getPlayers().get(2).getWorkerSecond().getPosition().equals(match.getMatchBoard().getCell(4, 4))

                );


            } catch(NullPointerException e) {
                //fail(); //
                System.out.println("");
                System.out.println("NULL POINTER EXCEPTION CAUGHT: Maybe all threads from a Player who's not the model.getCurrentPlayer() where executed before model.getCurrentPlayer()?");
                System.out.println("");
            }

        }

        // Real Game Concurrency Test

        System.out.println();
        System.out.println("TESTING REAL GAME CONCURRENCY ...");
        System.out.println();

        for(int i = 0; i < 500; i++) {
            Match match = new Match(3);
            Model model = new Model(match);
            Controller controller = new Controller(model);

            Player p1 = new Player(UUID.randomUUID().toString(), model, match);
            Player p2 = new Player(UUID.randomUUID().toString(), model, match);
            Player p3 = new Player(UUID.randomUUID().toString(), model, match);

            PrintableColor color1 = PrintableColor.RED;
            PrintableColor color2 = PrintableColor.BLUE;
            PrintableColor color3 = PrintableColor.GREEN;

            String nickname1 = UUID.randomUUID().toString();
            String nickname2 = UUID.randomUUID().toString();
            String nickname3 = UUID.randomUUID().toString();

            p1.setNickname(nickname1);
            p2.setNickname(nickname2);
            p3.setNickname(nickname3);

            p1.setColor(color1);
            p2.setColor(color2);
            p3.setColor(color3);

            p1.setGodStrategy(GodsUtils.godsFactory("athena"));
            p2.setGodStrategy(GodsUtils.godsFactory("apollo"));
            p3.setGodStrategy(GodsUtils.godsFactory("atlas"));

            match.addPlayer(p1);
            match.addPlayer(p2);
            match.addPlayer(p3);

            controller.initialPhase(); // Initial Info
            model.nextGamePhase(); // God Choice
            model.nextGamePhase(); // Game Prep
            model.nextGamePhase(); // Real Game

            model.endTurn();

            p1.getWorkerFirst().setInitialPosition(0, 0);
            p1.getWorkerSecond().setInitialPosition(3, 2);
            p2.getWorkerFirst().setInitialPosition(3, 1);
            p2.getWorkerSecond().setInitialPosition(4, 3);
            p3.getWorkerFirst().setInitialPosition(2, 4);
            p3.getWorkerSecond().setInitialPosition(1, 4);


            Thread [] threads = new Thread[300];
            int k = 0;
            for(int j = 0; j < 50; j++) {

                threads[k] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PlayerCommand playerCommand = PlayerCommand.parseInput("move w1 a2");
                        playerCommand.setPlayer(p1);
                        controller.update(playerCommand);

                        playerCommand = PlayerCommand.parseInput("build w1 a1");
                        playerCommand.setPlayer(p1);
                        controller.update(playerCommand);

                        playerCommand = PlayerCommand.parseInput("end");
                        playerCommand.setPlayer(p1);
                        controller.update(playerCommand);
                    }
                });

                threads[k].start();

               /* k++; All playerCommand of the turn in the same Thread ?

                threads[k] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PlayerCommand playerCommand = PlayerCommand.parseInput("build w1 a1");
                        playerCommand.setPlayer(p1);
                        controller.update(playerCommand);
                    }
                });

                threads[k].start();*/

                k++;

                threads[k] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PlayerCommand playerCommand = PlayerCommand.parseInput("move w1 d1");
                        playerCommand.setPlayer(p2);
                        controller.update(playerCommand);

                        playerCommand = PlayerCommand.parseInput("build w1 d2");
                        playerCommand.setPlayer(p2);
                        controller.update(playerCommand);

                        playerCommand = PlayerCommand.parseInput("end");
                        playerCommand.setPlayer(p2);
                        controller.update(playerCommand);
                    }
                });

                threads[k].start();

                /*k++;

                threads[k] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PlayerCommand playerCommand = PlayerCommand.parseInput("build w1 d2");
                        playerCommand.setPlayer(p2);
                        controller.update(playerCommand);
                    }
                });

                threads[k].start();*/

                k++;

                threads[k] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PlayerCommand playerCommand = PlayerCommand.parseInput("move w1 d5");
                        playerCommand.setPlayer(p3);
                        controller.update(playerCommand);

                        playerCommand = PlayerCommand.parseInput("build w1 c5 DOME");
                        playerCommand.setPlayer(p3);
                        controller.update(playerCommand);

                        playerCommand = PlayerCommand.parseInput("end");
                        playerCommand.setPlayer(p3);
                        controller.update(playerCommand);
                    }
                });

                threads[k].start();

               /* k++;

                threads[k] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PlayerCommand playerCommand = PlayerCommand.parseInput("build w1 c5 DOME");
                        playerCommand.setPlayer(p3);
                        controller.update(playerCommand);
                    }
                });

                threads[k].start(); */

                k++;

            }

            for(int z = 0; z < k; z++) {
                try {
                    threads[z].join();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }


            System.out.println(i+ "th MATCH");

            try {
                assertTrue(
                        model.getPlayers().get(0).getWorkerFirst().getPosition().equals(match.getMatchBoard().getCell(0, 1)) &&
                                model.getPlayers().get(0).getWorkerSecond().getPosition().equals(match.getMatchBoard().getCell(3, 2)) &&

                                model.getPlayers().get(1).getWorkerFirst().getPosition().equals(match.getMatchBoard().getCell(3, 0)) &&
                                model.getPlayers().get(1).getWorkerSecond().getPosition().equals(match.getMatchBoard().getCell(4, 3)) &&

                                model.getPlayers().get(2).getWorkerFirst().getPosition().equals(match.getMatchBoard().getCell(3, 4)) &&
                                model.getPlayers().get(2).getWorkerSecond().getPosition().equals(match.getMatchBoard().getCell(1, 4))

                );

                assertTrue(
                        match.getMatchBoard().getCell(0, 0).getLevel() == BlockType.LEVEL_ONE &&

                                match.getMatchBoard().getCell(3, 1).getLevel() == BlockType.LEVEL_ONE &&

                                match.getMatchBoard().getCell(2, 4).getLevel() == BlockType.DOME


                );


            } catch(NullPointerException e) {
                //fail(); //
                System.out.println("");
                System.out.println("NULL POINTER EXCEPTION CAUGHT: Maybe all threads from a Player who's not the model.getCurrentPlayer() where executed before model.getCurrentPlayer()?");
                System.out.println("");
            }

        }
    }
}
