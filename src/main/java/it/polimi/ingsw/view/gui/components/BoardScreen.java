package it.polimi.ingsw.view.gui.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.ui.JCellButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final static Map<String, Map<String, ImageIcon>> towerIcons = new HashMap<>();
    private final static Map<String, Map<String, ImageIcon>> workerIcons = new HashMap<>();


    static {
        new Thread(() -> {

            int i = 1;

            while(i <= 3) {

                try {

                    ImageIcon imageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/BoardScreen/tower-level" + i + ".png")));

                    ImageIcon smallImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(20,-1, Image.SCALE_SMOOTH));

                    ImageIcon mediumSmallImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(30,-1, Image.SCALE_SMOOTH));

                    ImageIcon mediumImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(35,-1, Image.SCALE_SMOOTH));

                    ImageIcon mediumBigImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(40,-1, Image.SCALE_SMOOTH));

                    ImageIcon bigImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(45,-1, Image.SCALE_SMOOTH));

                    ImageIcon veryBigImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(45,-1, Image.SCALE_SMOOTH));

                    Map<String, ImageIcon> m = new HashMap<>();
                    m.put("small", smallImageIcon);
                    m.put("medium_small", mediumSmallImageIcon);
                    m.put("medium", mediumImageIcon);
                    m.put("medium_big", mediumBigImageIcon);
                    m.put("big", bigImageIcon);
                    m.put("very_big", veryBigImageIcon);

                    towerIcons.put("level_" + i, m);


                } catch(IOException e) {
                    e.printStackTrace();
                }

                i++;

            }

            PrintableColor.getColorList().forEach(color -> {
                try {

                    ImageIcon imageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/BoardScreen/worker_" + color.toString().toLowerCase() + ".png")));

                    ImageIcon smallImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(40,40, Image.SCALE_SMOOTH));

                    ImageIcon mediumSmallImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(50,50, Image.SCALE_SMOOTH));

                    ImageIcon mediumImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(60,60, Image.SCALE_SMOOTH));

                    ImageIcon mediumBigImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(70,70, Image.SCALE_SMOOTH));

                    ImageIcon bigImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(80,80, Image.SCALE_SMOOTH));

                    ImageIcon veryBigImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(80,80, Image.SCALE_SMOOTH));

                    Map<String, ImageIcon> m = new HashMap<>();
                    m.put("small", smallImageIcon);
                    m.put("medium_small", mediumSmallImageIcon);
                    m.put("medium", mediumImageIcon);
                    m.put("medium_big", mediumBigImageIcon);
                    m.put("big", bigImageIcon);
                    m.put("very_big", veryBigImageIcon);

                    workerIcons.put("worker_" + color.toString().toLowerCase(), m);


                } catch(IOException e) {
                    e.printStackTrace();
                }
            });



        }).start();
    }

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

                    workerIcon = new ImageIcon(workerIcon.getImage().getScaledInstance(70,70, Image.SCALE_SMOOTH));

                    JLabel overImage = new JLabel(workerIcon);

                    overImage.setHorizontalAlignment(JLabel.CENTER);
                    overImage.setVerticalAlignment(JLabel.CENTER);


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

                    levelPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 8));

                    btn.add(levelPanel, BorderLayout.NORTH);

                    JPanel bottom = new JPanel();
                    bottom.setOpaque(false);
                    btn.add(bottom, BorderLayout.SOUTH);

                    btn.setLevelData(levelPanel, imageIconLevel);
                }


                this.buttons[i][j] = btn;
                cellsGrid.add(this.buttons[i][j]);

                btn.addComponentListener(new ComponentAdapter() {

                    @Override
                    public void componentResized(ComponentEvent e) {
                        JCellButton btn = (JCellButton) e.getComponent();
                        Dimension size = btn.getSize();

                        if(btn.getLevelPanel() != null && btn.getImageIconLevel() != null) {

                            btn.remove(btn.getLevelPanel());

                            JPanel newLevelPanel = new JPanel(new BorderLayout());

                            ImageIcon imageIcon;

                            if(size.width > 400 || size.height > 400) {
                                imageIcon = towerIcons.get("level_" + blockLevel.getLevelNumber()).get("very_big");
                                newLevelPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 8));

                            }

                            else if(size.width > 200 || size.height > 200) {
                                imageIcon = towerIcons.get("level_" + blockLevel.getLevelNumber()).get("big");
                                newLevelPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 8));

                            }

                            else if(size.width > 120 || size.height > 120) {
                                imageIcon = towerIcons.get("level_" + blockLevel.getLevelNumber()).get("medium_big");
                                newLevelPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 8));

                            }

                            else if(size.width > 50 || size.height > 50) {
                                imageIcon = towerIcons.get("level_" + blockLevel.getLevelNumber()).get("medium");
                                newLevelPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 8));

                            }

                            else if(size.width > 25 || size.height > 25) {
                                imageIcon = towerIcons.get("level_" + blockLevel.getLevelNumber()).get("medium_small");
                                newLevelPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 8));

                            }

                            else {
                                imageIcon = towerIcons.get("level_" + blockLevel.getLevelNumber()).get("small");
                                newLevelPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 8));

                            }

                            JLabel label = new JLabel(imageIcon);

                            label.setHorizontalAlignment(JLabel.CENTER);
                            label.setVerticalAlignment(JLabel.CENTER);

                            newLevelPanel.add(label, BorderLayout.EAST);

                            newLevelPanel.setOpaque(false);

                            btn.setLevelData(newLevelPanel, imageIcon);

                            btn.add(newLevelPanel, BorderLayout.NORTH);

                            btn.revalidate();
                            btn.repaint();


                        }

                        /*if(btn.getWorker() != null && btn.getWorkerLabel() != null) {

                            btn.remove(btn.getWorkerLabel());

                            JLabel workerLabel = new JLabel();

                            ImageIcon imageIcon;

                            if(size.width > 400 || size.height > 400) {
                                imageIcon = workerIcons.get("worker_" + btn.getWorker().player.getColor().toString().toLowerCase()).get("very_big");
                            }

                            else if(size.width > 200 || size.height > 200) {

                                imageIcon = workerIcons.get("worker_" + btn.getWorker().player.getColor().toString().toLowerCase()).get("big");


                            }

                            else if(size.width > 120 || size.height > 120) {

                                imageIcon = workerIcons.get("worker_" + btn.getWorker().player.getColor().toString().toLowerCase()).get("medium_big");



                            }

                            else if(size.width > 50 || size.height > 50) {
                                imageIcon = workerIcons.get("worker_" + btn.getWorker().player.getColor().toString().toLowerCase()).get("medium");


                            }

                            else if(size.width > 25 || size.height > 25) {
                                imageIcon = workerIcons.get("worker_" + btn.getWorker().player.getColor().toString().toLowerCase()).get("medium_small");


                            }

                            else {
                                imageIcon = workerIcons.get("worker_" + btn.getWorker().player.getColor().toString().toLowerCase()).get("small");


                            }

                            workerLabel.setIcon(imageIcon);

                            btn.setWorkerLabel(workerLabel);

                            btn.add(workerLabel, BorderLayout.CENTER);

                            btn.revalidate();
                            btn.repaint();


                        }*/

                    }

                });

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
