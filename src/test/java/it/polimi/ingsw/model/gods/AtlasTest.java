package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AtlasTest {
    @Test
    public void atlasBuildDomeAnywhereTest()  {


        Match.clearInstances();

        Atlas atlas = new Atlas();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        atlas.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.DOME);

        assertSame(BlockType.DOME, match.getMatchBoard().getCell(0, 1).getLevel());
    }

    @Test
    public void atlasStandardBuild()  {


        Match.clearInstances();

        Atlas atlas = new Atlas();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        atlas.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertSame(BlockType.LEVEL_ONE, match.getMatchBoard().getCell(0, 1).getLevel());
    }

}