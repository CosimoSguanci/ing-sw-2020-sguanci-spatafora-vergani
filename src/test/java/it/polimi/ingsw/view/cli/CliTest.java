package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.updates.ChooseGodsUpdate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CliTest {

    @Disabled
    @Test
    void startTest() {
        Match.clearInstances();


        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player p = new Player("Andrea",  match);
        match.addPlayer(p);
        Client client = null;
        try {
            client = new Client();
        } catch (IOException e){
            e.printStackTrace();
        }
        Cli cli = new Cli(client, null);

        boolean isGodChooser = true;
        ChooseGodsUpdate chooseGodsUpdate = new ChooseGodsUpdate(p.ID, isGodChooser,null);
        cli.update(chooseGodsUpdate);
        assertThrows(BadCommandException.class, ()-> {
            new Scanner("select apollo");
        });
    }

    @Disabled
    @Test
    void update() {
    }
}