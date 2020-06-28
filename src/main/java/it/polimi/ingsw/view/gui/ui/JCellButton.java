package it.polimi.ingsw.view.gui.ui;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * This class defines a button which is used in RealGame phase.
 * When a match starts, cells can have different levels: GROUND, ONE, TWO, THREE
 */
public class JCellButton extends JButton {
    private final BlockType blockLevel;
    private final int row;
    private final int col;

    private JLabel workerLabel;
    private boolean isEmpty = true;

    private Worker worker;

    /**
     * This is the creator of the class. At the moment of creation:
     *
     * @param worker     contains a reference of a Worker. If no worker is in referred cell it would be null
     * @param blockLevel contains information about the level of the cell
     * @param row        is an integer which indicates the number of row which identifies the cell in the Board
     * @param col        is an integer which indicates the number of column which identifies the cell in the Board
     */
    public JCellButton(Worker worker, BlockType blockLevel, int row, int col) {
        this.worker = worker;
        this.blockLevel = blockLevel;
        this.row = row;
        this.col = col;
    }

    /**
     * This method is a simple getter of the Worker inside the cell.
     *
     * @return a Worker if inside a cell
     */
    public Worker getWorker() {
        return worker;
    }

    /**
     * This method is a simple setter of the class.
     *
     * @param worker contains a reference of the worker that is in the cell
     */
    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    /**
     * This method is a getter of the boolean attribute of the class.
     *
     * @return true if the cell doesn't contains any worker inside, false otherwise.
     */
    public boolean isEmpty() {
        return this.isEmpty;
    }

    /**
     * This method is a simple setter of the boolean attribute of the cell
     *
     * @param isEmpty is a boolean value which if true indicates that no worker is in the cell.
     */
    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    /**
     * This method is a simple getter of the class.
     *
     * @return a JLabel which indicates the presence of a worker in the cell.
     */
    public JLabel getWorkerLabel() {
        return this.workerLabel;
    }

    /**
     * This method is a simple setter of the class attribute workerLabel.
     *
     * @param workerLabel contains an image which indicates the presence of a worker
     */
    public void setWorkerLabel(JLabel workerLabel) {
        this.workerLabel = workerLabel;
    }

    /**
     * This is a simple getter of the integer number which identifies the column of the cell related to the button
     *
     * @return the integer number which identifies the cell row in the board.
     */
    public int getRow() {
        return row;
    }

    /**
     * This is a simple getter of the integer number which identifies the column of the cell related to the button
     *
     * @return the integer number which identifies the cell column in the board.
     */
    public int getCol() {
        return col;
    }

    /**
     * This method is used to set the background of the cell.
     *
     * @param g contains a reference to the button of which we want to set the background
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image backgroundImage = null;
        try {
            backgroundImage = computeImage();
        } catch (IOException e) {
            System.err.println("Error loading some Cell Button resource");
        }

        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    /**
     * This private method computes the image that will be set as background image of the button
     *
     * @return an Image which would be used as background
     * @throws IOException when some error is encountered while getting the needed resources
     */
    private Image computeImage() throws IOException {
        switch (blockLevel) {
            case DOME:
            case GROUND:
                return new ImageIcon(Gui.class.getResource("/images/BoardScreen/row-" + (row + 1) + "-col-" + (col + 1) + ".png")).getImage();
            case LEVEL_ONE:
                return new ImageIcon(Gui.class.getResource("/images/BoardScreen/row-" + (row + 1) + "-col-" + (col + 1) + "-level1.png")).getImage();
            case LEVEL_TWO:
                return new ImageIcon(Gui.class.getResource("/images/BoardScreen/row-" + (row + 1) + "-col-" + (col + 1) + "-level2.png")).getImage();
            case LEVEL_THREE:
                return new ImageIcon(Gui.class.getResource("/images/BoardScreen/row-" + (row + 1) + "-col-" + (col + 1) + "-level3.png")).getImage();
            default:
                throw new IOException();
        }
    }
}
