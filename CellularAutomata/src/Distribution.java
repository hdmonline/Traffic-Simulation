/**
 * Distribution.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * Helper class for inter arrival interval distributions
 */
class Distribution {
    int intersection;
    int direction;
    double interval[];
    double prob[];
    double cumuProb[];

    public Distribution(int intersection, int direction, int numBins) {
        this.intersection = intersection;
        this.direction = direction;

        this.interval = new double[numBins];
        this.prob = new double[numBins];
        this.cumuProb = new double[numBins];
    }
}
