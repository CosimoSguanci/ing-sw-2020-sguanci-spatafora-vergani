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

public class RealGame extends Game implements ActionListener {
    private String classImagePath = "src/main/resources/images/RealGame/";
    private int dimension = 70;
    private int buttonWidth = 170;
    private int buttonHeight = 80;

    private Gui gui;
    private Controller controller;

    private JCellButton worker1Button;
    private JCellButton worker2Button;

    private JCellButton selectedWorkerButton;
    private JCellButton selectedCellButton;

    private CommandType lastCommand;

    private boolean newTurn;


    private boolean buttonsEnabled = true;

    public void setLastCommand(CommandType lastCommand) {
        this.lastCommand = lastCommand;
    }

    public RealGame() {
        this.gui = Gui.getInstance();
        this.controller = gui.getController();
    }

    public JCellButton getSelectedWorker() {
        return this.selectedWorkerButton;
    }

    public JCellButton getSelectedCellButton() {
        return this.selectedCellButton;
    }


    @Override
    public void setBoard(String board) {
        this.boardString = board;

        if(this.boardString != null) {
            this.removeAll();
            this.draw();
            this.revalidate();

            if(this.lastCommand == CommandType.MOVE) {
                onMove();
            }
            else if(this.lastCommand == CommandType.BUILD) {
                onBuild();
            }
        }
    }

    @Override
    public void changeTurn() {
        super.changeTurn();

        if(controller.getCurrentPlayerID().equals(controller.getClientPlayerID()) && this.worker1Button != null && this.worker2Button != null) {
            reinitialization();

            this.selectedWorkerButton = worker1Button;
            this.selectedWorkerButton.setBorder( BorderFactory.createLineBorder(Color.YELLOW, 2, true));
        }

        this.newTurn = true;

    }

    @Override
    void draw() {

        drawCommonBoard();

        List<JCellButton> cells = twoDArrayToList(this.getBoard().getBoardCells());

        cells.forEach(cell -> {
            cell.addActionListener(this);
        });

        if(this.buttonsEnabled) {

            //need to set rightPanel's layout
            //rightPanel contains buttons to perform a move, a build and to end turn
            ImageIcon moveIcon = new ImageIcon(this.classImagePath + "move.png");
            moveIcon = new ImageIcon(moveIcon.getImage().getScaledInstance(this.dimension, -1, Image.SCALE_SMOOTH));

            JButton moveButton = new JButton("Move", moveIcon);

            moveButton.addActionListener(new MoveButtonListener(this));

            moveButton.setMinimumSize(new Dimension(this.buttonWidth, this.buttonHeight));
            moveButton.setPreferredSize(new Dimension(this.buttonWidth, this.buttonHeight));
            moveButton.setMaximumSize(new Dimension(this.buttonWidth, this.buttonHeight));

            ImageIcon buildIcon = new ImageIcon(this.classImagePath + "build.png");
            buildIcon = new ImageIcon(buildIcon.getImage().getScaledInstance(this.dimension, -1, Image.SCALE_SMOOTH));

            JButton buildButton = new JButton("Build", buildIcon);

            buildButton.addActionListener(new BuildButtonListener(this));

            buildButton.setMinimumSize(new Dimension(this.buttonWidth, this.buttonHeight));
            buildButton.setPreferredSize(new Dimension(this.buttonWidth, this.buttonHeight));
            buildButton.setMaximumSize(new Dimension(this.buttonWidth, this.buttonHeight));

            ImageIcon endIcon = new ImageIcon(this.classImagePath + "end.png");
            endIcon = new ImageIcon(endIcon.getImage().getScaledInstance(this.dimension, -1, Image.SCALE_SMOOTH));

            JButton endButton = new JButton("End turn", endIcon);

            endButton.addActionListener(new EndTurnButtonListener(this));

            endButton.setMinimumSize(new Dimension(this.buttonWidth, this.buttonHeight));
            endButton.setPreferredSize(new Dimension(this.buttonWidth, this.buttonHeight));
            endButton.setMaximumSize(new Dimension(this.buttonWidth, this.buttonHeight));

            this.rightPanel.setLayout(new BoxLayout(this.rightPanel, BoxLayout.Y_AXIS));

            this.rightPanel.add(Box.createVerticalGlue());
            this.rightPanel.add(moveButton);
            this.rightPanel.add(Box.createVerticalGlue());
            this.rightPanel.add(buildButton);
            this.rightPanel.add(Box.createVerticalGlue());
            this.rightPanel.add(endButton);
            this.rightPanel.add(Box.createVerticalGlue());

        }

        //setting title and subtitle
        this.title.setText("MATCH");
        this.subtitle.setText("Play, enjoy and become the best!");


        if(controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) {
            if(this.newTurn) {
                highlightWorkerFirst();
                this.newTurn = false;
            }
        }

        this.revalidate();
    }

    private void highlightWorkerFirst() {
        List<JCellButton> cellBtns = Game.twoDArrayToList(getBoard().getBoardCells());

        cellBtns = cellBtns
                .stream()
                .filter(btn -> btn.getWorker() != null && btn.getWorker().player.getPlayerID().equals(this.controller.getClientPlayerID()))
                .collect(Collectors.toList());

        this.worker1Button = cellBtns.get(0).getWorker().workerType.equals(Command.WORKER_FIRST) ? cellBtns.get(0) : cellBtns.get(1);
        this.worker2Button = cellBtns.get(0).getWorker().workerType.equals(Command.WORKER_SECOND) ? cellBtns.get(0) : cellBtns.get(1);

        this.worker1Button.setBorder( BorderFactory.createLineBorder(Color.YELLOW, 2, true));
        this.selectedWorkerButton = worker1Button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) {
            JCellButton button = (JCellButton) e.getSource();

            if(button.equals(worker1Button)) {
                this.worker1Button.setBorder( BorderFactory.createLineBorder(Color.YELLOW, 2, true));
                this.worker2Button.setBorder( BorderFactory.createEmptyBorder());

                this.selectedWorkerButton = worker1Button;
            }

            else if(button.equals(worker2Button)) {
                this.worker2Button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2, true));
                this.worker1Button.setBorder(BorderFactory.createEmptyBorder());

                this.selectedWorkerButton = worker2Button;
            }
            else {

                if(this.selectedCellButton != null) {
                    this.selectedCellButton.setBorder(BorderFactory.createEmptyBorder());
                }

                this.selectedCellButton = button;
                this.selectedCellButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2, true));

            }
        }
    }

    private void reinitialization() { // onturnchanged
        List<JCellButton> cellBtns = Game.twoDArrayToList(getBoard().getBoardCells());

        cellBtns = cellBtns
                .stream()
                .filter(btn -> btn.getWorker() != null && btn.getWorker().player.getPlayerID().equals(this.controller.getClientPlayerID()))
                .collect(Collectors.toList());

        this.worker1Button = cellBtns.get(0).getWorker().workerType.equals(Command.WORKER_FIRST) ? cellBtns.get(0) : cellBtns.get(1);
        this.worker2Button = cellBtns.get(0).getWorker().workerType.equals(Command.WORKER_SECOND) ? cellBtns.get(0) : cellBtns.get(1);
    }

    public void onMove() {
        Worker movedWorker = this.getBoard().getBoardCells()[this.selectedCellButton.getRow()][this.selectedCellButton.getCol()].getWorker();
        reinitialization();
        if(movedWorker.workerType.equals(Command.WORKER_FIRST)) {
            this.selectedWorkerButton = worker1Button;
        }
        else {
            this.selectedWorkerButton = worker2Button;
        }

        this.selectedWorkerButton.setBorder( BorderFactory.createLineBorder(Color.YELLOW, 2, true));
    }

    public void onBuild() {
        Worker movedWorker = this.getBoard().getBoardCells()[this.selectedWorkerButton.getRow()][this.selectedWorkerButton.getCol()].getWorker();
        reinitialization();
        if(movedWorker.workerType.equals(Command.WORKER_FIRST)) {
            this.selectedWorkerButton = worker1Button;
        }
        else {
            this.selectedWorkerButton = worker2Button;
        }

        this.selectedWorkerButton.setBorder( BorderFactory.createLineBorder(Color.YELLOW, 2, true));
    }

    public void disableButtons() {
        this.buttonsEnabled = false;
    }
}
