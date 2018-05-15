import javafx.util.Pair;

import java.util.ArrayList;

public class SimulationLog {

    // Store the log of event. Just like the Scenario class, save (Action, Timebound class)
    // but because this is log, the timebound is only constant one.
    // Event.occur() return the constant timebound of such event.
    ArrayList<Event> eventLog;

    // This variable is used in the property checking part
    // The first part of this variable is
    ArrayList<Pair<ArrayList<Integer>, ArrayList<Integer>>> propertyLog;

    ArrayList<Pair<Integer, String>> sosRunLog;

    // Constructor
    public SimulationLog() {
        this.eventLog = new ArrayList<>();
        this.propertyLog = new ArrayList<>();
        this.sosRunLog = new ArrayList<>();
    }

    public void clear() {
      this.eventLog.clear();
      this.propertyLog.clear();
      this.sosRunLog.clear();
    }

    // Add log into eventlog
    public void addEventLog(Action action, TimeBound timeBound) {
        Event event = new Event(action, timeBound);
        this.eventLog.add(event);
    }

    public void addSosRunLog(int tick, ArrayList<String> results) {
        for(String res: results) {
            this.sosRunLog.add(new Pair<Integer, String>(tick, res));
        }
    }

    // Add result of simulation into propertylog
    // THIS PART COULD BE CHANGED ACCORDING TO THE DEVELOPMENT OF MODELING PART
    public void addPropertyLog(ArrayList<CS> csArrayList, ArrayList<Integer> environment) {
        ArrayList<Integer> rescued = new ArrayList<>();
        for (CS cs : csArrayList) {
            rescued.add(cs.getRescued());
        }

        this.propertyLog.add(new Pair<>(rescued, environment));
    }

    public Pair<ArrayList<Integer>, ArrayList<Integer>> getPropertyLog() {
      Pair<ArrayList<Integer>, ArrayList<Integer>> ret =
          new Pair<> (this.propertyLog.get(0).getKey(),
                      this.propertyLog.get(0).getValue());
      
      return ret;
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


    // print the result of simulation
    // THIS PART COULD BE CHANGED ACCORDING TO THE DEVELOPMENT OF MODELING PART
    public void printPropertyLog() {
        ArrayList<Integer> tmpRescued = this.propertyLog.get(0).getKey();
        ArrayList<Integer> tmpEnv = this.propertyLog.get(0).getValue();

        System.out.println("The number of rescued people by each CS");
        for(int i : tmpRescued) {
            System.out.println(i);
        }

        System.out.println("Environment Setting");
        for(int i: tmpEnv) {
            System.out.println(i);
        }
    }
}
