/**
 * Event.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * The class of Event.
 */
public class Event implements Comparable<Event> {
    double time;
    EventType type;
    int intersection;
    Direction direction;
    Vehicle vehicle;

    // Constructor
    public Event(double time, EventType type, int intersection, Direction direction, Vehicle car) {
        this.time = time;
        this.type = type;
        this.intersection = intersection;
        this.direction = direction;
        this.vehicle = car;
    }

    public String toString() {
        String str = "";
        str += time + " ";
        str += type + " ";
        str += intersection + " ";
        str += direction + " ";
        str += vehicle.id;
        return str;
    }

    @Override
    public int compareTo(Event e) {
        return Double.compare(this.time, e.time);
    }
}