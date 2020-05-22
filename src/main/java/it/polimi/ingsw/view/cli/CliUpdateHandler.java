package it.polimi.ingsw.view.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.ErrorType;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.view.UpdateHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CliUpdateHandler implements UpdateHandler {
    private final Cli cliInstance;
    private final it.polimi.ingsw.network.client.controller.Controller controller;

    CliUpdateHandler(Cli cliInstance, Controller controller) {
        this.cliInstance = cliInstance;
        this.controller = controller;
    }

    public void handle(MatchStartedUpdate update) {
        cliInstance.printBoard(update.getBoard());

        cliInstance.println(Cli.toBold("MATCH STARTED!"));
        cliInstance.newLine();
        cliInstance.println(Cli.toBold("Tip") + ": type 'help " + cliInstance.getCurrentPhaseString() + "' to know command format");
        cliInstance.newLine();
        cliInstance.printCurrentTurn();
    }


    public void handle(GodsUpdate update) {


        if (update.getSelectedGods().size() == cliInstance.getPlayersNum()) {
            cliInstance.setPlayersGods(update.getSelectedGods());
            cliInstance.printPlayerGods();
        } else {
            if (!controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) return;

            if (controller.isClientPlayerGodChooser()) {
                cliInstance.setInitialGodChooser(true);
                cliInstance.println(Cli.toBold("You are the God Chooser!"));
                cliInstance.newLine();
                cliInstance.println(Cli.toBold("Choose ") + cliInstance.getPlayersNum() + " gods. For a perfect match, choose the ones you like most!    Command " + Cli.toBold("format") + " expected: select [god(1)] ... [god(n)]");
                cliInstance.newLine();
                cliInstance.println("Available gods are: " + availableGods());
                cliInstance.newLine();
                cliInstance.println("REMEMBER: if you need info about a god and his/her powers, type 'info [god]'");
            } else {
                cliInstance.setInitialGodChooser(false);
                cliInstance.setSelectableGods(update.getSelectableGods());
                cliInstance.println(Cli.toBold("Choose ") + "your god.    Command " + Cli.toBold("format") + " expected: select [god]");
                if (listToStringBuilder(update.getSelectableGods()) != null) {
                    cliInstance.newLine();
                    cliInstance.println("Available choices are: " + listToStringBuilder(update.getSelectableGods()));
                }
                cliInstance.newLine();
                cliInstance.println("REMEMBER: if you need info about a god, type 'info [god]'");
            }
            cliInstance.newLine();
        }
    }

    public void handle(InitialInfoUpdate update) {

        if (update.getInitialInfo().size() == cliInstance.getPlayersNum()) {
            cliInstance.setPlayersColors(update.getInitialInfo());
            cliInstance.printPlayersColors();
        } else {
            if (!controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) return;

            cliInstance.println("Type your " + Cli.toBold("nickname and color") + " separated by a space.    Command " + Cli.toBold("format") + " expected: pick [nickname] [color]");

            List<String> selectedNicknames = new ArrayList<>(update.getInitialInfo().keySet());
            List<PrintableColor> selectedColors = new ArrayList<>(update.getInitialInfo().values());
            List<PrintableColor> selectableColors = PrintableColor.getColorList().stream().filter(color -> !selectedColors.contains(color)).collect(Collectors.toList());


            if (!selectedNicknames.isEmpty()) {
                cliInstance.newLine();
                cliInstance.println("Nicknames already taken are: " + listToStringBuilder(selectedNicknames));
            }

            cliInstance.newLine();

            cliInstance.setSelectedNicknames(selectedNicknames);
            cliInstance.println("Available colors are: ");

            selectableColors.forEach((color) -> {
                cliInstance.println(Cli.convertColorToAnsi(color) + color + PrintableColor.RESET);
            });


            cliInstance.setSelectableColors(selectableColors);
            cliInstance.newLine();
        }


    }

    public void handle(BoardUpdate update) {

        if (cliInstance.getCurrentPhase().equals(GamePhase.MATCH_LOST) && !cliInstance.wantsToContinueToWatch()) return;

        if ((update.getExecutedCommand() == null || update.getExecutedCommand().commandType != CommandType.END_TURN)) {
            cliInstance.printBoard(update.getBoard());
        }

        if (update.getExecutedCommand() != null && (update.getExecutedCommand().commandType == CommandType.BUILD || update.getExecutedCommand().commandType == CommandType.MOVE)) {
            PlayerCommand executedCommand = update.getExecutedCommand();


            cliInstance.newLine();

            String nickname = executedCommand.getPlayerNickname() != null ? executedCommand.getPlayerNickname() : "Player";
            String rowChar = "";

            switch (executedCommand.row) {
                case 0:
                    rowChar = "A";
                    break;
                case 1:
                    rowChar = "B";
                    break;
                case 2:
                    rowChar = "C";
                    break;
                case 3:
                    rowChar = "D";
                    break;
                case 4:
                    rowChar = "E";
                    break;
            }

            if (executedCommand.commandType == CommandType.MOVE) {
                cliInstance.println(cliInstance.playerWithColor(nickname) + " moved " + executedCommand.workerID.toUpperCase() + " to " + rowChar + (executedCommand.col + 1));
            } else {
                cliInstance.println(cliInstance.playerWithColor(nickname) + " built with " + executedCommand.workerID.toUpperCase() + " in " + rowChar + (executedCommand.col + 1));
            }

            cliInstance.newLine();

        }


        cliInstance.printCurrentTurn();
    }

    public void handle(ErrorUpdate update) {

        if (!update.getCurrentPlayer().getPlayerID().equals(controller.getClientPlayerID())) return;

        

        switch (update.command) {
            case MOVE:

                if (update.errorType == ErrorType.DENIED_BY_OPPONENT_GOD) {

                    String inhibitorGod = update.getInhibitorGod().get(GodsUtils.GOD_NAME);

                    cliInstance.println(Cli.toBold("Move Error") + ": you can't perform this move because " + inhibitorGod + " doesn't let you move in the position you specified!"); // Your god -> specific god?
                }
                else if(update.errorType == ErrorType.DENIED_BY_PLAYER_GOD) {
                    cliInstance.println(Cli.toBold("Move Error") + ": you can't perform this move because your God doesn't let you move in the position you specified!"); // Your god -> specific god?
                }
                else if(update.errorType == ErrorType.WRONG_TURN) {
                    cliInstance.println(Cli.toBold("Move Error") + ": you can't perform this move because it's not your turn!"); // Your god -> specific god?
                }
                else if(update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    cliInstance.println(Cli.toBold("Wrong Game Phase") + ": current Game Phase is not Real Game Phase");
                }

                break;

            case BUILD:

                if (update.errorType == ErrorType.DENIED_BY_OPPONENT_GOD) {
                    String inhibitorGod = update.getInhibitorGod().get(GodsUtils.GOD_NAME);
                    cliInstance.println(Cli.toBold("Build Error") + ": you can't perform this build because " + inhibitorGod + " doesn't let you build in the position you specified!"); // Your god -> specific god?
                }
                else if(update.errorType == ErrorType.DENIED_BY_PLAYER_GOD) {
                    cliInstance.println(Cli.toBold("Build Error") + ": you can't perform this build because your God doesn't let you build in the position you specified!"); // Your god -> specific god?
                }
                else if(update.errorType == ErrorType.WRONG_TURN) {
                    cliInstance.println(Cli.toBold("Build Error") + ": you can't perform this build because it's not your turn!"); // Your god -> specific god?
                }
                else if(update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    cliInstance.println(Cli.toBold("Wrong Game Phase") + ": current Game Phase is not Real Game Phase");
                }

                break;

            case END_TURN:
                if (update.errorType == ErrorType.DENIED_BY_OPPONENT_GOD) {
                    String inhibitorGod = update.getInhibitorGod().get(GodsUtils.GOD_NAME);
                    cliInstance.println(Cli.toBold("End Turn Error") + ": " +  inhibitorGod + " doesn't let you end your turn now!"); // Your god -> specific god?
                }
                else if(update.errorType == ErrorType.DENIED_BY_PLAYER_GOD) {
                    cliInstance.println(Cli.toBold("End Turn Error") + ": you can't end your turn now: maybe you must move or build!"); // Your god -> specific god?
                }
                else if(update.errorType == ErrorType.WRONG_TURN) {
                    cliInstance.println(Cli.toBold("End Turn Error") + ": you can't end your turn because it's not your turn!"); // Your god -> specific god?
                }
                else if(update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    cliInstance.println(Cli.toBold("Wrong Game Phase") + ": current Game Phase is not Real Game Phase");
                }

                break;

            case PICK:

                if(update.errorType == ErrorType.ALREADY_TAKEN_NICKNAME) {
                    cliInstance.println(Cli.toBold("Nickname Error") + ": already taken nickname");
                }
                else if(update.errorType == ErrorType.INVALID_COLOR) {
                    cliInstance.println(Cli.toBold("Color Error") + ": invalid or already taken color");
                }
                else if(update.errorType == ErrorType.WRONG_TURN) {
                    cliInstance.println(Cli.toBold("Turn Error") + ": Not your turn!");
                }
                else if(update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    cliInstance.println(Cli.toBold("Wrong Game Phase") + ": current Game Phase is not Initial Info Phase");
                }

                break;

            case PLACE:
                if (update.errorType == ErrorType.DENIED_BY_OPPONENT_GOD) {
                    String inhibitorGod = update.getInhibitorGod().get(GodsUtils.GOD_NAME);
                    cliInstance.println(Cli.toBold("Game Preparation Error") + ": you can't place your Worker where you specified because " + inhibitorGod + " doesn't allow it"); // Your god -> specific god?
                }
                else if(update.errorType == ErrorType.DENIED_BY_PLAYER_GOD) {
                    cliInstance.println(Cli.toBold("Game Preparation Error") + ": you can't place your Workers where you specified because your God doesn't allow it"); // Your god -> specific god?
                }
                else if(update.errorType == ErrorType.WRONG_TURN) {
                    cliInstance.println(Cli.toBold("Game Preparation Error") + ": you can't place your Workers because it's not your turn!"); // Your god -> specific god?
                }
                else if(update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    cliInstance.println(Cli.toBold("Wrong Game Phase") + ": current Game Phase is not Game Preparation Phase");
                }
                break;

            case SELECT:

                if(update.errorType == ErrorType.WRONG_TURN) {
                    cliInstance.println(Cli.toBold("God Choice Error") + ": you can't choose your God because it's not your turn!"); // Your god -> specific god?
                }
                else if(update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    cliInstance.println(Cli.toBold("Wrong Game Phase") + ": current Game Phase is not Gods Choice Phase");
                }
                break;
        }
        cliInstance.println("For more information about commands or rules, type 'help " + cliInstance.getCurrentPhaseString() + "' or 'rules'");
        cliInstance.newLine();
    }

    public void handle(GamePhaseUpdate update) {
        cliInstance.newLine();
        if (update.newGamePhase.isPrintable()) {
            switch (update.newGamePhase) {
                case INITIAL_INFO:
                    cliInstance.println(Cli.toBold("INITIAL INFORMATION PHASE"));
                    break;
                case CHOOSE_GODS:
                    cliInstance.println(Cli.toBold("GODS CHOOSE PHASE"));
                    break;
                case GAME_PREPARATION:
                    cliInstance.println(Cli.toBold("GAME PREPARATION PHASE"));
                    break;
                case REAL_GAME:
                    cliInstance.println(Cli.toBold("GAME STARTED PHASE"));
                    break;
            }
        }
        cliInstance.setCurrentGamePhase(update.newGamePhase);
    }


    public void handle(TurnUpdate update) {
        cliInstance.forwardNotify(update);

        if (controller.getCurrentPlayerID().equals(controller.getClientPlayerID()) && cliInstance.getCurrentPhase() == GamePhase.GAME_PREPARATION) {
            cliInstance.printGamePreparationInfo();
        }
    }

    public void handle(WinUpdate update) {
        cliInstance.newLine();
        cliInstance.println(cliInstance.playerWithColor(update.getWinnerPlayer().getNickname()) + " wins!");

        cliInstance.newLine();
    }

    public void handle(LoseUpdate update) {

        boolean hasClientLost = controller.getClientPlayerID().equals(update.getLoserPlayer().getPlayerID());

        String subject = hasClientLost ? "you" : "it"; // update.getLoserPlayer().getNickname()
        String loseCauseMsg = " because " + subject + " can't " + (update.getLoseCause() == LoseUpdate.LoseCause.CANT_MOVE ? "move" : "build") +
        " with any Worker";

        /*switch(update.getLoseCause()) {
            case CANT_MOVE:
                loseCauseMsg = " because " + subject + " can't move with any Worker";
                break;
            case CANT_BUILD:
                loseCauseMsg = " because " + subject + " can't build with any Worker";
                break;
        }*/

        if (controller.getClientPlayerID().equals(update.getLoserPlayer().getPlayerID())) {
            cliInstance.newLine();
            cliInstance.println(Cli.toBold("You lost" + loseCauseMsg + "!"));
            cliInstance.newLine();
            cliInstance.setCurrentGamePhase(GamePhase.MATCH_LOST);

            if (update.onePlayerRemaining) {
                cliInstance.setCurrentGamePhase(GamePhase.MATCH_ENDED); // todo method to avoid duplicate
            } else {
                cliInstance.println("Do you want to continue to watch this match?");
            }
        } else if (update.onePlayerRemaining) {
            cliInstance.newLine();
            cliInstance.println(cliInstance.playerWithColor(update.getLoserPlayer().getNickname()) + " lost" + loseCauseMsg + "!");
            cliInstance.println(Cli.toBold("You Win!"));
            cliInstance.newLine();
            cliInstance.setCurrentGamePhase(GamePhase.MATCH_ENDED);
        } else {
            cliInstance.newLine();

            cliInstance.println(cliInstance.playerWithColor(update.getLoserPlayer().getNickname()) + " lost" + loseCauseMsg + "!");
        }


    }

    public void handle(ServerUnreachableUpdate update) {
        cliInstance.println("Cannot communicate to the Server, maybe it's down. Otherwise, check your connection.");
        cliInstance.println("Quitting..."); // todo method quit()
        System.exit(0);
    }

    public void handle(DisconnectedPlayerUpdate update) {
        String nicknameToShow = update.getDisconnectedPlayer().getNickname() != null ? update.getDisconnectedPlayer().getNickname() : "A player";
        cliInstance.println(nicknameToShow + " disconnected!");
    }


    private StringBuilder listToStringBuilder(List<String> value) {
        if (value.size() == 0) {
            return null;
        }
        StringBuilder result = new StringBuilder(value.get(0));
        for (int i = 1; i < value.size(); i++) {
            result.append(", ").append(value.get(i));
        }
        return result;
    }

    private StringBuilder availableGods() {
        ArrayList<String> godNames = new ArrayList<>(GodsUtils.getGodsInfo().keySet());  //list of gods' names
        StringBuilder result = new StringBuilder(godNames.get(0).toUpperCase());
        for (int i = 1; i < godNames.size(); i++) {
            result.append(", ").append(godNames.get(i).toUpperCase());
        }
        return result;
    }

}

