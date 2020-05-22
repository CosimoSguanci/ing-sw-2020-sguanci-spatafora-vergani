package it.polimi.ingsw.view.gui.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;

public class BoardScreen extends JPanel {
    private JButton[][] buttons = new JButton[Board.WIDTH_SIZE][Board.HEIGHT_SIZE];

    //public JPanel BoardScreen(String boardString) {
    public BoardScreen(String boardString) {
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
                Player playerPrintableWorker = printableWorker.player;

                //BackgroundButton btn = new BackgroundButton(blockLevel);
                JButton btn = new JButton();
                btn.setLayout(new BorderLayout());

                if (!gameBoard.getCell(i, j).isEmpty()) {
                    ImageIcon workerIcon = new ImageIcon(Gui.class.getResource("/images/BoardScreen/"
                            + playerPrintableWorker.getColor().toString().toLowerCase() + ".png"));
                    //+ playerPrintableWorker.getColor().toString().toLowerCase() + printableWorker.workerType + ".png"));

                    workerIcon = new ImageIcon(workerIcon.getImage().getScaledInstance(40,40, Image.SCALE_DEFAULT));

                    JLabel overImage = new JLabel(workerIcon);

                    btn.add(overImage, BorderLayout.CENTER);            //Use btn.getComponents() to have access to JLabel
                }

                this.buttons[i][j] = btn;
                cellsGrid.add(this.buttons[i][j]);
            }
        }

        //return cellGrid
    }
}
