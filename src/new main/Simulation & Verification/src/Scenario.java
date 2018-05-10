import java.util.ArrayList;

public class Scenario {

  ArrayList <Event> events;
  public Scenario(ArrayList <Event> events) {
    this.events = events;

  }

  public void addEvent(ArrayList <Event> event) {

  }

  public void addEvents(ArrayList <Event> events) {

  }

  public void reset() {
    for (Event ev : events) {
        ev.reset();
    }
  }

}
