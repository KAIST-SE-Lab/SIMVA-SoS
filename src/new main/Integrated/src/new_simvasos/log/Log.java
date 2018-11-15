package new_simvasos.log;

import new_simvasos.not_decided.CS;
import new_simvasos.scenario.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Log {
  
  // Save the whole information of CS Activity & Environment State per EACH TICK
  // This snapshotMap is currently used for Property Verification (11/14)
  private HashMap<Integer, Snapshot> snapshotMap;
  
  // Save the final result of CS & Environment per Simulation
  //private SimulationLog simuLog;
  
  public Log() {
    this.snapshotMap = new HashMap<>();
    //this.simuLog = new SimulationLog();
  }
  
  public void addSnapshot(int tick, String snapshot) {
    Snapshot tmp = new Snapshot(snapshot);
    
    snapshotMap.put(tick, tmp);
  }
//
//  public void addSimuLog(ArrayList<CS> Css, ArrayList<Integer> Evs) {
//    this.simuLog.addCsResultLog(Css);
//    this.simuLog.addEnvironmentResultLog(Evs);
//  }

  
  public void printSnapshot() {
    Iterator<Integer> keys = snapshotMap.keySet().iterator();
    System.out.println("===================== SNAPSHOT PRINT =====================");
    while(keys.hasNext()) {
      Integer key = keys.next();
      System.out.println("===================== TICK:" + key.toString() + " " + snapshotMap.get(key).getSnapshotString() + " =====================");
      //snapshotMap.get(key).printSnapshotLog();
    }
  }
  
  /*
  public SimulationLog getSimuLog() {
    return this.simuLog;
  }
*/
  public HashMap<Integer, Snapshot> getSnapshotMap() {
    return snapshotMap;
  }
}
