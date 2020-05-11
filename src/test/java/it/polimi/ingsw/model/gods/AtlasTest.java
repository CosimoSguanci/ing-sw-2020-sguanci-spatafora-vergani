package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AtlasTest {
    @Test
    public void atlasBuildDomeAnywhereTest()  {




        Atlas atlas = new Atlas();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        atlas.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.DOME);

        assertSame(BlockType.DOME, match.getMatchBoard().getCell(0, 1).getLevel());
    }

    @Test
    public void atlasStandardBuild()  {




        Atlas atlas = new Atlas();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        atlas.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertSame(BlockType.LEVEL_ONE, match.getMatchBoard().getCell(0, 1).getLevel());
    }

}