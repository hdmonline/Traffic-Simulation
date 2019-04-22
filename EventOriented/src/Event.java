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

    public Event(double time, EventType type, int intersection, Direction direction) {
        this.time = time;
        this.type = type;
        this.intersection = intersection;
        this.direction = direction;
    }

    public Event(double time, EventType type, int intersection) {
        this.time = time;
        this.type = type;
        this.intersection = intersection;
    }

    // Helper function to output event
    public String toString() {
        String str = "";
        str += String.format("%.2f", time) + ",";
        str += type + ",";
        str += intersection + ",";
        str += direction + ",";
        if (vehicle == null) {
            str += "null";
        } else {
            str += vehicle.id;
        }
        return str;
    }

    @Override
    public int compareTo(Event e) {
        return Double.compare(this.time, e.time);
    }
}