import javafx.util.Pair;

import java.util.ArrayList;
public class Simulator {
  int simulationTime;
  SoS sos;
  Scenario scenario;
  
  public Simulator(int simulationTime, SoS sos, Scenario scenario) {
    this.simulationTime = simulationTime;
    this.sos = sos;
    this.scenario = scenario;
  }

  public SimulationLog run() {
    this.reset();

    SimulationLog simulationLog = new SimulationLog();


    // this "result" variable is for getting the information of each event
    Pair<Action, TimeBound> result;

    // for every tick in simulation time
    for(int tick  = 0; tick < this.simulationTime; tick++) {
      // check every event in scenario
      //System.out.println(tick);
      for (Event ev : this.scenario.events) {
        result = ev.occur(tick);

        // result = (Action, Constant TimeBound)
        if (result != null) {
          simulationLog.addEventLog(result.getKey(), result.getValue());
        }
      }

      int sum = 0;

      for (int i : this.sos.environment) {
        sum += i;
      }

      if (tick == 0 && sum != 20) System.out.println(tick + ": " + sum);
      // Run SoS for such tick and get the result of each running
      // And add this running result into simulationLog.sosRunLog with tick
      simulationLog.addSosRunLog(tick, this.sos.run(tick));
    }

    // Save the other information from sos
    // this Resultlog is also for checking the result easier
    simulationLog.addPropertyLog(this.sos.CSs, this.sos.environment);
    //System.out.println(simulationLog.getPropertyLog());

    int sum = 0;


    return simulationLog;
  }

  public void stop() {

  }

  public void monitor() {

  }

  // reset the attributes of Simulator
  public void reset() {
    this.scenario.reset();
    this.sos.reset();
  }

}
