package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.listeners.PlaceWorkersButtonListener;
import it.polimi.ingsw.view.gui.ui.BackgroundButton;
import it.polimi.ingsw.view.gui.ui.JRoundButton;

;import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GamePreparation extends Game implements ActionListener {
    private String classImagePath = "src/main/resources/images/GamePreparation/";
    private JRoundButton continueButton;

    private List<BackgroundButton> selectedButtons = new ArrayList<>();

    public List<BackgroundButton> getSelectedButtons() {
        return this.selectedButtons;
    }

    @Override
    public void draw() {

        drawCommonBoard();

        List<JButton> cells = twoDArrayToList(this.getBoard().getBoardCells());

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

    private java.util.List<JButton> twoDArrayToList(JButton[][] twoDArray) {
        List <JButton> list = new ArrayList<>();
        for (JButton[] array : twoDArray) {
            list.addAll(Arrays.asList(array));
        }
        return list;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Gui gui = Gui.getInstance();
        Controller controller = gui.getController();

        if(controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) {
            BackgroundButton button = (BackgroundButton) e.getSource();

            if(button.isEmpty() && selectedButtons.size() < 2) {
                String playerNickname = controller.getClientPlayer().getNickname();
                PrintableColor color = gui.getPlayersColors().get(playerNickname);
                ImageIcon workerIcon = new ImageIcon(Gui.class.getResource("/images/BoardScreen/worker_" + color.toString().toLowerCase() + ".png"));
                workerIcon = new ImageIcon(workerIcon.getImage().getScaledInstance(80,80, Image.SCALE_SMOOTH));
                JLabel overImage = new JLabel(workerIcon);
                button.add(overImage);

                button.revalidate();
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
