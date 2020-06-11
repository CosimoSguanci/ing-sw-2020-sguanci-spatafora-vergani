package it.polimi.ingsw.view;

import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.observer.Observable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class View extends Observable<Object> {

    public abstract void start();

    public static void playOnTurnSound() {
        try {
            URL defaultSound = View.class.getResource("/turn.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(defaultSound);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start( );
        } catch (Exception ignored) {}
    }

    public static StringBuilder listToStringBuilder(List<String> value) {
        if (value.size() == 0) {
            return null;
        }
        StringBuilder result = new StringBuilder(value.get(0));
        for (int i = 1; i < value.size(); i++) {
            result.append(", ").append(value.get(i));
        }
        return result;
    }

    public static List<String> getGodsNamesList() {
        return new ArrayList<>(GodsUtils.getGodsInfo().keySet());  //list of gods' names
    }
}