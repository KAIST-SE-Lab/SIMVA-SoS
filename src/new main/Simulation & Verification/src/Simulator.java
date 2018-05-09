import java.util.ArrayList;
public class Simulator {
  int simulationTime;
  SoS sos;
  Scenario scenario;
  ArrayList <String> simulationLog;
  public Simulator(int simulationTime, SoS sos, Scenario scenario) {
    this.simulationTime = simulationTime;
    this.sos = sos;
    this.scenario = scenario;
    this.simulationLog = new ArrayList<String>();
  }

  public ArrayList run() {

    return null;
  }

  public void stop() {

  }

  public void monitor() {

  }

  public void reset() {

  }

}
