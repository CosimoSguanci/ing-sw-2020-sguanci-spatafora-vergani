package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.network.client.Client;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SendObjectConcurrencyTest {

    @Disabled
    @Test
    public void sendObjectConcurrencyStressTest() throws IOException, InterruptedException, ExecutionException {

        System.out.println("TESTING SEND OBJECT INFO CONCURRENCY ...");
        System.out.println();

        AtomicBoolean exception = new AtomicBoolean(false);

        ExecutorService es = Executors.newCachedThreadPool();

        Client client1 = new Client();

        client1.sendPlayersNumber(2);

        Client client2 = new Client();

        client2.sendPlayersNumber(2);

        //client1.setupUpdateListener();

        for (int i = 0; i < 2000; i++) {

            es.execute(() -> {
                try {
                    InitialInfoCommand initialInfoCommand = new InitialInfoCommand("nickname", PrintableColor.RED);
                    client1.sendCommand(initialInfoCommand);
                } catch (Exception e) {
                    e.printStackTrace();
                    exception.set(true);
                }
            });


        }

        // Wait for other threads
        es.shutdown();
        es.awaitTermination(5, TimeUnit.SECONDS);

        assert !exception.get();

        assert true;


    }

}
