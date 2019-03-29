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

    public Vehicle(int id, double startTime, int entranceIntersection, int entranceDirection) {
        this.id = id;
        this.startTime = startTime;
        this.entranceIntersection = entranceIntersection;
        this.entranceDirection = entranceDirection;
    }

    public String toString() {
        String str = "";
        str += id + " ";
        str += String.format("%.2f", startTime) + " ";
        str += String.format("%.2f", endTime) + " ";
        str += entranceIntersection + " ";
        str += entranceDirection + " ";
        str += exitIntersection + " ";
        str += exitDirection;
        return str;
    }
}
