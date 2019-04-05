/**
 * Scheduler.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * The main entry point for CSE6730 Project 2 - Process-oriented program
 * The singleton scheduler thread class, Scheduling all other VehicleProcess threads.
 */

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Scheduler implements Runnable {
    private static Scheduler instance = null;

    private EventHandler eventHandler;

    private FileIo ioHandler;

    private double time;

    ArrayList<VehicleProcess> enteringVehs;
    ArrayList<VehicleProcess> finishedVehs;


    /**
     * Private constructor for this singleton class
     */
    private Scheduler() {
        ioHandler = new FileIo();
        eventHandler = EventHandler.getInstance();
        enteringVehs = eventHandler.getEnteringVehs();
        finishedVehs = eventHandler.getFinishedVehs();
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
        // TODO: Handle FEL, then PEL
        PriorityQueue<Event> eventQueue = eventHandler.getEventQueue();
        while(!eventQueue.isEmpty()) {
            Event currEvent = eventQueue.poll();
            time = currEvent.time;
            ioHandler.writeEvent(currEvent);
            eventHandler.handleEvent(currEvent);

            // Traverse the waiting vehicle events
            for (Event event : eventHandler.getWaitingVehEvents()) {
                eventHandler.checkWait(event);
            }

            // Wait for notifying from other processes
            if (currEvent.type == EventType.Resume || currEvent.type == EventType.Enter) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Write results to file
        ioHandler.writeVehicles();
        ioHandler.closeEvnetWriter();
    }

    /**
     * Add start events of each vehicles to the event queue
     */
    private void initializeEventQueue() {
        if (eventHandler.getEnteringVehs().isEmpty()) {
            return;
        }

        for (VehicleProcess veh : enteringVehs) {
            // Thread vehThread = new Thread(veh);
            eventHandler.addScheduleEvent(new Event(veh.startTime, EventType.Enter, veh));
        }

        // TODO: generate light events.
        // Generate turnRed and turnGreen events in northbound dir during the whole simulation time
        TrafficLight[] trafficLights = eventHandler.getTrafficLights();
        for (TrafficLight tl : trafficLights) {
            tl.generateLightEvents();
        }
    }

    public double getTime() {
        return time;
    }
}
