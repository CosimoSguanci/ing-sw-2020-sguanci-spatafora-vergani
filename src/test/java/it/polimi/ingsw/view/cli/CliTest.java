package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.ClientApp;
import it.polimi.ingsw.ServerApp;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.updates.ChooseGodsUpdate;
import it.polimi.ingsw.network.client.UpdateListener;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.network.server.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CliTest {

    @Disabled
    @Test
    void startTest() {



        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = new Match(playersNum);
        Player p = new Player("Andrea",  new Model(match), match);
        match.addPlayer(p);
        Client client = null;
        try {
            client = new Client();
        } catch (IOException e){
            e.printStackTrace();
        }
        Cli cli = new Cli(client, null, null);

        boolean isGodChooser = true;
        ChooseGodsUpdate chooseGodsUpdate = new ChooseGodsUpdate(p.ID, isGodChooser,null);
        cli.update(chooseGodsUpdate);
        assertThrows(BadCommandException.class, ()-> {
            new Scanner("select apollo");
        });
    }

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