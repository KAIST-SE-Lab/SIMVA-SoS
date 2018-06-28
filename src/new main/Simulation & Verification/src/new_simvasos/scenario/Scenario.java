package new_simvasos.scenario;

import java.util.ArrayList;

public class Scenario {

  public ArrayList <Event> events;
  public Scenario(ArrayList<Event> events) {
    this.events = events;

  }

  private void addEvent(Event event) {
    this.events.add(event);
  }

  private void addEvents(ArrayList <Event> events) {
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
