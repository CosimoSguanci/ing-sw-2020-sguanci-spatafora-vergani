package it.polimi.ingsw.view.gui.gods;

import it.polimi.ingsw.view.gui.components.RealGame;
import it.polimi.ingsw.view.gui.gods.listeners.AtlasListener;
import it.polimi.ingsw.view.gui.listeners.EndTurnButtonListener;

import javax.swing.*;
import java.awt.*;

public class AtlasGuiStrategy implements GodGuiDrawer {
    @Override
    public JComponent draw(RealGame realGameComponent) {

        ImageIcon domeIcon = new ImageIcon( getClass().getResource("/images/RealGame/build_dome.png"));
        domeIcon = new ImageIcon(domeIcon.getImage().getScaledInstance(RealGame.dimension, -1, Image.SCALE_SMOOTH));

        JButton buildDomeBtn = new JButton("<html>Build<br />Dome</html", domeIcon);

        buildDomeBtn.addActionListener(new AtlasListener(realGameComponent));

        buildDomeBtn.setMinimumSize(new Dimension(RealGame.buttonWidth, RealGame.buttonHeight));
        buildDomeBtn.setPreferredSize(new Dimension(RealGame.buttonWidth, RealGame.buttonHeight));
        buildDomeBtn.setMaximumSize(new Dimension(RealGame.buttonWidth, RealGame.buttonHeight));

        return buildDomeBtn;
    }
}
