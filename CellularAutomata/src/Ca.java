/**
 * Ca.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * The main entry point for CSE6730 Project 2 - Cellular Automata Model
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Ca {
    static private ArrayList<Vehicle> vehs = new ArrayList<>();
    static private ArrayList<Vehicle> enteringVehs = new ArrayList<>();
    static private ArrayList<Vehicle> finishedVehs = new ArrayList<>();

    static private ArrayList<TrafficLight> trafficLights = new ArrayList<>();

    static private double now = 0; // in second

    static private final int END_POSITION = 300;
    static private final double TIME_INTERVAL = 1;
    static public final double SIMULATION_TIME = 15; // minute

    public static void main() {
        // TODO: Initialize trafficLights

        // Sort the traffic lights by position
        Collections.sort(trafficLights);

        // Read input file to get all cars into enteringVehs
        FileIo ioHandler = new FileIo();
        ioHandler.readFile();
        ioHandler.generateFlow();

        // Sort the enteringVehs by startTime in descending order
        enteringVehs.sort(Comparator.comparing((Vehicle v) -> v.startTime));

        // Terminate when the interval is over 15min, no car on the street, and no car is about to start
        while (now < SIMULATION_TIME * 60 || vehs.size() > 0 || enteringVehs.size() > 0) {
            // Put entering cars to the lanes
            enteringVehs();

            // Update vehicle's speed, position and lane
            updateVehs();

            // Update vehicle's leaders, laggers and other information
            updateEnvironment();

            // Increase interval
            now += TIME_INTERVAL;
        }

        // TODO: Write the result to file

    }

    // Put any vehicle entering the tracking area to the lane
    private static void enteringVehs() {
        if (enteringVehs.isEmpty()) {
            return;
        }
        Vehicle veh;
        ArrayList<Vehicle> found = new ArrayList<>();
        for (int i = 0; i < enteringVehs.size(); i++) {
            veh = enteringVehs.get(i);
            // Check if the entering pos is taken or not.
            if (posAvailable(veh.pos, veh.lane)) {
                // Remove vehicle from enteringVehs
                enteringVehs.remove(veh);
                // Put the vehicle on road
                veh.startTime = now;
                vehs.add(veh);
                found.add(veh);
            }
        }
        enteringVehs.removeAll(found);
    }

    /**
     * Update each vehicle's position, speed and lane
     */
    private static void updateVehs() {
        for (Vehicle veh : vehs) {
            veh.update();
            // If the vehicle is exiting the tracking area
            if (veh.pos > END_POSITION) {
                veh.endTime = now;
                vehs.remove(veh);
                finishedVehs.add(veh);
            }
        }

        // TODO: check if this is the best place to sort the vehicle array
        Collections.sort(vehs);
    }

    /**
     * Update each vehicle's leaders, laggers and isFollowingLight
     */
    private static void updateEnvironment() {
        for (int i = 0; i < vehs.size(); i++) {
            updateLeader(i);
            updateLagger(i);
            updateLeaderRight(i);
            updateLaggerRight(i);
            updateLeaderLeft(i);
            updateLaggerLeft(i);
            vehs.get(i).isFollowingLight = false;
            vehs.get(i).trafficLight = null;
        }
        updateFollowingLight();
    }



    /**
     * Update the leader on the current lane for the ith vehicle
     *
     * @param i the index of the vehicle
     */
    private static void updateLeader(int i) {
        Vehicle veh = vehs.get(i);
        if (i == 0) {
            veh.leader = null;
            return;
        }
        int leader = i - 1;
        boolean success = vehs.get(leader).lane == veh.lane;
        while (!success) {
            if (leader == 0) {
                veh.leader = null;
                return;
            } else {
                leader--;
            }
            success = vehs.get(leader).lane == veh.lane;
        }
        veh.leader = vehs.get(leader);
    }

    private static void updateLagger(int i) {
        Vehicle veh = vehs.get(i);
        int n = vehs.size();
        if (i == n - 1) {
            veh.lagger = null;
            return;
        }
        int lagger = i + 1;
        boolean success = vehs.get(lagger).lane == veh.lane;
        while (!success) {
            if (lagger == n - 1) {
                veh.lagger = null;
                return;
            } else {
                lagger++;
            }
            success = vehs.get(lagger).lane == veh.lane;
        }
        veh.lagger = vehs.get(lagger);
    }

    private static void updateLeaderRight(int i) {
        Vehicle veh = vehs.get(i);
        if (i == 0 || veh.lane == 1) {
            veh.rightLeader = null;
            return;
        }
        int leader = i - 1;
        boolean success = vehs.get(leader).lane == 1;
        while (!success) {
            if (leader == 0) {
                veh.rightLeader = null;
                return;
            } else {
                leader--;
            }
            success = vehs.get(leader).lane == 1;
        }
        veh.rightLeader = vehs.get(leader);
    }

    private static void updateLaggerRight(int i) {
        Vehicle veh = vehs.get(i);
        int n = vehs.size();
        if (i == n - 1 || veh.lane == 1) {
            veh.rightLagger = null;
            return;
        }
        int lagger = i + 1;
        boolean success = vehs.get(lagger).lane == 1;
        while (!success) {
            if (lagger == n - 1) {
                veh.rightLagger = null;
                return;
            } else {
                lagger++;
            }
            success = vehs.get(lagger).lane == 1;
        }
        veh.rightLagger = vehs.get(lagger);
    }

    private static void updateLeaderLeft(int i) {
        Vehicle veh = vehs.get(i);
        if (i == 1 || veh.lane == 0) {
            veh.leftLeader = null;
            return;
        }
        int leader = i - 1;
        boolean success = vehs.get(leader).lane == 0;
        while (!success) {
            if (leader == 0) {
                veh.leftLeader = null;
                return;
            } else {
                leader--;
            }
            success = vehs.get(leader).lane == 0;
        }
        veh.leftLeader = vehs.get(leader);
    }

    private static void updateLaggerLeft(int i) {
        Vehicle veh = vehs.get(i);
        int n = vehs.size();
        if (i == n - 1 || veh.lane == 0) {
            veh.leftLagger = null;
            return;
        }
        int lagger = i - 1;
        boolean success = vehs.get(lagger).lane == 0;
        while (!success) {
            if (lagger == n + 1) {
                veh.leftLagger = null;
                return;
            } else {
                lagger++;
            }
            success = vehs.get(lagger).lane == 0;
        }
        veh.leftLagger = vehs.get(lagger);
    }

    // Update isFollowingLight for ith vehicle
    private static void updateFollowingLight() {
        int i = 0;
        int n = vehs.size();
        for (TrafficLight tl : trafficLights) {
            while (vehs.get(i).pos >= tl.getPos()) {
                i++;
            }
            vehs.get(i).isFollowingLight = true;
            vehs.get(i).trafficLight = tl;
        }
    }

    private static boolean posAvailable(int pos, int lane) {
        return vehs.stream().filter(v -> v.pos == pos && v.lane == lane).findFirst().isPresent();
    }

    public static ArrayList<Vehicle> getEnteringVehs() {
        return enteringVehs;
    }

    public static ArrayList<Vehicle> getFinishedVehs() {
        return finishedVehs;
    }
}