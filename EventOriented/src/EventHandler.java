/**
 * EventHandler.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * A singleton class for handling all kinds of events.
 */

import java.util.LinkedList;

public class EventHandler {
    private static EventHandler instance = null;
    private LinkedList<Vehicle> vehicleQueueSouth10, vehicleQueueSouth11, vehicleQueueSouth12, vehicleQueueSouth14;
    private TrafficLight tl10, tl11, tl12, tl14;

    /**
     * Private constructor for this singleton class
     */
    private EventHandler() {
        // Initialize the vehicle queues for each traffic light
        vehicleQueueSouth10 = new LinkedList<>();
        vehicleQueueSouth11 = new LinkedList<>();
        vehicleQueueSouth12 = new LinkedList<>();
        vehicleQueueSouth14 = new LinkedList<>();

        // Initialize the traffic lights
        tl10 = new TrafficLight(1, )
    }

    public static EventHandler getInstance() {
        if (instance == null) {
            instance = new EventHandler();
        }
        return instance;
    }

    public void handleEvent(Event event) {
        switch(event.name) {
            case TLArrival10:
                arrival10(event.time, event.vehicle);
                break;
            case TLArrival11:
                arrival11(event.time, event.vehicle);
                break;
            case TLArrival12:
                arrival12(event.time, event.vehicle);
                break;
            case TLArrival14:
                arrival14(event.time, event.vehicle);
                break;
            case TLDeparture10:
                departure10(event.time, event.vehicle);
                break;
            case TLDeparture11:
                departure11(event.time, event.vehicle);
                break;
            case TLDeparture12:
                departure12(event.time, event.vehicle);
                break;
            case TLDeparture14:
                departure14(event.time, event.vehicle);
                break;
            default:
                System.out.println("Error - handleEvent: Wrong Event!");
        }
    }

    private void arrival10(double time, Vehicle car) {
        int numVehicleToPass = vehicleQueueSouth10.size();
        int greenPass = Math.floor();
    }

    private void arrival11(double time, Vehicle car) {

    }

    private void arrival12(double time, Vehicle car) {

    }

    private void arrival14(double time, Vehicle car) {

    }

    private void departure10(double time, Vehicle car) {

    }

    private void departure11(double time, Vehicle car) {

    }

    private void departure12(double time, Vehicle car) {

    }

    private void departure14(double time, Vehicle car) {

    }
}
