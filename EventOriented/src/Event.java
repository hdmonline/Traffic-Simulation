public class Event implements Comparable<Event> {
    public double time;
    public EventName name;
    public Vehicle vehicle;

    @Override
    public int compareTo(Event e) {
        return Double.compare(this.time, e.time);
    }
}