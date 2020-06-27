package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.listeners.BuildButtonListener;
import it.polimi.ingsw.view.gui.listeners.EndTurnButtonListener;
import it.polimi.ingsw.view.gui.listeners.MoveButtonListener;
import it.polimi.ingsw.view.gui.ui.JCellButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class creates and manages REAL_GAME phase layout.
 * In this class the layout in which players start the match is defined.
 * During real game phase, if a player is the current turn player he would
 * see one of his worker selected, the border of the cell is in yellow and
 * he can also select the other worker by clicking on it.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class RealGame extends Game implements ActionListener {
    public static final int dimension = 70;
    public static final int buttonWidth = 170;
    public static final int buttonHeight = 80;
    private final Gui gui;
    private final Controller controller;

    private JCellButton worker1Button;
    private JCellButton worker2Button;

    private JCellButton selectedWorkerButton;
    private JCellButton selectedCellButton;

    private CommandType lastCommand;

    private boolean newTurn;

    private boolean buttonsEnabled = true;

    /**
     * This is the creator of the class. At the moment of creation
     * gui attribute takes a reference to the static attribute Gui.
     * At the moment of creation a controller is associated to the class.
     */
    public RealGame() {
        this.gui = Gui.getInstance();
        this.controller = gui.getController();
    }

    /**
     * This public method is a simple setter of the CommandType attribute
     * of the class lastCommand in which the last command, after clicking on a button
     * @param lastCommand contains the type of the last command generated.
     */
    public void setLastCommand(CommandType lastCommand) {
        this.lastCommand = lastCommand;
    }

    /**
     * This method is a simple getter of the selectedWorkerButton, attribute of the class.
     * @return a reference to a button whose cell referred contains the selected worker.
     */
    public JCellButton getSelectedWorker() {
        return this.selectedWorkerButton;
    }

    /**
     * This method is a simple getter of the selectedCellButton, attribute of the class.
     * @return a reference to a button whose cell referred is the cell in which a player
     *          wants his worker to move or build in.
     */
    public JCellButton getSelectedCellButton() {
        return this.selectedCellButton;
    }

    /**
     * This method is a setter of the class. It handles the remove of the
     * content of the component and handles the draw on it.
     * @param board is a String JSon-format which contains information
     *              about all the cell involved in the match board.
     */
    @Override
    public void setBoard(String board) {
        this.boardString = board;

        if (this.boardString != null) {
            this.removeAll();
            this.draw();
            this.revalidate();

            if (this.lastCommand == CommandType.MOVE) {
                onMove();
            } else if (this.lastCommand == CommandType.BUILD) {
                onBuild();
            }
        }
    }

    /**
     * This method handles the real game component when turn changes.
     * When a turn changes, the cell of the worker1 of the current turn player,
     * is yellow-colored.
     */
    @Override
    public void changeTurn() {
        super.changeTurn();

        if (controller.getCurrentPlayerID().equals(controller.getClientPlayerID()) && this.worker1Button != null && this.worker2Button != null) {
            reinitialization();

            this.selectedWorkerButton = worker1Button;
            this.selectedWorkerButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2, true));
        }

        this.newTurn = true;

    }

    /**
     * This method manages the title, the subtitle and buttons of
     * the real game component. Every time a turn changes the component is
     * emptied and re-drew. During real game phase a right panel is enabled
     * and players will see buttons for their Gods commands.
     */
    @Override
    void draw() {

        drawCommonBoard();

        List<JCellButton> cells = twoDArrayToList(this.getBoard().getBoardCells());

        cells.forEach(cell -> {
            cell.addActionListener(this);
        });

        if (this.buttonsEnabled) {

            //need to set rightPanel's layout
            //rightPanel contains buttons to perform a move, a build and to end turn
            String classImagePath = "/images/RealGame/";
            ImageIcon moveIcon = new ImageIcon(getClass().getResource(classImagePath + "move.png"));
            moveIcon = new ImageIcon(moveIcon.getImage().getScaledInstance(dimension, -1, Image.SCALE_SMOOTH));

            JButton moveButton = new JButton("Move", moveIcon);

            moveButton.addActionListener(new MoveButtonListener(this));

            moveButton.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
            moveButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            moveButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));

            ImageIcon buildIcon = new ImageIcon(getClass().getResource(classImagePath + "build.png"));
            buildIcon = new ImageIcon(buildIcon.getImage().getScaledInstance(dimension, -1, Image.SCALE_SMOOTH));

            JButton buildButton = new JButton("Build", buildIcon);

            buildButton.addActionListener(new BuildButtonListener(this));

            buildButton.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
            buildButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            buildButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));

            ImageIcon endIcon = new ImageIcon(getClass().getResource(classImagePath + "end.png"));
            endIcon = new ImageIcon(endIcon.getImage().getScaledInstance(dimension, -1, Image.SCALE_SMOOTH));

            JButton endButton = new JButton("End turn", endIcon);

            endButton.addActionListener(new EndTurnButtonListener(this));

            endButton.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
            endButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            endButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));

            this.rightPanel.setLayout(new BoxLayout(this.rightPanel, BoxLayout.Y_AXIS));

            this.rightPanel.add(Box.createVerticalGlue());
            this.rightPanel.add(moveButton);
            this.rightPanel.add(Box.createVerticalGlue());
            this.rightPanel.add(buildButton);
            this.rightPanel.add(Box.createVerticalGlue());

            JComponent godComponent = gui.getGodGuiDrawer().draw(this);

            if (godComponent != null) {
                this.rightPanel.add(godComponent);
                this.rightPanel.add(Box.createVerticalGlue());
            }

            this.rightPanel.add(endButton);
            this.rightPanel.add(Box.createVerticalGlue());

        }

        //setting title and subtitle
        this.title.setText("MATCH");
        this.subtitle.setText("Play, enjoy and become the best!");


        if (controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) {
            if (this.newTurn) {
                highlightWorkerFirst();
                this.newTurn = false;
            }
        }

        this.revalidate();
    }

    /**
     * This private method handles the highlight of the cell in which
     * workerFirst of the current turn player is in.
     */
    private void highlightWorkerFirst() {
        List<JCellButton> cellBtns = Game.twoDArrayToList(getBoard().getBoardCells());

        cellBtns = cellBtns
                .stream()
                .filter(btn -> btn.getWorker() != null && btn.getWorker().player.getPlayerID().equals(this.controller.getClientPlayerID()))
                .collect(Collectors.toList());

        this.worker1Button = cellBtns.get(0).getWorker().workerType.equals(Command.WORKER_FIRST) ? cellBtns.get(0) : cellBtns.get(1);
        this.worker2Button = cellBtns.get(0).getWorker().workerType.equals(Command.WORKER_SECOND) ? cellBtns.get(0) : cellBtns.get(1);

        this.worker1Button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2, true));
        this.selectedWorkerButton = this.worker1Button;
    }

    /**
     * This method manages the activities on the cells of the board.
     * When the current player wants to change the worker through which he
     * plays the command, he just have to click on the worker he wants and its
     * border cell will be yellow-colored. Once decided the worker, player can select
     * another cell whose border will be blue-colored and it indicates the
     * cell in which the command will take place.
     * @param e contains a reference to the JCellbutton clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (this.controller.getCurrentPlayerID().equals(this.controller.getClientPlayerID())) {
            JCellButton button = (JCellButton) e.getSource();

            if (button.equals(this.worker1Button)) {
                this.worker1Button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2, true));
                this.worker2Button.setBorder(BorderFactory.createEmptyBorder());

                this.selectedWorkerButton = this.worker1Button;
            } else if (button.equals(worker2Button)) {
                this.worker2Button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2, true));
                this.worker1Button.setBorder(BorderFactory.createEmptyBorder());

                this.selectedWorkerButton = this.worker2Button;
            } else {

                if (this.selectedCellButton != null) {
                    this.selectedCellButton.setBorder(BorderFactory.createEmptyBorder());
                }

                this.selectedCellButton = button;
                this.selectedCellButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2, true));

            }
        }
    }

    /**
     * This private method is used when the turn changes.
     * In this case, a cell containing the workerFirst of the
     * current player will be yellow-colored and the player
     * can start his turn.
     */
    private void reinitialization() { // onturnchanged
        List<JCellButton> cellBtns = Game.twoDArrayToList(getBoard().getBoardCells());

        cellBtns = cellBtns
                .stream()
                .filter(btn -> btn.getWorker() != null && btn.getWorker().player.getPlayerID().equals(this.controller.getClientPlayerID()))
                .collect(Collectors.toList());

        if (cellBtns.size() > 0) {
            this.worker1Button = cellBtns.get(0).getWorker().workerType.equals(Command.WORKER_FIRST) ? cellBtns.get(0) : cellBtns.get(1);
            this.worker2Button = cellBtns.get(0).getWorker().workerType.equals(Command.WORKER_SECOND) ? cellBtns.get(0) : cellBtns.get(1);
        }

    }

    /**
     * This method handles borders of the cell in which a player move into.
     * When a player move from a cell to an adjacent one, the border of the latter
     * one will be yellow-colored and the former one lose his original border color.
     */
    public void onMove() {
        Worker movedWorker = this.getBoard().getBoardCells()[this.selectedCellButton.getRow()][this.selectedCellButton.getCol()].getWorker();
        reinitialization();
        if (movedWorker.workerType.equals(Command.WORKER_FIRST)) {
            this.selectedWorkerButton = worker1Button;
        } else {
            this.selectedWorkerButton = worker2Button;
        }

        this.selectedWorkerButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2, true));
    }

    /**
     * This method handles borders of the cell in which the builder worker
     * of the current player is in. Once the worker builds a level, border of
     * its relative cell still remain yellow-colored.
     */
    public void onBuild() {
        Worker movedWorker = this.getBoard().getBoardCells()[this.selectedWorkerButton.getRow()][this.selectedWorkerButton.getCol()].getWorker();
        reinitialization();
        if (movedWorker.workerType.equals(Command.WORKER_FIRST)) {
            this.selectedWorkerButton = worker1Button;
        } else {
            this.selectedWorkerButton = worker2Button;
        }

        this.selectedWorkerButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2, true));
    }

    /**
     * This method is simply used to disable the buttons that are on the real game component
     */
    public void disableButtons() {
        this.buttonsEnabled = false;
        this.removeAll();
        this.draw();
        this.revalidate();
    }
}
