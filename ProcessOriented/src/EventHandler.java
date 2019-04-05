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

//TODO: Consider to include this class in the Scheduler
public class EventHandler {
    private static EventHandler instance = null;

    // Vehicle queues, First -> In, Last -> Out
    private ArrayList<LinkedList<VehicleProcess>> southVehs;
    private TrafficLight[] trafficLights;
    private boolean[] isGreenSouth;

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
        southVehs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            southVehs.add(new LinkedList<>());
        }

        eventQueue = new PriorityQueue<>();
        waitingVehEvents = new LinkedList<>();

        // Initialize traffic light status
        isGreenSouth = new boolean[4];

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
            case TurnGreen:
                turnLight(EventType.TurnGreen, event.intersection, event.direction);
                break;
            case TurnRed:
                turnLight(EventType.TurnRed, event.intersection, event.direction);
                break;
            default:
                System.out.println("Error - EventHandler.handleEvent: Wrong Event!");
        }
    }

    private synchronized void startThread(VehicleProcess veh) {
        Thread vehThread = new Thread(veh);
        vehThread.start();
    }

    private synchronized void waitUntil(Event event) {
        waitingVehEvents.addFirst(event);
        int index = getIntersectionIndex(event.intersection);
        switch(event.direction) {
            case S:
                southVehs.get(index).addFirst(event.veh);
                break;
            case N:
                // TODO
                break;
            case W:
                // TODO
                break;
            case E:
                // TODO
                break;
            default:
                System.out.println("Error - EventHandler.checkWait: Wrong Direction!");
        }
    }

    public synchronized void checkWait(Event event) {
        int index = getIntersectionIndex(event.intersection);
        switch(event.direction) {
            case S:
                // If light is green and it is the first vehicle in the queue
                if (isGreenSouth[index] && southVehs.get(index).indexOf(event.veh) == southVehs.get(index).size() - 1) {
                    addScheduleEvent(new Event(Scheduler.getInstance().getTime() + Parameter.W, EventType.Resume, event.veh));
                    southVehs.get(index).removeLast();
                    waitingVehEvents.remove(event);
                }
                break;
            case N:
                // TODO
                break;
            case W:
                // TODO
                break;
            case E:
                // TODO
                break;
            default:
                System.out.println("Error - EventHandler.checkWait: Wrong Direction!");
        }
    }

    // TODO:
    private synchronized void exitArea(Event event) {
        event.veh.endTime = event.time;
        event.veh.exitDirection = event.direction;
        event.veh.exitIntersection = event.intersection;
        finishedVehs.add(event.veh);
    }

    private synchronized void turnLight(EventType type, int intersection, Direction direction) {
        boolean green;
        switch(type) {
            case TurnGreen:
                green = true;
                break;
            case TurnRed:
                green = false;
                break;
            default:
                green = false;
                System.out.println("Error - EventHandler.turnLight: Wrong Event!");
        }
        int index = getIntersectionIndex(intersection);
        switch(direction) {
            case S:
                isGreenSouth[index] = green;
                break;
            case W:
                // TODO: handle West lights
                break;
            case E:
                // TODO: handle East lights
                break;
            default:
                System.out.println("Error - EventHandler.turnLight: Wrong Direction!");
        }
    }

    private synchronized int getIntersectionIndex(int intersection) {
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

    public LinkedList<Event> getWaitingVehEvents() {
        return waitingVehEvents;
    }

    public TrafficLight[] getTrafficLights() {
        return trafficLights;
    }
}
