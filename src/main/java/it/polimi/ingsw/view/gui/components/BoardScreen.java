package it.polimi.ingsw.view.gui.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.*;
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
    private final static Map<String, ImageIcon> domeIcon = new HashMap<>();
    private final static Map<String, Map<String, ImageIcon>> workerIcons = new HashMap<>();

    static {
        new Thread(() -> {
            try {
                ImageIcon imageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/BoardScreen/dome.png")));

                ImageIcon smallImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(30, -1, Image.SCALE_SMOOTH));

                ImageIcon mediumSmallImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(35, -1, Image.SCALE_SMOOTH));

                ImageIcon mediumImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(40, -1, Image.SCALE_SMOOTH));

                ImageIcon mediumBigImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(50, -1, Image.SCALE_SMOOTH));

                ImageIcon bigImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(60, -1, Image.SCALE_SMOOTH));

                ImageIcon veryBigImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(70, -1, Image.SCALE_SMOOTH));

                domeIcon.put("small", smallImageIcon);
                domeIcon.put("medium_small", mediumSmallImageIcon);
                domeIcon.put("medium", mediumImageIcon);
                domeIcon.put("medium_big", mediumBigImageIcon);
                domeIcon.put("big", bigImageIcon);
                domeIcon.put("very_big", veryBigImageIcon);

            } catch (IOException e) {
                System.err.println("Error loading some Board Screen resource");

            }

        }).start();
    }

    static {
        new Thread(() -> {

            List<PrintableColor> colors = PrintableColor.getColorList();

            colors.forEach(color -> {
                try {

                    ImageIcon imageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/BoardScreen/worker_" + color.toString().toLowerCase() + ".png")));

                    ImageIcon smallImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

                    ImageIcon mediumSmallImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));

                    ImageIcon mediumImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));

                    ImageIcon mediumBigImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

                    ImageIcon bigImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

                    ImageIcon veryBigImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));

                    Map<String, ImageIcon> m = new HashMap<>();
                    m.put("small", smallImageIcon);
                    m.put("medium_small", mediumSmallImageIcon);
                    m.put("medium", mediumImageIcon);
                    m.put("medium_big", mediumBigImageIcon);
                    m.put("big", bigImageIcon);
                    m.put("very_big", veryBigImageIcon);

                    workerIcons.put("worker_" + color.toString().toLowerCase(), m);

                } catch (IOException e) {
                    System.err.println("Error loading some Worker resource in Board Screen");
                }
            });
        }).start();
    }

    private final JCellButton[][] buttons = new JCellButton[Board.WIDTH_SIZE][Board.HEIGHT_SIZE];

    /**
     * This method is the constructor of the class.
     * It creates a layout in which a 5x5 Cell buttons are defined.
     * Every button contains information about its relative cell level,
     * if a worker is on that cell or a dome if the level raised the maximum.
     *
     * @param boardString serialized JSON Board Game received from Server
     */
    public BoardScreen(String boardString) {


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
                            (int) d.getWidth(), (int) d.getHeight());
                } else if (c.getWidth() > d.getWidth() && c.getHeight() > d.getHeight()) {
                    prefSize = c.getSize();
                } else {
                    prefSize = d;
                }
                int w = (int) prefSize.getWidth();
                int h = (int) prefSize.getHeight();
                // the smaller of the two sizes
                int s = (Math.min(w, h));
                return new Dimension(s, s);
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
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

                    btn.addComponentListener(new ComponentAdapter() {

                        @Override
                        public void componentResized(ComponentEvent e) {
                            JCellButton btn = (JCellButton) e.getComponent();
                            Dimension size = btn.getSize();

                            if (btn.getComponents().length > 0) {
                                btn.remove(btn.getComponent(0));
                            }

                            ImageIcon imageIcon;

                            if (size.width > 120 || size.height > 120) {
                                imageIcon = workerIcons.get("worker_" + playerPrintableWorker.getColor().toString().toLowerCase()).get("very_big");

                            } else if (size.width > 100 || size.height > 100) {
                                imageIcon = workerIcons.get("worker_" + playerPrintableWorker.getColor().toString().toLowerCase()).get("big");

                            } else if (size.width > 80 || size.height > 80) {
                                imageIcon = workerIcons.get("worker_" + playerPrintableWorker.getColor().toString().toLowerCase()).get("medium_big");

                            } else if (size.width > 60 || size.height > 60) {
                                imageIcon = workerIcons.get("worker_" + playerPrintableWorker.getColor().toString().toLowerCase()).get("medium");

                            } else if (size.width > 30 || size.height > 30) {
                                imageIcon = workerIcons.get("worker_" + playerPrintableWorker.getColor().toString().toLowerCase()).get("medium_small");

                            } else {
                                imageIcon = workerIcons.get("worker_" + playerPrintableWorker.getColor().toString().toLowerCase()).get("small");

                            }

                            JLabel overImage = new JLabel(imageIcon);
                            btn.add(overImage, BorderLayout.CENTER);

                            btn.revalidate();
                            btn.repaint();

                        }

                    });
                }


                if (blockLevel == BlockType.DOME) {


                    btn.addComponentListener(new ComponentAdapter() {

                        @Override
                        public void componentResized(ComponentEvent e) {
                            JCellButton btn = (JCellButton) e.getComponent();
                            Dimension size = btn.getSize();

                            if (btn.getComponents().length > 0) {

                                btn.remove(btn.getComponent(0));
                            }

                            ImageIcon imageIcon;

                            if (size.width > 120 || size.height > 120) {
                                imageIcon = domeIcon.get("very_big");

                            } else if (size.width > 100 || size.height > 100) {
                                imageIcon = domeIcon.get("big");

                            } else if (size.width > 80 || size.height > 80) {
                                imageIcon = domeIcon.get("medium_big");

                            } else if (size.width > 60 || size.height > 60) {
                                imageIcon = domeIcon.get("medium");

                            } else if (size.width > 30 || size.height > 30) {
                                imageIcon = domeIcon.get("medium_small");

                            } else {
                                imageIcon = domeIcon.get("small");

                            }

                            JLabel overImage = new JLabel(imageIcon);
                            btn.add(overImage, BorderLayout.CENTER);

                            btn.revalidate();
                            btn.repaint();


                        }

                    });
                }


                this.buttons[i][j] = btn;
                cellsGrid.add(this.buttons[i][j]);

            }
        }

    }

    /**
     * This method is a simple getter of the workerIcons Map created in
     * a different thread at the first moment in which this class is invoked.
     *
     * @return a Map which contains references to all colored workers in different
     * dimension due to adapt them according to window size.
     */
    public static Map<String, Map<String, ImageIcon>> getWorkerIcons() {
        return workerIcons;
    }

    /**
     * This method allows to have references to all the
     * different buttons of the board
     *
     * @return a bi-dimensional array that references every button of the board.
     */
    JCellButton[][] getBoardCells() {
        return this.buttons;
    }

}
