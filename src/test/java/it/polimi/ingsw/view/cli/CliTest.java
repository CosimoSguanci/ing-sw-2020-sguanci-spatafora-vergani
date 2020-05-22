package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.ClientApp;
import it.polimi.ingsw.ServerApp;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CliTest {

    @Disabled
    @Test
    void startConnectTest() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        new Thread() {
            public void run() {
                ServerApp.main(null);
            }
        }.start();
        assertEquals("Waiting for incoming connections...", out.toString());
        ClientApp.main(null);
        assertEquals("How many players do you want in your match? ", out.toString());
    }

    @Disabled
    @Test
    void update() {
    }
}