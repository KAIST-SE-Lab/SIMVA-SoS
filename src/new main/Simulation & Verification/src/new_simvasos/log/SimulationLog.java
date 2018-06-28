package new_simvasos.log;

import javafx.util.Pair;
import java.util.ArrayList;
import new_simvasos.not_decided.CS;

public class SimulationLog {
  
    // csResultLog save each CSs' final state of simulation
    // In this FireFighter case, this log store the rescued number of values for each CS
    // , when this simulation finished.
    ArrayList<Integer> csResultLog;

    // environmentResultLog save final state of SoS.environment values
    // In this case, this log store the unrescued number of values for each CS,
    // when this simulation finished.
    ArrayList<Integer> environmentResultLog;

    public SimulationLog() {
      this.csResultLog = new ArrayList<>();
      this.environmentResultLog = new ArrayList<>();
    }

    private void clear() {
      this.csResultLog.clear();
      this.environmentResultLog.clear();
    }

    protected void addCsResultLog(ArrayList<CS> log) {
      ArrayList<Integer> rescued = new ArrayList<>();
      for (CS cs : log) {
        rescued.add(cs.getRescued());
      }
      this.csResultLog = rescued;
    }

    protected void addEnvironmentResultLog(ArrayList<Integer> log) {
      this.environmentResultLog = new ArrayList<>(log);
    }

    // For the convenience of getting csResultLog and environmentResultLog simultaneously,
    // this get PropertyCheckLogs returns the pair of logs.
    // You can use csResultLog for getKey(), and environmentResultLog for getValue() functions
    // in each simulation.
    public Pair<ArrayList<Integer>,ArrayList<Integer>> getPropertyCheckLogs() {
      return new Pair<>(this.csResultLog, this.environmentResultLog);
    }
    
}
