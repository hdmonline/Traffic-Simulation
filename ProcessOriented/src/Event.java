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
    boolean turningLeft;

    public Event(double time, EventType type, VehicleProcess veh) {
        this.time = time;
        this.type = type;
        this.veh = veh;
        this.turningLeft = false;
    }

    // TurnRedThrough or TurnGrean
    public Event(double time, EventType type, int intersection, Direction direction) {
        this.time = time;
        this.type = type;
        this.intersection = intersection;
        this.direction = direction;
        this.turningLeft = false;
    }

    // Resume
    public Event(double time, EventType type, int intersection,
                 Direction direction, VehicleProcess veh) {
        this.time = time;
        this.type = type;
        this.intersection = intersection;
        this.direction = direction;
        this.veh = veh;
        this.turningLeft = false;
    }

    // Wait until
    public Event(double time, EventType type, int intersection,
                 Direction direction, VehicleProcess veh, boolean turningLeft) {
        this.time = time;
        this.type = type;
        this.intersection = intersection;
        this.direction = direction;
        this.veh = veh;
        this.turningLeft = turningLeft;
    }

    public String toString() {
        String str = "";

        str += String.format("%.2f", time) + ",";
        str += type + ",";
        str += (intersection == 0 ? "null" : intersection) + ",";
        str += direction + ",";
        str += veh == null ? "null" : veh.id;
        return str;
    }
    @Override
    public int compareTo(Event e) {
        return Double.compare(this.time, e.time);
    }
}
