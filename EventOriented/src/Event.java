/**
 * Event.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * The class of Event.
 */
public class Event implements Comparable<Event> {
    double time;
    EventName name;
    int intersection;
    Vehicle vehicle;

    // Constructor
    public Event(double time, EventName name, int intersection, Vehicle car) {
        this.time = time;
        this.name = name;
        this.intersection = intersection;
        this.vehicle = car;
    }

    @Override
    public int compareTo(Event e) {
        return Double.compare(this.time, e.time);
    }
}