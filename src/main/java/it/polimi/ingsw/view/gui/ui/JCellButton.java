package it.polimi.ingsw.view.gui.ui;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;

public class JCellButton extends JButton {
    private final BlockType blockLevel;
    private final int row;
    private final int col;

    private JLabel workerLabel;
    private boolean isEmpty = true;

    private Worker worker;

    public JCellButton(Worker worker, BlockType blockLevel, int row, int col) {
        this.worker = worker;
        this.blockLevel = blockLevel;
        this.row = row;
        this.col = col;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public void setWorkerLabel(JLabel workerLabel) {
        this.workerLabel = workerLabel;
    }

    public JLabel getWorkerLabel() {
        return workerLabel;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image backgroundImage = null;
        try {
            backgroundImage = computeImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    private Image computeImage() throws Exception{
        switch (blockLevel) {
            case DOME:
            case GROUND:
                return new ImageIcon(Gui.class.getResource("/images/BoardScreen/row-" + (row + 1) + "-col-" + (col + 1) + ".png")).getImage();
            case LEVEL_ONE:
            case LEVEL_TWO:
            case LEVEL_THREE:
                return new ImageIcon(Gui.class.getResource("/images/BoardScreen/row-" + (row + 1) + "-col-" + (col + 1) + "level.png")).getImage();
            /*case DOME:
                return new ImageIcon(Gui.class.getResource("/images/BoardScreen/dome.png")).getImage();*/
            default: throw  new Exception();
        }
    }
}
