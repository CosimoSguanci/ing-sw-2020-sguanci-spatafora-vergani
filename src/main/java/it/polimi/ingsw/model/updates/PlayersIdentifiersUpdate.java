package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

import java.util.Map;

public class PlayersIdentifiersUpdate extends Update { // TODO Consider Removing

    private final Map<String, String> identifiers;

    public PlayersIdentifiersUpdate(Map<String, String> identifiers) {
        super(null, null);
        this.identifiers = identifiers;
    }

    public Map<String, String> getIdentifiers() {
        return this.identifiers;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }

}
