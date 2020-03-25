package it.polimi.ingsw.model.gods.handlers;

public class MultipleBuildHandler {
    final int MAX_BUILD_COUNT;
    private int buildCount;

    public MultipleBuildHandler(int maxBuildCount) {
        this.MAX_BUILD_COUNT = maxBuildCount;
    }

    public int getBuildCount() {
        return buildCount;
    }
    public boolean canBuildAgain() {
        return buildCount < MAX_BUILD_COUNT;
    }
    public void increaseBuildCount() {this.buildCount++;}


}
