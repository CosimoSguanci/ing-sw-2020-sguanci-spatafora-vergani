package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.view.gui.components.InitialInfo;
import it.polimi.ingsw.view.gui.components.NicknameAlreadyUsed;
import it.polimi.ingsw.view.gui.components.PlayerNumberChoice;
import it.polimi.ingsw.view.gui.components.WaitingForAMatch;

import javax.swing.*;

public class Gui {

    private static JFrame frame;
    private static JDialog dialog;

    public Gui(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showGui();
            }
        });
    }

    private static void showGui(){
        frame = new JFrame("Santorini");
        dialog = new JDialog(frame, "ABC");

        //frame.add(new PlayerNumberChoice());
        //frame.add(new WaitingForAMatch());
        //frame.add(new InitialInfo());
        dialog.add(new NicknameAlreadyUsed(null));

        /*frame.pack();
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/

        dialog.pack();
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
