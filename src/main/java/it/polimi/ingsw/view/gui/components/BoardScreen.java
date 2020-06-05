package it.polimi.ingsw.view.gui.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.ui.JCellButton;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * BoardScreen is a class in which the GUI-component of
 * the board game is generated. Every time a BoardUpdate
 * arrives from the server, this class creates a new Board with
 * the same layout as the pre-update one.
 * Every button of the board contains information about its relative cell level,
 * if a worker is on that cell or a dome if the level raised the maximum.
 *
 * @author Andrea Vergani
 * @author Roberto Spatafora
 */
public class BoardScreen extends JPanel {
    private final JCellButton[][] buttons = new JCellButton[Board.WIDTH_SIZE][Board.HEIGHT_SIZE];

    /**
     * This method is the constructor of the class.
     * It creates a layout in which a 5x5 Cell buttons are defined.
     * Every button contains information about its relative cell level,
     * if a worker is on that cell or a dome if the level raised the maximum.
     * @param boardString
     */
    public BoardScreen(String boardString) {        //This methods returns a JPanel. Now it is used to test it playing.
        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();
        Board gameBoard = gson.fromJson(boardString, Board.class);

        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        //JPanel cellsGrid = new JPanel();

       /* cellsGrid.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                JPanel panel = (JPanel) e.getComponent();
                Dimension size = panel.getSize();

                System.out.println("JPANEL RESIZED, HEIGHT: " + size.getHeight());
                System.out.println("JPANEL RESIZED, WIDTH: " + size.getWidth());

                int dim = Math.max(panel.getWidth(), panel.getHeight());


                panel.setSize(dim, dim);



            }

        });*/


            JPanel cellsGrid = new JPanel(new GridLayout(Board.HEIGHT_SIZE, Board.WIDTH_SIZE)) {

            /**
             * Override the preferred size to return the largest it can, in
             * a square shape.  Must (must, must) be added to a GridBagLayout
             * as the only component (it uses the parent as a guide to size)
             * with no GridBagConstraint (so it is centered).
             */
            @Override
            public final Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Dimension prefSize;
                Component c = getParent();
                if (c == null) {
                    prefSize = new Dimension(
                            (int)d.getWidth(),(int)d.getHeight());
                } else if (c.getWidth() > d.getWidth() && c.getHeight() > d.getHeight()) {
                    prefSize = c.getSize();
                } else {
                    prefSize = d;
                }
                int w = (int) prefSize.getWidth();
                int h = (int) prefSize.getHeight();
                // the smaller of the two sizes
                int s = (Math.min(w, h));
                return new Dimension(s,s);
            }
        };

        JPanel boardConstrain = new JPanel(new GridBagLayout());

        boardConstrain.setOpaque(false);

        boardConstrain.setBorder(null);
        Border boardBorder = BorderFactory.createLineBorder(Color.WHITE);
        cellsGrid.setBorder(BorderFactory.createCompoundBorder(boardBorder, boardBorder));
        this.setBorder(null);


        boardConstrain.add(cellsGrid);

        this.add(boardConstrain);
        this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        for (int i = 0; i < Board.WIDTH_SIZE; i++) {
            for (int j = 0; j < Board.HEIGHT_SIZE; j++) {

                Worker printableWorker = gameBoard.getCell(i, j).getWorker();
                BlockType blockLevel = gameBoard.getCell(i, j).getLevel();
                Player playerPrintableWorker;

                JCellButton btn = new JCellButton(printableWorker, blockLevel, i, j);

                btn.setLayout(new BorderLayout());

                btn.setEmpty(gameBoard.getCell(i, j).isEmpty());

                btn.setBorder(BorderFactory.createEmptyBorder());

                if (!gameBoard.getCell(i, j).isEmpty()) {

                    playerPrintableWorker = printableWorker.player;
                    ImageIcon workerIcon = new ImageIcon(Gui.class.getResource("/images/BoardScreen/worker_" + playerPrintableWorker.getColor().toString().toLowerCase() + ".png"));

                    workerIcon = new ImageIcon(workerIcon.getImage().getScaledInstance(80,80, Image.SCALE_SMOOTH));

                    JLabel overImage = new JLabel(workerIcon);

                    btn.setWorkerLabel(overImage);

                    btn.add(overImage, BorderLayout.CENTER);            //Use btn.getComponents() to have access to JLabel
                }

                if(blockLevel == BlockType.DOME) {
                    ImageIcon domeIcon = new ImageIcon(Gui.class.getResource("/images/BoardScreen/dome.png"));
                    domeIcon = new ImageIcon(domeIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
                    JLabel overImage = new JLabel(domeIcon);
                    btn.add(overImage, BorderLayout.CENTER);
                }

                else if(blockLevel != BlockType.GROUND){

                    JPanel levelPanel = new JPanel();
                    levelPanel.setLayout(new BorderLayout());
                    levelPanel.setOpaque(false);


                    ImageIcon imageIconLevel = new ImageIcon(Gui.class.getResource("/images/BoardScreen/tower-level" + blockLevel.getLevelNumber() + ".png"));
                    imageIconLevel = new ImageIcon(imageIconLevel.getImage().getScaledInstance(20, -1, Image.SCALE_SMOOTH));
                    JLabel levelImage = new JLabel(imageIconLevel);

                    levelPanel.add(levelImage, BorderLayout.EAST);

                    levelPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));

                    btn.add(levelPanel, BorderLayout.NORTH);
                }


                this.buttons[i][j] = btn;
                cellsGrid.add(this.buttons[i][j]);

                /*btn.addComponentListener(new ComponentAdapter() {

                    @Override
                    public void componentResized(ComponentEvent e) {
                        JButton btn = (JButton) e.getComponent();
                        Dimension size = btn.getSize();

                        String god = btn.getText().toLowerCase();

                        if(size.width > 600 || size.height > 600) {
                            btn.setIcon(allIconGods.get(god).get("very_big"));
                        }

                        else if(size.width > 400 || size.height > 400) {
                            btn.setIcon(allIconGods.get(god).get("big"));
                        }

                        else if(size.width > 260 || size.height > 260) {
                            btn.setIcon(allIconGods.get(god).get("medium_big"));
                        }

                        else if(size.width > 70 || size.height > 70) {
                            btn.setIcon(allIconGods.get(god).get("medium"));
                        }

                        else if(size.width > 40 || size.height > 40) {
                            btn.setIcon(allIconGods.get(god).get("medium_small"));
                        }

                        else {
                            btn.setIcon(allIconGods.get(god).get("small"));

                        }

                    }

                });*/

            }
        }

    }

    /**
     * This method allows to have references to all the
     * different buttons of the board
     * @return a bi-dimensional array that references every button of the board.
     */
    JCellButton[][] getBoardCells() {
        return this.buttons;
    }

}
