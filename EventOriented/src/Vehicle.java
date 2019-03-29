/**
 * Vehicle.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * The class of Vehicle.
 */

public class Vehicle {
    public int id;
    public double startTime;
    public double endTime;
    int entranceIntersection;
    int entranceDirection;
    int exitIntersection;
    int exitDirection;

    public Vehicle(int id, double startTime, double endTime, int entranceIntersection, int entranceDirection, int exitIntersection, int exitDirection) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.entranceIntersection = entranceIntersection;
        this.entranceDirection = entranceDirection;
        this.exitIntersection = exitIntersection;
        this.exitDirection = exitDirection;
    }
}
