/**
 * Event.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * The class of Event.
 */
public class Event implements Comparable<Event> {
    double time;
    EventType name;
    int street;
    VehicleProcess vehicleProcess;

    // Constructor
    public Event(double time, EventType name, int street, VehicleProcess car) {
        this.time = time;
        this.name = name;
        this.street = street;
        this.vehicleProcess = car;
    }

    @Override
    public int compareTo(Event e) {
        return Double.compare(this.time, e.time);
    }
}