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
    // Current time
    private static double now = 0;
    // Travelling time from starting point to Intersection 1
    private static final double BETWEEN_START_INTERSECTION1 = 15;

    public static void main(String[] args) {
        // ProcessEvents processor = new ProcessEvents();
        // read the input file and generate the entering vehs/flow
        FileIo ioHandler = new FileIo();
        ioHandler.readFile();
        ioHandler.generateFlow();

        EventHandler handler = EventHandler.getInstance();
        initializeEventQueue();

        // processing loop
        Iterator itr = eventQueue.iterator();
        while (itr.hasNext()) {
            Event currentEvent = eventQueue.poll();
            now = currentEvent.time;
            handler.handleEvent(currentEvent);
        }

        // TODO: write results to file
    }

    public static ArrayList<Vehicle> getEnteringVehs() {
        return enteringVehs;
    }

    private static void initializeEventQueue() {
        if (enteringVehs.isEmpty()) {
            return;
        }
        // int i = enteringVehs.size() - 1;
        for (int i = 0; i < enteringVehs.size(); i++) {
            Vehicle veh = enteringVehs.get(i);
            Event firstEvent;
            if (veh.entranceIntersection == 1 && veh.exitDirection == 1) {
                firstEvent = new Event(veh.startTime + BETWEEN_START_INTERSECTION1, getEventName(veh.entranceDirection), veh.entranceIntersection, veh);
            } else {
                firstEvent = new Event(veh.startTime, getEventName(veh.entranceDirection), veh.entranceIntersection, veh);
            }
            eventQueue.add(firstEvent);
        }
    }

    public static PriorityQueue<Event> getEventQueue() {
        return eventQueue;
    }

    private static EventName getEventName(int EntranceDir) {
        switch (EntranceDir) {
            case 1:
                return EventName.ArrivalSouth;
            case 2:
                return EventName.ArrivalEst;
            case 3:
                return EventName.ArrivalWest;
            default:
                System.out.println("Error - ProcessEvents.getEventName: Wrong entrance direction!");
                return null;
        }
    }
}
