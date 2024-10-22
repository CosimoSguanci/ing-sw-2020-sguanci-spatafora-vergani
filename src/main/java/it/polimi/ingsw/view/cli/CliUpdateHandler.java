package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.model.ErrorType;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.view.UpdateHandler;
import it.polimi.ingsw.view.View;

import java.util.List;

/**
 * This class handles all the updates from server to different clients.
 * Every possible updates go to this class before it's viewable from users (in the CLI).
 * BoardUpdate, DisconnectedPlayerUpdate, ErrorUpdate, GamePhaseUpdate,
 * GodsUpdate, InitialInfoUpdate, LoseUpdate, MatchStartedUpdate,
 * ServerUnreachableUpdate, TurnUpdate, WinUpdate are all managed in this class.
 * <p>
 * This class represent the Visitor in the Visitor Pattern, it implements {@link UpdateHandler}
 * to have all the necessary methods to handle all possible updates.
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Vergani
 * @see UpdateHandler
 */
public class CliUpdateHandler extends UpdateHandler {

    /**
     * Instance of the {@link Cli} which this handler has to update
     */
    private final Cli cliInstance;

    /**
     * This is the creator of the class. At the moment of the instance creation
     * an instance of {@link Cli} and {@link Controller}, to which the class refers, are set.
     *
     * @param cliInstance is the instance of {@link Cli} associated to the CliUpdateHandler for updates.
     * @param controller  is the instance of the controller that is observed by the {@link Cli}.
     */
    CliUpdateHandler(Cli cliInstance, Controller controller) {
        super(controller);
        this.cliInstance = cliInstance;
    }

    /**
     * This method handles {@link MatchStartedUpdate} arrived from the {@link Cli}.
     * It prints several lines in console, to let players know that match has started.
     *
     * @param update is the instance of {@link MatchStartedUpdate} from {@link Cli}.
     */
    @Override
    public void handle(MatchStartedUpdate update) {
        cliInstance.printBoard(update.getBoard());
        cliInstance.println(Cli.toBold("MATCH STARTED!"));
        cliInstance.newLine();
        cliInstance.println(Cli.toBold("Tip") + ": type 'help " + cliInstance.getCurrentPhaseString() + "' to know command format");
        cliInstance.newLine();
        cliInstance.printCurrentTurn();
    }

    /**
     * This method handles {@link GodsUpdate} arrived from the {@link Cli}.
     * In this method there is a distinction between GodChooser and other players.
     * If the player in Cli associated is GodChooser than some lines explains him what to do,
     * In non-GodChooser case players are invited to select their Gods.
     *
     * @param update is the instance of {@link GodsUpdate} from {@link Cli}.
     */
    @Override
    public void handle(GodsUpdate update) {
        if (update.getSelectedGods().size() == cliInstance.getPlayersNumber()) {
            cliInstance.setPlayersGods(update.getSelectedGods());
            cliInstance.printPlayerGods();
        } else {
            if (!controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) return;

            if (controller.isClientPlayerGodChooser()) {
                cliInstance.setInitialGodChooser(true);
                cliInstance.println(Cli.toBold("You are the God Chooser!"));
                cliInstance.newLine();
                cliInstance.println(Cli.toBold("Choose ") + cliInstance.getPlayersNumber() + " gods. For a perfect match, choose the ones you like most!    Command " + Cli.toBold("format") + " expected: select [god(1)] ... [god(n)]");
                cliInstance.newLine();
                cliInstance.println("Available gods are: " + availableGods());
                cliInstance.newLine();
                cliInstance.println("REMEMBER: if you need info about a god and his/her powers, type 'info [god]'");
            } else {
                cliInstance.setInitialGodChooser(false);
                cliInstance.setSelectableGods(update.getSelectableGods());
                cliInstance.println(Cli.toBold("Choose ") + "your god.    Command " + Cli.toBold("format") + " expected: select [god]");
                if (View.listToStringBuilder(update.getSelectableGods()) != null) {
                    cliInstance.newLine();
                    cliInstance.println("Available choices are: " + View.listToStringBuilder(update.getSelectableGods()));
                }
                cliInstance.newLine();
                cliInstance.println("REMEMBER: if you need info about a god, type 'info [god]'");
            }
            cliInstance.newLine();
        }
    }

    /**
     * This method handles {@link InitialInfoUpdate} arrived from the {@link Cli}.
     * Once players choose how many players are involved in their match, they have to choose
     * a nickname and a color to play. This class notifies them about that, inviting them
     * to choose a nickname and a color, not already chosen from other players.
     *
     * @param update is the instance of {@link InitialInfoUpdate} from {@link Cli}.
     */
    @Override
    public void handle(InitialInfoUpdate update) {

        if (update.getInitialInfo().size() == cliInstance.getPlayersNumber()) {
            cliInstance.setPlayersColors(update.getInitialInfo());
            cliInstance.printPlayersColors();
        } else {
            if (!controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) return;

            cliInstance.println("Type your " + Cli.toBold("nickname and color") + " separated by a space.    Command " + Cli.toBold("format") + " expected: pick [nickname] [color]");

            List<String> selectedNicknames = extractSelectedNicknames(update);
            List<PrintableColor> selectedColors = extractSelectedColors(update);
            List<PrintableColor> selectableColors = extractSelectableColors(selectedColors);


            if (!selectedNicknames.isEmpty()) {
                cliInstance.newLine();
                cliInstance.println("Nicknames already taken are: " + View.listToStringBuilder(selectedNicknames));
            }

            cliInstance.newLine();

            cliInstance.setSelectedNicknames(selectedNicknames);
            cliInstance.println("Available colors are: ");

            selectableColors.forEach((color) -> cliInstance.println(Cli.convertColorToAnsi(color) + color + PrintableColor.RESET));


            cliInstance.setSelectableColors(selectableColors);
            cliInstance.newLine();
        }
    }

    /**
     * This method handles {@link BoardUpdate} arrived from the {@link Cli}.
     * This method manages commands during REAL_GAME phase. In cases in which players
     * move or build in a specific cell, to let all players clearly know about that,
     * it is printed that player has [moved/built] with a worker in a cell.
     *
     * @param update is the instance of {@link BoardUpdate} from {@link Cli}.
     */
    @Override
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

    /**
     * This method handles {@link ErrorUpdate}  arrived from the {@link Cli}.
     * This method manages all the wrong command generated. Once the command is parsed
     * for the correct-format it is notified to the Controller, after some checks server-side
     * if an ErrorUpdate is notified it means that the command was correct in syntax but
     * an impossible action was requested.
     *
     * @param update the instance of {@link ErrorUpdate} given from {@link Cli}.
     */
    @Override
    public void handle(ErrorUpdate update) {

        if (!update.getCurrentPlayer().getPlayerID().equals(controller.getClientPlayerID())) return;

        switch (update.command) {
            case MOVE:

                if (update.errorType == ErrorType.DENIED_BY_OPPONENT_GOD) {

                    String inhibitorGod = update.getInhibitorGod().get(GodsUtils.GOD_NAME);

                    cliInstance.println(Cli.toBold("Move Error") + ": you can't perform this move because " + inhibitorGod + " doesn't let you move in the position you specified!");
                } else if (update.errorType == ErrorType.DENIED_BY_PLAYER_GOD) {
                    cliInstance.println(Cli.toBold("Move Error") + ": you can't perform this move because you can't move to this cell now (maybe you've already moved), or your God doesn't let you move in the position you specified!");
                } else if (update.errorType == ErrorType.WRONG_TURN) {
                    cliInstance.println(Cli.toBold("Move Error") + ": you can't perform this move because it's not your turn!");
                } else {
                    commonErrorHandling(update);
                }

                break;

            case BUILD:

                if (update.errorType == ErrorType.DENIED_BY_OPPONENT_GOD) {
                    String inhibitorGod = update.getInhibitorGod().get(GodsUtils.GOD_NAME);
                    cliInstance.println(Cli.toBold("Build Error") + ": you can't perform this build because " + inhibitorGod + " doesn't let you build in the position you specified!");
                } else if (update.errorType == ErrorType.DENIED_BY_PLAYER_GOD) {
                    cliInstance.println(Cli.toBold("Build Error") + ": you can't perform this build because you can't build in this cell (maybe you haven't moved yet), or your God doesn't let you build in the position you specified!");
                } else if (update.errorType == ErrorType.WRONG_TURN) {
                    cliInstance.println(Cli.toBold("Build Error") + ": you can't perform this build because it's not your turn!");
                } else commonErrorHandling(update);

                break;

            case END_TURN:
                if (update.errorType == ErrorType.DENIED_BY_OPPONENT_GOD) {
                    String inhibitorGod = update.getInhibitorGod().get(GodsUtils.GOD_NAME);
                    cliInstance.println(Cli.toBold("End Turn Error") + ": " + inhibitorGod + " doesn't let you end your turn now!");
                } else if (update.errorType == ErrorType.DENIED_BY_PLAYER_GOD) {
                    cliInstance.println(Cli.toBold("End Turn Error") + ": you can't end your turn now: maybe you must move or build!");
                } else if (update.errorType == ErrorType.WRONG_TURN) {
                    cliInstance.println(Cli.toBold("End Turn Error") + ": you can't end your turn because it's not your turn!");
                } else if (update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    cliInstance.println(Cli.toBold("Wrong Game Phase") + ": current Game Phase is not Real Game Phase");
                }

                break;

            case PICK:

                if (update.errorType == ErrorType.ALREADY_TAKEN_NICKNAME) {
                    cliInstance.println(Cli.toBold("Nickname Error") + ": already taken nickname");
                } else if (update.errorType == ErrorType.INVALID_COLOR) {
                    cliInstance.println(Cli.toBold("Color Error") + ": invalid or already taken color");
                } else if (update.errorType == ErrorType.WRONG_TURN) {
                    cliInstance.println(Cli.toBold("Turn Error") + ": Not your turn!");
                } else if (update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    cliInstance.println(Cli.toBold("Wrong Game Phase") + ": current Game Phase is not Initial Info Phase");
                }

                break;

            case PLACE:
                if (update.errorType == ErrorType.DENIED_BY_OPPONENT_GOD) {
                    String inhibitorGod = update.getInhibitorGod().get(GodsUtils.GOD_NAME);
                    cliInstance.println(Cli.toBold("Game Preparation Error") + ": you can't place your Worker where you specified because " + inhibitorGod + " doesn't allow it");
                } else if (update.errorType == ErrorType.DENIED_BY_PLAYER_GOD) {
                    cliInstance.println(Cli.toBold("Game Preparation Error") + ": you can't place your Workers where you specified because your God doesn't allow it");
                } else if (update.errorType == ErrorType.WRONG_TURN) {
                    cliInstance.println(Cli.toBold("Game Preparation Error") + ": you can't place your Workers because it's not your turn!");
                } else if (update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    cliInstance.println(Cli.toBold("Wrong Game Phase") + ": current Game Phase is not Game Preparation Phase");
                }
                break;

            case SELECT:

                if (update.errorType == ErrorType.WRONG_TURN) {
                    cliInstance.println(Cli.toBold("God Choice Error") + ": you can't choose your God because it's not your turn!");
                } else if (update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    cliInstance.println(Cli.toBold("Wrong Game Phase") + ": current Game Phase is not Gods Choice Phase");
                } else if (update.errorType == ErrorType.INVALID_GOD) {
                    cliInstance.println(Cli.toBold("God Error") + ": invalid God selected, it's not in selectable Gods list!");
                }
                break;
        }
        cliInstance.println("For more information about commands or rules, type 'help " + cliInstance.getCurrentPhaseString() + "' or 'rules'");
        cliInstance.newLine();
    }

    /**
     * This private method was extracted from error handling procedure, to avoid duplicates for errors handled the same way for different {@link CommandType}.
     * Specifically, this handles {@link ErrorType#WRONG_GAME_PHASE}, {@link ErrorType#INVALID_CELL} and {@link ErrorType#GENERIC_ERROR}.
     *
     * @param update the error update which arrived from {@link Cli}.
     */
    private void commonErrorHandling(ErrorUpdate update) {
        if (update.errorType == ErrorType.WRONG_GAME_PHASE) {
            cliInstance.println(Cli.toBold("Wrong Game Phase") + ": current Game Phase is not Real Game Phase");
        } else if (update.errorType == ErrorType.INVALID_CELL) {
            cliInstance.println(Cli.toBold("Error") + ": Invalid Cell");
        } else if (update.errorType == ErrorType.GENERIC_ERROR) {
            cliInstance.println(Cli.toBold("Generic Error") + ": please, try another command");
        }
    }

    /**
     * This method handles {@link GamePhaseUpdate}  arrived from the {@link Cli}.
     * Every time the game phase changes, this method prints the name of the new current phase
     * upper case font to all the players involved in the match.
     *
     * @param update contains a reference to the new {@link GamePhase}.
     */
    @Override
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

    /**
     * This method handles {@link TurnUpdate}  arrived from the {@link Cli}.
     * When a player ends is turn, new current turn pass to the next player in game.
     *
     * @param update contains information about the new turn, it means that new
     *               current turn needs to be updated.
     */
    @Override
    public void handle(TurnUpdate update) {
        cliInstance.forwardNotify(update);

        if (controller.getCurrentPlayerID().equals(controller.getClientPlayerID()) && cliInstance.getCurrentPhase() == GamePhase.GAME_PREPARATION) {
            cliInstance.printGamePreparationInfo();
        }
    }

    /**
     * This method handles {@link WinUpdate}  arrived from the {@link Cli}.
     * When a player wins the match, all the players are notified, and they can decide
     * to play another game or exit the game.
     *
     * @param update contains information who won the match.
     */
    @Override
    public void handle(WinUpdate update) {
        cliInstance.newLine();

        if (update.getWinnerPlayer().getNickname().toLowerCase().equals(controller.getClientPlayer().getNickname().toLowerCase())) {
            cliInstance.println("You Win!");
        } else {
            cliInstance.println(cliInstance.playerWithColor(update.getWinnerPlayer().getNickname()) + " Wins!");
        }
        cliInstance.newLine();
        cliInstance.println("Do you want to play another match?");
        cliInstance.newLine();
    }

    /**
     * This method handles {@link LoseUpdate}  arrived from the {@link Cli}.
     * When a player lose, the motivation of this lose is notified to all the players.
     * In case two other players remains, player who lost is asked if he wants to continue watch
     * the match in which he was involved. In case only one player remains, it automatically wins.
     *
     * @param update contains information about the new turn, it means that new
     *               current turn needs to be updated.
     */
    @Override
    public void handle(LoseUpdate update) {

        boolean hasClientLost = controller.getClientPlayerID().equals(update.getLoserPlayer().getPlayerID());

        String subject = hasClientLost ? "you" : "he/she"; // update.getLoserPlayer().getNickname()
        String loseCauseMsg = " because " + subject + " can't " + (update.getLoseCause() == LoseUpdate.LoseCause.CANT_MOVE ? "move" : "build") +
                " with any Worker";


        if (controller.getClientPlayerID().equals(update.getLoserPlayer().getPlayerID())) {
            cliInstance.newLine();
            cliInstance.println(Cli.toBold("You lost" + loseCauseMsg + "!"));
            cliInstance.newLine();
            cliInstance.setCurrentGamePhase(GamePhase.MATCH_LOST);

            if (update.onePlayerRemaining) {
                cliInstance.setCurrentGamePhase(GamePhase.MATCH_ENDED);
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

    /**
     * This method handles {@link ServerUnreachableUpdate}  arrived from the {@link it.polimi.ingsw.network.client.UpdateListener}.
     * This method informs players that something went wrong during connection to server phase, and quits the game.
     *
     * @param update contains the instance of the serverError given from client-side Controller.
     */
    @Override
    public void handle(ServerUnreachableUpdate update) {
        cliInstance.println("Cannot communicate to the Server, maybe it's down. Otherwise, check your connection.");
        cliInstance.println("Quitting...");
        System.exit(0);
    }

    /**
     * This method handles {@link DisconnectedPlayerUpdate} arrived from the {@link Cli}.
     * When a player logs out the match ends and all the players are informed about the end
     * of the match due to "[playerDisconnectedNickname] disconnected!"
     *
     * @param update contains the information about which player logged out.
     */
    @Override
    public void handle(DisconnectedPlayerUpdate update) {
        String nicknameToShow = update.getDisconnectedPlayer().getNickname() != null ? update.getDisconnectedPlayer().getNickname() : "A player";
        cliInstance.println(nicknameToShow + " disconnected!");
        this.cliInstance.println("Do you want to play another match?");
        this.cliInstance.newLine();

    }

    /**
     * This private method creates a StringBuilder containing all
     * the names of all possible Gods available. It is used in CHOOSE_GODS game phase,
     * it allows GodChooser to choose as many Gods as players involved in the match,
     * having all the GodsName available in console.
     *
     * @return a String that is printed only for GodChooser during CHOOSE_GODS phase.
     */
    private StringBuilder availableGods() {
        List<String> godNames = View.getGodsNamesList();
        StringBuilder result = new StringBuilder(godNames.get(0).toUpperCase());
        for (int i = 1; i < godNames.size(); i++) {
            result.append(", ").append(godNames.get(i).toUpperCase());
        }
        return result;
    }

}

