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
    int lastPos;
    int lane;
    int speed;
    int lastSpeed;
    int entranceIntersection;
    Direction entranceDirection;
    int exitIntersection;
    Direction exitDirection;
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
     * @param id vehicle id
     * @param len vehicle length
     * @param pos vehicle position (front)
     * @param lane the lane the vehicle is on
     * @param speed the speed of the vehicle
     * @param startTime the entrance time of the vehicle
     */
    public Vehicle(int id, int len, int pos, int lane, int speed, double startTime,
                   int entranceIntersection, Direction entranceDirection) {
        this.id = id;
        this.len = len;
        this.pos = pos;
        this.lane = lane;
        this.speed = speed;
        this.startTime = startTime;
        this.entranceIntersection = entranceIntersection;
        this.entranceDirection = entranceDirection;
        this.isFollowingLight = false;
    }

    /**
     * Update vehicle position, speed and lane based on current status
     */
    public void update(double now) {
        // 1. tell if change lane based on 4 rules:
        //       1.0 pos is not following light
        //       1.1 gap_i < l
        //       1.2 gap_oi > l
        //       1.3 gap_obacki > l_oback = v_oback + 1
        //       1.4 random
        // 2. if change lane, keep v_i, if keep lane:
        //       2.1 if v_i < v_max, v_i += 1
        //       2.2 if v_i > gap_i, v = gap_i (consider light: if green, treat like following car)
        //       2.3 if v_i > 0, randomly v_i -= 1

        // Change lane
        boolean changeLane = false;
        if (!isFollowingLight) {
            int gap = leader == null ? Parameter.END_POSITION - lastPos : leader.lastPos - leader.len - lastPos;
            Vehicle leaderOther = lane == 0 ? rightLeader : leftLeader;
            Vehicle laggerOther = lane == 0 ? rightLagger : leftLagger;
            int gapOther = leaderOther == null ? Parameter.END_POSITION - lastPos : leaderOther.lastPos - leaderOther.len - lastPos;
            int gapOtherBack = laggerOther == null ? lastPos : lastPos - len - laggerOther.lastPos;
            int speedOtherBack = laggerOther == null ? 0 : laggerOther.lastSpeed;
            int l = lastSpeed + Parameter.COMFORT_ACC;
            double random = FileIo.rand.nextDouble();
            changeLane = gap < l && gapOther > l && gapOtherBack > speedOtherBack + 1 &&
                    random > Parameter.PROB_CHANGE_LANE;
            if (changeLane) {
                lane = 1 - lane;
            }
        }

        // Update speed and position
        if (speed > 0 && FileIo.rand.nextDouble() < Parameter.PROB_BRAKE) {
            speed = speed - Parameter.COMFORT_ACC / 2 < 0 ? 0 : speed - Parameter.COMFORT_ACC / 2;
        } else {
            if (!changeLane) {
                if (speed < Parameter.MAX_SPEED) {
                    speed = speed + Parameter.COMFORT_ACC > Parameter.MAX_SPEED ? Parameter.MAX_SPEED : speed + Parameter.COMFORT_ACC;
                }
            }
        }
        int gap;
        if (isFollowingLight && !trafficLight.isSouthThroughGreen(now)) {
            gap = trafficLight.getPos() - lastPos;
        } else {
            gap = leader == null ? Integer.MAX_VALUE : leader.lastPos - leader.len - lastPos;
        }
        if (speed >= gap) {
            speed = gap - 1;
        }
        pos += speed;
    }

    @Override
    public int compareTo(Vehicle v) {
        return v.pos - this.pos;
    }

    public String toString() {
        String str = "";
        str += id + ",";
        str += lane + ",";
        str += String.format("%.2f", startTime) + ",";
        str += String.format("%.2f", endTime) + ",";
        str += entranceIntersection + ",";
        str += entranceDirection + ",";
        str += exitIntersection + ",";
        str += exitDirection;
        return str;
    }
}