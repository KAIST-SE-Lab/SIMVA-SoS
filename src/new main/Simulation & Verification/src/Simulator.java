import javafx.util.Pair;

import java.util.ArrayList;
public class Simulator {
  int simulationTime;
  SoS sos;
  Scenario scenario;
  SimulationLog simulationLog;
  
  public Simulator(int simulationTime, SoS sos, Scenario scenario) {
    this.simulationTime = simulationTime;
    this.sos = sos;
    this.scenario = scenario;
    this.simulationLog = new SimulationLog();
  }

  public SimulationLog run() {
    this.reset();
    
    // this "result" variable is for getting the information of each event
    Pair<Action, TimeBound> result;

    // for every tick in simulation time
    for(int tick  = 0; tick < this.simulationTime; tick++) {
      // check every event in scenario
      for (Event ev : this.scenario.events) {
        result = ev.occur(tick);

        // result = (Action, Constant TimeBound)
        if (result != null) {
          this.simulationLog.addEventLog(result.getKey(), result.getValue());
        }
      }

      // Run SoS for such tick and get the result of each running
      // And add this running result into simulationLog.sosRunLog with tick
      this.simulationLog.addSosRunLog(tick, this.sos.run(tick));
    }

    // Save the other information from sos
    // this Resultlog is also for checking the result easier
    this.simulationLog.addResultLog(this.sos.CSs, this.sos.environment);

    return simulationLog;
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
