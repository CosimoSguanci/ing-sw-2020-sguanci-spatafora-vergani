package it.polimi.ingsw.view;

import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.observer.Observable;

import java.util.ArrayList;
import java.util.List;

public abstract class View extends Observable<Object> {

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