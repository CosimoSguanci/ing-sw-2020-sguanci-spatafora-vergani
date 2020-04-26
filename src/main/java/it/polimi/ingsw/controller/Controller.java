package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.exceptions.WrongPlayerException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gods.GodStrategy;
import it.polimi.ingsw.observer.Observer;

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
public class Controller implements Observer<Object> {
    private Model model;
    private Player godChooserPlayer;
    private List<String> selectableGods;
    private int initialTurn;
    private GamePhase currentGamePhase;

    /**
     * The constructor creates an instance of Controller class. This class
     * needs to reference to the general Model of the game, so that changes
     * and operations in it are possible.
     *
     * @param model class Model of MVC pattern
     */
    public Controller(Model model) {
        this.model = model;
    }


    /**
     * This method is an overriding method of "update" in Observer interface.
     * Its task is to handle player command coming from View.
     *
     * @param message player move/build from View
     */
    @Override
    public void update(Object message) {  //TODO handle exceptions
        try {

            if(message instanceof PlayerCommand) {
                if(currentGamePhase == GamePhase.REAL_GAME) {
                    PlayerCommand playerCommand = (PlayerCommand) message;

                    for (Player player : model.getPlayers()) {
                        if (player.ID.equals(playerCommand.playerID)) {
                            playerCommand.setPlayer(player);
                            break;
                        }
                    }

                    Cell correctCell = playerCommand.getCell() != null ? model.getBoard().getCell(playerCommand.getCell().getRowIdentifier(), playerCommand.getCell().getColIdentifier()) : null;
                    playerCommand.setCell(correctCell);

                    Worker worker = playerCommand.workerID != null ? playerCommand.workerID.equals(PlayerCommand.WORKER_FIRST) ? playerCommand.getPlayer().getWorkerFirst() : playerCommand.getPlayer().getWorkerSecond() : null;
                    playerCommand.setWorker(worker);

                    handlePlayerCommand(playerCommand);
                } else{
                    throw new WrongGamePhaseException();
                }
            }
            else if(message instanceof GodChoiceCommand) {
                if(currentGamePhase == GamePhase.CHOOSE_GODS) {
                    GodChoiceCommand godChoiceCommand = (GodChoiceCommand) message;
                    handleGodChoiceCommand(godChoiceCommand);
                } else{
                    throw new WrongGamePhaseException();
                }
            }
            else if(message instanceof GamePreparationCommand) {
                if(currentGamePhase == GamePhase.GAME_PREPARATION) {
                    GamePreparationCommand gamePreparationCommand = (GamePreparationCommand) message;

                    for (Player player : model.getPlayers()) {
                        if (player.ID.equals(gamePreparationCommand.getPlayerID())) {
                            gamePreparationCommand.setPlayer(player);
                            break;
                        }
                    }

                    Cell workerFirstCell = model.getBoard().getCell(gamePreparationCommand.workerFirstRow, gamePreparationCommand.workerFirstCol);
                    Cell workerSecondCell = model.getBoard().getCell(gamePreparationCommand.workerSecondRow, gamePreparationCommand.workerSecondCol);
                    gamePreparationCommand.setWorkerFirstCell(workerFirstCell);
                    gamePreparationCommand.setWorkerSecondCell(workerSecondCell);

                    handleGamePreparationCommand(gamePreparationCommand);
                } else{
                    throw new WrongGamePhaseException();
                }

            }


        } catch (InvalidPlayerNumberException e) {  //must be understood what happens when this exception occurs
            //this exception occurs when a player is giving a command during a turn "owned" by another player
            e.printStackTrace();
        }
    }

    private void handleGodChoiceCommand(GodChoiceCommand godChoiceCommand) {  // TODO Handle Exceptions
       List<String> chosenGods = godChoiceCommand.getChosenGods();
       boolean isGodChooser = godChoiceCommand.isGodChooser();
       if(!godChoiceCommand.getPlayerID().equals(model.getCurrentPlayer().ID)) {  //received command from a player not owning the turn
           throw new WrongPlayerException();
       }

       if(isGodChooser) {
           model.setInitialTurn(initialTurn);
           //godChooserPlayer = model.getCurrentPlayer();
           this.selectableGods = chosenGods;

       } else {
            String god = chosenGods.get(0);
            model.getCurrentPlayer().setGodStrategy(GodStrategy.instantiateGod(god));
            this.selectableGods.remove(god);
       }

        model.nextTurn();

       if(!model.getCurrentPlayer().equals(godChooserPlayer)) {
            model.chooseGodsUpdate(model.getCurrentPlayer(), selectableGods);
       }
       else {
           String god = null;
           for(String available : selectableGods) {
               if(available != null) {
                   god = available;
               }
           }
           model.getCurrentPlayer().setGodStrategy(GodStrategy.instantiateGod(god));
           HashMap<String, String> selectedGods = new HashMap<>();

           model.getPlayers().forEach((player) -> {
               selectedGods.put(player.nickname, player.getGodStrategy().getGodInfo().get("name"));
           });

           model.selectedGodsUpdate(selectedGods);
           model.nextTurn();

           gamePreparation();

       }

    }

    private void handleGamePreparationCommand(GamePreparationCommand gamePreparationCommand) { // TODO Handle Exceptions, different catch etc...
        Player currentPlayer = model.getCurrentPlayer();
        if (!gamePreparationCommand.getPlayer().equals(currentPlayer)) {
            throw new WrongPlayerException();
        } else {

            if(currentPlayer.getGodStrategy().checkGamePreparation(currentPlayer.getWorkerFirst(), gamePreparationCommand.getWorkerFirstCell(), currentPlayer.getWorkerSecond(), gamePreparationCommand.getWorkerSecondCell())) {
                currentPlayer.getGodStrategy().executeGamePreparation(currentPlayer.getWorkerFirst(), gamePreparationCommand.getWorkerFirstCell(), currentPlayer.getWorkerSecond(), gamePreparationCommand.getWorkerSecondCell());
            }

            if(currentPlayer.equals(godChooserPlayer)) { // Giro di game preparation fatto
                //model.boardUpdate();
                model.nextTurn();
                startActualMatch();
            } else {
                model.nextTurn();
                gamePreparation();
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
    private void handlePlayerCommand(PlayerCommand playerCommand) throws WrongPlayerException {
        Player currentPlayer = model.getCurrentPlayer();
        if (!playerCommand.getPlayer().equals(currentPlayer)) {
            throw new WrongPlayerException();
        } else {
            // TODO When Player Lose?
            // TODO When Game Prep? DONE
            switch (playerCommand.commandType) {
                case MOVE:
                    if (checkAllMoveConstraints(playerCommand) && currentPlayer.getGodStrategy().checkMove(playerCommand.getWorker(), playerCommand.getCell())) {

                        currentPlayer.getGodStrategy().executeMove(playerCommand.getWorker(), playerCommand.getCell());

                        boolean hasWon = checkAllWinConstraints(playerCommand) && // TODO first checkWinCond?
                                currentPlayer.getGodStrategy().checkWinCondition(playerCommand.getWorker());
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

                    if(currentPlayer.getGodStrategy().checkEndTurn()) {
                        model.endTurn();
                    } else {
                        model.reportError(playerCommand.getPlayer(), playerCommand.commandType);
                    }

                    break;
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

    public void startMatch() {
        List<Player> playerList = model.getPlayers();
        initialTurn = new Random().nextInt((playerList.size()));
        currentGamePhase = GamePhase.CHOOSE_GODS;
        godChooserPlayer = playerList.get(initialTurn);
        godChooserPlayer.setAsGodChooser();
        model.chooseGodsUpdate(godChooserPlayer, null);
    }

    private void gamePreparation() {
        currentGamePhase = GamePhase.GAME_PREPARATION;
        model.boardUpdate();
        model.gamePreparationUpdate(model.getCurrentPlayer());
    }

    private void startActualMatch() {
        currentGamePhase = GamePhase.REAL_GAME;
        model.matchStartedUpdate();
    }
}
