import java.util.ArrayList;

public class Scenario {

  ArrayList <Event> events;
  public Scenario(ArrayList <Event> events) {
    this.events = events;

  }

  public void addEvent(Event event) {
    this.events.add(event);
  }

  public void addEvents(ArrayList <Event> events) {
    for(Event ev: events) {
      this.events.add(ev);
    }
  }

  public void reset() {
    for (Event ev : events) {
        ev.reset();
    }
  }

}
