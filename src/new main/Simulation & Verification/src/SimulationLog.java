import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SimulationLog {

    // Store the log of event. Just like the Scenario class, save (Action, Timebound class)
    // but because this is log, the timebound is only constant one.
    // Event.occur() return the constant timebound of such event.
    ArrayList<Event> eventLog;

    // csResultLog save eacs CSs' final state at each simulation
    // In this FireFighter case, this log store the rescued number of values for each CS
    // , when this simulation finished.
    ArrayList<Integer> csResultLog;

    // environmentResultLog save final state of SoS.environment values
    // In this case, this log store the unrescued number of values for each CS,
    // when this simulation finished.
    ArrayList<Integer> environmentResultLog;

    ArrayList<Pair<Integer, String>> sosRunLog;

    public SimulationLog() {
      this.eventLog = new ArrayList<>();
      //this.propertyLog = new ArrayList<>();
      this.csResultLog = new ArrayList<>();
      this.environmentResultLog = new ArrayList<>();
      this.sosRunLog = new ArrayList<>();
    }

    public void clear() {
      this.eventLog.clear();
      this.csResultLog.clear();
      this.environmentResultLog.clear();
      this.sosRunLog.clear();
    }

    public void addCsResultLog(ArrayList<CS> log) {
      ArrayList<Integer> rescued = new ArrayList<>();
      for (CS cs : log) {
        rescued.add(cs.getRescued());
      }
      this.csResultLog = rescued;
    }

    public void addEnvironmentResultLog(ArrayList<Integer> log) {
      this.environmentResultLog = new ArrayList<>(log);
    }

    // For the convenience of getting csResultLog and environmentResultLog simultaneously,
    // this get PropertyCheckLogs returns the pair of logs.
    // You can use csResultLog for getKey(), and environmentResultLog for getValue() functions
    // in each simulation.
    public Pair<ArrayList<Integer>,ArrayList<Integer>> getPropertyCheckLogs() {
      return new Pair<>(this.csResultLog, this.environmentResultLog);
    }


  // Add log into eventlog
    public void addEventLog(Action action, TimeBound timeBound) {
        Event event = new Event(action, timeBound);
        this.eventLog.add(event);
    }

    public void addSosRunLog(int tick, ArrayList<String> results) {
        for(String res: results) {
            this.sosRunLog.add(new Pair<>(tick, res));
        }
    }

    // print the log of each occurred event
    public void printEventLog() {
      System.out.println("Event Log");

      for (Event ev: this.eventLog) {
        System.out.println(ev.timebound.getTick() + ": " + ev.action.behave());
      }
    }


    // print the log of sos behaviour
    public void printSosRunLog() {
      System.out.println("SoS Running Log");
      for(Pair<Integer, String> tmp : this.sosRunLog) {
        System.out.println(tmp.getKey() + ": " + tmp.getValue());
      }
    }

}
