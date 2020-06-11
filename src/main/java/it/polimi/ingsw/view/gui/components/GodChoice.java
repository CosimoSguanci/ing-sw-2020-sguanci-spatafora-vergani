package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.controller.commands.GodChoiceCommand;
import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.listeners.GodChoiceInfoButtonListener;
import it.polimi.ingsw.view.gui.listeners.GodChoiceJButtonListener;
import it.polimi.ingsw.view.gui.ui.JRoundButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GodChoice extends JPanel implements ActionListener {

    private final String standardImgPath = "src/main/resources/images/GodChoice/";
    private final Image backgroundImage = new ImageIcon(standardImgPath + "title_sky.png").getImage(); // todo background ok? Or Olympus
    private ArrayList<JButton> buttons = new ArrayList<>();
    private GodChoiceJButtonListener godChoiceJButtonListener;
    private int selectedGodsNumber = 0;

    private int playersNumber;

    private Gui gui;

    private List<String> selectedGods;
    private List<String> selectableGods;

    GodScreen godScreen;

    private Font titleFont = Gui.getFont(Gui.FONT_BOLD, 20);
    private Font godLabelFont = Gui.getFont(Gui.FONT_BOLD, 14);


    //private Map<String, Map<String, ImageIcon>> allIconGods = new HashMap<>(); // todo synchronized

    public void setSelectableGods(List<String> selectableGods) {
        this.selectableGods = selectableGods;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public GodChoice(){

        this.gui = Gui.getInstance();
        this.selectedGods = new ArrayList<>();
        this.add(new LoadingComponent("Waiting...", Color.BLACK));

        /*new Thread(() -> {
            List<String> gods =  View.getGodsNamesList();

            gods.forEach(god -> {

                try {

                    ImageIcon smallImageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + god + ".png")));
                    smallImageIcon = new ImageIcon(smallImageIcon.getImage().getScaledInstance(30,-1, Image.SCALE_SMOOTH));

                    ImageIcon mediumSmallImageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + god + ".png")));
                    mediumSmallImageIcon = new ImageIcon(mediumSmallImageIcon.getImage().getScaledInstance(50,-1, Image.SCALE_SMOOTH));

                    ImageIcon mediumImageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + god + ".png")));
                    mediumImageIcon = new ImageIcon(mediumImageIcon.getImage().getScaledInstance(70,-1, Image.SCALE_SMOOTH));

                    ImageIcon mediumBigImageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + god + ".png")));
                    mediumBigImageIcon = new ImageIcon(mediumBigImageIcon.getImage().getScaledInstance(100,-1, Image.SCALE_SMOOTH));

                    ImageIcon bigImageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + god + ".png")));
                    bigImageIcon = new ImageIcon(bigImageIcon.getImage().getScaledInstance(130,-1, Image.SCALE_SMOOTH));

                    ImageIcon veryBigImageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + god + ".png")));
                    veryBigImageIcon = new ImageIcon(veryBigImageIcon.getImage().getScaledInstance(200,-1, Image.SCALE_SMOOTH));

                    Map<String, ImageIcon> m = new HashMap<>();
                    m.put("small", smallImageIcon);
                    m.put("medium_small", mediumSmallImageIcon);
                    m.put("medium", mediumImageIcon);
                    m.put("medium_big", mediumBigImageIcon);
                    m.put("big", bigImageIcon);
                    m.put("very_big", veryBigImageIcon);

                    allIconGods.put(god, m);


                } catch(IOException e) {
                    e.printStackTrace();
                }


            });


        }).start();*/
    }
    
    private void commonGodChoose2(JPanel possibleGodsPanel, ArrayList<ImageIcon> iconGods) throws IOException {
        ImageIcon imageIcon;
        for (int i = 0; i < playersNumber; i++) {
            imageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + this.selectableGods.get(i) + ".png")));
            imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(30,-1, Image.SCALE_SMOOTH));
            iconGods.add(imageIcon);
            //possibleGodsPanel.add(new JButton(chosenGods[i], iconGods.get(i)));+
            this.buttons.add(new JButton(this.selectableGods.get(i), iconGods.get(i)));
            this.buttons.get(i).addActionListener(godChoiceJButtonListener);
            possibleGodsPanel.add(this.buttons.get(i));
        }
    }




    public void setGodChoiceSelected(JButton button) {
        if(gui.getController().isClientPlayerGodChooser()){
            if(!button.isSelected()){
                if(selectedGodsNumber < playersNumber){
                    button.setSelected(true);

                    String operSys = System.getProperty("os.name").toLowerCase();

                    if(operSys.contains("mac")) {
                        button.setBorderPainted(true);
                    }

                    button.setBorder( BorderFactory.createLineBorder(Color.GREEN, 2, true));
                    selectedGodsNumber++;
                    this.selectedGods.add(button.getText());
                }
            }
            else {
                button.setSelected(false);
                button.setBorder(UIManager.getBorder("Button.border"));

                String operSys = System.getProperty("os.name").toLowerCase();

                if(operSys.contains("mac")) {
                    button.setBorderPainted(false);
                }

                selectedGodsNumber--;
                this.selectedGods.remove(button.getText());
            }

        }
        else {
            if(!button.isSelected()){
                if(selectedGodsNumber < 1) {
                    button.setSelected(true);

                    String operSys = System.getProperty("os.name").toLowerCase();

                    if(operSys.contains("mac")) {
                        button.setBorderPainted(true);
                    }

                    button.setBorder( BorderFactory.createLineBorder(Color.GREEN, 2, true));

                    selectedGodsNumber++;
                    this.selectedGods.add(button.getText());
                }
            }
            else {
                button.setSelected(false);

                button.setBorder(UIManager.getBorder("Button.border"));

                String operSys = System.getProperty("os.name").toLowerCase();

                if(operSys.contains("mac")) {
                    button.setBorderPainted(false);
                }

                selectedGodsNumber--;
                this.selectedGods.remove(button.getText());
            }
        }

    }


    private void setSpecific() {
        ImageIcon startImg = new ImageIcon("src/main/resources/images/done.png");
        startImg = new ImageIcon(startImg.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JRoundButton continueButton = new JRoundButton(startImg);
        continueButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        continueButton.setOpaque(false);
        continueButton.addActionListener(this);

        ImageIcon infoImg = new ImageIcon(this.standardImgPath + "information.png");
        infoImg = new ImageIcon(infoImg.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JRoundButton infoButton = new JRoundButton(infoImg);
        infoButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        infoButton.setOpaque(false);
        infoButton.addActionListener(new GodChoiceInfoButtonListener(this));

        //JPanel innerPanel = new JPanel();
        //innerPanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.add(infoButton);
        buttonPanel.add(continueButton);
        //innerPanel.add(buttonPanel, BorderLayout.SOUTH);
        this.godScreen.getRightPanel().add(buttonPanel, BorderLayout.SOUTH);


        boolean isGodChooser = this.gui.getController().isClientPlayerGodChooser();
        if (isGodChooser) {
            this.godScreen.setTitle("Select " + playersNumber + " Gods");
        } else {
            this.godScreen.setTitle("Select a God");
        }
    }


    private void addListeners() {
        boolean isGodChooser = this.gui.getController().isClientPlayerGodChooser();
        List<String> gods = isGodChooser ? View.getGodsNamesList() : this.selectableGods;
        ArrayList<JButton> godButtons = this.godScreen.getButtons();
        for (int i = 0; i < gods.size(); i++) {
            this.godChoiceJButtonListener = new GodChoiceJButtonListener(this);
            godButtons.get(i).addActionListener(godChoiceJButtonListener);
        }
    }


    // todo add interface
    public void showGuiOnTurn() {

        this.playersNumber = gui.getPlayersNumber();
        this.removeAll();

        try {
            LayoutManager layoutManager = new BorderLayout();
            this.setLayout(layoutManager);
            this.setBorder(BorderFactory.createEmptyBorder(20,20, 20,20));
            this.godScreen = new GodScreen(this.playersNumber, this.selectableGods);
            this.godScreen.setOpaque(false);
            this.add(this.godScreen, BorderLayout.CENTER);
            //drawGodChoice(this, this.gui, this.possibleGodsPanel, this.innerPanel, this.innerPanel2, this.titleLabel, this.buttons);
            setSpecific();
            //commonGodChoose(possibleGodsPanel, continueButton, innerPanel, innerPanel2);
            addListeners();
            this.revalidate();
            this.repaint();
        } catch(IOException e) {
            e.printStackTrace();
        }

        this.revalidate();
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        boolean isGodChooser = gui.getController().isClientPlayerGodChooser();

        if(isGodChooser && this.selectedGods.size() != playersNumber) {
            JOptionPane.showMessageDialog(gui.getMainFrame(),  "You must select " + playersNumber + " gods!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if(!isGodChooser && this.selectedGods.size() != 1) {
            JOptionPane.showMessageDialog(gui.getMainFrame(),  "You must select one god!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {

            this.selectedGods = this.selectedGods.stream().map(String::toLowerCase).collect(Collectors.toList());

            GodChoiceCommand godChoiceCommand = new GodChoiceCommand(this.selectedGods);
            gui.notify(godChoiceCommand);

            onGodChoiceSent();
        }
    }

    private void onGodChoiceSent() {
        this.removeAll();
        this.add(new LoadingComponent("Waiting...", Color.BLACK));
        this.revalidate();
    }
}
