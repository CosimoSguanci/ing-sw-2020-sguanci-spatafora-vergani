package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.exceptions.InvalidColorException;
import it.polimi.ingsw.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.exceptions.WrongPlayerException;
import it.polimi.ingsw.view.cli.Cli;

import java.util.Arrays;

/**
 * This class manages every possible interaction with user.
 * Every time a user write a command go through this class and
 * different approaches are applied based on GAME_PHASE.
 *
 * @author Andrea Vergani
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 */
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
                    this.cliPlayerHelper.helperHandle(splitCommand);
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
                    this.cli.println("Unknown Command");
                }
            } catch (BadCommandException e) {
                this.cli.println("Bad command generated, please repeat it.");
            } catch (NicknameAlreadyTakenException e) {
                this.cli.println("Nickname already taken for this match, please select another nickname.");
            } catch (InvalidColorException e) {
                this.cli.println("Invalid color requested: another player already chose it or this color is not available in this game.");
            } catch (WrongPlayerException e) {
                this.cli.println("Invalid command: please check if it's your turn!");
            }

            this.cli.newLine();
        }
    }

    /**
     * This private method checks if a command received through
     * console is a valid command or not.
     * @param splitCommand is an array of strings which contains, separately,
     *                     all the different words entered in the console.
     * @return false if the command contains computed words, even if these are not
     *         valid in the game; true otherwise.
     */
    private boolean computeCommand(String[] splitCommand) {
        return (splitCommand.length == 0) || (splitCommand[0].equals("") && splitCommand.length == 1);
    }

    /**
     * This method invokes another method which contains an
     * algorithm to print the board game Cli version.
     * @param board indicates the board, JSON-format which is used in
     *              the relative match. It contains references to each cell
     */
    public void printBoard(String board) {
        this.boardDelegate.printBoard(board);
    }
}
