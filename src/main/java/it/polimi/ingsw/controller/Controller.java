package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.exceptions.WrongPlayerException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gods.GodStrategy;
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
public class Controller implements Observer<Object> {
    private Model model;
    private Player godChooserPlayer;
    private List<String> selectableGods;
    private int initialTurn;

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
    public void update(Object message) {
        try {

            if(message instanceof PlayerCommand) {
                PlayerCommand playerCommand = (PlayerCommand) message;

                for(Player player : model.getPlayers()) {
                    if(player.ID.equals(playerCommand.playerID)) {
                        playerCommand.setPlayer(player);
                        break;
                    }
                }

                Cell correctCell = playerCommand.getCell() != null ? model.getBoard().getCell(playerCommand.getCell().getRowIdentifier(), playerCommand.getCell().getColIdentifier()) : null;
                playerCommand.setCell(correctCell);

                Worker worker = playerCommand.workerID != null ? playerCommand.workerID.equals(PlayerCommand.WORKER_FIRST) ? playerCommand.getPlayer().getWorkerFirst() : playerCommand.getPlayer().getWorkerSecond() : null;
                playerCommand.setWorker(worker);

                handlePlayerCommand(playerCommand);
            }
            else if(message instanceof GodChoiceCommand) {
                GodChoiceCommand godChoiceCommand = (GodChoiceCommand) message;
                handleGodChoiceCommand(godChoiceCommand);

            }


        } catch (InvalidPlayerNumberException e) {  //must be understood what happens when this exception occurs
            //this exception occurs when a player is giving a command during a turn "owned" by another player
            e.printStackTrace();
        }
    }

    private void handleGodChoiceCommand(GodChoiceCommand godChoiceCommand) {
       List<String> chosenGods = godChoiceCommand.getChosenGods();
       boolean isGodChooser = godChoiceCommand.isGodChooser();

       if(isGodChooser) {
           model.setInitialTurn(initialTurn);
           godChooserPlayer = model.getCurrentPlayer();
           this.selectableGods = chosenGods;

       } else {
            String god = chosenGods.get(0);
            //model.getCurrentPlayer().setGod(new God("", "", "", GodStrategy.instantiateGod(god)));
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
           model.getCurrentPlayer().setGod(new God("", "", "", GodStrategy.instantiateGod(god)));
           HashMap<String, String> selectedGods = new HashMap<>();

           model.getPlayers().forEach((player) -> {
               //selectedGods.put(player.nickname, player.getGod().godStrategy.getGodInfo().get("name")); // TODO REMOVE GOD
               selectedGods.put(player.nickname, player.getGodStrategy().getGodInfo().get("name"));
           });

           model.selectedGodsUpdate(selectedGods);
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
            // TODO When Game Prep?
            switch (playerCommand.commandType) {
                case MOVE:
                    if (checkAllMoveConstraints(playerCommand) &&
                            //currentPlayer.getGod().godStrategy.checkMove(playerCommand.getWorker(), playerCommand.getCell())) {
                            currentPlayer.getGodStrategy().checkMove(playerCommand.getWorker(), playerCommand.getCell())) {

                        //currentPlayer.getGod().godStrategy.executeMove(playerCommand.getWorker(), playerCommand.getCell());
                        currentPlayer.getGodStrategy().executeMove(playerCommand.getWorker(), playerCommand.getCell());

                        boolean hasWon = checkAllWinConstraints(playerCommand) && // TODO first checkWinCond?
                                //currentPlayer.getGod().godStrategy.checkWinCondition(playerCommand.getWorker());
                                currentPlayer.getGodStrategy().checkWinCondition(playerCommand.getWorker());
                    } else {
                        model.reportError(playerCommand.getPlayer(), playerCommand.commandType);
                    }
                    break;
                case BUILD:
                    //if (checkAllBuildConstraints(playerCommand) && currentPlayer.getGod().godStrategy.checkBuild(playerCommand.getWorker(), playerCommand.getCell(), playerCommand.cellBlockType)) {
                    if (checkAllBuildConstraints(playerCommand) && currentPlayer.getGodStrategy().checkBuild(playerCommand.getWorker(), playerCommand.getCell(), playerCommand.cellBlockType)) {
                        //currentPlayer.getGod().godStrategy.executeBuild(playerCommand.getWorker(), playerCommand.getCell(), playerCommand.cellBlockType);
                        currentPlayer.getGodStrategy().executeBuild(playerCommand.getWorker(), playerCommand.getCell(), playerCommand.cellBlockType);

                        /*
                         *  Necessary to cover the possibility that a Gods could have a Win Conditions that triggers when it builds a block under certain conditions.
                         *  Example: "If XXX built a Dome and it's adjacent to another Worker, it wins"
                         */
                        //boolean hasWon = checkAllWinConstraints(playerCommand) && currentPlayer.getGod().godStrategy.checkWinCondition(playerCommand.getWorker());
                        boolean hasWon = checkAllWinConstraints(playerCommand) && currentPlayer.getGodStrategy().checkWinCondition(playerCommand.getWorker());

                    } else {
                        model.reportError(playerCommand.getPlayer(), playerCommand.commandType);
                    }
                    break;
                case END_TURN:
                    model.endTurn();
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
            //if (!p.equals(playerCommand.getPlayer()) && !p.getGod().godStrategy.checkWinConstraints(playerCommand.getWorker(), playerCommand.getCell())) {
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
            //if (!p.equals(playerCommand.getPlayer()) && !p.getGod().godStrategy.checkMoveConstraints(playerCommand.getWorker(), playerCommand.getCell())) {
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
            //if (!p.equals(playerCommand.getPlayer()) && !p.getGod().godStrategy.checkBuildConstraints(playerCommand.getWorker(), playerCommand.getCell(), playerCommand.cellBlockType)) {
            if (!p.equals(playerCommand.getPlayer()) && !p.getGodStrategy().checkBuildConstraints(playerCommand.getWorker(), playerCommand.getCell(), playerCommand.cellBlockType)) {
                return false;
            }
        }

        return true;
    }

    public void startMatch() {
        List<Player> playerList = model.getPlayers();
        initialTurn = new Random().nextInt((playerList.size()));
        godChooserPlayer = playerList.get(initialTurn);
        godChooserPlayer.setAsGodChooser();
        model.chooseGodsUpdate(godChooserPlayer, null);
    }
}
