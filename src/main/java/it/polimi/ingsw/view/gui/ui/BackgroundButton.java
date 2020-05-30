package it.polimi.ingsw.view.gui.ui;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;

public class BackgroundButton extends JButton {
    private final BlockType blockLevel;
    private int row, col;

    private JLabel workerLabel;
    private boolean isEmpty = true;

    public BackgroundButton(BlockType blockLevel, int row, int col) {
        this.blockLevel = blockLevel;
        this.row = row;
        this.col = col;
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
            case GROUND:
                return new ImageIcon(Gui.class.getResource("/images/BoardScreen/row-" + (row + 1) + "-col-" + (col + 1) + ".png")).getImage();
            case LEVEL_ONE:
                return new ImageIcon(Gui.class.getResource("/images/BoardScreen/level1.png")).getImage();
            case LEVEL_TWO:
                return new ImageIcon(Gui.class.getResource("/images/BoardScreen/level2.png")).getImage();
            case LEVEL_THREE:
                return new ImageIcon(Gui.class.getResource("/images/BoardScreen/level3.png")).getImage();
            case DOME:
                return new ImageIcon(Gui.class.getResource("/images/BoardScreen/level.png")).getImage();

            default: throw  new Exception();
        }
    }
}
