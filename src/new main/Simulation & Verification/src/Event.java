public class Event {

  Action action;
  TimeBound timebound;
  public Event(Action action, TimeBound timebound) {  //constructor
    this.action = action;
    this.timebound = timebound;
  }

  public String occur(int current) {
    String result = "";
    if (this.timebound.getTick() == current) {
      result = this.action.behave();
    }
    return result;
  }

  public void reset() {
    this.timebound.reset();
  }
}
