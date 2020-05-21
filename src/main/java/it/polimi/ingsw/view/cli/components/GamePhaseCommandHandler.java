package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.exceptions.InvalidColorException;
import it.polimi.ingsw.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.exceptions.WrongPlayerException;
import it.polimi.ingsw.view.cli.Cli;

import java.util.Arrays;

public class GamePhaseCommandHandler {
    private final RealGame realGame;
    private final GodChoice godChoice;
    private final InitialInfo initialInfo;
    private final MatchEnded matchEnded;
    private final GamePreparation gamePreparation;
    private final CliPlayerHelper cliPlayerHelper;
    private final WaitingForAMatch waitingForAMatch;
    private final BoardDelegate boardDelegate;
    private final MatchLost matchLost;

    private final Cli cli;

    public GamePhaseCommandHandler(Cli cli) {
        this.cli = cli;
        this.realGame = new RealGame(this.cli);
        this.godChoice = new GodChoice(this.cli);
        this.initialInfo = new InitialInfo(this.cli);
        this.matchEnded = new MatchEnded(this.cli);
        this.gamePreparation = new GamePreparation(this.cli);
        this.cliPlayerHelper = new CliPlayerHelper(this.cli);
        this.waitingForAMatch = new WaitingForAMatch(this.cli);
        this.boardDelegate = new BoardDelegate(this.cli);
        this.matchLost = new MatchLost(this.cli);
    }

    /**
     * This method manages every possible interaction with the client.
     * All the commands received by the client are parsed in this method
     * grouped by game phases.
     */
    public void gameLoop() {
        waitingForAMatch.handleWaiting();
        String command;

        while (true) {

            command = cli.getStdin().nextLine().toLowerCase();

            String[] splitCommand = command.toLowerCase().split("\\s+");

            if (computeCommand(splitCommand)) { continue; }

            if (splitCommand[0].length() == 0) {  // command starting with space
                splitCommand = Arrays.copyOfRange(splitCommand, 1, splitCommand.length);
            }

            try {

                if (this.cli.getCurrentPhase() == GamePhase.MATCH_ENDED) {
                    boolean breakWhile = matchEnded.handle(command);
                    if (breakWhile) { break; }
                }
                else if (this.cli.getCurrentPhase() == GamePhase.MATCH_LOST) {
                    boolean continueWhile = matchLost.handleMatchLost(command);
                    if (continueWhile) { continue; }
                }
                else if (CommandType.isHelperCommandType(splitCommand[0])){
                    cliPlayerHelper.helperHandle(splitCommand);
                }
                else if (this.cli.getCurrentPhase() == GamePhase.INITIAL_INFO) {
                    this.initialInfo.handleInitialInfoCommand(splitCommand);
                }
                else if (this.cli.getCurrentPhase() == GamePhase.CHOOSE_GODS && this.cli.getInitialGodChooser()) {
                    this.godChoice.handleIsGodChooserGodsChoice(splitCommand);
                }
                else if (this.cli.getCurrentPhase() == GamePhase.CHOOSE_GODS) { // but not initial god chooser
                    this.godChoice.handleGodChoice(splitCommand);
                }
                else if (this.cli.getCurrentPhase() == GamePhase.GAME_PREPARATION) {
                    this.gamePreparation.handle(command);
                }
                else if (this.cli.getCurrentPhase() == GamePhase.REAL_GAME) {
                    this.realGame.handleRealGame(command);
                }
                else {
                    this.cli.print("Unknown Command");
                }
            } catch (BadCommandException e) {
                this.cli.print("Bad command generated, please repeat it.");
            } catch (NicknameAlreadyTakenException e) {
                this.cli.print("Nickname already taken for this match, please select another nickname.");
            } catch (InvalidColorException e) {
                this.cli.print ("Invalid color requested: another player already chose it or this color is not available in this game.");
            } catch (WrongPlayerException e) {
                this.cli.print ("Invalid command: please check if it's your turn!");
            }

            this.cli.newLine();
        }
    }

    private boolean computeCommand(String[] splitCommand) {
        return (splitCommand.length == 0) || (splitCommand[0].equals("") && splitCommand.length == 1);
    }

    public void printBoard(String board) {
        this.boardDelegate.printBoard(board);
    }
}
