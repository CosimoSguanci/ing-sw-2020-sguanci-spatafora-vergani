package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PrintableColor;

import java.util.LinkedHashMap;

/**
 * This class defines a Game Manual. This manual is available to all players in all game phases
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class Manual {

    private static final String info = "General information";
    private static final String play = "How to play";
    private static final String winLose = "Win and lose a match";
    private static final String generalInfo = "Santorini is a strategy board-game;" +
            " in this version, you play online against 1 or 2 other players." +
            System.lineSeparator() +
            "Every player is identified by a unique nickname during a match.";
    private static final String howToPlay = "Players take turns, starting with the Start Player, who first" +
            " places his/her workers." + System.lineSeparator() +
            "On your turn, select one of your workers. In general, you must move and then build " +
            "with the selected worker." + System.lineSeparator() +
            "A Worker may move up a maximum of one level higher, " +
            "move down any number of levels lower, or move along the same level." +
            System.lineSeparator() + "Every move must be performed into a neighbouring cell, " +
            "where there is not a dome or another worker." + System.lineSeparator() +
            "A worker can build only in an unoccupied cell and can only increase its height of one level." +
            System.lineSeparator() + "Possible levels are: ground(0), 1, 2, 3, dome(4).";
    private static final String howToWinAndLose = "If one of your workers moves " +
            "up on top of level 3 during your turn, you instantly win!" +
            System.lineSeparator() + "You must always perform a move then build " +
            "on your turn. If you are unable to, you lose.";
    private static final String chooseGods = "Gods provide you with powerful abilities " +
            "that can be used throughout the game. " +
            "Every player has one god, chosen in the way described below." + System.lineSeparator() +
            "A random player becomes the god-chooser and selects a number of gods equal to the number of players." +
            System.lineSeparator() + "Then, every player chooses his/her god, and the god-chooser receives the remaining one.";
    private static final String gameWithGods = "God powers apply or are triggered at a " +
            "specific time, according to the god's power description." + System.lineSeparator() +
            "You can have more information about god powers in-game." + System.lineSeparator() +
            "REMEMBER: some god powers may cause workers to be 'forced' into another space." +
            System.lineSeparator() + "A Worker that is 'forced', is not considered to have moved. " +
            "For example, being 'forced' to move up at level 3 does not mean winning!";
    private static final String gods = "Gods";
    private static final String infoGods = "More about gods";

    private Manual() {
    }

    /**
     * This method is used to generate a String which contains all the game rules
     *
     * @return a String which contains information about info, play, win conditions, gods and info about them
     */
    public static String manual() {
        String info = PrintableColor.BOLD + Manual.info + PrintableColor.RESET;
        String play = PrintableColor.BOLD + Manual.play + PrintableColor.RESET;
        String winLose = PrintableColor.BOLD + Manual.winLose + PrintableColor.RESET;
        String gods = PrintableColor.BOLD + Manual.gods + PrintableColor.RESET;
        String infoGods = PrintableColor.BOLD + Manual.infoGods + PrintableColor.RESET;

        return info + System.lineSeparator() + generalInfo + System.lineSeparator() +
                play + System.lineSeparator() + howToPlay + System.lineSeparator() +
                winLose + System.lineSeparator() + howToWinAndLose + System.lineSeparator() +
                gods + System.lineSeparator() + chooseGods + System.lineSeparator() +
                infoGods + System.lineSeparator() + gameWithGods + System.lineSeparator();
    }

    /**
     * This method is a simple getter of a correspondence between info Title and the explanation of them
     *
     * @return a Map which contains a correspondence between info title and the explanation
     */
    public static LinkedHashMap<String, String> getRules() {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        result.put(info, generalInfo);
        result.put(play, howToPlay);
        result.put(winLose, howToWinAndLose);
        result.put(gods, chooseGods);
        result.put(infoGods, gameWithGods);
        return result;
    }

}
