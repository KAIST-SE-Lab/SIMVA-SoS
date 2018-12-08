package new_simvasos.log;

import new_simvasos.scenario.Event;

import java.util.ArrayList;

/**
 * The type Snapshot.
 * includes a snapshot string
 */
public class Snapshot {
  private String snapshotString;
  
  public Snapshot(String str) {
    this.snapshotString = str;
  }

  public String getSnapshotString(){
    return this.snapshotString;
  }
  /*
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
  */
}
