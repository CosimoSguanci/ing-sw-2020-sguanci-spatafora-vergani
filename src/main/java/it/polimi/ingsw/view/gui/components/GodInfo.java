package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.listeners.GodInfoActionListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * GodInfo is a class used to give players information about God.
 * This class gives players the possibility to have information
 * about Gods. During GodChoice game phase players can click on info button
 * and based on GodChooser or other they can have information about Gods.
 * GodChooser has the possibility to read all the God's power by clicking on them,
 * other players, by clicking on the info button, can see the power of Gods chosen
 * from GodChooser for their match.
 *
 * @author Roberto Spatafora
 * @author Andrea Vergani
 */
public class GodInfo extends JDialog {

    private final Gui gui = Gui.getInstance();
    private final List<String> selectableGods;
    private final String godChoiceImgPath = "/images/GodChoice/";
    private final Image backgroundImage = new ImageIcon(getClass().getResource(godChoiceImgPath + "title_sky.png")).getImage();
    private GodScreen godScreen;

    /**
     * This method is the constructor of the class.
     * At the moment of creation a list of String containing available God's names is given as parameter.
     * By the strings received the constructor generates a JDialog with as many info about God buttons as
     * the number of selectableGods.
     *
     * @param selectableGods is a list which contains selectable Gods names.
     *                       Every String in list comports the presence of a button that
     *                       if clicked it appears a JDialog with information about the power of that God
     */
    public GodInfo(List<String> selectableGods) {
        super((Frame) null, "Information about Gods", false);

        this.selectableGods = selectableGods;
        int playersNumber = gui.getPlayersNumber();


        try {
            LayoutManager layoutManager = new BorderLayout();
            this.setLayout(layoutManager);
            JPanel mainPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
                }
            };
            mainPanel.setOpaque(false);
            mainPanel.setLayout(new BorderLayout());
            //this.setBorder(BorderFactory.createEmptyBorder(20,20, 20,20));
            this.godScreen = new GodScreen(playersNumber, this.selectableGods);
            this.godScreen.setOpaque(false);
            //this.add(this.godScreen, BorderLayout.CENTER);
            this.godScreen.setTitle("Click on a God to have info about his/her powers!");
            addListeners();
            mainPanel.add(this.godScreen, BorderLayout.CENTER);
            this.add(mainPanel);
            this.revalidate();
            this.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.revalidate();
        this.repaint();

    }

    /**
     * This private method is used to add listeners to buttons that contains
     * information about the power of a God in the component.
     */
    private void addListeners() {
        boolean isGodChooser = this.gui.getController().isClientPlayerGodChooser();
        List<String> gods = isGodChooser ? View.getGodsNamesList() : this.selectableGods;
        ArrayList<JButton> godButtons = this.godScreen.getButtons();
        for (int i = 0; i < gods.size(); i++) {
            GodInfoActionListener godInfoActionListener = new GodInfoActionListener(this);
            godButtons.get(i).addActionListener(godInfoActionListener);
        }
    }

}
