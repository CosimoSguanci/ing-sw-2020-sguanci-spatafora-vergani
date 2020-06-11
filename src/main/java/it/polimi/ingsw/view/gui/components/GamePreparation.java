package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.listeners.PlaceWorkersButtonListener;
import it.polimi.ingsw.view.gui.ui.JCellButton;
import it.polimi.ingsw.view.gui.ui.JRoundButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class creates and manages GAME_PREPARATION phase layout.
 * The moment in which player choose where to place their workers.
 * In this class a creator in which the layout is defined and a
 * listener for action performed are defined.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class GamePreparation extends Game implements ActionListener {
    private String classImagePath = "src/main/resources/images/GamePreparation/";
    private JRoundButton continueButton;

    private List<JCellButton> selectedButtons = new ArrayList<>();
    private Map<String, Map<String, ImageIcon>> workerIcons = new HashMap<>();

    /**
     * This method is a simple getter which gives information
     * about button that a single player has already selected.
     * @return a list containing references to button a player has selected.
     */
    public List<JCellButton> getSelectedButtons() {
        return this.selectedButtons;
    }

    /**
     * This method is used to display CommonBoard and any workers
     * already placed in the board.
     */
    @Override
    public void draw() {

        drawCommonBoard();

        List<JCellButton> cells = twoDArrayToList(this.getBoard().getBoardCells());

        cells.forEach(cell -> {
            cell.addActionListener(this);
        });

        //need to set rightPanel's layout, with a "continue" button in the southern part
        ImageIcon continueImg = new ImageIcon(this.classImagePath + "continue.png");
        continueImg = new ImageIcon(continueImg.getImage().getScaledInstance(this.buttonDim, this.buttonDim, Image.SCALE_SMOOTH));
        this.continueButton = new JRoundButton(continueImg);

        this.continueButton.addActionListener(new PlaceWorkersButtonListener(this));

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BorderLayout());
        innerPanel.setOpaque(false);
        innerPanel.add(continueButton, BorderLayout.EAST);

        this.rightPanel.setLayout(new BorderLayout());
        this.rightPanel.add(innerPanel, BorderLayout.SOUTH);


        //setting title and subtitle
        this.title.setText("GAME PREPARATION");
        this.subtitle.setText("Place your workers by selecting two cells");

        this.revalidate();
    }

    /**
     * This method handles every action performed by a user.
     * A player has to select 2 different empty cell to place its workers.
     * If a player wrongly tries to select more than 2 cells, only the first two
     * remain selected. A selected cell can be rejected by selecting it again.
     * @param e contains references to the event performed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        Gui gui = Gui.getInstance();
        Controller controller = gui.getController();

        if(controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) {
            JCellButton button = (JCellButton) e.getSource();

            //TODO Si potrebbe fare una resize dei worker anche in GamePreparatio. Ne vale la pena far partire un nuovo thread per una cosa che scompare alla fase successiva?
            //Al momento settiamo una dimensione media per la GamePreparation
            if(button.isEmpty() && selectedButtons.size() < 2) {
                String playerNickname = controller.getClientPlayer().getNickname();
                PrintableColor color = gui.getPlayersColors().get(playerNickname);

                workerIcons = BoardScreen.getWorkerIcons();

                button.addComponentListener(new ComponentAdapter() {

                    @Override
                    public void componentResized(ComponentEvent e) {
                        JCellButton btn = (JCellButton) e.getComponent();
                        Dimension size = btn.getSize();

                        if(btn.getComponents().length> 0) {
                            btn.remove(btn.getComponent(0));
                        }

                        ImageIcon imageIcon;

                        if(size.width > 120 || size.height > 120) {
                            imageIcon = workerIcons.get("worker_" + color.toString().toLowerCase()).get("very_big");

                        }

                        else if(size.width > 100 || size.height > 100) {
                            imageIcon = workerIcons.get("worker_" + color.toString().toLowerCase()).get("big");

                        }

                        else if(size.width > 80 || size.height > 80) {
                            imageIcon = workerIcons.get("worker_" + color.toString().toLowerCase()).get("medium_big");

                        }

                        else if(size.width > 60 || size.height > 60) {
                            imageIcon = workerIcons.get("worker_" + color.toString().toLowerCase()).get("medium");

                        }

                        else if(size.width > 30 || size.height > 30) {
                            imageIcon = workerIcons.get("worker_" + color.toString().toLowerCase()).get("medium_small");

                        }

                        else {
                            imageIcon = workerIcons.get("worker_" + color.toString().toLowerCase()).get("small");

                        }

                        JLabel overImage = new JLabel(imageIcon);
                        btn.add(overImage, BorderLayout.CENTER);

                        btn.revalidate();
                        btn.repaint();

                    }

                });

                ImageIcon workerIcon = new ImageIcon(Gui.class.getResource("/images/BoardScreen/worker_" + color.toString().toLowerCase() + ".png"));
                workerIcon = new ImageIcon(workerIcon.getImage().getScaledInstance(50,50, Image.SCALE_SMOOTH));
                JLabel overImage = new JLabel(workerIcon);
                button.add(overImage);

                button.revalidate();
                this.getBoard().revalidate();

                this.getBoard().revalidate();
                this.selectedButtons.add(button);

                button.setWorkerLabel(overImage);
                button.setEmpty(false);
            }
            else if(selectedButtons.contains(button)) {
                button.remove(button.getWorkerLabel());
                button.setWorkerLabel(null);
                this.selectedButtons.remove(button);

                button.revalidate();
                this.getBoard().revalidate();

                button.setEmpty(true);

            }
        }
    }
}
