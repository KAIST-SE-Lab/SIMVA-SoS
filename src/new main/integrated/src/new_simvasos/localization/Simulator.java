package new_simvasos.localization;

import new_simvasos.log.Log;
import new_simvasos.localization.SoS;
import new_simvasos.scenario.Event;
import new_simvasos.scenario.Scenario;

import java.util.ArrayList;

/**
 * The type Simulator.
 * A module of SIMVA-SoS to run a simulation of input SoS model
 */
public class Simulator {
  int simulationTime;

  /**
   * Instantiates a new Simulator.
   *
   * @param simulationTime the simulation time
   */
  public Simulator(int simulationTime) {
    this.simulationTime = simulationTime;
  }

  /**
   * Run the input SoS model with the input scenario.
   * repeats SoS and scenario during simulationTime.
   *
   * @param sos      the sos
   * @param scenario the scenario
   * @return the log
   */
  public Log run(SoS sos, Scenario scenario) {
    this.reset();

    Log log = new Log();
    String result_sos = null;
    ArrayList<Event> occuredEvents = new ArrayList<>();

    // for every tick in simulation time
    for(int tick  = 0; tick < this.simulationTime; tick++) {
      // check every event in scenario
      for (Event ev : scenario.events) {
        // result = (Action, Constant TimeBound)
        if (ev.occur(tick) != null) {
          occuredEvents.add(ev);
        }
      }

      // Run SoS for such tick and get the result of each running
      // And add this running result into simulationLog.sosRunLog with tick
      result_sos = sos.run(tick);
      //System.out.println(result_sos);
                                            // CS Actition      // Environment State
      log.addSnapshot(tick, result_sos);
      //log.printSnapshot();
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
  private void reset() { }

}
