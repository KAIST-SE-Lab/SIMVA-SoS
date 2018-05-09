public class Event {

  Action action;
  TimeBound timebound;
  public Event(Action action, TimeBound timebound) {  //constructor
    this.action = action;
    this.timebound = timebound;
  }

  public String occur() {
    return "todo";
  }
}
