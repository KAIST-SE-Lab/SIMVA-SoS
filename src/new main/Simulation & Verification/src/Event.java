import javafx.util.Pair;

public class Event {

  Action action;
  TimeBound timebound;
  public Event(Action action, TimeBound timebound) {  //constructor
    this.action = action;
    this.timebound = timebound;
  }

  public Pair<Action, TimeBound> occur(int current) {
    if (this.timebound.getTick() == current) {
      this.action.behave();

      // return constant timebound because simulation log only need the time(tick) when the event was occurred.
      ConstantTimeBound csnt_tb = new ConstantTimeBound(current);

      return new Pair<>(this.action, csnt_tb);
    }

    return null;
  }

  public void reset() {
    this.timebound.reset();
  }
}
