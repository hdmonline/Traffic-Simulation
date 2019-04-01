/**
 * VehicleProcess.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * The class of VehicleProcess.
 */

public class VehicleProcess implements Runnable {
    static final Scheduler schedular = Scheduler.getInstance();
    int id;
    double startTime;
    double endTime;
    int entranceIntersection;
    Direction entranceDirection;
    int exitIntersection;
    Direction exitDirection;

    public VehicleProcess(int id) {
        this.id = id;
    }

    public VehicleProcess(int id, double startTime, int entranceIntersection, Direction entranceDirection) {
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

    @Override
    public void run() {

    }
}
