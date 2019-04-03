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
    VehicleProcess veh;
    Thread thread;

    // TODO: check if thread is necessary.
    public Event(double time, EventType type, VehicleProcess veh, Thread thread) {
        this.time = time;
        this.type = type;
        this.veh = veh;
        this.thread = thread;
    }

    // TurnRed or TurnGrean
    public Event(double time, EventType type, int intersection, Direction direction) {
        this.time = time;
        this.type = type;
        this.intersection = intersection;
        this.direction = direction;
    }

    // Resume
    public Event(double time, EventType type, int intersection, Direction direction, VehicleProcess veh, Thread thread) {
        this.time = time;
        this.type = type;
        this.intersection = intersection;
        this.direction = direction;
        this.veh = veh;
        this.thread = thread;
    }

    // TODO: check this function
    public String toString() {
        String str = "";

        int printVehId = (type == EventType.TurnGreen || type == EventType.TurnGreen) ? veh.id : -1;
        str += String.format("%.2f", time) + " ";
        str += type + " ";
        str += intersection + " ";
        str += direction + " ";
        str += printVehId + " ";
        return str;
    }
    @Override
    public int compareTo(Event e) {
        return Double.compare(this.time, e.time);
    }
}
