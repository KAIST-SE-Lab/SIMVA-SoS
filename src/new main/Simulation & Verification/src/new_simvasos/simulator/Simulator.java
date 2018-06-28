package new_simvasos.simulator;

import javafx.util.Pair;
import new_simvasos.log.Log;
import new_simvasos.not_decided.SoS;
import new_simvasos.scenario.Event;
import new_simvasos.scenario.Scenario;

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

  public Log run() {
    this.reset();

    Log log = new Log();
    String result_sos = null;
    ArrayList<Event> occuredEvents = new ArrayList<>();

    // for every tick in simulation time
    for(int tick  = 0; tick < this.simulationTime; tick++) {
      // check every event in scenario
      for (Event ev : this.scenario.events) {
        // result = (Action, Constant TimeBound)
        if (ev.occur(tick) != null) {
          occuredEvents.add(ev);
        }
      }

      // Run SoS for such tick and get the result of each running
      // And add this running result into simulationLog.sosRunLog with tick
      result_sos = this.sos.run(tick);
      //System.out.println(result_sos);
                                            // CS Actition      // Environment State
      log.addSnapshot(tick, result_sos);
    }

    // Save the other information from sos
    // this Resultlog is also for checking the result easier
    //log.addSimuLog(this.sos.CSs, result_sos.getValue());
    //System.out.println(simulationLog.getCsLog() + " : " + simulationLog.getEnvironmentLog());


    return log;
  }


  public  void pause() {

  }


  public void stop() {

  }


  public void monitor() {

  }


  // reset the attributes of Simulator
  private void reset() {
    this.scenario.reset();
    this.sos.reset();
  }

}
