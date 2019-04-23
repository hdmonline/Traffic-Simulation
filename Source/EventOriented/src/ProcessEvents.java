/**
 * ProcessEvents.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * The main entry point for CSE6730 Project 2 - Event Oriented Model
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

public class ProcessEvents {
    // Event queue to store all the event in the order of time
    private static PriorityQueue<Event> eventQueue = new PriorityQueue<>();
    private static ArrayList<Vehicle> enteringVehs = new ArrayList<>();
    private static ArrayList<Vehicle> finishedVehs = new ArrayList<>();
    // Current time
    private static double now = 0;


    public static void main(String[] args) {
        // Parse arguments
        parseArguments(args);

        // read the input file and generate the entering vehs/flow
        FileIo ioHandler = new FileIo();
        ioHandler.readFile();
        ioHandler.generateFlow();

        EventHandler handler = EventHandler.getInstance();
        // Initialize the vehs arriving events and all traffic lights events
        initializeEventQueue(handler);

        // processing loop
        Iterator itr = eventQueue.iterator();
        while (itr.hasNext()) {
            Event currentEvent = eventQueue.poll();
            now = currentEvent.time;
            ioHandler.writeEvent(currentEvent);
            handler.handleEvent(currentEvent);
        }

        // Write results to file
        ioHandler.writeVehicles();
        ioHandler.closeEvnetWriter();
    }

    public static ArrayList<Vehicle> getEnteringVehs() {
        return enteringVehs;
    }

    private static void initializeEventQueue(EventHandler eventHandler) {
        if (enteringVehs.isEmpty()) {
            return;
        }

        for (Vehicle veh : enteringVehs) {
            Event firstEvent;
            // If it's entering from south before 10th street, then add travelling time from start point to 10th street intersection
            double delay = (veh.entranceIntersection == 1 && veh.entranceDirection == Direction.S) ? Parameter.BETWEEN_START_INTERSECTION1 : 0;
            firstEvent = new Event(veh.startTime + delay, EventType.Arrival, veh.entranceIntersection, veh.entranceDirection, veh);
            eventQueue.add(firstEvent);
        }

        // Generate traffic lights events during the whole simulation time
        TrafficLight[] trafficLights = eventHandler.getTrafficLights();
        for (TrafficLight tl : trafficLights) {
            tl.generateTrafficLights();
        }

        // Delete enteringVehs
        enteringVehs = null;
        System.gc();
    }

    /**
     * Parse the arguments.
     *
     * @param args Input arguments
     */
    private static void parseArguments(String[] args) {

        String arg;

        // The number of input arguments can only be 8 or 10
        if (args.length < 8 || args.length > 12) {
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
                    System.err.println("-inst requires a input path");
                    System.exit(1);
                }
            }

            // -log
            if (arg.equals("-log")) {
                if (i < args.length) {
                    Parameter.OUTPUT_EVENT_FILE = args[i++];
                } else {
                    System.err.println("-algo requires a algorithm name");
                    System.exit(1);
                }
            }

            // -vehs
            if (arg.equals("-vehs")) {
                if (i < args.length) {
                    Parameter.OUTPUT_VEHICLE_FILE = args[i++];
                } else {
                    System.err.println("-algo requires a algorithm name");
                    System.exit(1);
                }
            }

            // -time
            if (arg.equals("-time")) {
                if (i < args.length) {
                    Parameter.SIMULATION_TIME = Integer.parseInt(args[i++]);
                } else {
                    System.err.println("-time requires a integer");
                    System.exit(1);
                }
            }

            // -delay
            if (arg.equals("-delay")) {
                if (i < args.length) {
                    Parameter.TRAFFIC_LIGHT_DELAY = Double.parseDouble(args[i++]);
                } else {
                    System.err.println("-delay requires a float number");
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

    public static PriorityQueue<Event> getEventQueue() {
        return eventQueue;
    }

    public static ArrayList<Vehicle> getFinishedVehs() {
        return finishedVehs;
    }

    public static void addFinishedvehs(Vehicle veh) {
        finishedVehs.add(veh);
    }
}
