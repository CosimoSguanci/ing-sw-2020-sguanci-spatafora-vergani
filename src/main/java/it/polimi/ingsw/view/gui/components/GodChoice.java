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

    private Font titleFont = Gui.getFont(Gui.FONT_BOLD, 20);
    private Font godLabelFont = Gui.getFont(Gui.FONT_BOLD, 14);


    private Map<String, Map<String, ImageIcon>> allIconGods = new HashMap<>(); // todo synchronized

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

        new Thread(() -> {
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


        }).start();
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


    private void setSpecific() {
        ImageIcon startImg = new ImageIcon("src/main/resources/images/done.png");
        startImg = new ImageIcon(startImg.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        this.continueButton = new JRoundButton(startImg);
        this.continueButton.setBorder(BorderFactory.createEmptyBorder(0,5, 0,5));

        ImageIcon infoImg = new ImageIcon(this.standardImgPath + "information.png");
        infoImg = new ImageIcon(infoImg.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        this.infoButton = new JRoundButton(infoImg);
        this.infoButton.setBorder(BorderFactory.createEmptyBorder(0,5, 0,5));

        boolean isGodChooser = this.gui.getController().isClientPlayerGodChooser();
        if(isGodChooser) {
            titleLabel.setText("Select " + playersNumber + " Gods");
        }
        else {
            titleLabel.setText("Select a God");
        }
    }

    private void addListeners() {
        this.continueButton.addActionListener(this);
        this.infoButton.addActionListener(new GodChoiceInfoButtonListener(this));

        boolean isGodChooser = this.gui.getController().isClientPlayerGodChooser();
        List<String> gods = isGodChooser ? View.getGodsNamesList() : this.selectableGods;
        for (int i = 0; i < gods.size(); i++) {
            this.godChoiceJButtonListener = new GodChoiceJButtonListener(this);
            this.buttons.get(i).addActionListener(godChoiceJButtonListener);
        }
    }

    private JRoundButton continueButton;
    private JRoundButton infoButton;
    private JLabel titleLabel = new JLabel();
    private JPanel possibleGodsPanel = new JPanel();
    private JPanel innerPanel = new JPanel();
    private JPanel innerPanel2 = new JPanel();

    /*private*/public void drawGodChoice(JPanel owner, Gui gui, JPanel possibleGodsPanel, JPanel innerPanel, JPanel innerPanel2, JLabel titleLabel, ArrayList<JButton> buttons) throws IOException {

        boolean isGodChooser = /*this.*/gui.getController().isClientPlayerGodChooser();

        LayoutManager layoutManager = new BorderLayout();
        /*this.setLayout(layoutManager);*/
        owner.setLayout(layoutManager);

        JPanel titlePanel = new JPanel();
        /*JPanel possibleGodsPanel = new JPanel();*/
        /*ImageIcon startImg = new ImageIcon("src/main/resources/images/done.png");
        startImg = new ImageIcon(startImg.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        JRoundButton continueButton = new JRoundButton(startImg);
        continueButton.addActionListener(this);*/
        /*JPanel innerPanel = new JPanel();
        JPanel innerPanel2 = new JPanel();/*

        /*JLabel titleLabel;

        if(isGodChooser) {
            titleLabel = new JLabel("Select " + playersNumber + " Gods");
        }
        else {
            titleLabel = new JLabel("Select a God");
        }*/

        //titleLabel = new JLabel();

        titleLabel.setFont(this.titleFont);
        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        /*this.add(titlePanel, BorderLayout.NORTH);
        this.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));*/
        owner.add(titlePanel, BorderLayout.NORTH);
        owner.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));


       // GridLayout gridLayout = new GridLayout(4, 4);
       // gridLayout.setVgap(10);
        // possibleGodsPanel.setLayout(gridLayout); Uncomment for SINGLE BUTTONS GOD

        GridLayout gridLayout = isGodChooser ? new GridLayout(4, 4) : new GridLayout(1, playersNumber);

        possibleGodsPanel.setLayout(gridLayout);

        ArrayList<ImageIcon> iconGods = new ArrayList<>();
        ImageIcon imageIcon;

        List<String> gods = isGodChooser ? View.getGodsNamesList() : this.selectableGods;

        /*this.godChoiceJButtonListener = new GodChoiceJButtonListener(this);*/
        for (int i = 0; i < gods.size(); i++) {
            imageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + gods.get(i).toLowerCase() + ".png")));
            imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(60,-1, Image.SCALE_SMOOTH));
            iconGods.add(imageIcon);

            JButton godBtn = new JButton(gods.get(i).toUpperCase(), iconGods.get(i));

            godBtn.setFont(this.godLabelFont);
            godBtn.setForeground(Color.BLACK); // Color.decode("0xc8102e")

            godBtn.addComponentListener(new ComponentAdapter() {

                @Override
                public void componentResized(ComponentEvent e) {
                    JButton btn = (JButton) e.getComponent();
                    Dimension size = btn.getSize();

                    String god = btn.getText().toLowerCase();

                    if(size.width > 600 || size.height > 600) {
                        btn.setIcon(allIconGods.get(god).get("very_big"));
                    }

                    else if(size.width > 400 || size.height > 400) {
                        btn.setIcon(allIconGods.get(god).get("big"));
                    }

                    else if(size.width > 260 || size.height > 260) {
                        btn.setIcon(allIconGods.get(god).get("medium_big"));
                    }

                    else if(size.width > 70 || size.height > 70) {
                        btn.setIcon(allIconGods.get(god).get("medium"));
                    }

                    else if(size.width > 40 || size.height > 40) {
                        btn.setIcon(allIconGods.get(god).get("medium_small"));
                    }

                    else {
                        btn.setIcon(allIconGods.get(god).get("small"));

                    }

                }

            });

            /*this.*/buttons.add(godBtn);
            //this.buttons.get(i).addActionListener(godChoiceJButtonListener);

            godBtn.setHorizontalTextPosition(JButton.CENTER);
            godBtn.setVerticalTextPosition(JButton.BOTTOM);
            godBtn.setOpaque(false);
            godBtn.setContentAreaFilled(false);

            String operSys = System.getProperty("os.name").toLowerCase();

            if(operSys.contains("mac")) {
                godBtn.setBorderPainted(false);
            }
            else {
                godBtn.setBorderPainted(true);
            }
            possibleGodsPanel.add(godBtn);


          /*  JPanel pannel = new JPanel();
            pannel.setLayout(new BoxLayout(pannel, BoxLayout.Y_AXIS));
            godBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            pannel.add(godBtn);

            pannel.setOpaque(false);

            possibleGodsPanel.add(pannel); */
        }

        //commonGodChoose(possibleGodsPanel, continueButton, innerPanel, innerPanel2);

        titlePanel.setOpaque(false);
        innerPanel.setOpaque(false);
        innerPanel2.setOpaque(false);
        possibleGodsPanel.setOpaque(false);
    }

    private void commonGodChoose(/*JPanel possibleGodsPanel, JButton continueButton, JPanel innerPanel, JPanel innerPanel2*/) {
        this.add(possibleGodsPanel);

        this.add(innerPanel, BorderLayout.SOUTH);
        innerPanel.setLayout(new BorderLayout());
        innerPanel2.setLayout(new BorderLayout());
        JPanel boxLayout = new JPanel();
        boxLayout.setLayout(new BoxLayout(boxLayout, BoxLayout.X_AXIS));
        boxLayout.setOpaque(false);
        boxLayout.add(infoButton);
        boxLayout.add(Box.createHorizontalGlue());
        boxLayout.add(continueButton);
        innerPanel2.add(boxLayout, BorderLayout.EAST);
        //innerPanel2.add(continueButton, BorderLayout.EAST);
        innerPanel.add(innerPanel2);
        innerPanel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
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

    // todo add interface
    public void showGuiOnTurn() {

        this.playersNumber = gui.getPlayersNumber();
        this.removeAll();

        try {
            drawGodChoice(this, this.gui, this.possibleGodsPanel, this.innerPanel, this.innerPanel2, this.titleLabel, this.buttons);
            setSpecific();
            commonGodChoose(/*possibleGodsPanel, continueButton, innerPanel, innerPanel2*/);
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
