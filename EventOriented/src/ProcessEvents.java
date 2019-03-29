/**
 * ProcessEvents.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * The main entry point for CSE6730 Project 2
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

public class ProcessEvents {
    // Event queue to store all the event in the order of time
    static private PriorityQueue<Event> eventQueue = new PriorityQueue<>();
    static private ArrayList<Vehicle> enteringVehs = new ArrayList<>();
    // Current time
    static private double now = 0;
    // Travelling time from starting point to Intersection 1
    static private final double BETWEEN_START_INTERSECTION1 = 15;

    /**
    public ProcessEvents() {
        this.now = 0;
        this.eventQueue = new PriorityQueue<>();
        this.enteringVehs = new ArrayList<>();
    }
     */

    public static void main() {
        // ProcessEvents processor = new ProcessEvents();
        // read the input file and generate the entering vehs/flow
        FileIo ioHandler = new FileIo();
        ioHandler.readFile();
        ioHandler.generateFlow();

        EventHandler handler = EventHandler.getInstance();


        // processing loop
        Iterator itr = eventQueue.iterator();
        while (itr.hasNext()) {
            Event currentEvent = eventQueue.poll();
            now = currentEvent.time;
            handler.handleEvent(currentEvent);
        }
    }

    public static ArrayList<Vehicle> getEnteringVehs() {
        return enteringVehs;
    }

    private void setEnteringVehs() {
        if (enteringVehs.isEmpty()) {
            return;
        }
        // int i = enteringVehs.size() - 1;
        for (int i = 0; i < enteringVehs.size(); i++) {
            Vehicle veh = enteringVehs.get(i);
            Event depart = new Event(veh.startTime + BETWEEN_START_INTERSECTION1, EventName.Departure, veh.entranceIntersection + 1, veh);
            eventQueue.add(depart);
        }
    }

    public static PriorityQueue<Event> getEventQueue() {
        return eventQueue;
    }


}
