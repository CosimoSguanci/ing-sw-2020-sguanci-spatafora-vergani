package it.polimi.ingsw.view.gui.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.ui.BackgroundButton;

import javax.swing.*;
import java.awt.*;

public class BoardScreen extends JPanel {
    private JButton[][] buttons = new JButton[Board.WIDTH_SIZE][Board.HEIGHT_SIZE];

    JButton[][] getBoardCells() {
        return this.buttons;
    }

    //public JPanel BoardScreen(String boardString) {
    public BoardScreen(String boardString) {        //This methods returns a JPanel. Now it is used to test it playing.
        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();
        Board gameBoard = gson.fromJson(boardString, Board.class);

        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        JPanel cellsGrid = new JPanel();
        cellsGrid.setLayout(new GridLayout(Board.HEIGHT_SIZE, Board.WIDTH_SIZE));
        this.add(cellsGrid);
        this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        for (int i = 0; i < Board.WIDTH_SIZE; i++) {
            for (int j = 0; j < Board.HEIGHT_SIZE; j++) {

                Worker printableWorker = gameBoard.getCell(i, j).getWorker();
                BlockType blockLevel = gameBoard.getCell(i, j).getLevel();
                Player playerPrintableWorker;

                BackgroundButton btn = new BackgroundButton(blockLevel, i, j);

                btn.setLayout(new BorderLayout());

                btn.setEmpty(gameBoard.getCell(i, j).isEmpty());

                if (!gameBoard.getCell(i, j).isEmpty()) {

                    btn.setEmpty(false);

                    playerPrintableWorker = printableWorker.player;
                    ImageIcon workerIcon = new ImageIcon(Gui.class.getResource("/images/BoardScreen/worker_" + playerPrintableWorker.getColor().toString().toLowerCase() + ".png"));
                    //+ playerPrintableWorker.getColor().toString().toLowerCase() + printableWorker.workerType + ".png"));

                    workerIcon = new ImageIcon(workerIcon.getImage().getScaledInstance(80,80, Image.SCALE_SMOOTH));

                    JLabel overImage = new JLabel(workerIcon);

                    btn.setWorkerLabel(overImage);

                    btn.add(overImage, BorderLayout.CENTER);            //Use btn.getComponents() to have access to JLabel
                }

                else if(blockLevel.equals(BlockType.DOME)) {
                    ImageIcon domeIcon = new ImageIcon(Gui.class.getResource("/images/BoardScreen/dome.png"));
                    domeIcon = new ImageIcon(domeIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
                    JLabel overImage = new JLabel(domeIcon);
                    btn.add(overImage, BorderLayout.CENTER);

                }

                this.buttons[i][j] = btn;
                cellsGrid.add(this.buttons[i][j]);
            }
        }

        //return cellGrid
    }
}
