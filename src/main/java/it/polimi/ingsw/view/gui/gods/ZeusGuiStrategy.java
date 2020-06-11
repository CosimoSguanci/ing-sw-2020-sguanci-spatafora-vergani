package it.polimi.ingsw.view.gui.gods;

import it.polimi.ingsw.view.gui.components.RealGame;

import javax.swing.*;
import java.awt.*;

public class ZeusGuiStrategy implements GodGuiDrawer {
    @Override
    public JComponent draw() {
        ImageIcon buildIcon = new ImageIcon( "src/main/resources/images/RealGame/build_under_yourself.png");
        buildIcon = new ImageIcon(buildIcon.getImage().getScaledInstance(RealGame.dimension, -1, Image.SCALE_SMOOTH));

        JButton buildBtn = new JButton("<html>Build<br />Under<br />Yourself</html", buildIcon);

        //   buildDomeBtn.addActionListener(new EndTurnButtonListener(this));

        buildBtn.setMinimumSize(new Dimension(RealGame.buttonWidth, RealGame.buttonHeight));
        buildBtn.setPreferredSize(new Dimension(RealGame.buttonWidth, RealGame.buttonHeight));
        buildBtn.setMaximumSize(new Dimension(RealGame.buttonWidth, RealGame.buttonHeight));

        return buildBtn;
    }
}
