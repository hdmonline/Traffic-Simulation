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
        // Parse arguments
        parseArguments(args);

        Scheduler scheduler = getInstance();
        Thread schedulerThread = new Thread(scheduler);

        // read the input file and generate the entering vehs/flow
        scheduler.ioHandler.readFile();
        scheduler.ioHandler.generateFlow();
        scheduler.initializeEventQueue();

        // Run the main thread
        schedulerThread.start();
    }

    /**
     * Parse the arguments.
     *
     * @param args Input arguments
     */
    private static void parseArguments(String[] args) {

        String arg;

        // The number of input arguments can only be 8 or 10
        if (args.length < 8 || args.length >12) {
            System.err.println("Usage: -input <file_path> -log <log_file_path> -vehs <veh_file_path> " +
                    "-time <simulation_time_in_seconds> [-seed <random_seed>]");
            System.exit(1);
        }

        // Iterate through the arguments
        int i = 0;
        while (i < args.length && args[i].startsWith("-")) {
            arg = args[i++];

            // -input
            if (arg.equals("-input")) {
                if (i < args.length) {
                    Parameter.INPUT_FILE = args[i++];
                } else {
                    System.err.println("-input requires a input path");
                    System.exit(1);
                }
            }

            // -log
            if (arg.equals("-log")) {
                if (i < args.length) {
                    Parameter.OUTPUT_EVENT_FILE = args[i++];
                } else {
                    System.err.println("-vehs requires a event output path");
                    System.exit(1);
                }
            }

            // -vehs
            if (arg.equals("-vehs")) {
                if (i < args.length) {
                    Parameter.OUTPUT_VEHICLE_FILE = args[i++];
                } else {
                    System.err.println("-vehs requires a vehicle output paths");
                    System.exit(1);
                }
            }

            // -time
            if (arg.equals("-time")) {
                if (i < args.length) {
                    Parameter.VEHICLE_TIME = Double.parseDouble(args[i++]);
                    Parameter.SIMULATION_TIME = Parameter.VEHICLE_TIME + 10 * 60;
                } else {
                    System.err.println("-time requires a float number");
                    System.exit(1);
                }
            }

            // -offset
                if (arg.equals("-offset")) {
                    if (i < args.length) {
                        Parameter.TRAFFIC_LIGHT_DELAY = Double.parseDouble(args[i++]);
                    } else {
                        System.err.println("-offset requires a float number");
                        System.exit(1);
                    }
            }

            // -seed
            if (arg.equals("-seed")) {
                if (i < args.length) {
                    Parameter.HAS_SEED = true;
                    Parameter.RANDOM_SEED = Long.parseLong(args[i++]);
                } else {
                    System.err.println("-time requires a integer");
                    System.exit(1);
                }
            }

        }
    }

    /**
     * The running method for the scheduler thread
     */
    @Override
    public synchronized void run() {
        // Handle FEL, then PEL
        PriorityQueue<Event> eventQueue = eventHandler.getEventQueue();
        LinkedList<Event> waitingVehEvents = eventHandler.getWaitingVehEvents();
        while(!eventQueue.isEmpty() || !waitingVehEvents.isEmpty() && time < Parameter.SIMULATION_TIME) {
            if (time > Parameter.SIMULATION_TIME) {
                eventHandler.turnAllGreens();
            }
            if (!eventQueue.isEmpty()) {
                Event currEvent = eventQueue.poll();
                time = currEvent.time;
                ioHandler.writeEvent(currEvent);
                eventHandler.handleEvent(currEvent);

                // Wait for notifying from other processes
                if (currEvent.type == EventType.Resume || currEvent.type == EventType.Enter) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            // Check every waiting vehicles in the queue
            eventHandler.checkWait();
        }

        // Write results to file
        ioHandler.writeVehicles();
        ioHandler.closeEvnetWriter();

        // All threads should be stopped here.
    }

    /**
     * Add start events of each vehicles to the event queue
     */
    private void initializeEventQueue() {
        if (eventHandler.getEnteringVehs().isEmpty()) {
            return;
        }

        // Generate turnRed and turnGreen events in northbound dir during the whole simulation time
        TrafficLight[] trafficLights = eventHandler.getTrafficLights();
        for (TrafficLight tl : trafficLights) {
            tl.generateLightEvents();
        }

        for (VehicleProcess veh : enteringVehs) {
            eventHandler.addEvent(new Event(veh.startTime, EventType.Enter,
                    veh.entranceIntersection, veh.entranceDirection, veh));
        }
    }

    /**
     * Get current time from the scheduler
     *
     * @return current time
     */
    public double getTime() {
        return time;
    }
}
