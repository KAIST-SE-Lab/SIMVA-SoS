package new_simvasos.adaptation;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Class System-of-Systems
 * will be replaced by the newly defined SoS
 */
public class SoS {
  public ArrayList <CS> CSs;
  ArrayList <Double> environment;

  public SoS(ArrayList <CS> CSs, ArrayList<Double> environment) {
    this.CSs = CSs;
    this.environment = environment;
  }

  public String run(int tick) {

    String logs = "";
    //Collections.shuffle(CSs);

    for(CS cs : CSs) {
      String result = cs.act(tick, this.environment);

      if(result != null && !result.isEmpty()) {
        logs += result;
        logs += " ";
      }
    }
    //logs.add(String (environment));

    logs += this.environment.toString();
    logs += " ";
    logs += "temperature: " + getTemperature();
    logs += " ";
    logs += "humidity: " + getHumidity();

    return logs;
  }

  // reset all cs's attributes ex) firefighter
  public void reset() {
    for (int i = 0; i < CSs.size(); i++) {
        CSs.get(i).reset();
    }
    this.resetEnvironment();
  }

  private void resetEnvironment() {
      for (int i = 0; i < this.environment.size(); i++) {
        this.environment.set(i, 0.);
      }
  }

  private double getHumidity(){
    return environment.get(1);
  }

  private double getTemperature(){
    return environment.get(0);
  }

  public ArrayList<Double> getEnvironment() {
    return environment;
  }
  
}
