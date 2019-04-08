/**
 * Vehicle.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * Class for vehicles
 */

class Vehicle implements Comparable<Vehicle> {
    int id;
    int len;
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
     * @param len
     * @param pos
     * @param lane
     * @param speed
     * @param startTime
     */
    public Vehicle(int id, int len, int pos, int lane, int speed, double startTime,
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
        // TODO: 1. tell if change lane based on 4 rules:
        //       1.0 pos is not following light
        //       1.1 gap_i < l
        //       1.2 gap_oi > l
        //       1.3 gap_obacki > l_oback = v_oback + 1
        //       1.4 random
        // TODO: 2. if change lane, keep v_i, if keep lane:
        //       2.1 if v_i < v_max, v_i += 1
        //       2.2 if v_i > gap_i, v = gap_i (consider light: if green, treat like following car)
        //       2.3 if v_i > 0, randomly v_i -= 1
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