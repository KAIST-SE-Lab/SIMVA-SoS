package new_simvasos.adaptation;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Class System-of-Systems
 * will be replaced by the newly defined SoS
 */
public class SoS {
  public ArrayList <CS> CSs;
  ArrayList <Double> environment;
  ArrayList <Double> initialEnv;
  private Double degreeOfOpeningOfWindow;

  public SoS(ArrayList <CS> CSs, String configFile) {
    this.CSs = CSs;
    this.environment = new ArrayList<Double>();
    ArrayList<Pair<String, String>> config = FileManager.readConfiguration(configFile);
    this.environment.add(Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "initial_indoor_temperature")));
    this.environment.add(Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "initial_indoor_humidity")));

    this.initialEnv = new ArrayList<Double>();
    this.initialEnv.addAll(this.environment);

    degreeOfOpeningOfWindow = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "degree_of_opening_of_window"));
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
        this.environment.set(i, this.initialEnv.get(i));
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
