package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.view.cli.Cli;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class OtherInfoHandler {
    private final Cli cli;

    private boolean soundPlayed = false;

    public OtherInfoHandler(Cli cli) {
        this.cli = cli;
    }

    public void printCurrentTurn() {
        String currentPlayerNickname = this.cli.getController().getCurrentPlayerNickname();
        if(currentPlayerNickname != null && this.cli.playerWithColor(currentPlayerNickname) != null) {
            if (!this.cli.getController().getCurrentPlayerID().equals(this.cli.getController().getClientPlayerID())) {  //not player's turn
                soundPlayed = false;
                this.cli.println("It's " + this.cli.playerWithColor(currentPlayerNickname) + "'s turn!");
            } else {  //client's turn
                this.cli.println("It's" + Cli.convertColorToAnsi(this.cli.getPlayersColors().get(currentPlayerNickname)) + " your " + PrintableColor.RESET + "turn!");
                if(!soundPlayed) {
                    try {
                        URL defaultSound = getClass().getResource("/turn.wav");
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(defaultSound);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start( );
                        soundPlayed = true;
                    } catch (Exception ignored) {}
                }
            }
            this.cli.newLine();
        }
    }

    public void printGamePhase(GamePhase currentGamePhase) {
        switch(currentGamePhase) {
            case INITIAL_INFO:
                this.cli.println("Players are choosing nickname and color... Wait for your turn.");
                this.cli.newLine();
                break;
            case CHOOSE_GODS:
                this.cli.println("Players are choosing their gods... Wait for your turn.");
                this.cli.newLine();
                break;
            case GAME_PREPARATION:

                if(this.cli.getController().getCurrentPlayerID().equals(this.cli.getController().getClientPlayerID())) {
                    this.cli.printGamePreparationInfo();
                }
                else {
                    this.cli.println("Players are placing their Workers... Wait for your turn.");
                    this.cli.newLine();
                }

                break;
            case MATCH_ENDED:
                this.cli.println("Do you want to play another match?");
                this.cli.newLine();
                break;
        }
    }
}
