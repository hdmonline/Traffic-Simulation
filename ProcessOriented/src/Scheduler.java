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
    private PriorityQueue<Event> eventQueue;

    // Vehicles generated from flow generator
    private ArrayList<VehicleProcess> enteringVehs = new ArrayList<>();
    // Vehicles exited the measuring area
    private ArrayList<VehicleProcess> finishedVehs = new ArrayList<>();

    private EventHandler eventHandler;

    private FileIo ioHandler;

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
        ioHandler = new FileIo();
        eventHandler = EventHandler.getInstance();
        eventQueue = new PriorityQueue<>();
        time = 0;
    }

    public static Scheduler getInstance() {
        if (instance == null) {
            instance = new Scheduler();
        }
        return instance;
    }

    public static void main(String args[]) {
        Scheduler scheduler = getInstance();
        Thread schedulerThread = new Thread(scheduler);

        // read the input file and generate the entering vehs/flow
        scheduler.ioHandler.readFile();
        scheduler.ioHandler.generateFlow();
        scheduler.initializeEventQueue();

        // Run the main thread
        schedulerThread.start();
    }

    @Override
    public synchronized void run() {
        while(!eventQueue.isEmpty()) {
            Event currEvent = eventQueue.poll();
            time = currEvent.time;
            ioHandler.writeEvent(currEvent);
            eventHandler.handleEvent(currEvent);
        }
    }

    /**
     * Add start events of each vehicles to the event queue
     */
    private void initializeEventQueue() {
        if (enteringVehs.isEmpty()) {
            return;
        }

        for (VehicleProcess veh : enteringVehs) {
            Thread vehThread = new Thread(veh);
            addScheduleEvent(new Event(veh.startTime, EventType.Enter, veh, vehThread));
        }
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
    public synchronized void addScheduleEvent(Event event) {
        eventQueue.add(event);
    }

    public synchronized double getTime() {
        return time;
    }

    public ArrayList<VehicleProcess> getEnteringVehs() {
        return enteringVehs;
    }

    public ArrayList<VehicleProcess> getFinishedVehs() {
        return finishedVehs;
    }
}
