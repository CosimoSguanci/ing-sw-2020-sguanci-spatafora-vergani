package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.Gui;

import it.polimi.ingsw.view.gui.listeners.GodInfoActionListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GodInfo extends JDialog {

    private Gui gui = Gui.getInstance();
    private GodScreen godScreen;
    private List<String> selectableGods;


    public GodInfo(List<String> selectableGods) {
        super((Frame)null, "Information about Gods", false);

        this.selectableGods = selectableGods;
        int playersNumber = gui.getPlayersNumber();


        this.removeAll();

        try {
            LayoutManager layoutManager = new BorderLayout();
            this.setLayout(layoutManager);
            //this.setBorder(BorderFactory.createEmptyBorder(20,20, 20,20));
            this.godScreen = new GodScreen(playersNumber, this.selectableGods);
            this.godScreen.setOpaque(false);
            //this.add(this.godScreen, BorderLayout.CENTER);
            this.setContentPane(this.godScreen);
            setSpecific();
            addListeners();
            this.revalidate();
            this.repaint();
        } catch(IOException e) {
            e.printStackTrace();
        }

        this.revalidate();
        this.repaint();

    }


    private void setSpecific() {
        this.godScreen.setTitle("Click on a God to have info about his/her powers!");
    }


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
