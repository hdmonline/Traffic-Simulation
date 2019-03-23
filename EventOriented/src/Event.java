public class Event implements Comparable<Event> {
    public double time;
    public EventName name;

    @Override
    public int compareTo(Event e) {
        return Double.compare(this.time, e.time);
    }
}