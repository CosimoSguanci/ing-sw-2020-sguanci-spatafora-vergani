package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;
import org.mockito.Mockito;

public class UpdateTest {
    public static UpdateHandler getMockUpdateHandler() {

        return Mockito.spy(new UpdateHandler() {
            @Override
            public void handle(GamePhaseUpdate update) {
                System.out.println("GamePhaseUpdate");
            }

            @Override
            public void handle(InitialInfoUpdate update) {
                System.out.println("InitialInfoUpdate");
            }

            @Override
            public void handle(MatchStartedUpdate update) {
                System.out.println("MatchStartedUpdate");
            }

            @Override
            public void handle(GodsUpdate update) {
                System.out.println("GodsUpdate");
            }

            @Override
            public void handle(BoardUpdate update) {
                System.out.println("BoardUpdate");
            }

            @Override
            public void handle(ErrorUpdate update) {
                System.out.println("ErrorUpdate");
            }

            @Override
            public void handle(TurnUpdate update) {
                System.out.println("TurnUpdate");
            }

            @Override
            public void handle(WinUpdate update) {
                System.out.println("WinUpdate");
            }

            @Override
            public void handle(LoseUpdate update) {
                System.out.println("LoseUpdate");
            }

            @Override
            public void handle(ServerUnreachableUpdate update) {
                System.out.println("ServerUnreachableUpdate");
            }

            @Override
            public void handle(DisconnectedPlayerUpdate update) {
                System.out.println("DisconnectedPlayerUpdate");
            }
        });
    }
}
