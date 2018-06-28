package new_simvasos.log;

import new_simvasos.scenario.Event;

import java.util.ArrayList;

public class Snapshot {
  
  // Store the log of event. Just like the Scenario class, save (Action, Timebound class)
  // but because this is log, the timebound is only constant one.
  // Event.occur() return the constant timebound of such event.
  private ArrayList<Event> eventLog;
  private ArrayList<String> sosLog;
  private ArrayList<Integer> environmentLog;
  
  public Snapshot() {
    this.eventLog = new ArrayList<>();
    this.sosLog = new ArrayList<>();
    this.environmentLog = new ArrayList<>();
  }
  
  public void addEnvironmentLog(ArrayList<Integer> log) {
    for (int i : log) {
      this.environmentLog.add(i);
    }
  }
  
  // Add log into eventlog
  public void addEventLog(ArrayList<Event> Evs) {
    for(Event ev : Evs) {
      this.eventLog.add(ev);
    }
  }
  
  public void addSosLog(ArrayList<String> results) {
    for(String res: results) {
      this.sosLog.add(res);
    }
  }
  
  public void printSnapshotLog() {
    System.out.println("===================== OCCURED EVENT =====================");
    for (Event ev: this.eventLog) {
      System.out.println(ev.action.behave());
    }
    System.out.println("===================== CS ACTIVITY =====================");
    for (String str: this.sosLog) {
      System.out.println(str);
    }
    System.out.println("===================== ENVIRONMENT STATE =====================");
    System.out.println(this.environmentLog);
    
  }
}
