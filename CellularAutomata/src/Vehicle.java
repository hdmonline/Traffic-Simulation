/**
 * Vehicle.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * Class for vehicles
 */

class Vehicle implements Comparable<Vehicle> {
    int id;
    int pos;
    int lane;
    int speed;
    int entranceIntersection;
    int entranceDirection;
    int exitIntersection;
    int exitDirection;
    Vehicle leader;
    Vehicle lagger;
    Vehicle leftLeader;
    Vehicle leftLagger;
    Vehicle rightLeader;
    Vehicle rightLagger;

    double startTime;
    double endTime;

    boolean isFollowingLight;
    TrafficLight trafficLight;

    /**
     * Constructor
     *
     * @param id
     * @param pos
     * @param lane
     * @param speed
     * @param startTime
     */
    public Vehicle(int id, int pos, int lane, int speed, double startTime,
                   int entranceIntersection, int entranceDirection) {
        this.id = id;
        this.pos = pos;
        this.lane = lane;
        this.speed = speed;
        this.startTime = startTime;
        this.entranceIntersection = entranceIntersection;
        this.entranceDirection = entranceDirection;
        this.isFollowingLight = false;
    }

    /**
     * TODO: Update vehicle position, speed and lane based on current status
     */
    public void update() {

    }

    @Override
    public int compareTo(Vehicle v) {
        return v.pos - this.pos;
    }

    public String toString() {
        String str = "";
        str += id + " ";
        str += lane + " ";
        str += speed + " ";
        str += String.format("%.2f", startTime) + " ";
        str += String.format("%.2f", endTime) + " ";
        str += entranceIntersection + " ";
        str += entranceDirection + " ";
        str += exitIntersection + " ";
        str += exitDirection;
        return str;
    }
}