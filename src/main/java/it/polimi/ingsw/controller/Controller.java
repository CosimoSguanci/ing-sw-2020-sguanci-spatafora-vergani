package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gods.GodStrategy;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * This class represents the Controller in MVC design pattern. The whole
 * application, in fact, is based on MVC pattern, and in particular
 * Controller class is View's observer. The "dialogue" between Controller
 * and View is possible through PlayerCommand class: this class represent a
 * move/build that a player wants to do during the game. This is the reason
 * for Controller class to implement Observer(PlayerCommand), since (as
 * explained) View notifies Controller using PlayerCommand as update message.
 * Then, following MVC pattern, Controller invokes Model's methods to
 * modify the model itself.
 *
 * @author Andrea Mario Vergani
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 */
public class Controller extends Observable<Model> implements Observer<Command> { // TODO Refactor Controller in submodules ?
    private final Model model; // todo handle attribute visibility and immutability
    private Player godChooserPlayer;
    private List<String> selectableGods;
    private List<String> selectedNicknames;
    private List<PrintableColor> selectableColors;
    private final CommandHandler commandHandler;


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

    GamePhase getCurrentGamePhase() {
        return this.model.getCurrentGamePhase();
    }

    List<Player> getPlayers() {
        return this.model.getPlayers();
    }

    Board getBoard() {
        return this.model.getBoard();
    }


    /**
     * This method is an overriding method of "update" in Observer interface.
     * Its task is to handle player command coming from View.
     *
     * @param command player move/build from View
     */
    @Override
    public void update(Command command) {
        try {
            command.handleCommand(this.commandHandler);
        } catch (WrongGamePhaseException e) {
            model.reportError(command.getPlayer(), command.commandType);
        } catch (WrongPlayerException e) {  //TODO -> modify reportError
            model.reportError(command.getPlayer(), command.commandType);
        } catch (NicknameAlreadyTakenException e) {
            model.reportError(command.getPlayer(), command.commandType);
        } catch (InvalidColorException e) {
            model.reportError(command.getPlayer(), command.commandType);
        }
    }

    void handleInitialInfoCommand(InitialInfoCommand initialInfoCommand) {


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

        // here getCurrentPlayer() is who just chose the info
        if (!model.getCurrentPlayer().equals(godChooserPlayer)) {
            model.initialInfoUpdate(model.getCurrentPlayer(), selectedNicknames, selectableColors);
        } else {
            // all the players chose the info

            HashMap<String, PrintableColor> initialInfo = new HashMap<>();

            model.getPlayers().forEach((player) -> initialInfo.put(player.getNickname(), player.getColor()));

            model.selectedInitialInfoUpdate(initialInfo);

            godChoosePhase();

        }
    }

    void handleGodChoiceCommand(GodChoiceCommand godChoiceCommand) {
        List<String> chosenGods = godChoiceCommand.getChosenGods();
        boolean isGodChooser = godChoiceCommand.isGodChooser();

        if (!godChoiceCommand.getPlayer().equals(model.getCurrentPlayer())) {
            throw new WrongPlayerException();
        }

        if (isGodChooser) {
            this.selectableGods = chosenGods;

        } else {
            String god = chosenGods.get(0);
            model.getCurrentPlayer().setGodStrategy(GodStrategy.instantiateGod(god));
            this.selectableGods.remove(god);
        }

        model.endTurn();

        if (!model.getCurrentPlayer().equals(godChooserPlayer)) {
            model.chooseGodsUpdate(model.getCurrentPlayer(), selectableGods);
        } else {
            // The God Chooser picks the last gods
            String god = selectableGods.get(0);
            model.getCurrentPlayer().setGodStrategy(GodStrategy.instantiateGod(god));
            HashMap<String, String> selectedGods = new HashMap<>();

            model.getPlayers().forEach((player) -> selectedGods.put(player.getNickname(), player.getGodStrategy().NAME));

            model.selectedGodsUpdate(selectedGods);
            model.endTurn();

            gamePreparationPhase();

        }

    }


    void handleGamePreparationCommand(GamePreparationCommand gamePreparationCommand) {
        Player currentPlayer = model.getCurrentPlayer();
        if (!gamePreparationCommand.getPlayer().equals(currentPlayer)) {
            throw new WrongPlayerException();
        } else {

            if (checkAllGamePreparationConstraints(gamePreparationCommand) &&
                    currentPlayer.getGodStrategy().checkGamePreparation(
                            currentPlayer.getWorkerFirst(),
                            gamePreparationCommand.getWorkerFirstCell(),
                            currentPlayer.getWorkerSecond(),
                            gamePreparationCommand.getWorkerSecondCell())
            ) {
                currentPlayer.getGodStrategy().executeGamePreparation(currentPlayer.getWorkerFirst(), gamePreparationCommand.getWorkerFirstCell(), currentPlayer.getWorkerSecond(), gamePreparationCommand.getWorkerSecondCell());
            }
            else {
                model.reportError(currentPlayer, CommandType.PLACE);
                return;
            }


            model.endTurn();

            if (currentPlayer.equals(godChooserPlayer)) { // Game Preparation Done
                startMatch();
            } else {
                model.boardUpdate();
                model.gamePreparationUpdate(model.getCurrentPlayer());
            }

        }
    }

    /**
     * This private method simply extends update's task. In fact, to handle
     * a command is necessary to: verify that the player who required it is
     * game's current player (otherwise, there is no need to call the Model,
     * since its state will not modify); call the selected command from the
     * player, as an invocation of one of model's methods.
     *
     * @param playerCommand player command from View
     * @throws InvalidPlayerNumberException when command's player is not current player
     */
    void handlePlayerCommand(PlayerCommand playerCommand) throws WrongPlayerException {
        Player currentPlayer = model.getCurrentPlayer();
        if (!playerCommand.getPlayer().equals(currentPlayer)) {
            throw new WrongPlayerException();
        } else {
            switch (playerCommand.commandType) {
                case MOVE:
                    if (checkAllMoveConstraints(playerCommand) && currentPlayer.getGodStrategy().checkMove(playerCommand.getWorker(), playerCommand.getCell())) {

                        currentPlayer.getGodStrategy().executeMove(playerCommand.getWorker(), playerCommand.getCell());

                        boolean hasWon = currentPlayer.getGodStrategy().checkWinCondition(playerCommand.getWorker()) && checkAllWinConstraints(playerCommand);

                        model.boardUpdate(playerCommand);

                        if(hasWon) {
                            model.nextGamePhase();
                            model.gamePhaseChangedUpdate(model.getCurrentGamePhase());
                            model.winUpdate(currentPlayer);

                            return; // end match
                        }
                        else {
                            // if !hasWon check if the new currentPlayer can build

                            checkLoseConditionsBuild(playerCommand);
                        }

                    } else {
                        model.reportError(playerCommand.getPlayer(), playerCommand.commandType);
                    }
                    break;
                case BUILD:
                    if (checkAllBuildConstraints(playerCommand) && currentPlayer.getGodStrategy().checkBuild(playerCommand.getWorker(), playerCommand.getCell(), playerCommand.cellBlockType)) {
                        currentPlayer.getGodStrategy().executeBuild(playerCommand.getWorker(), playerCommand.getCell(), playerCommand.cellBlockType);

                        /*
                         *  Necessary to cover the possibility that a Gods could have a Win Conditions that triggers when it builds a block under certain conditions.
                         *  Example: "If XXX built a Dome and it's adjacent to another Worker, it wins"
                         */
                        boolean hasWon = currentPlayer.getGodStrategy().checkWinCondition(playerCommand.getWorker()) && checkAllWinConstraints(playerCommand);

                        model.boardUpdate(playerCommand);

                        if(hasWon) {
                            model.nextGamePhase();
                            model.gamePhaseChangedUpdate(model.getCurrentGamePhase());
                            model.winUpdate(currentPlayer);

                            return; // end match
                        }

                    } else {
                        model.reportError(playerCommand.getPlayer(), playerCommand.commandType);
                    }
                    break;
                case END_TURN:

                    if (checkAllEndTurnConstraints(playerCommand) && currentPlayer.getGodStrategy().checkEndTurn()) {

                        currentPlayer.getGodStrategy().endPlayerTurn(currentPlayer);

                        model.endTurn();

                        model.getCurrentPlayer().getGodStrategy().onTurnStarted(model.getCurrentPlayer()); // onTurnStart

                        model.boardUpdate();

                        // check if the new currentPlayer can move
                        checkLoseConditionsMove();

                    } else {
                        model.reportError(playerCommand.getPlayer(), playerCommand.commandType);
                    }

                    break;
            }

        }
    }

    private void checkLoseConditionsMove() {
        if (!model.getCurrentPlayer().getGodStrategy().canMove(model.getBoard(), model.getCurrentPlayer())) {
            model.onPlayerLose(model.getCurrentPlayer());

            if(model.getPlayers().size() != 1) {
                model.boardUpdate();
            }
        } else {
            if(!checkCanMoveOtherGodsConstraints(model.getCurrentPlayer())) {
                model.onPlayerLose(model.getCurrentPlayer());

                if(model.getPlayers().size() != 1) {
                    model.boardUpdate();
                }
            }
        }
    }

    private void checkLoseConditionsBuild(PlayerCommand playerCommand) {
        if (!model.getCurrentPlayer().getGodStrategy().canBuild(model.getBoard(), playerCommand.getWorker())) {
            model.onPlayerLose(model.getCurrentPlayer());

            if(model.getPlayers().size() != 1) { // 2 players remaining
                model.boardUpdate();
            }
        } else {
            if(!checkCanBuildOtherGodsConstraints(model.getCurrentPlayer())) {
                model.onPlayerLose(model.getCurrentPlayer());

                if(model.getPlayers().size() != 1) {
                    model.boardUpdate();
                }
            }
        }
    }


    /**
     * This methods checks all Players God's Win Constraints to check if there is a power which
     * prevents the Player which executed the PlayerCommand given to win.
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

    private boolean checkAllGamePreparationConstraints(GamePreparationCommand gamePreparationCommand) {

        for (Player p : model.getPlayers()) {
            if (!p.equals(gamePreparationCommand.getPlayer()) &&

                    !p.getGodStrategy().checkGamePreparationConstraints(
                            gamePreparationCommand.getPlayer().getWorkerFirst(),
                            gamePreparationCommand.getWorkerFirstCell(),
                            gamePreparationCommand.getPlayer().getWorkerSecond(),
                            gamePreparationCommand.getWorkerSecondCell())

            ) {
                return false;
            }
        }

        return true;
    }

    /**
     * This methods checks all Players God's Move Constraints to check if there is a power which
     * prevents the Player which executed the PlayerCommand given to move.
     *
     * @param playerCommand player command from View
     * @return true if move is permitted, false otherwise.
     */
    private boolean checkAllMoveConstraints(PlayerCommand playerCommand) {

        for (Player p : model.getPlayers()) {
            if (!p.equals(playerCommand.getPlayer()) && !p.getGodStrategy().checkMoveConstraints(playerCommand.getWorker(), playerCommand.getCell())) {
                return false;
            }
        }

        return true;
    }

    /**
     * This methods checks all Players God's Build Constraints to check if there is a power which
     * prevents the Player which executed the PlayerCommand given to build.
     *
     * @param playerCommand player command from View
     * @return true if build is permitted, false otherwise.
     */
    private boolean checkAllBuildConstraints(PlayerCommand playerCommand) {

        for (Player p : model.getPlayers()) {
            if (!p.equals(playerCommand.getPlayer()) && !p.getGodStrategy().checkBuildConstraints(playerCommand.getWorker(), playerCommand.getCell(), playerCommand.cellBlockType)) {
                return false;
            }
        }

        return true;
    }

    private boolean checkAllEndTurnConstraints(PlayerCommand playerCommand) {

        for (Player p : model.getPlayers()) {
            if (!p.equals(playerCommand.getPlayer()) && !p.getGodStrategy().checkEndTurnConstraints(playerCommand.getPlayer())) {
                return false;
            }
        }

        return true;
    }

    private boolean checkCanMoveOtherGodsConstraints(Player player) { // todo necessary or reuse checkAllMoveConstraints
        List<Cell> availableMoveCellsWorkerFirst = model.getBoard().getAvailableMoveCells(player.getWorkerFirst());
        List<Cell> availableMoveCellsWorkerSecond = model.getBoard().getAvailableMoveCells(player.getWorkerSecond());


        for(Cell moveCell : availableMoveCellsWorkerFirst) {
            boolean feasible = true;
            for(Player p : model.getPlayers()) {
                if(!p.equals(player)) {
                    if(!p.getGodStrategy().checkMoveConstraints(player.getWorkerFirst(), moveCell)) {
                        feasible = false;
                        break;
                    }
                }
            }

            if(feasible) return true;
        }

        for(Cell moveCell : availableMoveCellsWorkerSecond) {
            boolean feasible = true;
            for(Player p : model.getPlayers()) {
                if(!p.equals(player)) {
                    if(!p.getGodStrategy().checkMoveConstraints(player.getWorkerSecond(), moveCell)) {
                        feasible = false;
                        break;
                    }
                }
            }

            if(feasible) return true;
        }


        return false;
    }

    private boolean checkCanBuildOtherGodsConstraints(Player player) {
        List<Cell> availableBuildCellsWorkerFirst = model.getBoard().getAvailableBuildCells(player.getWorkerFirst());
        List<Cell> availableBuildCellsWorkerSecond = model.getBoard().getAvailableBuildCells(player.getWorkerSecond());


        for(Cell buildCell : availableBuildCellsWorkerFirst) {
            boolean feasible = true;
            for(Player p : model.getPlayers()) {
                if(!p.equals(player)) {
                    if(!p.getGodStrategy().checkBuildConstraints(player.getWorkerFirst(), buildCell, null)) {
                        feasible = false;
                        break;
                    }
                }
            }

            if(feasible) return true;
        }

        for(Cell buildCell : availableBuildCellsWorkerSecond) {
            boolean feasible = true;
            for(Player p : model.getPlayers()) {
                if(!p.equals(player)) {
                    if(!p.getGodStrategy().checkBuildConstraints(player.getWorkerSecond(), buildCell, null)) {
                        feasible = false;
                        break;
                    }
                }
            }

            if(feasible) return true;
        }


        return false;
    }


    // Entry point from Server class
    public void initialPhase() {
        model.gamePhaseChangedUpdate(model.getCurrentGamePhase());

        List<Player> playerList = model.getPlayers();
        int initialTurn = new Random().nextInt((playerList.size()));
        godChooserPlayer = playerList.get(initialTurn);
        godChooserPlayer.setAsGodChooser();
        model.setInitialTurn(initialTurn);

        model.turnUpdate(model.getCurrentPlayer());

        selectedNicknames = new ArrayList<>();
        selectableColors = PrintableColor.getColorList();
        model.initialInfoUpdate(godChooserPlayer, selectedNicknames, selectableColors);

    }

    private void godChoosePhase() {
        model.nextGamePhase();
        model.gamePhaseChangedUpdate(model.getCurrentGamePhase());
        model.chooseGodsUpdate(godChooserPlayer, null);
    }

    private void gamePreparationPhase() {
        model.nextGamePhase();
        model.gamePhaseChangedUpdate(model.getCurrentGamePhase());
        model.boardUpdate();
        model.gamePreparationUpdate(model.getCurrentPlayer());
    }

    private void startMatch() {
        model.nextGamePhase();
        model.gamePhaseChangedUpdate(model.getCurrentGamePhase());
        model.matchStartedUpdate();

        // check if the first currentPlayer can move
        checkLoseConditionsMove();
    }

    public void onPlayerDisconnected(String playerID) {
        Player disconnectedPlayer = model.getPlayers().stream().filter((player) -> player.ID.equals(playerID)).findFirst().orElse(null);

        if (disconnectedPlayer == null) return;

        if (disconnectedPlayer.getWorkerFirst().getPosition() != null) {
            disconnectedPlayer.getWorkerFirst().getPosition().setWorker(null);
        }

        if (disconnectedPlayer.getWorkerSecond().getPosition() != null) {
            disconnectedPlayer.getWorkerSecond().getPosition().setWorker(null);
        }

        model.removePlayer(disconnectedPlayer);
        model.disconnectedPlayerUpdate(disconnectedPlayer);

        if (model.getPlayers().size() > 1 && model.getCurrentPlayer().equals(disconnectedPlayer)) {
            model.turnUpdate(model.getCurrentPlayer());
        }

    }

}
