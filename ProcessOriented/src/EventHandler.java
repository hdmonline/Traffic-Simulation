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
    private ArrayList<LinkedList<VehicleProcess>> northThroughVehs, westThroughVehs, eastThroughVehs;
    // Vehicle queues for turning left
    private ArrayList<LinkedList<VehicleProcess>> northLeftVehs, eastLeftVehs;
    private TrafficLight[] trafficLights;
    private boolean[] isThroughNorthGreen, isTroughWestGreen, isThroughEastGreen;
    private boolean[] isLeftNorthGreen, isLeftEastGreen;
    private boolean[] availableThroughNorth, availableThroughWest, availableThroughEast;


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
        northThroughVehs = new ArrayList<>();
        westThroughVehs = new ArrayList<>();
        eastThroughVehs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            northThroughVehs.add(new LinkedList<>());
            westThroughVehs.add(new LinkedList<>());
            eastThroughVehs.add(new LinkedList<>());
        }

        //Initialize FEL and PEL
        eventQueue = new PriorityQueue<>();
        waitingVehEvents = new LinkedList<>();

        // Initialize traffic light status
        isThroughNorthGreen = new boolean[4];
        isTroughWestGreen = new boolean[4];
        isThroughEastGreen = new boolean[4];
        isLeftNorthGreen = new boolean[4];
        isLeftEastGreen = new boolean[4];

        // Initialize intersection/direction status
        availableThroughNorth = new boolean[4];
        availableThroughWest = new boolean[4];
        availableThroughEast = new boolean[4];
        Arrays.fill(availableThroughNorth, true);
        Arrays.fill(availableThroughWest, true);
        Arrays.fill(availableThroughEast, true);

        // Initialize the traffic lights
        trafficLights = new TrafficLight[4];
        trafficLights[0] = new TrafficLight(1, 49.3, 38.3, 10.6, 2.2);
        trafficLights[1] = new TrafficLight(2, 55.4, 44.7, 0, 0);
        trafficLights[2] = new TrafficLight(3, 35.7, 64.6, 0, 0);
        trafficLights[3] = new TrafficLight(5, 46.1, 37.8, 12.4, 3.6);
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
    // TODO: handle different directions
    private synchronized void waitUntil(Event event) {
        waitingVehEvents.addFirst(event);
        int index = getIntersectionIndex(event.intersection);
        if (event.turningLeft) {
            switch(event.direction) {
                case N:
                    northLeftVehs.get(index).addFirst(event.veh);
                    break;
                case E:
                    eastLeftVehs.get(index).addFirst(event.veh);
                    break;
                default:
                    System.out.println("Error - EventHandler.checkWait: Wrong Direction!");
            }
        } else {
            switch(event.direction) {
                case N:
                    northThroughVehs.get(index).addFirst(event.veh);
                    break;
                case W:
                    westThroughVehs.get(index).addFirst(event.veh);
                    break;
                case E:
                    eastThroughVehs.get(index).addFirst(event.veh);
                    break;
                default:
                    System.out.println("Error - EventHandler.checkWait: Wrong Direction!");
            }
        }

    }

    /**
     * Traverse the PEL to find any
     */
    // TODO: handle left turn
    public synchronized void checkWait() {
        ArrayList<Event> handled = new ArrayList<>();

        for (Event event : waitingVehEvents) {
            int index = getIntersectionIndex(event.intersection);
            if (event.turningLeft) {
                switch(event.direction) {
                    case N:
                        // If light is green and it is the first vehicle in the queue
                        if (availableLeftNorth[index] && isThroughNorthGreen[index] && northThroughVehs.get(index).indexOf(event.veh) == northThroughVehs.get(index).size() - 1) {
                            addScheduleEvent(new Event(Scheduler.getInstance().getTime() + Parameter.W, EventType.Resume, event.veh));
                            availableThroughNorth[index] = false;
                            northThroughVehs.get(index).removeLast();
                            handled.add(event);
                        }
                        break;
                    case W:
                        // If light is green and it is the first vehicle in the queue
                        if (availableThroughWest[index] && isTroughWestGreen[index] && westThroughVehs.get(index).indexOf(event.veh) == westThroughVehs.get(index).size() - 1) {
                            addScheduleEvent(new Event(Scheduler.getInstance().getTime() + Parameter.W, EventType.Resume, event.veh));
                            availableThroughWest[index] = false;
                            westThroughVehs.get(index).removeLast();
                            handled.add(event);
                        }
                        break;
                    default:
                        System.out.println("Error - EventHandler.checkWait: Wrong Direction!");
                }
            } else {
                switch(event.direction) {
                    case N:
                        // If light is green and it is the first vehicle in the queue
                        if (availableThroughNorth[index] && isThroughNorthGreen[index] && northThroughVehs.get(index).indexOf(event.veh) == northThroughVehs.get(index).size() - 1) {
                            addScheduleEvent(new Event(Scheduler.getInstance().getTime() + Parameter.W, EventType.Resume, event.veh));
                            availableThroughNorth[index] = false;
                            northThroughVehs.get(index).removeLast();
                            handled.add(event);
                        }
                        break;
                    case W:
                        // If light is green and it is the first vehicle in the queue
                        if (availableThroughWest[index] && isTroughWestGreen[index] && westThroughVehs.get(index).indexOf(event.veh) == westThroughVehs.get(index).size() - 1) {
                            addScheduleEvent(new Event(Scheduler.getInstance().getTime() + Parameter.W, EventType.Resume, event.veh));
                            availableThroughWest[index] = false;
                            westThroughVehs.get(index).removeLast();
                            handled.add(event);
                        }
                        break;
                    case E:
                        // If light is green and it is the first vehicle in the queue
                        if (availableThroughEast[index] && isThroughEastGreen[index] && eastThroughVehs.get(index).indexOf(event.veh) == eastThroughVehs.get(index).size() - 1) {
                            addScheduleEvent(new Event(Scheduler.getInstance().getTime() + Parameter.W, EventType.Resume, event.veh));
                            availableThroughEast[index] = false;
                            eastThroughVehs.get(index).removeLast();
                            handled.add(event);
                        }
                        break;
                    default:
                        System.out.println("Error - EventHandler.checkWait: Wrong Direction!");
                }
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
        event.veh.exitDirection = event.direction;
        event.veh.exitIntersection = event.intersection;
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
                isThroughNorthGreen[index] = green;
                break;
            case W:
                isTroughWestGreen[index] = green;
                break;
            case E:
                isThroughEastGreen[index] = green;
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
                isLeftNorthGreen[index] = green;
                break;
            case E:
                isLeftEastGreen[index] = green;
                break;
            default:
                System.out.println("Error - EventHandler.turnThroughLight: Wrong Direction!");
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
                System.out.println("Error - EventHandler.handleEvent: Wrong Intersection!");
                return -1;
        }
    }

    /**
     * Add a scheduler event to the event queue.
     *
     * @param event event to be added
     */
    public void addScheduleEvent(Event event) {
        eventQueue.add(event);
    }

    public PriorityQueue<Event> getEventQueue() {
        return eventQueue;
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

    public boolean[] getAvailableThroughNorth() {
        return availableThroughNorth;
    }
}
