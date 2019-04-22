/**
 * EventHandler.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * A singleton class for handling all kinds of events.
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class EventHandler {
    private static EventHandler instance = null;
    private TrafficLight[] trafficLights;

    // Event queue to store all the event in the order of time
    private PriorityQueue<Event> eventQueue;
    // Waiting vehicles queue
    private LinkedList<Event> waitingVehEvents;
    // Vehicles generated from flow generator
    private ArrayList<VehicleProcess> enteringVehs = new ArrayList<>();
    // Vehicles exited the measuring area
    private ArrayList<VehicleProcess> finishedVehs = new ArrayList<>();

    /**
     * Private constructor for this singleton class
     */
    private EventHandler() {
        //Initialize FEL and PEL
        eventQueue = new PriorityQueue<>();
        waitingVehEvents = new LinkedList<>();

        // Initialize the traffic lights
        trafficLights = new TrafficLight[4];
        trafficLights[0] = new TrafficLight(1, 0,
                10.6, 2.2, 38.3, 49.3,
                8.6, 4.2, 31.8, 55,
                9.8, 1.8, 33.8, 55);
        trafficLights[1] = new TrafficLight(2, Parameter.TRAFFIC_LIGHT_DELAY,
                0, 0, 44.7, 55.4,
                0, 0, 23.9, 76.2,
                0, 0, 23.9, 76.2);
        trafficLights[2] = new TrafficLight(3, Parameter.TRAFFIC_LIGHT_DELAY * 2,
                0, 0, 64.1, 35.7,
                0, 0, 30.9, 69.2,
                0, 0, 30.9, 69.2);
        trafficLights[3] = new TrafficLight(5, Parameter.TRAFFIC_LIGHT_DELAY * 3,
                12.4, 3.6, 37.8, 46.1,
                0, 0, 26.1, 74,
                13.4, 60, 40.6, 60.2);
    }

    public static EventHandler getInstance() {
        if (instance == null) {
            instance = new EventHandler();
        }
        return instance;
    }

    public synchronized void handleEvent(Event event) {
        switch(event.type) {
            case Enter:
                startThread(event.veh);
                break;
            case Resume:
                synchronized (event.veh) {
                    event.veh.notify();
                }
                break;
            case WaitUntil:
                waitUntil(event);
                break;
            case CheckWait:
                // Do nothing
                break;
            case Exit:
                exitArea(event);
                break;
            case TurnGreenThrough:
                turnThroughLight(EventType.TurnGreenThrough, event.intersection, event.direction);
                break;
            case TurnRedThrough:
                turnThroughLight(EventType.TurnRedThrough, event.intersection, event.direction);
                break;
            case TurnGreenLeft:
                turnLeftLight(EventType.TurnGreenLeft, event.intersection, event.direction);
                break;
            case TurnRedLeft:
                turnLeftLight(EventType.TurnRedLeft, event.intersection, event.direction);
                break;
            default:
                System.err.println("Error - EventHandler.handleEvent: Wrong Event!");
        }
    }

    /**
     * Start a new thread for the vehicle
     *
     * @param veh vehicle to be started
     */
    private synchronized void startThread(VehicleProcess veh) {
        Thread vehThread = new Thread(veh);
        vehThread.start();
    }

    /**
     * Wait for a passing condition of a intersection
     *
     * @param event the WaitUntil event
     */
    private synchronized void waitUntil(Event event) {
        int index = getIntersectionIndex(event.intersection);
        TrafficLight tl = trafficLights[index];
        boolean available = tl.available(event.direction, event.turningLeft);
        boolean isGreen = tl.isGreen(event.direction, event.turningLeft);
        LinkedList<VehicleProcess> vehs = tl.getVehsQueue(event.direction, event.turningLeft);

        // If it's green light and free, pass immediately
        if (available && isGreen && vehs.size() == 0) {
            addEvent(new Event(Scheduler.getInstance().getTime(), EventType.Resume, event.veh));
        } else {
            waitingVehEvents.add(event);
            vehs.addFirst(event.veh);
        }
    }

    /**
     * Traverse the PEL to find any
     */
    public synchronized void checkWait() {
        ArrayList<Event> handled = new ArrayList<>();
        for (Event event : waitingVehEvents) {
            int index = getIntersectionIndex(event.intersection);
            TrafficLight tl = trafficLights[index];
            boolean available = tl.available(event.direction, event.turningLeft);
            boolean isGreen = tl.isGreen(event.direction, event.turningLeft);
            LinkedList<VehicleProcess> vehs = tl.getVehsQueue(event.direction, event.turningLeft);

            assert(vehs != null);

            if (available && isGreen
                    && vehs.indexOf(event.veh) == vehs.size() - 1) {
                addEvent(new Event(Scheduler.getInstance().getTime() + Parameter.W, EventType.Resume, event.veh));
                tl.setAvailable(event.direction, event.turningLeft, false);
                vehs.removeLast();
                handled.add(event);
            }
        }
        waitingVehEvents.removeAll(handled);
    }

    /**
     *  Add vehicle to {@link #finishedVehs}
     *
     * @param event The exit event
     */
    private synchronized void exitArea(Event event) {
        event.veh.endTime = event.time;
        finishedVehs.add(event.veh);
    }

    /**
     * Handle through traffic light events. Change the corresponding variables.
     *
     * @param type TurnGreenThrough or TurnRedThrough
     * @param intersection intersection of the event
     * @param direction direction of the event
     */
    private synchronized void turnThroughLight(EventType type, int intersection, Direction direction) {
        boolean green;
        switch(type) {
            case TurnGreenThrough:
                green = true;
                break;
            case TurnRedThrough:
                green = false;
                break;
            default:
                green = false;
                System.err.println("Error - EventHandler.turnThroughLight: Wrong Event!");
        }
        int index = getIntersectionIndex(intersection);
        trafficLights[index].setLight(direction, false, green);
    }

    /**
     * Handle left traffic light events. Change the corresponding variables.
     *
     * @param type TurnGreenThrough or TurnRedThrough
     * @param intersection intersection of the event
     * @param direction direction of the event
     */
    private synchronized void turnLeftLight(EventType type, int intersection, Direction direction) {
        boolean green;
        switch(type) {
            case TurnGreenLeft:
                green = true;
                break;
            case TurnRedLeft:
                green = false;
                break;
            default:
                green = false;
                System.err.println("Error - EventHandler.turnLeftLight: Wrong Event!");
        }
        int index = getIntersectionIndex(intersection);
        trafficLights[index].setLight(direction, true, green);
    }

    /**
     * Get the index of the intersection to access the array
     *
     * @param intersection Intersection number
     * @return the index of the intersection
     */
    public synchronized int getIntersectionIndex(int intersection) {
        switch(intersection) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 5:
                return 3;
            default:
                System.err.println("Error - EventHandler.getIntersectionIndex: Wrong Intersection!");
                return -1;
        }
    }

    /**
     * Add a scheduler event to the event queue.
     *
     * @param event event to be added
     */
    public void addEvent(Event event) {
        eventQueue.add(event);
    }

    public boolean getAvailable(int intersection, boolean left, Direction direction) {
        int index = getIntersectionIndex(intersection);
        return trafficLights[index].available(direction, left);
    }

    public void setAvailable(int intersection, boolean left, Direction direction, boolean available) {
        int index = getIntersectionIndex(intersection);
        trafficLights[index].setAvailable(direction, left, available);
    }

    public void turnAllGreens() {
        for (TrafficLight tl : trafficLights) {
            tl.turnAllGreen();
        }
    }

    public PriorityQueue<Event> getEventQueue() {
        return eventQueue;
    }

    public LinkedList<Event> getWaitingVehEvents() {
        return waitingVehEvents;
    }

    public ArrayList<VehicleProcess> getEnteringVehs() {
        return enteringVehs;
    }

    public ArrayList<VehicleProcess> getFinishedVehs() {
        return finishedVehs;
    }

    public TrafficLight[] getTrafficLights() {
        return trafficLights;
    }
}
