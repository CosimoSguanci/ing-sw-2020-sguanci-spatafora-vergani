package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.exceptions.WrongPlayerException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gods.GodStrategy;
import it.polimi.ingsw.observer.Observer;

import java.util.*;


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
public class Controller implements Observer<Command> {
    private final Model model; // todo handle attribute visibility and immutability
    private Player godChooserPlayer;
    private List<String> selectableGods;
    private List<String> selectedNicknames;
    private List<PrintableColour> selectableColors;
    private GamePhase currentGamePhase;
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
        return this.currentGamePhase;
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
            // todo handle
        } catch (WrongPlayerException e) {
            // todo handle
        }
    }

    void handleInitialInfoCommand(InitialInfoCommand initialInfoCommand) {


        if (!initialInfoCommand.getPlayer().equals(model.getCurrentPlayer())) {
            throw new WrongPlayerException();
        }


        String nickname = initialInfoCommand.nickname;

        if (selectedNicknames.contains(nickname)) {
            // exception
        }


        PrintableColour color = initialInfoCommand.color;

        if (!selectableColors.contains(color)) {
            // exception
        }

        model.getCurrentPlayer().setNickname(nickname);
        model.getCurrentPlayer().setColor(color);

        selectedNicknames.add(nickname); // todo additional checks on server about nickname, colors, etc
        selectableColors.remove(color);

        model.endTurn();


        if (!model.getCurrentPlayer().equals(godChooserPlayer)) { // here getCurrentPlayer() is who just chose the info
            model.initialInfoUpdate(model.getCurrentPlayer(), selectedNicknames, selectableColors);
        } else { // all the players chose the info


            HashMap<String, PrintableColour> initialInfo = new HashMap<>();

            model.getPlayers().forEach((player) -> {
                initialInfo.put(player.getNickname(), player.getColor());
            });

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

            model.getPlayers().forEach((player) -> {
                selectedGods.put(player.getNickname(), player.getGodStrategy().getGodInfo().get("name"));
            });

            model.selectedGodsUpdate(selectedGods);
            model.endTurn(); // TODO OK endTurn here?

            gamePreparation();

        }

    }


    void handleGamePreparationCommand(GamePreparationCommand gamePreparationCommand) {
        Player currentPlayer = model.getCurrentPlayer();
        if (!gamePreparationCommand.getPlayer().equals(currentPlayer)) {
            throw new WrongPlayerException();
        } else {

            if (currentPlayer.getGodStrategy().checkGamePreparation(currentPlayer.getWorkerFirst(), gamePreparationCommand.getWorkerFirstCell(), currentPlayer.getWorkerSecond(), gamePreparationCommand.getWorkerSecondCell())) {
                currentPlayer.getGodStrategy().executeGamePreparation(currentPlayer.getWorkerFirst(), gamePreparationCommand.getWorkerFirstCell(), currentPlayer.getWorkerSecond(), gamePreparationCommand.getWorkerSecondCell());
            }
            // todo else

            model.endTurn();

            if (currentPlayer.equals(godChooserPlayer)) { // Game Preparation Done
                startMatch();
            } else {
                //gamePreparation();

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
            // TODO When Player Lose?
            switch (playerCommand.commandType) {
                case MOVE:
                    if (checkAllMoveConstraints(playerCommand) && currentPlayer.getGodStrategy().checkMove(playerCommand.getWorker(), playerCommand.getCell())) {

                        currentPlayer.getGodStrategy().executeMove(playerCommand.getWorker(), playerCommand.getCell());

                        boolean hasWon = checkAllWinConstraints(playerCommand) && // TODO first checkWinCond?
                                currentPlayer.getGodStrategy().checkWinCondition(playerCommand.getWorker());


                        // if !hasWon check if the new currentPlayer can build
                        if (!currentPlayerCanBuild(playerCommand.getWorker())) {
                            // lose
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
                        boolean hasWon = checkAllWinConstraints(playerCommand) && currentPlayer.getGodStrategy().checkWinCondition(playerCommand.getWorker());

                    } else {
                        model.reportError(playerCommand.getPlayer(), playerCommand.commandType);
                    }
                    break;
                case END_TURN:

                    if (currentPlayer.getGodStrategy().checkEndTurn()) {
                        model.endTurn();

                        // check if the new currentPlayer can move
                        if (!currentPlayerCanMove()) {
                            // lose -> TODO Remove player from list and workers from board
                        }
                    } else {
                        model.reportError(playerCommand.getPlayer(), playerCommand.commandType);
                    }

                    break;
            }

            model.boardUpdate();
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

    private boolean currentPlayerCanMove() {
        return model.getBoard().canMove(model.getCurrentPlayer());
    }

    private boolean currentPlayerCanBuild(Worker movedWorker) {
        return model.getBoard().canBuild(movedWorker);
    }

    // Entry point from Server class
    public void initialPhase() {

        currentGamePhase = GamePhase.firstPhase();
        model.gamePhaseChangedUpdate(currentGamePhase);

        List<Player> playerList = model.getPlayers();
        int initialTurn = new Random().nextInt((playerList.size()));
        godChooserPlayer = playerList.get(initialTurn);
        godChooserPlayer.setAsGodChooser();
        model.setInitialTurn(initialTurn);

        model.turnUpdate(model.getCurrentPlayer());

        selectedNicknames = new ArrayList<>();
        selectableColors = PrintableColour.getColorList();
        model.initialInfoUpdate(godChooserPlayer, selectedNicknames, selectableColors);

    }

    private void godChoosePhase() {
        currentGamePhase = GamePhase.nextGamePhase(currentGamePhase);
        model.gamePhaseChangedUpdate(currentGamePhase);
        model.chooseGodsUpdate(godChooserPlayer, null);
    }

    private void gamePreparation() {
        /*try {
            nextPhase();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        currentGamePhase = GamePhase.nextGamePhase(currentGamePhase);
        model.gamePhaseChangedUpdate(currentGamePhase);
        model.boardUpdate();
        model.gamePreparationUpdate(model.getCurrentPlayer());
    }

    private void startMatch() {
        /*try {
            nextPhase();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        currentGamePhase = GamePhase.nextGamePhase(currentGamePhase);
        model.gamePhaseChangedUpdate(currentGamePhase);
        model.matchStartedUpdate();
    }

    /*private void nextPhase() throws Exception {
        if (this.currentGamePhase == GamePhase.CHOOSE_GODS) {
            this.currentGamePhase = GamePhase.GAME_PREPARATION;
        }
        else if(this.currentGamePhase == GamePhase.GAME_PREPARATION) {
            this.currentGamePhase = GamePhase.REAL_GAME;
        }
        else{
            throw new Exception();  //TODO decide what to do with this exception
        }
    }*/
}
