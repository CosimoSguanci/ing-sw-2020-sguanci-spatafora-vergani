package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.ui.JRoundButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class InitialInfo extends JPanel implements ActionListener {

    private static final String loadingMsgBefore = "Waiting for other players...";
    private static final String loadingMsgAfter = "Waiting for other players...";

    private static final String errorDialogTitle = "Error";
    private static final String errorDialogMessage = "You can't use this nickname!" + System.lineSeparator() +
            "Nicknames already used are: ";

    private JTextField nicknameTextField;
    private JComboBox<PrintableColor> color;
    private String standardImgPath = "src/main/resources/images/InitialInfo/";
    private String externalImgPath = "src/main/resources/images/";
    private Image backgroundImage = new ImageIcon(standardImgPath + "backgroundTemple.png").getImage();
    private Font font = Gui.getFont(Gui.FONT_REGULAR, 20);
    private Color textColor = Color.WHITE;
    private int buttonWidth = 60;


    private List<PrintableColor> selectableColors;
    private List<String> selectedNicknames;


    private Gui gui;


    public InitialInfo() { // Controller

        this.gui = Gui.getInstance();
        this.add(new LoadingComponent(loadingMsgBefore, Color.WHITE));
    }

    public void setSelectableColors(List<PrintableColor> selectableColors) {
        this.selectableColors = selectableColors;

        this.showGuiOnTurn();
    }

    public void setSelectedNicknames(List<String> selectedNicknames) {
        this.selectedNicknames = selectedNicknames;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    // todo interface
    public void showGuiOnTurn() {
        // todo add actual GUI

        this.removeAll();


        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        //borderLayout for nickname and color, in the north
        JPanel nicknameColorPanel = new JPanel();
        nicknameColorPanel.setLayout(new BorderLayout());
        nicknameColorPanel.setOpaque(false);

        //nickname
        JPanel panelNickname = new JPanel();
        panelNickname.setLayout(new BoxLayout(panelNickname, BoxLayout.Y_AXIS));
        panelNickname.setOpaque(false);
        JPanel centredNickname = new JPanel();
        centredNickname.setOpaque(false);
        JLabel labelNickname = new JLabel("Insert a nickname:   (1 word)");
        labelNickname.setForeground(textColor);
        labelNickname.setFont(font);
        centredNickname.add(labelNickname);
        this.nicknameTextField = new JTextField();
        this.nicknameTextField.setFont(this.font);
        this.nicknameTextField.setHorizontalAlignment(JTextField.CENTER);
        panelNickname.add(centredNickname);
        panelNickname.add(Box.createVerticalGlue());
        panelNickname.add(this.nicknameTextField);

        //color
        JPanel panelColor = new JPanel();
        panelColor.setLayout(new BoxLayout(panelColor, BoxLayout.Y_AXIS));
        panelColor.setOpaque(false);
        JPanel centredColor = new JPanel();
        centredColor.setOpaque(false);
        JLabel labelColor = new JLabel("Choose a color: ");
        labelColor.setForeground(textColor);
        labelColor.setFont(font);
        centredColor.add(labelColor);

        if(selectableColors == null) {
            this.selectableColors = PrintableColor.getColorList();
        }

        PrintableColor [] colors = new PrintableColor[selectableColors.size()];

        colors = this.selectableColors.toArray(colors);

        this.color = new JComboBox<PrintableColor>(colors);

        ((JLabel)this.color.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);  //centred alignment for JComboBox alternatives
        panelColor.add(centredColor);
        panelNickname.add(Box.createVerticalGlue());
        panelColor.add(this.color);

        panelNickname.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
        //panelColor.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));

        nicknameColorPanel.add(panelNickname, BorderLayout.NORTH);
        nicknameColorPanel.add(panelColor, BorderLayout.SOUTH);
        this.add(nicknameColorPanel, BorderLayout.NORTH);


        //button to continue must be south-east
        ImageIcon continueImg = new ImageIcon(this.standardImgPath + "button-play-normal.png");
        continueImg = new ImageIcon(continueImg.getImage().getScaledInstance(this.buttonWidth, -1, Image.SCALE_SMOOTH));
        JButton continueButton = new JButton(continueImg);
        continueButton.addActionListener(this);
        JPanel innerPanel = new JPanel();
        JPanel innerPanel2 = new JPanel();
        innerPanel.setLayout(new BorderLayout());
        innerPanel.setOpaque(false);
        innerPanel2.setLayout(new BorderLayout());
        innerPanel2.setOpaque(false);
        innerPanel2.add(continueButton, BorderLayout.EAST);

        //button to quit must be south-west
        ImageIcon quitImg = new ImageIcon(this.externalImgPath + "exit.png");
        quitImg = new ImageIcon(quitImg.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JRoundButton quitButton = new JRoundButton(quitImg);



        innerPanel2.add(quitButton, BorderLayout.WEST);

        innerPanel.add(innerPanel2);
        this.add(innerPanel, BorderLayout.SOUTH);

        this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        this.revalidate(); // Todo new component in CardLayout for Waiting?
        this.repaint();
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        Gui gui = Gui.getInstance(null, null);

        String nickname = nicknameTextField.getText().toLowerCase();

        if(this.selectedNicknames.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(nickname)) {
            JOptionPane.showMessageDialog(gui.getMainFrame(), errorDialogMessage + View.listToStringBuilder(selectedNicknames), errorDialogTitle, JOptionPane.ERROR_MESSAGE);
        }
        else {
            PrintableColor color = (PrintableColor) this.color.getSelectedItem();
            InitialInfoCommand initialInfoCommand = new InitialInfoCommand(nickname, color);
            gui.notify(initialInfoCommand);

            onInitialInfoSent();
        }
    }

    private void onInitialInfoSent() {
        this.removeAll();

        this.add(new LoadingComponent(loadingMsgAfter, Color.WHITE));

        this.revalidate();
        this.repaint();
    }
}
