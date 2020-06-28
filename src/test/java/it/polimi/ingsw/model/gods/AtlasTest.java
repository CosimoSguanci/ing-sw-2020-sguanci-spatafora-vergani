package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AtlasTest {

    private Atlas atlas;
    private Match match;
    private Worker worker;

    @BeforeEach
    public void initGameProperties() {

        this.atlas = new Atlas();
        this.match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        this.worker = player.getWorkerFirst();
    }

    @Test
    public void atlasBuildDomeAnywhereTest() {

        worker.setInitialPosition(0, 0);
        atlas.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.DOME);
        assertSame(BlockType.DOME, match.getMatchBoard().getCell(0, 1).getLevel());
    }

    @Test
    public void atlasStandardBuildTest() {

        worker.setInitialPosition(0, 0);
        atlas.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);
        assertSame(BlockType.LEVEL_ONE, match.getMatchBoard().getCell(0, 1).getLevel());
    }

    @Test
    public void atlasCheckBuildTest() {

        worker.setInitialPosition(0, 0);

        assertTrue(atlas.checkMove(worker, match.getMatchBoard().getCell(1, 1)));

        atlas.executeMove(worker, match.getMatchBoard().getCell(1, 1));

        assertFalse(atlas.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_TWO));
        assertTrue(atlas.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.DOME));
    }

}