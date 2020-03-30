package it.polimi.ingsw.model.gods;

// Multiple Packages?

class MultipleBuildDelegate {
    private final int MAX_BUILD_COUNT;
    private int buildCount;

    MultipleBuildDelegate(int maxBuildCount) {
        this.MAX_BUILD_COUNT = maxBuildCount;
    }

    int getBuildCount() {
        return buildCount;
    }
    boolean canBuildAgain() {
        return buildCount < MAX_BUILD_COUNT;
    }
    void increaseBuildCount() {this.buildCount++;}
    void reinitializeBuildCount(){this.buildCount = 0;}

}
