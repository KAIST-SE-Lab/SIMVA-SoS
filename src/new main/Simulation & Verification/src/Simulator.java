import javafx.util.Pair;

import java.util.ArrayList;
public class Simulator {
  int simulationTime;
  SoS sos;
  Scenario scenario;
  // Change simualationLog type froom ArrayList<String> into ArrayList<Pair<Int, String>>
  ArrayList <Pair<Integer,String>> simulationLog;
  
  public Simulator(int simulationTime, SoS sos, Scenario scenario) {
    this.simulationTime = simulationTime;
    this.sos = sos;
    this.scenario = scenario;
    this.simulationLog = new ArrayList<> ();
  }

  public ArrayList run() {
    this.reset();
    
    // result variable is for getting the result of event in such tick
    String result;
    // for every tick in simulation time
    for(int tick  = 0; tick < this.simulationTime; tick++) {
      // check every event in scenario
      for (Event ev : this.scenario.events) {
        result = ev.occur(tick);
        
        // the default return value is "" from event.occur from Event.java
        if (!result.equals("")) {
          this.simulationLog.add(new Pair<>(tick, result));
        }
      }
      // sosRunResults variable is for getting the result of SoS run in such tick
      // Because the return type of sos.run is ArrayList<String>
      ArrayList<String> sosRunResults = new ArrayList<>();
      sosRunResults = this.sos.run(tick);
      
      // append each sosRunResults with tick in the simulation Log
      for (String res : sosRunResults) {
        this.simulationLog.add(new Pair<>(tick, res));
      }
      
      // TODO!! ASK to YONG JUN!!
      // self.simulationLog.append( ( [CS.rescued for CS in self.SoS.CSs],
      // self.SoS.environment.copy() ) )
      // this.simulationLog.add()
      
      return simulationLog;
      
    }
    
    return null;
  }

  public void stop() {

  }

  public void monitor() {

  }

  // reset the attributes of Simulator
  public void reset() {
    this.simulationLog.clear();
    this.scenario.reset();
    this.sos.reset();
  }

}
