package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.gui.ui.JRoundButton;

;import javax.swing.*;
import java.awt.*;

public class GamePreparation extends Game {
    private String classImagePath = "src/main/resources/images/GamePreparation/";
    private JRoundButton continueButton;

    public GamePreparation(String boardString) {
        super(boardString);

        //need to set rightPanel's layout, with a "continue" button in the southern part
        ImageIcon continueImg = new ImageIcon(this.classImagePath + "continue.png");
        continueImg = new ImageIcon(continueImg.getImage().getScaledInstance(this.buttonDim, this.buttonDim, Image.SCALE_SMOOTH));
        this.continueButton = new JRoundButton(continueImg);
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
}
