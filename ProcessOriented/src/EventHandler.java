/**
 * EventHandler.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * A singleton class for handling all kinds of events.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class EventHandler {
    private static EventHandler instance = null;

    // Vehicle queues for going through, First -> In, Last -> Out
    private ArrayList<LinkedList<VehicleProcess>> southThroughRightVehs, westThroughRightVehs, eastThroughRightVehs;
    // Vehicle queues for turning left
    private ArrayList<LinkedList<VehicleProcess>> southLeftVehs, westLeftVehs;
    private TrafficLight[] trafficLights;
    private boolean[] isThroughRightSouthGreen, isThroughRightWestGreen, isThroughRightEastGreen;
    private boolean[] isLeftSouthGreen, isLeftWestGreen;
    private boolean[] availableThroughRightSouth, availableThroughRightWest, availableThroughRightEast;
    private boolean[] availableLeftSouth, availableLeftWest;


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
        // Initialize the vehicle queues for each traffic light
        southThroughRightVehs = new ArrayList<>();
        westThroughRightVehs = new ArrayList<>();
        eastThroughRightVehs = new ArrayList<>();
        southLeftVehs = new ArrayList<>();
        westLeftVehs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            southThroughRightVehs.add(new LinkedList<>());
            westThroughRightVehs.add(new LinkedList<>());
            eastThroughRightVehs.add(new LinkedList<>());
            southLeftVehs.add(new LinkedList<>());
            westLeftVehs.add(new LinkedList<>());
        }

        //Initialize FEL and PEL
        eventQueue = new PriorityQueue<>();
        waitingVehEvents = new LinkedList<>();

        // Initialize traffic light status
        isThroughRightSouthGreen = new boolean[4];
        isThroughRightWestGreen = new boolean[4];
        isThroughRightEastGreen = new boolean[4];
        isLeftSouthGreen = new boolean[4];
        isLeftWestGreen = new boolean[4];
        Arrays.fill(isLeftWestGreen, true);
        Arrays.fill(isLeftSouthGreen, true);

        // Initialize intersection/direction status
        availableThroughRightSouth = new boolean[4];
        availableThroughRightWest = new boolean[4];
        availableThroughRightEast = new boolean[4];
        availableLeftSouth = new boolean[4];
        availableLeftWest = new boolean[4];
        Arrays.fill(availableThroughRightSouth, true);
        Arrays.fill(availableThroughRightWest, true);
        Arrays.fill(availableThroughRightEast, true);
        Arrays.fill(availableLeftSouth, true);
        Arrays.fill(availableLeftWest, true);

        // Initialize the traffic lights
        trafficLights = new TrafficLight[4];
        trafficLights[0] = new TrafficLight(1,
                10.6, 2.2, 38.3, 49.3,
                8.6, 4.2, 31.8, 55,
                9.8, 1.8, 33.8, 55);
        trafficLights[1] = new TrafficLight(2,
                0, 0, 44.7, 55.4,
                0, 0, 23.9, 76.2,
                0, 0, 23.9, 76.2);
        trafficLights[2] = new TrafficLight(3,
                0, 0, 64.1, 35.7,
                0, 0, 30.9, 69.2,
                0, 0, 30.9, 69.2);
        trafficLights[3] = new TrafficLight(5,
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
                System.out.println("Error - EventHandler.handleEvent: Wrong Event!");
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
        boolean[] available = null;
        boolean[] isGreen = null;
        ArrayList<LinkedList<VehicleProcess>> vehs = null;
        if (event.turningLeft) {
            switch (event.direction) {
                case S:
                    available = availableLeftSouth;
                    isGreen = isLeftSouthGreen;
                    vehs = southLeftVehs;
                    break;
                case W:
                    available = availableLeftWest;
                    isGreen = isLeftWestGreen;
                    vehs = westLeftVehs;
                    break;
                default:
                    System.out.println("Error - EventHandler.checkWait: Wrong Direction!");
            }
        } else {
            switch (event.direction) {
                case S:
                    available = availableThroughRightSouth;
                    isGreen = isThroughRightSouthGreen;
                    vehs = southThroughRightVehs;
                    break;
                case W:
                    available = availableThroughRightWest;
                    isGreen = isThroughRightWestGreen;
                    vehs = westThroughRightVehs;
                    break;
                case E:
                    available = availableThroughRightEast;
                    isGreen = isThroughRightEastGreen;
                    vehs = eastThroughRightVehs;
                    break;
                default:
                    System.out.println("Error - EventHandler.checkWait: Wrong Direction!");
            }
        }
        // If it's green light and free, pass immediately
        if (available[index] && isGreen[index] && vehs.size() == 0) {
            addEvent(new Event(Scheduler.getInstance().getTime(), EventType.Resume, event.veh));
        } else {
            waitingVehEvents.add(event);
            vehs.get(index).addFirst(event.veh);
        }
    }

    /**
     * Traverse the PEL to find any
     */
    public synchronized void checkWait() {
        ArrayList<Event> handled = new ArrayList<>();
        for (Event event : waitingVehEvents) {
            int index = getIntersectionIndex(event.intersection);
            boolean[] available = null;
            boolean[] isGreen = null;
            ArrayList<LinkedList<VehicleProcess>> vehs = null;
            if (event.turningLeft) {
                switch (event.direction) {
                    case S:
                        available = availableLeftSouth;
                        isGreen = isLeftSouthGreen;
                        vehs = southLeftVehs;
                        break;
                    case W:
                        available = availableLeftWest;
                        isGreen = isLeftWestGreen;
                        vehs = westLeftVehs;
                        break;
                    default:
                        System.out.println("Error - EventHandler.checkWait: Wrong Direction!");
                }
            } else {
                switch (event.direction) {
                    case S:
                        available = availableThroughRightSouth;
                        isGreen = isThroughRightSouthGreen;
                        vehs = southThroughRightVehs;
                        break;
                    case W:
                        available = availableThroughRightWest;
                        isGreen = isThroughRightWestGreen;
                        vehs = westThroughRightVehs;
                        break;
                    case E:
                        available = availableThroughRightEast;
                        isGreen = isThroughRightEastGreen;
                        vehs = eastThroughRightVehs;
                        break;
                    default:
                        System.out.println("Error - EventHandler.checkWait: Wrong Direction!");
                }
            }

            assert(available != null && isGreen != null && vehs != null);

            if (available[index] && isGreen[index]
                    && vehs.get(index).indexOf(event.veh) == vehs.get(index).size() - 1) {
                addEvent(new Event(Scheduler.getInstance().getTime() + Parameter.W, EventType.Resume, event.veh));
                available[index] = false;
                vehs.get(index).removeLast();
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
                System.out.println("Error - EventHandler.turnThroughLight: Wrong Event!");
        }
        int index = getIntersectionIndex(intersection);
        switch(direction) {
            case N:
                isThroughRightSouthGreen[index] = green;
                break;
            case W:
                isThroughRightEastGreen[index] = green;
                break;
            case E:
                isThroughRightWestGreen[index] = green;
                break;
            default:
                System.out.println("Error - EventHandler.turnThroughLight: Wrong Direction!");
        }
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
                System.out.println("Error - EventHandler.turnLeftLight: Wrong Event!");
        }
        int index = getIntersectionIndex(intersection);
        switch(direction) {
            case N:
                isLeftSouthGreen[index] = green;
                break;
            case E:
                isLeftWestGreen[index] = green;
                break;
            default:
                System.out.println("Error - EventHandler.turnLeftLight: Wrong Direction!");
        }
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
                System.out.println("Error - EventHandler.getIntersectionIndex: Wrong Intersection!");
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

    public boolean[] getAvailable(boolean left, Direction direction) {
        switch (direction) {
            case S:
                return left ? availableLeftSouth : availableThroughRightSouth;
            case W:
                return left ? availableLeftWest : availableThroughRightWest;
            case E:
                return availableThroughRightEast;
            default:
                System.out.println("Error - EventHandler.getAvailable: Wrong direction!");
                return new boolean[4];
        }
    }

    public void turnAllGreens() {
        Arrays.fill(isThroughRightSouthGreen, true);
        Arrays.fill(isThroughRightWestGreen, true);
        Arrays.fill(isThroughRightEastGreen, true);
        Arrays.fill(isLeftSouthGreen, true);
        Arrays.fill(isLeftWestGreen, true);
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
