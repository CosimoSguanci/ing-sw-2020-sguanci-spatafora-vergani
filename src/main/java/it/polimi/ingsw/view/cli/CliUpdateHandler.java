package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.view.UpdateHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CliUpdateHandler implements UpdateHandler {
    private final Cli cliInstance;

    CliUpdateHandler(Cli cliInstance) {
        this.cliInstance = cliInstance;
    }

    public void handle(MatchStartedUpdate update) {
        cliInstance.printBoard(update.getBoard());

        cliInstance.print(Cli.toBold("MATCH STARTED!"));
        cliInstance.newLine();
        cliInstance.print(Cli.toBold("Tip") + ": type 'help " + cliInstance.getCurrentPhaseString() + "' to know command format");
        cliInstance.newLine();
        cliInstance.printCurrentTurn();
    }


    public void handle(GodsUpdate update) {


        if (update.getSelectedGods().size() == cliInstance.getPlayersNum()) {
            cliInstance.setPlayersGods(update.getSelectedGods());
            cliInstance.printPlayerGods();
        } else {
            if (!cliInstance.controller.getCurrentPlayerID().equals(cliInstance.controller.getClientPlayerID())) return;

            if (cliInstance.controller.isClientPlayerGodChooser()) {
                cliInstance.setInitialGodChooser(true);
                cliInstance.print(Cli.toBold("You are the God Chooser!"));
                cliInstance.newLine();
                cliInstance.print(Cli.toBold("Choose ") + cliInstance.getPlayersNum() + " gods. For a perfect match, choose the ones you like most!    Command " + Cli.toBold("format") + " expected: select [god(1)] ... [god(n)]");
                cliInstance.newLine();
                cliInstance.print("Available gods are: " + availableGods());
                cliInstance.newLine();
                cliInstance.print("REMEMBER: if you need info about a god and his/her powers, type 'info [god]'");
            } else {
                cliInstance.setInitialGodChooser(false);
                cliInstance.setSelectableGods(update.getSelectableGods());
                cliInstance.print(Cli.toBold("Choose ") + "your god.    Command " + Cli.toBold("format") + " expected: select [god]");
                if (listToStringBuilder(update.getSelectableGods()) != null) {
                    cliInstance.newLine();
                    cliInstance.print("Available choices are: " + listToStringBuilder(update.getSelectableGods()));
                }
                cliInstance.newLine();
                cliInstance.print("REMEMBER: if you need info about a god, type 'info [god]'");
            }
            cliInstance.newLine();
        }
    }

    public void handle(InitialInfoUpdate update) {

        if (update.getInitialInfo().size() == cliInstance.getPlayersNum()) {
            cliInstance.setPlayersColors(update.getInitialInfo());
            cliInstance.printPlayersColors();
        } else {
            if (!cliInstance.controller.getCurrentPlayerID().equals(cliInstance.controller.getClientPlayerID())) return;

            cliInstance.print("Type your " + Cli.toBold("nickname and color") + " separated by a space.    Command " + Cli.toBold("format") + " expected: pick [nickname] [color]");

            List<String> selectedNicknames = new ArrayList<>(update.getInitialInfo().keySet());
            List<PrintableColor> selectedColors = new ArrayList<>(update.getInitialInfo().values());
            List<PrintableColor> selectableColors = PrintableColor.getColorList().stream().filter(color -> !selectedColors.contains(color)).collect(Collectors.toList());


            if (!selectedNicknames.isEmpty()) {
                cliInstance.newLine();
                cliInstance.print("Nicknames already taken are: " + listToStringBuilder(selectedNicknames));
            }

            cliInstance.newLine();

            cliInstance.setSelectedNicknames(selectedNicknames);
            cliInstance.print("Available colors are: ");

            selectableColors.forEach((color) -> {
                cliInstance.print(Cli.convertColorToAnsi(color) + color + PrintableColor.RESET);
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
                cliInstance.print(cliInstance.playerWithColor(nickname) + " moved " + executedCommand.workerID.toUpperCase() + " to " + rowChar + (executedCommand.col + 1));
            } else {
                cliInstance.print(cliInstance.playerWithColor(nickname) + " built with " + executedCommand.workerID.toUpperCase() + " in " + rowChar + (executedCommand.col + 1));
            }

            cliInstance.newLine();

        }


        cliInstance.printCurrentTurn();
    }

    public void handle(ErrorUpdate update) {

        if (!update.getCurrentPlayer().getPlayerID().equals(cliInstance.controller.getClientPlayerID())) return;

        switch (update.command) {
            case MOVE:
                cliInstance.print(Cli.toBold("Move Error") + ": maybe you can't perform this move or it's not time for a move. Try with another command or wait for your turn!");
                break;

            case BUILD:
                cliInstance.print(Cli.toBold("Build Error") + ": maybe you can't perform this build or it's not time for a build. Try with another command or wait for your turn!");
                break;

            case END_TURN:
                cliInstance.print("You " + Cli.toBold("can't end") + " your turn now");
                break;

            case PICK:
                cliInstance.print(Cli.toBold("Nickname/Color Choose Error") + ": maybe it's not your turn and/or you typed something incorrectly");
                break;

            case PLACE:
                cliInstance.print(Cli.toBold("Game Preparation Error") + ": maybe it's not your turn and/or you typed something incorrectly");
                break;

            case SELECT:
                cliInstance.print(Cli.toBold("Gods Choose Error") + ": maybe it's not your turn and/or you typed something incorrectly");
                break;
        }
        cliInstance.print("For more information about commands or rules, type 'help " + cliInstance.getCurrentPhaseString() + "' or 'rules'");
        cliInstance.newLine();
    }

    public void handle(GamePhaseUpdate update) {
        cliInstance.newLine();
        if (update.newGamePhase.isPrintable()) {
            switch (update.newGamePhase) {
                case INITIAL_INFO:
                    cliInstance.print(Cli.toBold("INITIAL INFORMATION PHASE"));
                    break;
                case CHOOSE_GODS:
                    cliInstance.print(Cli.toBold("GODS CHOOSE PHASE"));
                    break;
                case GAME_PREPARATION:
                    cliInstance.print(Cli.toBold("GAME PREPARATION PHASE"));
                    break;
                case REAL_GAME:
                    cliInstance.print(Cli.toBold("GAME STARTED PHASE"));
                    break;
            }
        }
        cliInstance.setCurrentGamePhase(update.newGamePhase);
    }


    public void handle(TurnUpdate update) {
        cliInstance.forwardNotify(update);

        if (cliInstance.controller.getCurrentPlayerID().equals(cliInstance.controller.getClientPlayerID()) && cliInstance.getCurrentPhase() == GamePhase.GAME_PREPARATION) {
            cliInstance.printGamePreparationInfo();
        }
    }

    public void handle(WinUpdate update) {
        cliInstance.newLine();
        cliInstance.print(cliInstance.playerWithColor(update.getWinnerPlayer().getNickname()) + " wins!");

        cliInstance.newLine();
    }

    public void handle(LoseUpdate update) {

        if (cliInstance.controller.getClientPlayerID().equals(update.getLoserPlayer().getPlayerID())) {
            cliInstance.newLine();
            cliInstance.print(Cli.toBold("You lost!"));
            cliInstance.newLine();
            cliInstance.setCurrentGamePhase(GamePhase.MATCH_LOST);

            if (update.onePlayerRemaining) {
                cliInstance.setCurrentGamePhase(GamePhase.MATCH_ENDED); // todo method to avoid duplicate
            } else {
                cliInstance.print("Do you want to continue to watch this match?");
            }
        } else if (update.onePlayerRemaining) {
            cliInstance.newLine();
            cliInstance.print(cliInstance.playerWithColor(update.getLoserPlayer().getNickname()) + " lost!");
            cliInstance.print(Cli.toBold("You Win!"));
            cliInstance.newLine();
            cliInstance.setCurrentGamePhase(GamePhase.MATCH_ENDED);
        } else {
            cliInstance.newLine();
            cliInstance.print(cliInstance.playerWithColor(update.getLoserPlayer().getNickname()) + " lost!");
        }


    }

    public void handle(ServerUnreachableUpdate update) {
        cliInstance.print("Cannot communicate to the Server, maybe it's down. Otherwise, check your connection.");
        cliInstance.print("Quitting..."); // todo method quit()
        System.exit(0);
    }

    public void handle(DisconnectedPlayerUpdate update) {
        String nicknameToShow = update.getDisconnectedPlayer().getNickname() != null ? update.getDisconnectedPlayer().getNickname() : "A player";
        cliInstance.print(nicknameToShow + " disconnected!");
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

