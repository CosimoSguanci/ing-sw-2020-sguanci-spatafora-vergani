package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.listeners.QuitButtonListener;
import it.polimi.ingsw.view.gui.ui.JRoundButton;
import it.polimi.ingsw.view.gui.ui.LoadingComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class InitialInfo extends JPanel implements ActionListener {

    private static final String loadingMsgBefore = "Waiting for other players...";
    private static final String loadingMsgAfter = "Waiting for other players...";

    private static final String errorDialogTitle = "Error";
    private static final String errorDialogMessage = "You can't use this nickname!" + System.lineSeparator() +
            "Nicknames already used are: ";
    private final String standardImgPath = "/images/InitialInfo/";
    private final Image backgroundImage = new ImageIcon(getClass().getResource(standardImgPath + "backgroundTemple.png")).getImage();
    private final Font font = Gui.getFont(Gui.FONT_REGULAR, 20);
    private final Color textColor = Color.WHITE;
    private final Gui gui;
    private JTextField nicknameTextField;
    private JComboBox<PrintableColor> color;
    private List<PrintableColor> selectableColors;
    private List<String> selectedNicknames;
    private String nickname;


    public InitialInfo() {

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
        JLabel labelNickname = new JLabel("Insert a nickname:");
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

        if (selectableColors == null) {
            this.selectableColors = PrintableColor.getColorList();
        }

        PrintableColor[] colors = new PrintableColor[selectableColors.size()];

        colors = this.selectableColors.toArray(colors);

        this.color = new JComboBox<>(colors);

        ((JLabel) this.color.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        panelColor.add(centredColor);
        panelNickname.add(Box.createVerticalGlue());
        panelColor.add(this.color);

        panelNickname.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        nicknameColorPanel.add(panelNickname, BorderLayout.NORTH);
        nicknameColorPanel.add(panelColor, BorderLayout.SOUTH);
        this.add(nicknameColorPanel, BorderLayout.NORTH);


        //button to continue must be south-east
        ImageIcon continueImg = new ImageIcon(getClass().getResource(this.standardImgPath + "button-play-normal.png"));
        int buttonWidth = 60;
        continueImg = new ImageIcon(continueImg.getImage().getScaledInstance(buttonWidth, -1, Image.SCALE_SMOOTH));
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
        String externalImgPath = "/images/";
        ImageIcon quitImg = new ImageIcon(getClass().getResource(externalImgPath + "exit.png"));
        quitImg = new ImageIcon(quitImg.getImage().getScaledInstance(buttonWidth, -1, Image.SCALE_SMOOTH));
        JRoundButton quitButton = new JRoundButton(quitImg);

        quitButton.addActionListener(new QuitButtonListener(this));
        innerPanel2.add(quitButton, BorderLayout.WEST);

        innerPanel.add(innerPanel2);
        this.add(innerPanel, BorderLayout.SOUTH);

        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        this.revalidate();
        this.repaint();
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        this.nickname = nicknameTextField.getText();

        if (this.selectedNicknames.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(nickname.toLowerCase())) {
            JOptionPane.showMessageDialog(gui.getMainFrame(), errorDialogMessage + View.listToStringBuilder(selectedNicknames), errorDialogTitle, JOptionPane.ERROR_MESSAGE);
        } else {
            PrintableColor color = (PrintableColor) this.color.getSelectedItem();
            InitialInfoCommand initialInfoCommand = new InitialInfoCommand(this.nickname, color);
            gui.notify(initialInfoCommand);

            onInitialInfoSent();
        }
    }

    private void onInitialInfoSent() {
        this.removeAll();

        this.add(new LoadingComponent(loadingMsgAfter, Color.WHITE));

        this.gui.getController().setClientPlayerNickname(this.nickname);

        this.revalidate();
        this.repaint();
    }
}
