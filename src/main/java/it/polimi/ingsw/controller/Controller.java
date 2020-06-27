package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gods.GodStrategy;
import it.polimi.ingsw.model.updates.LoseUpdate;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.utils.GodsUtils.*;


/**
 * This class represents the Controller in MVC design pattern. The whole
 * application, in fact, is based on MVC pattern, and in particular
 * Controller class is View's observer. The "dialogue" between Controller
 * and View is possible (mainly) through Command class: this (abstract) class can represent a
 * move/build that a player wants to do during the game, or some initial information, ...
 * This is the reason for Controller class to implement Observer(Command), since (as
 * explained) View notifies Controller using Command as update message.
 * Then, following MVC pattern, Controller invokes Model's methods to
 * modify the model itself.
 *
 * @author Andrea Mario Vergani
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 */
public class Controller extends Observable<Controller> implements Observer<Command> { // TODO Refactor Controller in submodules ?
    private final Model model; // todo handle attribute visibility and immutability
    private final CommandHandler commandHandler;
    private Player godChooserPlayer;
    private List<String> selectableGods;
    private List<String> selectedNicknames;
    private List<PrintableColor> selectableColors;


    /**
     * The constructor creates an instance of Controller class. This class
     * needs to reference to the general Model of the game, so that changes
     * and operations in it are possible.
     *
     * @param model class Model of MVC pattern
     */
    public Controller(Model model) {
        this.model = model;
        this.commandHandler = new CommandHandlerImpl(this);
    }


    /**
     * This method is the getter for the current game-phase of the match.
     *
     * @return match's current game phase
     */
    GamePhase getCurrentGamePhase() {
        return this.model.getCurrentGamePhase();
    }


    /**
     * This method is the getter for the list of all players involved in the match.
     *
     * @return the list of players
     */
    List<Player> getPlayers() {
        return this.model.getPlayers();
    }

    /**
     * This method is the getter for the board of the match.
     *
     * @return match's board
     */
    Board getBoard() {
        return this.model.getBoard();
    }


    /**
     * This method is an overriding method of "update" in Observer interface.
     * Its task is to handle players' commands coming from View.
     *
     * @param command player move/build from View
     */
    @Override
    public void update(Command command) {
        try {
            command.handleCommand(this.commandHandler);
        } catch (WrongGamePhaseException e) {
            model.reportError(command.getPlayer(), command.commandType, ErrorType.WRONG_GAME_PHASE, null);
        } catch (WrongPlayerException e) {
            model.reportError(command.getPlayer(), command.commandType, ErrorType.WRONG_TURN, null);
        } catch (NicknameAlreadyTakenException e) {
            model.reportError(command.getPlayer(), command.commandType, ErrorType.ALREADY_TAKEN_NICKNAME, null);
        } catch (InvalidColorException e) {
            model.reportError(command.getPlayer(), command.commandType, ErrorType.INVALID_COLOR, null);
        } catch (InvalidGodException | UnknownGodException e) {
            model.reportError(command.getPlayer(), command.commandType, ErrorType.INVALID_GOD, null);
        } catch (InvalidCellException | CannotIncreaseLevelException | CellNotEmptyException e) {
            model.reportError(command.getPlayer(), command.commandType, ErrorType.INVALID_CELL, null);
        } catch (NullPointerException e) {
            model.reportError(command.getPlayer(), command.commandType, ErrorType.GENERIC_ERROR, null);
        }
    }


    /**
     * This method handles an InitialInfoCommand. In particular, its main task is to set the
     * chosen nickname and colour; then, the player's turn ends (with a possible change of
     * game-phase, if everyone has already finished InitialInfo phase).
     *
     * @param initialInfoCommand command containing nickname and colour chosen by one of the
     *                           players
     * @throws WrongPlayerException          if player who gave the command is not the current player
     * @throws NicknameAlreadyTakenException if selected nickname has already been chosen by
     *                                       another player
     * @throws InvalidColorException         if selected colour has already been chosen by another player
     *                                       (or, for any other reason, is not in selectable's colour
     *                                       list)
     */
    synchronized void handleInitialInfoCommand(InitialInfoCommand initialInfoCommand) {

        if (!initialInfoCommand.getPlayer().equals(model.getCurrentPlayer())) {
            throw new WrongPlayerException();
        }

        String nickname = initialInfoCommand.nickname;

        if (selectedNicknames.contains(nickname)) {
            throw new NicknameAlreadyTakenException();
        }


        PrintableColor color = initialInfoCommand.color;

        if (!selectableColors.contains(color)) {
            throw new InvalidColorException();
        }


        model.getCurrentPlayer().setNickname(nickname);
        model.getCurrentPlayer().setColor(color);

        selectedNicknames.add(nickname);
        selectableColors.remove(color);

        model.endTurn();

        HashMap<String, PrintableColor> initialInfo = new HashMap<>();

        List<Player> initialInfoDonePlayers = model.getPlayers().stream().filter(player -> player.getNickname() != null && player.getColor() != null).collect(Collectors.toList());

        initialInfoDonePlayers.forEach((player) -> initialInfo.put(player.getNickname(), player.getColor()));

        model.initialInfoUpdate(initialInfo);

        if (initialInfoDonePlayers.size() == model.getPlayersNumber()) {
            godChoosePhase();
        }

    }


    /**
     * This method handles a GodChoiceCommand. In particular, its main task is to set the
     * chosen god (or gods, if the player is god-chooser); then, the player's turn ends
     * (with a possible change of game-phase, if everyone has already finished GodChoice
     * phase).
     *
     * @param godChoiceCommand command containing the chosen god (not god-chooser player) or
     *                         gods(god-chooser player)
     * @throws WrongPlayerException if player who gave the command is not the current player
     * @throws InvalidGodException  if selected god is not in selectable's gods list
     */
    synchronized void handleGodChoiceCommand(GodChoiceCommand godChoiceCommand) {
        List<String> chosenGods = godChoiceCommand.getChosenGods();

        if (!godChoiceCommand.getPlayer().equals(model.getCurrentPlayer())) {
            throw new WrongPlayerException();
        }

        if (model.getCurrentPlayer().equals(this.godChooserPlayer)) {
            this.selectableGods = chosenGods;

        } else {
            String god = chosenGods.get(0);

            if (!selectableGods.contains(god)) {
                throw new InvalidGodException();
            }

            model.getCurrentPlayer().setGodStrategy(GodStrategy.instantiateGod(god));
            this.selectableGods.remove(god);
        }

        model.endTurn();

        HashMap<String, String> selectedGods = new HashMap<>();

        List<Player> godChosenPlayers = model.getPlayers().stream().filter(player -> player.getGodStrategy() != null).collect(Collectors.toList());

        godChosenPlayers.forEach((player) -> selectedGods.put(player.getNickname(), player.getGodStrategy().NAME));

        if (model.getCurrentPlayer().equals(godChooserPlayer)) { // The God Chooser is forced to pick the last remaining God
            String god = selectableGods.get(0);
            model.getCurrentPlayer().setGodStrategy(GodStrategy.instantiateGod(god));

            selectedGods.put(model.getCurrentPlayer().getNickname(), model.getCurrentPlayer().getGodStrategy().NAME);
            model.godsUpdate(this.selectableGods, selectedGods);

            model.endTurn();
            gamePreparationPhase();

        } else {
            model.godsUpdate(this.selectableGods, selectedGods);
        }
    }


    /**
     * This method handles a GamePreparationCommand. In particular, its main task is to place
     * workers in the specified positions; then, the player's turn ends (with a possible change
     * of game-phase, if everyone has already finished GamePreparation phase).
     *
     * @param gamePreparationCommand command containing the positions chosen for the two workers
     * @throws WrongPlayerException if player who gave the command is not the current player
     */
    synchronized void handleGamePreparationCommand(GamePreparationCommand gamePreparationCommand) {
        Player currentPlayer = model.getCurrentPlayer();
        if (!gamePreparationCommand.getPlayer().equals(model.getCurrentPlayer())) { // todo uniform use of model.getCurrentPlayer() / command.getPlayer()
            throw new WrongPlayerException();
        } else {

            Map<String, String> inhibitorGod = new HashMap<>();

            if (!checkAllGamePreparationConstraints(gamePreparationCommand, inhibitorGod)) {
                model.reportError(currentPlayer, CommandType.PLACE, ErrorType.DENIED_BY_OPPONENT_GOD, inhibitorGod);
                return;
            } else if (currentPlayer.getGodStrategy().checkGamePreparation(
                    currentPlayer.getWorkerFirst(),
                    gamePreparationCommand.getWorkerFirstCell(),
                    currentPlayer.getWorkerSecond(),
                    gamePreparationCommand.getWorkerSecondCell())
            ) {
                currentPlayer.getGodStrategy().executeGamePreparation(currentPlayer.getWorkerFirst(), gamePreparationCommand.getWorkerFirstCell(), currentPlayer.getWorkerSecond(), gamePreparationCommand.getWorkerSecondCell());
            } else {
                model.reportError(currentPlayer, CommandType.PLACE, ErrorType.DENIED_BY_PLAYER_GOD, null);
                return;
            }

            model.endTurn();

            if (currentPlayer.equals(godChooserPlayer)) { // Game Preparation Done
                startMatch();
            } else {
                model.boardUpdate();
            }
        }
    }

    /**
     * This private method simply extends update's task, for PlayerCommand. In fact, to handle
     * this command is necessary to: verify that the player who required it is
     * game's current player (otherwise, there is no need to call the Model,
     * since its state will not modify); call the selected command from the
     * player, as an invocation of one of model's methods.
     *
     * @param playerCommand player command from View
     * @throws WrongPlayerException when command's player is not current player
     */
    synchronized void handlePlayerCommand(PlayerCommand playerCommand) throws WrongPlayerException {
        Player currentPlayer = model.getCurrentPlayer();
        if (!playerCommand.getPlayer().equals(currentPlayer)) {
            throw new WrongPlayerException();
        } else {
            Map<String, String> inhibitorGod = new HashMap<>();
            switch (playerCommand.commandType) {
                case MOVE:

                    if (!checkAllMoveConstraints(playerCommand, inhibitorGod)) {
                        model.reportError(playerCommand.getPlayer(), playerCommand.commandType, ErrorType.DENIED_BY_OPPONENT_GOD, inhibitorGod);
                        return;
                    }

                    if (currentPlayer.getGodStrategy().checkMove(playerCommand.getWorker(), playerCommand.getCell())) {

                        currentPlayer.getGodStrategy().executeMove(playerCommand.getWorker(), playerCommand.getCell());

                        boolean hasWon = currentPlayer.getGodStrategy().checkWinCondition(playerCommand.getWorker()) && checkAllWinConstraints(playerCommand);

                        model.boardUpdate(playerCommand);

                        if (hasWon) {
                            model.nextGamePhase();
                            model.gamePhaseUpdate(model.getCurrentGamePhase());
                            model.winUpdate(currentPlayer);

                            onMatchWon();

                        } else {
                            // if !hasWon check if the new currentPlayer can build
                            checkLoseConditionsBuild(playerCommand);
                        }

                    } else {
                        model.reportError(playerCommand.getPlayer(), CommandType.MOVE, ErrorType.DENIED_BY_PLAYER_GOD, null);
                    }
                    break;
                case BUILD:

                    if (!checkAllBuildConstraints(playerCommand, inhibitorGod)) {
                        model.reportError(playerCommand.getPlayer(), playerCommand.commandType, ErrorType.DENIED_BY_OPPONENT_GOD, inhibitorGod);
                        return;
                    }

                    if (currentPlayer.getGodStrategy().checkBuild(playerCommand.getWorker(), playerCommand.getCell(), playerCommand.cellBlockType)) {
                        currentPlayer.getGodStrategy().executeBuild(playerCommand.getWorker(), playerCommand.getCell(), playerCommand.cellBlockType);

                        /*
                         *  Necessary to cover the possibility that a Gods could have a Win Conditions that triggers when it builds a block under certain conditions.
                         *  Example: "If XXX built a Dome and it's adjacent to another Worker, it wins"
                         */
                        boolean hasWon = currentPlayer.getGodStrategy().checkWinCondition(playerCommand.getWorker()) && checkAllWinConstraints(playerCommand);

                        model.boardUpdate(playerCommand);

                        if (hasWon) {
                            model.nextGamePhase();
                            model.gamePhaseUpdate(model.getCurrentGamePhase());
                            model.winUpdate(currentPlayer);

                            onMatchWon();
                        }

                    } else {
                        model.reportError(playerCommand.getPlayer(), CommandType.BUILD, ErrorType.DENIED_BY_PLAYER_GOD, null);
                    }
                    break;
                case END_TURN:

                    if (!checkAllEndTurnConstraints(playerCommand, inhibitorGod)) {
                        model.reportError(playerCommand.getPlayer(), playerCommand.commandType, ErrorType.DENIED_BY_OPPONENT_GOD, inhibitorGod);
                        return;
                    }

                    if (currentPlayer.getGodStrategy().checkEndTurn()) {

                        currentPlayer.getGodStrategy().endPlayerTurn(currentPlayer);

                        model.endTurn();

                        model.getCurrentPlayer().getGodStrategy().onTurnStarted(model.getCurrentPlayer()); // onTurnStart

                        model.boardUpdate(playerCommand);

                        // check if the new currentPlayer can move
                        checkLoseConditionsMove();

                    } else {
                        model.reportError(playerCommand.getPlayer(), CommandType.END_TURN, ErrorType.DENIED_BY_PLAYER_GOD, null);
                    }

                    break;
            }

        }
    }


    /**
     * This method checks if the current player has a possibility to move. In fact, if the
     * current player needs to move but can't do it, he/she immediately loses the match: in
     * this case, a proper method (which will notify all the players) is called.
     */
    private void checkLoseConditionsMove() {
        if (!model.getCurrentPlayer().getGodStrategy().canMove(model.getBoard(), model.getCurrentPlayer())) {
            model.onPlayerLose(model.getCurrentPlayer(), LoseUpdate.LoseCause.CANT_MOVE);

            if (model.getPlayers().size() != 1) {
                model.boardUpdate();
            }

        } else {
            if (!checkCanMoveOtherGodsConstraints(model.getCurrentPlayer())) {
                model.onPlayerLose(model.getCurrentPlayer(), LoseUpdate.LoseCause.CANT_MOVE);

                if (model.getPlayers().size() != 1) {
                    model.boardUpdate();
                } else {
                    onMatchWon();
                }
            }
        }
    }


    /**
     * This method checks if the current player has a possibility to build. In fact, if the
     * current player needs to build but can't do it, he/she immediately loses the match: in
     * this case, a proper method (which will notify all the players) is called.
     */
    private void checkLoseConditionsBuild(PlayerCommand playerCommand) {
        if (!model.getCurrentPlayer().getGodStrategy().canBuild(model.getBoard(), playerCommand.getWorker())) {
            model.onPlayerLose(model.getCurrentPlayer(), LoseUpdate.LoseCause.CANT_BUILD);

            if (model.getPlayers().size() != 1) { // 2 players remaining
                model.boardUpdate();
            }

        } else {
            if (!checkCanBuildOtherGodsConstraints(model.getCurrentPlayer())) {
                model.onPlayerLose(model.getCurrentPlayer(), LoseUpdate.LoseCause.CANT_BUILD);

                if (model.getPlayers().size() != 1) {
                    model.boardUpdate();
                } else {
                    onMatchWon();
                }
            }
        }
    }


    /**
     * This method checks all Players God's Win Constraints to check if there is a power which
     * prevents the Player who executed the given PlayerCommand to win.
     *
     * @param playerCommand player command from View
     * @return true if Win is permitted, false otherwise.
     */
    private boolean checkAllWinConstraints(PlayerCommand playerCommand) {

        for (Player p : model.getPlayers()) {
            if (!p.equals(playerCommand.getPlayer()) && !p.getGodStrategy().checkWinConstraints(playerCommand.getWorker(), playerCommand.getCell())) {
                return false;
            }
        }

        return true;
    }


    /**
     * This method checks if a GamePreparationCommand is valid or not. A GamePreparationCommand
     * can be not-valid when, for example, some rules (mainly related to god powers) are not respected.
     *
     * @param gamePreparationCommand the given command in GamePreparation Phase
     * @param inhibitor              an empty map that, in case, is filled with the name and description of the god that
     *                               does not allow the given command
     * @return true if the command is valid, false otherwise
     */
    private boolean checkAllGamePreparationConstraints(GamePreparationCommand gamePreparationCommand, Map<String, String> inhibitor) {

        for (Player p : model.getPlayers()) {
            if (!p.equals(gamePreparationCommand.getPlayer()) &&

                    !p.getGodStrategy().checkGamePreparationConstraints(
                            gamePreparationCommand.getPlayer().getWorkerFirst(),
                            gamePreparationCommand.getWorkerFirstCell(),
                            gamePreparationCommand.getPlayer().getWorkerSecond(),
                            gamePreparationCommand.getWorkerSecondCell())

            ) {
                inhibitor.put(GOD_NAME, p.getGodStrategy().NAME);
                inhibitor.put(GOD_DESCRIPTION, p.getGodStrategy().DESCRIPTION);
                inhibitor.put(POWER_DESCRIPTION, p.getGodStrategy().POWER_DESCRIPTION);
                return false;
            }
        }

        return true;
    }


    /**
     * This method checks all Players God's Move Constraints to check if there is a power which
     * prevents the Player who executed the given PlayerCommand to move.
     *
     * @param playerCommand player command from View
     * @param inhibitor     an empty map that, in case, is filled with the name and description of the god that
     *                      does not allow the given command
     * @return true if move is permitted, false otherwise
     */
    private boolean checkAllMoveConstraints(PlayerCommand playerCommand, Map<String, String> inhibitor) {

        for (Player p : model.getPlayers()) {
            if (!p.equals(playerCommand.getPlayer()) && !p.getGodStrategy().checkMoveConstraints(playerCommand.getWorker(), playerCommand.getCell())) {
                inhibitor.put(GOD_NAME, p.getGodStrategy().NAME);
                inhibitor.put(GOD_DESCRIPTION, p.getGodStrategy().DESCRIPTION);
                inhibitor.put(POWER_DESCRIPTION, p.getGodStrategy().POWER_DESCRIPTION);
                return false;
            }
        }

        return true;
    }

    /**
     * This method checks all Players God's Build Constraints to check if there is a power which
     * prevents the Player who executed the given PlayerCommand to build.
     *
     * @param playerCommand player command from View
     * @param inhibitor     an empty map that, in case, is filled with the name and description of the god that
     *                      does not allow the given command
     * @return true if build is permitted, false otherwise
     */
    private boolean checkAllBuildConstraints(PlayerCommand playerCommand, Map<String, String> inhibitor) {

        for (Player p : model.getPlayers()) {
            if (!p.equals(playerCommand.getPlayer()) && !p.getGodStrategy().checkBuildConstraints(playerCommand.getWorker(), playerCommand.getCell(), playerCommand.cellBlockType)) {
                inhibitor.put(GOD_NAME, p.getGodStrategy().NAME);
                inhibitor.put(GOD_DESCRIPTION, p.getGodStrategy().DESCRIPTION);
                inhibitor.put(POWER_DESCRIPTION, p.getGodStrategy().POWER_DESCRIPTION);
                return false;
            }
        }

        return true;
    }


    /**
     * This method checks if an End-Turn can be performed, or it is not allowed by some of the players' gods.
     *
     * @param playerCommand player command from View
     * @param inhibitor     an empty map that, in case, is filled with the name and description of the god that
     *                      does not allow the given command
     * @return true if end-turn is permitted, false otherwise
     */
    private boolean checkAllEndTurnConstraints(PlayerCommand playerCommand, Map<String, String> inhibitor) {

        for (Player p : model.getPlayers()) {
            if (!p.equals(playerCommand.getPlayer()) && !p.getGodStrategy().checkEndTurnConstraints(playerCommand.getPlayer())) {
                inhibitor.put(GOD_NAME, p.getGodStrategy().NAME);
                inhibitor.put(GOD_DESCRIPTION, p.getGodStrategy().DESCRIPTION);
                inhibitor.put(POWER_DESCRIPTION, p.getGodStrategy().POWER_DESCRIPTION);
                return false;
            }
        }

        return true;
    }


    /**
     * This method checks if one of the parameter-player's workers can move or not, related to other players' God
     * Powers; in particular, some God's Powers can totally inhibit a player's possibility to move with one of
     * his/her workers.
     *
     * @param player player during his/her turn
     * @return true if move is permitted, false if denied by a God Power
     */
    private boolean checkCanMoveOtherGodsConstraints(Player player) {
        List<Cell> availableMoveCellsWorkerFirst = model.getBoard().getAvailableMoveCells(player.getWorkerFirst());
        List<Cell> availableMoveCellsWorkerSecond = model.getBoard().getAvailableMoveCells(player.getWorkerSecond());


        for (Cell moveCell : availableMoveCellsWorkerFirst) {
            boolean feasible = true;
            for (Player p : model.getPlayers()) {
                if (!p.equals(player)) {
                    if (!p.getGodStrategy().checkMoveConstraints(player.getWorkerFirst(), moveCell)) {
                        feasible = false;
                        break;
                    }
                }
            }

            if (feasible) return true;
        }

        for (Cell moveCell : availableMoveCellsWorkerSecond) {
            boolean feasible = true;
            for (Player p : model.getPlayers()) {
                if (!p.equals(player)) {
                    if (!p.getGodStrategy().checkMoveConstraints(player.getWorkerSecond(), moveCell)) {
                        feasible = false;
                        break;
                    }
                }
            }

            if (feasible) return true;
        }


        return false;
    }


    /**
     * This method checks if one of the parameter-player's workers can build or not, related to other players' God
     * Powers; in particular, some God's Powers can totally inhibit a player's possibility to build with one of
     * his/her workers.
     *
     * @param player player during his/her turn
     * @return true if build is permitted, false if denied by a God Power
     */
    private boolean checkCanBuildOtherGodsConstraints(Player player) {
        List<Cell> availableBuildCellsWorkerFirst = model.getBoard().getAvailableBuildCells(player.getWorkerFirst());
        List<Cell> availableBuildCellsWorkerSecond = model.getBoard().getAvailableBuildCells(player.getWorkerSecond());


        for (Cell buildCell : availableBuildCellsWorkerFirst) {
            boolean feasible = true;
            for (Player p : model.getPlayers()) {
                if (!p.equals(player)) {
                    if (!p.getGodStrategy().checkBuildConstraints(player.getWorkerFirst(), buildCell, null)) {
                        feasible = false;
                        break;
                    }
                }
            }

            if (feasible) return true;
        }

        for (Cell buildCell : availableBuildCellsWorkerSecond) {
            boolean feasible = true;
            for (Player p : model.getPlayers()) {
                if (!p.equals(player)) {
                    if (!p.getGodStrategy().checkBuildConstraints(player.getWorkerSecond(), buildCell, null)) {
                        feasible = false;
                        break;
                    }
                }
            }

            if (feasible) return true;
        }


        return false;
    }

    /**
     * This method is the entry point from Server.java class. A random starting-player and god-chooser are chosen,
     * and the players are notified about the first turn of the match. This starting phase is followed by
     * InitialInfo phase, where players choose their nicknames and colours.
     */
    // Entry point from Server class
    public void initialPhase() {
        model.gamePhaseUpdate(model.getCurrentGamePhase());

        List<Player> playerList = model.getPlayers();
        int initialTurn = new Random().nextInt((playerList.size()));
        godChooserPlayer = playerList.get(initialTurn);
        godChooserPlayer.setAsGodChooser();
        model.setInitialTurn(initialTurn);

        model.turnUpdate(model.getCurrentPlayer());

        selectedNicknames = new ArrayList<>();
        selectableColors = PrintableColor.getColorList();

        model.initialInfoUpdate(new HashMap<>());

    }


    /**
     * This method must be called when GodChoice Phase starts. Every player is notified in the proper way.
     */
    private void godChoosePhase() {
        model.nextGamePhase();
        model.gamePhaseUpdate(model.getCurrentGamePhase());
        model.godsUpdate(new ArrayList<>(), new HashMap<>());
    }

    /**
     * This method must be called when GamePreparation Phase starts. Every player is notified in the proper way.
     */
    private void gamePreparationPhase() {
        model.nextGamePhase();
        model.gamePhaseUpdate(model.getCurrentGamePhase());
        model.boardUpdate();
    }

    /**
     * This method must be called when the real match starts. Every player is notified in the proper way.
     */
    private void startMatch() {
        model.nextGamePhase();
        model.gamePhaseUpdate(model.getCurrentGamePhase());
        model.matchStartedUpdate();

        // check if the first currentPlayer can move
        checkLoseConditionsMove();
    }


    /**
     * This method handles a disconnection from one of the clients involved in the match. In particular, when a
     * player disconnects, he/she is removed; then, everyone must be notified and the match immediately ends.
     */
    public void onPlayerDisconnected(String playerID) {
        Player disconnectedPlayer = model.getPlayers().stream().filter((player) -> player.getPlayerID().equals(playerID)).findFirst().orElse(null);

        if (disconnectedPlayer == null) return;

        if (disconnectedPlayer.getWorkerFirst().getPosition() != null) {
            disconnectedPlayer.getWorkerFirst().getPosition().setWorker(null);
        }

        if (disconnectedPlayer.getWorkerSecond().getPosition() != null) {
            disconnectedPlayer.getWorkerSecond().getPosition().setWorker(null);
        }

        model.removePlayer(disconnectedPlayer);
        model.disconnectedPlayerUpdate(disconnectedPlayer);

        model.gamePhaseUpdate(GamePhase.MATCH_ENDED);
    }

    /**
     * This method handles a victory by one of the players involved in the match.
     */
    private void onMatchWon() {
        notify(this);
    }

}
