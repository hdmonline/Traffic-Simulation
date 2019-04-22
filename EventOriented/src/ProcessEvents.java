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
