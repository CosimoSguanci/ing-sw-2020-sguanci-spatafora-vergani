package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AtlasTest {
    @Test
    public void atlasBuildDomeAnywhereTest()  {

        Board.clearInstances();
        Match.clearInstances();

        Atlas atlas = new Atlas();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        atlas.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.DOME);

        assertSame(BlockType.DOME, match.getMatchBoard().getCell(0, 1).getLevel());
    }

    @Test
    public void atlasStandardBuild()  {

        Board.clearInstances();
        Match.clearInstances();

        Atlas atlas = new Atlas();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        atlas.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertSame(BlockType.LEVEL_ONE, match.getMatchBoard().getCell(0, 1).getLevel());
    }

    @Test
    public void getGodInfoTest() {
        Atlas atlas = new Atlas();

        Map<String, String> info = atlas.getGodInfo();

        assertEquals(info.get("name"), Atlas.NAME);
        assertEquals(info.get("description"), Atlas.DESCRIPTION);
        assertEquals(info.get("power_description"), Atlas.POWER_DESCRIPTION);
    }
}