/**
 * Scheduler.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * The main entry point for CSE6730 Project 2 - Process-oriented program
 * The singleton scheduler thread class, Scheduling all other VehicleProcess threads.
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Scheduler implements Runnable {
    private static Scheduler instance = null;

    // VehicleProcess queues, First -> In, Last -> Out
    private ArrayList<LinkedList<VehicleProcess>> southVehicleQueues;

    // Event queue to store all the event in the order of time
    private PriorityQueue<ScheduleEvent> eventQueue;

    private double time;

    /**
     * Private constructor for this singleton class
     */
    private Scheduler() {
        // Initialize the vehicleProcess queues for each traffic light
        southVehicleQueues = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            southVehicleQueues.set(i, new LinkedList<>());
        }

        eventQueue = new PriorityQueue<>();
        time = 0;
    }

    public static Scheduler getInstance() {
        if (instance == null) {
            instance = new Scheduler();
        }
        return instance;
    }

    public static void main() {
        Scheduler scheduler = getInstance();
    }

    @Override
    public void run() {

    }

    /**
     * Start event.
     */
    public synchronized void start() {

    }

    /**
     * Add vehicleProcess to the south vehicleProcess queue of certain street.
     *
     * @param street the street number
     * @param veh the vehicleProcess
     */
    public void addVehicleToSouthQueue(int street, VehicleProcess veh) {

    }

    /**
     * Add a scheduler event to the event queue.
     *
     * @param event event to be added
     */
    public synchronized void addSchedulerEvent(ScheduleEvent event) {

    }

    public synchronized double getTime() {
        return time;
    }
}
