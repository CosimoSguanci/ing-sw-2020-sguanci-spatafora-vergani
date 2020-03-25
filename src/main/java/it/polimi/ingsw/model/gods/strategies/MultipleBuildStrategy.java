package it.polimi.ingsw.model.gods.strategies;

public interface MultipleBuildStrategy extends GodStrategy{
    int getBuildCount();
    void increaseBuildCount();
    boolean canBuildAgain();
}
