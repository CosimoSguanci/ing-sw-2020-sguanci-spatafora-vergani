package it.polimi.ingsw.view.gui.components;

import javax.swing.*;
import java.awt.*;

public class LoadingComponent extends JPanel {
    private String loadingMessage;

    public LoadingComponent(String loadingMessage) {
        this.loadingMessage = loadingMessage;

        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        this.add(new JLabel(this.loadingMessage), BorderLayout.CENTER);
    }


}
